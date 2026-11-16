package com.Faprire4.Kobmif.domain.usecase

import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.domain.model.CasePack
import com.Faprire4.Kobmif.domain.model.CaseSetup
import com.Faprire4.Kobmif.domain.model.Claimant
import com.Faprire4.Kobmif.domain.model.Statement
import com.Faprire4.Kobmif.domain.model.StatementKind
import com.Faprire4.Kobmif.domain.model.Tie
import com.Faprire4.Kobmif.domain.repository.CaseRepository
import kotlin.random.Random

private const val EXCLUSION_TOPIC_BASE = 2000
private const val NEUTRAL_TOPIC_BASE = 3000

class GenerateCaseUseCase(
    private val caseRepository: CaseRepository,
    private val validateCase: ValidateCaseUseCase
) {

    operator fun invoke(chapterIndex: Int, attempt: Int, questionBudget: Int): CaseSetup {
        val pack = caseRepository.pack()
        val perClaimant = caseRepository.statementsPerClaimant(chapterIndex)
        val liarCount = caseRepository.liarsForChapter(chapterIndex)
        val baseSeed = seedOf(chapterIndex, attempt)
        var tryIndex = 0
        while (tryIndex < GameConfig.MAX_GENERATION_ATTEMPTS) {
            val candidate = assemble(
                pack = pack,
                chapterIndex = chapterIndex,
                seed = baseSeed + tryIndex * 31L,
                perClaimant = perClaimant,
                liarCount = liarCount,
                questionBudget = questionBudget,
                fallback = false
            )
            if (validateCase(candidate)) {
                return candidate
            }
            tryIndex++
        }
        return fallbackCase(pack, chapterIndex, baseSeed, perClaimant, questionBudget)
    }

    private fun seedOf(chapterIndex: Int, attempt: Int): Long {
        val left = (chapterIndex + 1L) * GameConfig.SEED_CHAPTER_PRIME
        val right = (attempt + 1L) * GameConfig.SEED_ATTEMPT_PRIME
        return left xor right
    }

    private fun assemble(
        pack: CasePack,
        chapterIndex: Int,
        seed: Long,
        perClaimant: Int,
        liarCount: Int,
        questionBudget: Int,
        fallback: Boolean
    ): CaseSetup {
        val random = Random(seed)
        val names = pack.claimantNames.shuffled(random).take(GameConfig.CLAIMANTS)
        val motives = pack.motives.shuffled(random).take(GameConfig.CLAIMANTS)
        val claimants = (0 until GameConfig.CLAIMANTS).map { index ->
            Claimant(id = index, nameRes = names[index], motiveRes = motives[index])
        }
        val heirId = random.nextInt(GameConfig.CLAIMANTS)
        val others = (0 until GameConfig.CLAIMANTS).filter { it != heirId }.shuffled(random)
        val liarIds = others.take(liarCount).toSet()
        val truthful = (0 until GameConfig.CLAIMANTS).filterNot { liarIds.contains(it) }
        val liesPerLiar = 2
        val topics = pack.topicTruths.indices.shuffled(random)

        val buckets = HashMap<Int, MutableList<Statement>>()
        val fillers = HashMap<Int, MutableList<Statement>>()
        claimants.forEach { buckets[it.id] = mutableListOf(); fillers[it.id] = mutableListOf() }

        var nextId = 0
        var topicCursor = 0
        var partnerCursor = random.nextInt(truthful.size)

        for (liar in liarIds.sorted()) {
            var produced = 0
            while (produced < liesPerLiar && topicCursor < topics.size) {
                val topic = topics[topicCursor]
                topicCursor++
                val partner = truthful[partnerCursor % truthful.size]
                partnerCursor++
                buckets.getValue(liar).add(
                    Statement(
                        id = nextId++,
                        speakerId = liar,
                        textRes = pack.topicLies[topic],
                        targetNameRes = 0,
                        targetId = -1,
                        topicId = topic,
                        assertion = false,
                        truthful = false,
                        kind = StatementKind.TOPIC
                    )
                )
                buckets.getValue(partner).add(
                    Statement(
                        id = nextId++,
                        speakerId = partner,
                        textRes = pack.topicTruths[topic],
                        targetNameRes = 0,
                        targetId = -1,
                        topicId = topic,
                        assertion = true,
                        truthful = true,
                        kind = StatementKind.TOPIC
                    )
                )
                produced++
            }
        }

        val nonHeirs = (0 until GameConfig.CLAIMANTS).filter { it != heirId }.shuffled(random)
        var speakerCursor = random.nextInt(truthful.size)
        nonHeirs.forEach { target ->
            val speaker = truthful[speakerCursor % truthful.size]
            speakerCursor++
            buckets.getValue(speaker).add(
                Statement(
                    id = nextId++,
                    speakerId = speaker,
                    textRes = pack.exclusionLine,
                    targetNameRes = claimants[target].nameRes,
                    targetId = target,
                    topicId = EXCLUSION_TOPIC_BASE + target,
                    assertion = false,
                    truthful = true,
                    kind = StatementKind.EXCLUSION
                )
            )
        }

        liarIds.sorted().forEach { liar ->
            buckets.getValue(liar).add(
                Statement(
                    id = nextId++,
                    speakerId = liar,
                    textRes = pack.exclusionLine,
                    targetNameRes = claimants[heirId].nameRes,
                    targetId = heirId,
                    topicId = EXCLUSION_TOPIC_BASE + heirId,
                    assertion = false,
                    truthful = false,
                    kind = StatementKind.EXCLUSION
                )
            )
        }

        var neutralCursor = random.nextInt(pack.neutralLines.size)
        var neutralTopic = 0
        claimants.forEach { claimant ->
            val priority = buckets.getValue(claimant.id)
            while (priority.size + fillers.getValue(claimant.id).size < perClaimant) {
                val line = pack.neutralLines[neutralCursor % pack.neutralLines.size]
                neutralCursor++
                fillers.getValue(claimant.id).add(
                    Statement(
                        id = nextId++,
                        speakerId = claimant.id,
                        textRes = line,
                        targetNameRes = 0,
                        targetId = -1,
                        topicId = NEUTRAL_TOPIC_BASE + neutralTopic,
                        assertion = true,
                        truthful = !liarIds.contains(claimant.id),
                        kind = StatementKind.NEUTRAL
                    )
                )
                neutralTopic++
            }
        }

        val queues = claimants.associate { claimant ->
            val priority = buckets.getValue(claimant.id).shuffled(random)
            claimant.id to (priority + fillers.getValue(claimant.id))
        }

        val ties = (0 until GameConfig.CLAIMANTS).map { index ->
            Tie(
                fromId = index,
                toId = (index + 1) % GameConfig.CLAIMANTS,
                labelRes = pack.tieLabels[random.nextInt(pack.tieLabels.size)]
            )
        }

        return CaseSetup(
            chapterIndex = chapterIndex,
            chapterTitleRes = pack.chapterTitles[chapterIndex.coerceIn(0, pack.chapterTitles.size - 1)],
            seed = seed,
            claimants = claimants,
            queues = queues,
            ties = ties,
            heirId = heirId,
            liarIds = liarIds,
            questionBudget = questionBudget,
            fallback = fallback
        )
    }

    private fun fallbackCase(
        pack: CasePack,
        chapterIndex: Int,
        seed: Long,
        perClaimant: Int,
        questionBudget: Int
    ): CaseSetup {
        val claimants = (0 until GameConfig.CLAIMANTS).map { index ->
            Claimant(
                id = index,
                nameRes = pack.claimantNames[index % pack.claimantNames.size],
                motiveRes = pack.motives[index % pack.motives.size]
            )
        }
        val heirId = 0
        val liarId = 1
        val truthful = listOf(0, 2, 3)
        val buckets = HashMap<Int, MutableList<Statement>>()
        claimants.forEach { buckets[it.id] = mutableListOf() }
        var nextId = 0

        listOf(0, 1).forEachIndexed { slot, topic ->
            val partner = truthful[slot % truthful.size]
            buckets.getValue(liarId).add(
                Statement(
                    id = nextId++,
                    speakerId = liarId,
                    textRes = pack.topicLies[topic],
                    targetNameRes = 0,
                    targetId = -1,
                    topicId = topic,
                    assertion = false,
                    truthful = false,
                    kind = StatementKind.TOPIC
                )
            )
            buckets.getValue(partner).add(
                Statement(
                    id = nextId++,
                    speakerId = partner,
                    textRes = pack.topicTruths[topic],
                    targetNameRes = 0,
                    targetId = -1,
                    topicId = topic,
                    assertion = true,
                    truthful = true,
                    kind = StatementKind.TOPIC
                )
            )
        }

        listOf(1, 2, 3).forEachIndexed { slot, target ->
            val speaker = truthful[slot % truthful.size]
            buckets.getValue(speaker).add(
                Statement(
                    id = nextId++,
                    speakerId = speaker,
                    textRes = pack.exclusionLine,
                    targetNameRes = claimants[target].nameRes,
                    targetId = target,
                    topicId = EXCLUSION_TOPIC_BASE + target,
                    assertion = false,
                    truthful = true,
                    kind = StatementKind.EXCLUSION
                )
            )
        }

        buckets.getValue(liarId).add(
            Statement(
                id = nextId++,
                speakerId = liarId,
                textRes = pack.exclusionLine,
                targetNameRes = claimants[heirId].nameRes,
                targetId = heirId,
                topicId = EXCLUSION_TOPIC_BASE + heirId,
                assertion = false,
                truthful = false,
                kind = StatementKind.EXCLUSION
            )
        )

        var neutralTopic = 0
        claimants.forEach { claimant ->
            val bucket = buckets.getValue(claimant.id)
            while (bucket.size < perClaimant) {
                bucket.add(
                    Statement(
                        id = nextId++,
                        speakerId = claimant.id,
                        textRes = pack.neutralLines[neutralTopic % pack.neutralLines.size],
                        targetNameRes = 0,
                        targetId = -1,
                        topicId = NEUTRAL_TOPIC_BASE + neutralTopic,
                        assertion = true,
                        truthful = claimant.id != liarId,
                        kind = StatementKind.NEUTRAL
                    )
                )
                neutralTopic++
            }
        }

        val ties = (0 until GameConfig.CLAIMANTS).map { index ->
            Tie(
                fromId = index,
                toId = (index + 1) % GameConfig.CLAIMANTS,
                labelRes = pack.tieLabels[index % pack.tieLabels.size]
            )
        }

        return CaseSetup(
            chapterIndex = chapterIndex,
            chapterTitleRes = pack.chapterTitles[chapterIndex.coerceIn(0, pack.chapterTitles.size - 1)],
            seed = seed,
            claimants = claimants,
            queues = claimants.associate { it.id to buckets.getValue(it.id).toList() },
            ties = ties,
            heirId = heirId,
            liarIds = setOf(liarId),
            questionBudget = questionBudget,
            fallback = true
        )
    }
}

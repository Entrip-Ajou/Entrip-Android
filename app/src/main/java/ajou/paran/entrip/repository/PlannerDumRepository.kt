package ajou.paran.entrip.repository

import ajou.paran.entrip.model.PlannerDate

interface PlannerDumRepository {
    suspend fun getPlannerDates(): List<PlannerDate>
}
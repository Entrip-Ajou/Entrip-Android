package ajou.paran.entrip.repository

import ajou.paran.entrip.model.PlannerDate

interface PlannerRepository {
    suspend fun getPlannerDates(): List<PlannerDate>
}
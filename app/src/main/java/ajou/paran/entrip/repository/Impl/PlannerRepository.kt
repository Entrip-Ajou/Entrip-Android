package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlannerDate

interface PlannerRepository {
    suspend fun getPlannerDates(): List<PlannerDate>
}
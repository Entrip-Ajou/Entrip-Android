package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlannerDate

interface PlannerDumRepository {
    suspend fun getPlannerDates(): List<PlannerDate>
}
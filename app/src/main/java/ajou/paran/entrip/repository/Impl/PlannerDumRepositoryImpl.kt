package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlannerDate
import ajou.paran.entrip.model.fakeDateItemList
import ajou.paran.entrip.repository.PlannerDumRepository

class PlannerDumRepositoryImpl: PlannerDumRepository {
    override suspend fun getPlannerDates(): List<PlannerDate> = fakeDateItemList
}
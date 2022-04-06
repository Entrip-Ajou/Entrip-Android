package ajou.paran.entrip.repository.room.planner.repository

import ajou.paran.entrip.model.test.PlannerTestEntity
import ajou.paran.entrip.repository.room.planner.dao.PlannerDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlannerRepositoryImpl
    @Inject
    constructor(
        private val plannerDao: PlannerDao
    ): PlannerRepository{
    override suspend fun insertPlanner(plannerTestEntity: PlannerTestEntity) {
        plannerDao.insertPlanner(
            plannerTestEntity = plannerTestEntity
        )
    }

    override fun updatePlanner(
        title: String,
        start_date: String,
        end_date: String,
        time_stamp: String,
        id: Long
    ) {
        plannerDao.updatePlanner(
            title = title,
            start_date = start_date,
            end_date = end_date,
            time_stamp = time_stamp,
            id = id
        )
    }

    override fun selectPlanner(
        start_date: String,
        end_date: String,
        id: Long
    ): Flow<List<PlannerTestEntity>>
        = plannerDao.selectPlanner(
            start_date = start_date,
            end_date = end_date,
            id = id
        )
}
package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.repository.network.dto.PlanResponse
import ajou.paran.entrip.repository.network.dto.PlannerResponse
import retrofit2.Response
import retrofit2.http.GET

interface PlanApi {
    companion object{
        const val BASE_URL = "https://"
    }

    // Planner 추가를 눌렀을 때
    @GET("")
    suspend fun createPlanner() : Response<PlannerResponse>

    // Home화면 Planner 선택란에서 기존에 저장된 Planner를 눌렀을 때
    @GET("")
    suspend fun fetchPlanner(planner_id : Long) : Response<PlannerResponse>

    @GET("")
    suspend fun fetchPlans(planner_idFK : Long) : Response<List<PlanResponse>>
}
package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.dto.TripResponse

interface RecommendRepository {
    suspend fun getRecommendItem(): List<TripResponse>
}
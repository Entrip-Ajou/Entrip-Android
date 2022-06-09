package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.test.nullRecommenItem
import ajou.paran.entrip.repository.network.dto.TripResponse

class RecommendRepositoryImpl: RecommendRepository {
    override suspend fun getRecommendItem(): List<TripResponse>
        = nullRecommenItem
}
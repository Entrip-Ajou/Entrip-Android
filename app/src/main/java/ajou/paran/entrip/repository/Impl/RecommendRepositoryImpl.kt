package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.test.RecommendationItem
import ajou.paran.entrip.model.test.fakeRecommenItem

class RecommendRepositoryImpl: RecommendRepository {
    override suspend fun getRecommendItem(): List<RecommendationItem>
        = fakeRecommenItem
}
package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.test.RecommendationItem

interface RecommendRepository {
    suspend fun getRecommendItem(): List<RecommendationItem>
}
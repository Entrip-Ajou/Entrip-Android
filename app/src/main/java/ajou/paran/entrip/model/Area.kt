package ajou.paran.entrip.model

import kotlinx.serialization.Serializable

// assets/area.json serialize
@Serializable
data class Area(
    val city : String,
    val districts : String
)

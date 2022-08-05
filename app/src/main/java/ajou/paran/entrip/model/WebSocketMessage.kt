package ajou.paran.entrip.model

data class WebSocketMessage(
    val type : String,
    val content : Int, // 0 : Planners, 1: Plans
    val sender : String,
    val planner_id : Long,
    val date : String
)

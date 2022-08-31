package ajou.paran.entrip.model

data class WebSocketMessage(
    val type : String,
    val content : Int, // 0 : Planners, 1: Plans, 2: comment
    val sender : String,
    val planner_id : Long,
    val date : String,
    val plan_id : Long,
    val isExistComments : Boolean
)

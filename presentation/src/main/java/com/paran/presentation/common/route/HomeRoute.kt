package com.paran.presentation.common.route

sealed class HomeRoute(
    val tag: String
) {
    object Home : HomeRoute("home")
    object Planner : HomeRoute("planner")
    object PlannerDetail : HomeRoute("plannerDetail")
    object PlanInput : HomeRoute("planInput")
    object Notice : HomeRoute("notice")
    object Vote : HomeRoute("vote")
    object PlannerUserAdd : HomeRoute("plannerUserAdd")
    object Recommendation : HomeRoute("recommendation")
    object Community : HomeRoute("community")
    object MyPage : HomeRoute("myPage")
}

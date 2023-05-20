package ajou.paran.entrip.base

object BaseUrl {
    private const val BASE_URL = "https://2ntrip.link"
    private const val DEV_PORT_URL = ""
//    ":1010
    const val MAIN_URL = "$BASE_URL$DEV_PORT_URL/"

    object V1 {
        // request Access Token
        private const val BASE_API_URL = "api/v1"

        object User {
            private const val BASE_USER_API_URL = "$BASE_API_URL/users"

            const val USER_LOGOUT_URL = "$BASE_USER_API_URL/{user_id}/logout"
        }

        object Comment {
            private const val BASE_COMMENT_URL = "$BASE_API_URL/comments"

            const val COMMENT_SAVE_URL = BASE_COMMENT_URL
            const val COMMENT_DELETE_URL = "$BASE_COMMENT_URL/{comment_id}"
            const val COMMENT_SELECT_URL = "$BASE_COMMENT_URL/{plan_id}/getAllComments"
        }

        object Community {
            private const val BASE_COMMUNITY_PHOTOS_URL = "$BASE_API_URL/photos"
            private const val BASE_COMMUNITY_POST_URL = "$BASE_API_URL/posts"
            private const val BASE_COMMUNITY_POST_COMMENTS_URL = "$BASE_API_URL/postsComments"
            private const val BASE_COMMUNITY_POST_NESTED_COMMENTS_URL = "$BASE_API_URL/postsNestedComments"

            const val PHOTO_SAVE_URL = "$BASE_COMMUNITY_PHOTOS_URL/{priority}"
            const val PHOTO_FIND_URL = "$BASE_COMMUNITY_PHOTOS_URL/{photo_id}"
            const val PHOTO_DELETE_URL = "$BASE_COMMUNITY_PHOTOS_URL/{photo_id}"
            const val PHOTO_ADD_POST_URL = "$BASE_COMMUNITY_PHOTOS_URL/{photo_id}/{post_id}/addPosts"

            const val POST_SAVE_URL = BASE_COMMUNITY_POST_URL
            const val POST_FIND_URL = "$BASE_COMMUNITY_POST_URL/{post_id}"
            const val POST_DELETE_URL = "$BASE_COMMUNITY_POST_URL/{post_id}"
            const val POST_GET_PAGE_URL = "$BASE_COMMUNITY_POST_URL/{pageNumber}/all"
            const val POST_RAISE_LIKE_URL = "$BASE_COMMUNITY_POST_URL/{post_id}/{user_id}/like"
            const val POST_DECREASE_LIKE_URL = "$BASE_COMMUNITY_POST_URL/{post_id}/{user_id}/dislike"

            const val COMMENT_SAVE_URL = BASE_COMMUNITY_POST_COMMENTS_URL
            const val COMMENT_FIND_URL = "$BASE_COMMUNITY_POST_COMMENTS_URL/{postComment_id}"
            const val COMMENT_DELETE_URL = "$BASE_COMMUNITY_POST_COMMENTS_URL/{postComment_id}"
            const val COMMENT_GET_ALL_URL = "$BASE_COMMUNITY_POST_COMMENTS_URL/{post_id}/all"

            const val NESTED_COMMENT_SAVE_URL = BASE_COMMUNITY_POST_NESTED_COMMENTS_URL
            const val NESTED_COMMENT_FIND_URL = "$BASE_COMMUNITY_POST_NESTED_COMMENTS_URL/{postNestedComment_id}"
            const val NESTED_COMMENT_DELETE_URL = "$BASE_COMMUNITY_POST_NESTED_COMMENTS_URL/{postNestedComment_id}"
            const val NESTED_COMMENT_GET_ALL_URL = "$BASE_COMMUNITY_POST_NESTED_COMMENTS_URL/{postComment_id}/all"
        }

        object Notice {

        }

        object Plan {
            private const val BASE_PLANNER_URL = "$BASE_API_URL/planners"
            private const val BASE_PLAN_URL = "$BASE_API_URL/plans"

            const val PLANNER_CREATE_URL = "$BASE_PLANNER_URL/{user_id}"
            const val PLANNER_DELETE_URL = "$BASE_PLANNER_URL/{planner_id}/{user_id}/exit"
            const val PLANNER_FETCH_URL = "$BASE_PLANNER_URL/{planner_id}"
            const val PLANNER_FETCH_PLANS_URL = "$BASE_PLANNER_URL/{planner_id}/all"
            const val PLANNER_EXIST_URL = "$BASE_PLANNER_URL/{planner_id}/exist"
            const val PLANNER_UPDATE_URL = "$BASE_PLANNER_URL/{planner_id}"
            const val PLANNER_FIND_DATE_URL = "$BASE_PLANNER_URL/{planner_id}/{date}/find"

            const val PLAN_INSERT_URL = BASE_PLAN_URL
            const val PLAN_DELETE_URL = "$BASE_PLAN_URL/{plan_id}"
            const val PLAN_UPDATE_URL = "$BASE_PLAN_URL/{plan_id}"
        }

        object Vote {

        }
    }

    object V2 {
        private const val BASE_API_URL = "api/v2"

        object User {
            private const val BASE_USER_API_URL = "$BASE_API_URL/users"
            const val SAVE_URL = BASE_USER_API_URL
            const val LOGIN_URL = "$BASE_USER_API_URL/login"
            const val REISSUE_URL = "$BASE_USER_API_URL/reIssue/{refreshToken}"
            const val IS_EXIST_ID_URL = "$BASE_USER_API_URL/{user_id}/user_id/exist"
            const val IS_EXIST_NICKNAME_URL = "$BASE_USER_API_URL/{nickname}/nickname/exist"
        }
    }

}
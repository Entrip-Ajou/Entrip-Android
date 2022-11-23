package ajou.paran.entrip.base

object BaseUrl {
    private const val BASE_URL = "http://2ntrip.com"
    private const val DEV_PORT_URL = ":1010"
    const val MAIN_URL = "$BASE_URL$DEV_PORT_URL/"

    object V1 {
        // request Access Token
        private const val BASE_API_URL = "api/v1"

        object User {
            private const val BASE_USER_API_URL = "$BASE_API_URL/users"
            const val LOGOUT_URL = "$BASE_USER_API_URL/{user_id}/logout"
        }

        object Comment {

        }

        object Community {

        }

        object Notice {

        }

        object Plan {

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
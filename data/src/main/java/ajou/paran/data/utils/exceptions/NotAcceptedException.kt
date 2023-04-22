package ajou.paran.data.utils.exceptions

import okio.IOException

class NotAcceptedException : IOException() {
    companion object {
        const val FAIL_BY_ID = "Email is not valid"
        const val FAIL_BY_PASSWORD = "Password is not valid"
    }
}
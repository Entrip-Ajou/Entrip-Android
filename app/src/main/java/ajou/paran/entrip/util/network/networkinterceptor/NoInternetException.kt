package ajou.paran.entrip.util.network.networkinterceptor

import java.io.IOException

class NoInternetException : IOException() {
    override val message: String
        get() = "The network is not connected"
}
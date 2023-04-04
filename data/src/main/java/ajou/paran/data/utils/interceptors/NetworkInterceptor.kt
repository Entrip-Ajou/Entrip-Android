package ajou.paran.data.utils.interceptors

import ajou.paran.data.utils.exceptions.NoInternetException
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptor
constructor (
    private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isConnected()){
            throw NoInternetException()
        }
        val req = chain.request().newBuilder().build()
        return chain.proceed(req)
    }

    private fun isConnected() : Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val network = connectivityManager.activeNetwork ?: return false
                val actNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
                return when {
                    actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
            else -> {
                val nwInfo = connectivityManager.activeNetworkInfo ?: return false
                return nwInfo.isConnected
            }
        }
    }
}
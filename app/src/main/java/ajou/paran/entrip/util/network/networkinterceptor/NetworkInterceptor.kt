package ajou.paran.entrip.util.network.networkinterceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptor constructor(private val context: Context) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isConnected()){
            throw NoInternetException()
        }
        val req = chain.request().newBuilder().build()
        return chain.proceed(req)
    }

    private fun isConnected() : Boolean{
        val connectivitymanager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network = connectivitymanager.activeNetwork ?: return false
            val actNetwork = connectivitymanager.getNetworkCapabilities(network) ?: return false
            return when{
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }else{
            val nwInfo = connectivitymanager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }
}
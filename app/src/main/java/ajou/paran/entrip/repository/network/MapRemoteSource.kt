package ajou.paran.entrip.repository.network

import ajou.paran.entrip.repository.network.api.MapApi
import ajou.paran.entrip.repository.network.dto.ResultSearchKeyword
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.util.Log
import javax.inject.Inject

class MapRemoteSource
@Inject
constructor(
    private val mapApi : MapApi
){
    companion object{
        const val TAG = "[MapRemote]"
    }

    suspend fun searchKeyword(keyword : String) : BaseResult<ResultSearchKeyword, Failure> {
        try{
            val response = mapApi.getSearchKeyword(keyword)
            return if(response.isSuccessful){
                BaseResult.Success(response.body()!!)
            }else{
                Log.e(TAG, "err.code = "+response.code() + " err.message = " + response.raw().toString())
                BaseResult.Error(Failure(response.code(), response.errorBody().toString()))
            }
        } catch(e : NoInternetException){
            return BaseResult.Error(Failure(0, e.message))
        } catch(e : Exception){
            Log.e(TAG, e.printStackTrace().toString())
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }
}
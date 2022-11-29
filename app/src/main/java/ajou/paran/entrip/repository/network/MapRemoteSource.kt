package ajou.paran.entrip.repository.network

import ajou.paran.entrip.repository.network.api.MapApi
import ajou.paran.entrip.repository.network.dto.ResultSearchKeyword
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.util.Log
import retrofit2.HttpException
import javax.inject.Inject

class MapRemoteSource
@Inject
constructor(
    private val mapApi: MapApi
) {
    companion object {
        private const val TAG = "[MapRemote]"
    }

    suspend fun searchKeyword(keyword: String): BaseResult<ResultSearchKeyword, Failure> {
        try {
            val response = mapApi.getSearchKeyword(keyword)
            return if (response.isSuccessful) {
                BaseResult.Success(response.body()!!)
            } else {
                Log.e(TAG, "Err code = "+response.code()+ " Err message = " + response.raw().message)
                BaseResult.Error(Failure(response.code(), response.errorBody().toString()))
            }
        } catch (e: NoInternetException) {
            Log.e(TAG, "NoInternetException Message = "+e.localizedMessage)
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException Message = "+e.localizedMessage)
            return BaseResult.Error(Failure(e.code(), e.printStackTrace().toString()))
        } catch (e: Exception) {
            Log.e(TAG, "Exception Message = "+e.localizedMessage)
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }
}
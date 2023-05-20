package ajou.paran.data.utils.extensions

import ajou.paran.data.local.datastore
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException

internal suspend fun dataEmptyException(
    exception: Throwable
) = flow {
    if (exception is IOException) {
        emit(emptyPreferences())
    } else {
        throw exception
    }
}

internal fun Context.fetchStringPreference(
    key: Preferences.Key<String>
) = this.datastore.data.catch { exception ->
    dataEmptyException(exception = exception)
}.map { preference ->
    preference[key] ?: ""
}

internal fun Context.fetchBooleanPreference(
    key: Preferences.Key<Boolean>
) = this.datastore.data.catch { exception ->
    dataEmptyException(exception = exception)
}.map { preference ->
    preference[key] ?: false
}
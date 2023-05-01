package ajou.paran.data.local.datasourceimpl

import ajou.paran.data.local.datasource.DataStoreDataSource
import ajou.paran.data.local.datastore
import ajou.paran.data.utils.extensions.fetchBooleanPreference
import ajou.paran.data.utils.extensions.fetchStringPreference
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreDataSourceImpl
@Inject
constructor(
    private val context: Context,
) : DataStoreDataSource {
    companion object {
        private val ID_TOKEN = stringPreferencesKey("ID_TOKEN")
        private val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        private val IS_ENTRY = booleanPreferencesKey("IS_ENTRY")
    }

    override fun fetchIdToken(): Flow<String> = context.fetchStringPreference(key = ID_TOKEN)

    override fun fetchAccessToken(): Flow<String> = context.fetchStringPreference(key = ACCESS_TOKEN)

    override fun fetchRefreshToken(): Flow<String> = context.fetchStringPreference(key = REFRESH_TOKEN)
    override fun fetchIsEntry(): Flow<Boolean> = context.fetchBooleanPreference(key = IS_ENTRY)
    override suspend fun saveIdToken(token: String) {
        context.datastore.edit { preference ->
            preference[ID_TOKEN] = token
        }
    }

    override suspend fun saveAccessToken(token: String) {
        context.datastore.edit { preference ->
            preference[ACCESS_TOKEN] = token
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        context.datastore.edit { preference ->
            preference[REFRESH_TOKEN] = token
        }
    }

    override suspend fun saveIsEntry(isEntry: Boolean) {
        context.datastore.edit { preference -> preference[IS_ENTRY] = isEntry }
    }

    override suspend fun clearIdToken() {
        context.datastore.edit { preference ->
            preference.remove(ID_TOKEN)
        }
    }

    override suspend fun clearAccessToken() {
        context.datastore.edit { preference ->
            preference.remove(ACCESS_TOKEN)
        }
    }

    override suspend fun clearRefreshToken() {
        context.datastore.edit { preference ->
            preference.remove(REFRESH_TOKEN)
        }
    }

    override suspend fun clearIsEntry() {
        context.datastore.edit { preference -> preference.remove(IS_ENTRY) }
    }

}
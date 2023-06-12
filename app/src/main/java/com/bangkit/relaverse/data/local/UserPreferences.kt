package com.bangkit.relaverse.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = UserPreferences.USER_PREF)

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    fun getId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[ID_USER] ?: ""
        }
    }


    suspend fun saveToken(token: String, id: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[ID_USER] = id

        }
    }

    suspend fun saveLocation(location: String, lat: String, lng: String) {
        dataStore.edit { preferences ->
            preferences[LOCATION] = location
            preferences[LAT] = lat
            preferences[LNG] = lng

        }
    }

    fun getLoc(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[LOCATION] ?: ""
        }
    }

    fun getLat(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[LAT] ?: ""
        }
    }

    fun getLng(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[LNG] ?: ""
        }
    }

    suspend fun deleteLoc() {
        dataStore.edit { preferences ->
            preferences[LOCATION] = ""
            preferences[LAT] = ""
            preferences[LNG] = ""

        }
    }


    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = ""
            preferences[ID_USER] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val ID_USER = stringPreferencesKey("id")
        private val LOCATION = stringPreferencesKey("location")
        private val LAT = stringPreferencesKey("lat")
        private val LNG = stringPreferencesKey("lng")
        const val USER_PREF = "user_pref"


        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}

package info.mrprogrammer.mrpillstracker.Login.domain.repository

import kotlinx.coroutines.flow.Flow


interface LoginRepository {
    suspend fun performLogin(): Flow<Boolean>
}
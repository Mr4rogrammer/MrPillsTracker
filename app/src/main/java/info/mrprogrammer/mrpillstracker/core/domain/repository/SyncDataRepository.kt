package info.mrprogrammer.mrpillstracker.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface SyncDataRepository {
    suspend fun syncData():Flow<Boolean>
}
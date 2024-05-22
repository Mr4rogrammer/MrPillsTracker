package info.mrprogrammer.mrpillstracker.Login.domain.repository

import kotlinx.coroutines.flow.Flow

interface GetTextFlowDataRepository {
    suspend fun getTextFlowData(): Flow<String>
}
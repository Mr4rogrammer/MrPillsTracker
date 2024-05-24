package info.mrprogrammer.mrpillstracker.core.domain.repository

import info.mrprogrammer.mrpillstracker.core.domain.model.UserDataModel

interface GetUserLoginDetailsRepository {
    suspend fun getUserLoginDetails(): UserDataModel
}
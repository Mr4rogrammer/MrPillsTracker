package info.mrprogrammer.mrpillstracker.core.data.data_soruce.get_user_login_details

import info.mrprogrammer.mrpillstracker.core.domain.model.UserDataModel

interface GetUserLoginDetailsDataSource {
    suspend fun getUserLoginDetails(): UserDataModel
}
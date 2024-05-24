package info.mrprogrammer.mrpillstracker.core.data.data_soruce.get_user_login_details.local

import com.google.firebase.auth.FirebaseAuth
import info.mrprogrammer.mrpillstracker.core.data.data_soruce.get_user_login_details.GetUserLoginDetailsDataSource
import info.mrprogrammer.mrpillstracker.core.domain.model.UserDataModel

class GetUserLoginDetailsLocalDataSource : GetUserLoginDetailsDataSource {
    override suspend fun getUserLoginDetails(): UserDataModel {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        return UserDataModel(
            name = firebaseUser?.displayName,
            email = firebaseUser?.email,
            imageUrl = firebaseUser?.photoUrl
        )
    }
}
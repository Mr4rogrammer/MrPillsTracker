package info.mrprogrammer.mrpillstracker.Login.data.repository

import info.mrprogrammer.mrpillstracker.Login.domain.repository.LoginRepository
import info.mrprogrammer.mrpillstracker.Login.frame_work.GoogleLogin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val googleLogin: GoogleLogin):LoginRepository  {
    override suspend fun performLogin(): Flow<Boolean> {
        return googleLogin.performLogin()
    }
}
package info.mrprogrammer.mrpillstracker.Login.domain.use_cases

import info.mrprogrammer.mrpillstracker.Login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DoLogin @Inject constructor(private val loginRepository: LoginRepository){
    suspend operator fun invoke(): Flow<Boolean> {
        return loginRepository.performLogin()
    }
}
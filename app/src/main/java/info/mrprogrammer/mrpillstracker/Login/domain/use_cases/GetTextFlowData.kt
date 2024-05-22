package info.mrprogrammer.mrpillstracker.Login.domain.use_cases

import info.mrprogrammer.mrpillstracker.Login.domain.repository.GetTextFlowDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTextFlowData @Inject constructor(private val getTextFlowDataRepository: GetTextFlowDataRepository) {
    suspend operator fun invoke(): Flow<String> {
        return getTextFlowDataRepository.getTextFlowData()
    }
}
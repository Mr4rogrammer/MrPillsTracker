package info.mrprogrammer.mrpillstracker.core.domain.use_case

import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminderWrapper
import info.mrprogrammer.mrpillstracker.core.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMedicationReminderData @Inject constructor(private val dataRepository: DataRepository){

    suspend operator fun invoke():Flow<MedicineReminderWrapper> {
        return dataRepository.getMedicineReminderData()
    }
}
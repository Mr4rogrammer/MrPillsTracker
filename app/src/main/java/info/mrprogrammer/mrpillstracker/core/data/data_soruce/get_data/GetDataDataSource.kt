package info.mrprogrammer.mrpillstracker.core.data.data_soruce.get_data

import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminderWrapper
import kotlinx.coroutines.flow.Flow

interface GetDataDataSource {
    suspend fun getMedicineReminderData(): Flow<MedicineReminderWrapper>
}
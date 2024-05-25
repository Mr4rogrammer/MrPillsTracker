package info.mrprogrammer.mrpillstracker.core.domain.repository

import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminderWrapper
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    suspend fun getMedicineReminderData(): Flow<MedicineReminderWrapper>
}
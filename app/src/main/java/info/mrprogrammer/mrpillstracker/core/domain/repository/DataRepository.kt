package info.mrprogrammer.mrpillstracker.core.domain.repository

import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    suspend fun getMedicineReminderData(): Flow<List<MedicineReminder>>
}
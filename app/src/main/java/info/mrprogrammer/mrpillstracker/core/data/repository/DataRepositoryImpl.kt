package info.mrprogrammer.mrpillstracker.core.data.repository

import info.mrprogrammer.mrpillstracker.core.data.data_soruce.get_data.GetDataDataSource
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import info.mrprogrammer.mrpillstracker.core.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(private val getDataDataSource: GetDataDataSource): DataRepository {
    override suspend fun getMedicineReminderData(): Flow<List<MedicineReminder>> {
        return getDataDataSource.getMedicineReminderData()
    }
}
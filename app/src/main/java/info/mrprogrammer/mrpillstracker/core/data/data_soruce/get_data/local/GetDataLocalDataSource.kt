package info.mrprogrammer.mrpillstracker.core.data.data_soruce.get_data.local

import info.mrprogrammer.mrpillstracker.core.data.data_soruce.get_data.GetDataDataSource
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminderWrapper
import info.mrprogrammer.mrpillstracker.core.frame_work.LocalDataBase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDataLocalDataSource @Inject constructor(private val localDataBase: LocalDataBase):GetDataDataSource {
    override suspend fun getMedicineReminderData(): Flow<MedicineReminderWrapper> {
        return localDataBase.getAllMedicineReminder()
    }
}
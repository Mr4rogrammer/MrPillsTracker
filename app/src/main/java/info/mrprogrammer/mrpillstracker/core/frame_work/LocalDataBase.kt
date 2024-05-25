package info.mrprogrammer.mrpillstracker.core.frame_work

import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminderWrapper
import io.realm.RealmModel
import kotlinx.coroutines.flow.Flow

interface LocalDataBase {
    suspend fun insert(data: List<Any>)
    suspend fun insert(data: Any)
    suspend fun delete(data: Any, id:String)
    suspend fun getAllMedicineReminder(): Flow<MedicineReminderWrapper>
}
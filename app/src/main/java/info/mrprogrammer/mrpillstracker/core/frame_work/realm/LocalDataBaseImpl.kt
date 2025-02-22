package info.mrprogrammer.mrpillstracker.core.frame_work.realm

import androidx.appcompat.widget.ThemedSpinnerAdapter.Helper
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminderWrapper
import info.mrprogrammer.mrpillstracker.core.frame_work.LocalDataBase
import info.mrprogrammer.mrpillstracker.core.utils.filterDatesLessThanOrEqualToToday
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class LocalDataBaseImpl: LocalDataBase {
    override suspend fun insert(data: List<Any>) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.copyToRealmOrUpdate(data as RealmModel)
        }
    }

    override suspend fun insert(data: Any) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.copyToRealmOrUpdate(data as RealmModel)
        }
    }

    override suspend fun delete(data: Any, id:String) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
           it.where((data as RealmModel)::class.java).equalTo("id", id).findAll().deleteAllFromRealm()
        }
    }



    override suspend fun getAllMedicineReminder(): Flow<MedicineReminderWrapper> = withContext(Dispatchers.Main) {
        callbackFlow {
            val realm = Realm.getDefaultInstance()
            val results: RealmResults<MedicineReminder> = realm.where(MedicineReminder::class.java).findAll()
            val listener = RealmChangeListener<RealmResults<MedicineReminder>> { updatedResults ->
                val list = (filterDatesLessThanOrEqualToToday(realm.copyFromRealm(updatedResults)))
                trySend(list)
            }
            results.addChangeListener(listener)
            awaitClose {
                results.removeChangeListener(listener)
                realm.close()
            }
        }.flowOn(Dispatchers.Main)
    }
}
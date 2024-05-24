package info.mrprogrammer.mrpillstracker.core.frame_work.realm

import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import info.mrprogrammer.mrpillstracker.core.frame_work.LocalDataBase
import io.realm.Realm
import io.realm.RealmModel
import io.realm.kotlin.toFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

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

    override suspend fun getAllMedicineReminder(): Flow<List<MedicineReminder>> {
        val realm = Realm.getDefaultInstance()
        return realm.where(MedicineReminder::class.java)
            .findAllAsync()
            .toFlow()
            .flowOn(Dispatchers.Main)
            .map {
                realm.copyFromRealm(it)
            }
            .flowOn(Dispatchers.IO)
    }
}
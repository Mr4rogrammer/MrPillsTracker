package info.mrprogrammer.mrpillstracker.core.frame_work.realm

import info.mrprogrammer.mrpillstracker.core.frame_work.LocalDataBase
import io.realm.Realm
import io.realm.RealmModel

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
}
package info.mrprogrammer.mrpillstracker.core.frame_work

import io.realm.RealmModel

interface LocalDataBase {
    suspend fun insert(data: List<Any>)
    suspend fun insert(data: Any)
    suspend fun delete(data: Any, id:String)
}
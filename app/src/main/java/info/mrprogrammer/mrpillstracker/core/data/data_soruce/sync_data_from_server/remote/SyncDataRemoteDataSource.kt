package info.mrprogrammer.mrpillstracker.core.data.data_soruce.sync_data_from_server.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import info.mrprogrammer.mrpillstracker.core.data.data_soruce.sync_data_from_server.SyncDataDataSource
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import info.mrprogrammer.mrpillstracker.core.frame_work.LocalDataBase
import info.mrprogrammer.mrpillstracker.core.utils.FirebaseHelper.firebaseClearString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SyncDataRemoteDataSource @Inject constructor(private val localDataBase: LocalDataBase) :
    SyncDataDataSource {
    override suspend fun syncData(): Flow<Boolean> {
        val sharedFlow = MutableSharedFlow<Boolean>()

        val key = "data"
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val url = currentUser.email.firebaseClearString() + "/pillsData"
            val ref = FirebaseDatabase.getInstance().getReference(key).child(url)
            ref.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val data = snapshot.getValue(MedicineReminder::class.java)
                    data?.id = snapshot.key ?: ""
                    CoroutineScope(Dispatchers.IO).launch {
                        sharedFlow.emit(true)
                        localDataBase.insert(data as MedicineReminder)
                        println()
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val data = snapshot.getValue(MedicineReminder::class.java)
                    data?.id = snapshot.key ?: ""
                    CoroutineScope(Dispatchers.IO).launch {
                        sharedFlow.emit(true)
                        localDataBase.insert(data as MedicineReminder)
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(MedicineReminder::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        sharedFlow.emit(true)
                        localDataBase.delete(data as MedicineReminder, snapshot.key ?: "")
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onCancelled(error: DatabaseError) {

                }


            })
        }
        return sharedFlow.asSharedFlow()
    }
}
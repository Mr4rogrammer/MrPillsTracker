package info.mrprogrammer.mrpillstracker.core.data.data_soruce.save_data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import info.mrprogrammer.mrpillstracker.core.data.data_soruce.save_data.SavaDataDataSource
import info.mrprogrammer.mrpillstracker.core.domain.model.SaveDataModel
import info.mrprogrammer.mrpillstracker.core.utils.FirebaseHelper.firebaseClearString
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SaveDataRemoteDataSource: SavaDataDataSource {
    override suspend fun saveData(data: SaveDataModel): Boolean {
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return false
        val userKey = firebaseUser.email.firebaseClearString()
        val baseKey = data.key
        val dataKey = "$baseKey/$userKey"
        val dataRef = FirebaseDatabase.getInstance().getReference(dataKey)
        return suspendCoroutine { continuation ->
            dataRef.setValue(data.value).addOnCompleteListener {
                if (it.isSuccessful) {
                    continuation.resume(true)
                } else {
                    continuation.resume(false)
                }
            }
        }
    }
}
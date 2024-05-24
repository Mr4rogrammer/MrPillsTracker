package info.mrprogrammer.mrpillstracker.Login.frame_work

import android.app.Activity
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import info.mrprogrammer.mrpillstracker.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GoogleLogin @Inject constructor(private val context: Context) {
    fun performLogin(): Flow<Boolean> {
       return flow {
           emit(true)
       }
    }
}
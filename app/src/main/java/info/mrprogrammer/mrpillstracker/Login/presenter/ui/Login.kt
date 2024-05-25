package info.mrprogrammer.mrpillstracker.Login.presenter.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mrprogrammer.Utils.Toast.MrToast
import dagger.hilt.android.AndroidEntryPoint
import info.mrprogrammer.mrpillstracker.DashBoard.presenter.ui.DashBoard
import info.mrprogrammer.mrpillstracker.Login.presenter.utils.LoginState
import info.mrprogrammer.mrpillstracker.Login.presenter.viewmodel.LoginViewModel
import info.mrprogrammer.mrpillstracker.core.domain.model.SaveDataModel
import info.mrprogrammer.mrpillstracker.core.utils.screenOpenAnimation
import info.mrprogrammer.mrpillstracker.core.utils.setDebouncedOnClickListener
import info.mrprogrammer.mrpillstracker.core.utils.setFadeInAnimation
import info.mrprogrammer.mrpillstracker.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class Login : AppCompatActivity() {
    lateinit var root: ActivityLoginBinding
    private var mGoogleSignClient: GoogleSignInClient? = null
    private var mAuth: FirebaseAuth? = null
    private val RC_SIGN_IN = 123
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onStart() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            loginSuccess()
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        root = ActivityLoginBinding.inflate(layoutInflater)
        root.root.setFadeInAnimation(this)
        setContentView(root.root)
        initializeCollector()
        initializeOnClick()
    }

    private fun initializeOnClick() {
        root.login.setDebouncedOnClickListener {
            loginViewModel.login()
        }
    }

    private fun initializeCollector() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                loginViewModel.textFlow.collectLatest {
                    withContext(Dispatchers.Main) {
                        root.textFlow.text = it
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                loginViewModel.loginFlow.collect {
                    when (it) {
                        is LoginState.StartLogin -> {
                            openGoogleLogin()
                            loginViewModel.resetLoginState()
                        }

                        is LoginState.LoginSuccess -> {
                            requestPermission()
                        }

                        is LoginState.IDLE -> {

                        }

                        is LoginState.LoginFailed -> {

                        }
                    }
                }
            }
        }
    }

    private fun loginSuccess() {
        val intent = Intent(this, DashBoard::class.java)
        startActivity(intent)
        finish()
        screenOpenAnimation()
    }

    private fun requestPermission(){
        if(ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
           loginSuccess()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1134)
        }
    }
    private fun openGoogleLogin() {
        mAuth = FirebaseAuth.getInstance()
        createRequest()
        val sign = mGoogleSignClient?.signInIntent
        if (sign != null) {
            startActivityForResult(sign, RC_SIGN_IN)
        }
    }

    private fun createRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("473395054011-c8ck0bdakijq186hqhhn1q2hm6qkb2eo.apps.googleusercontent.com")
            .requestEmail()
            .requestProfile()
            .build()
        mGoogleSignClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                MrToast().error(this, e.toString())
            }
        }
        if (requestCode == 1134) {
            loginSuccess()
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth!!.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = mAuth?.currentUser
                val valueMap = hashMapOf<String, Any?>(
                    "name" to user?.displayName.toString(),
                    "email" to user?.email.toString(),
                    "photo" to user?.photoUrl.toString()
                )
                val saveDaModel = SaveDataModel("")
                saveDaModel.key = "User"
                saveDaModel.value = valueMap
                loginViewModel.saveUser(saveDaModel)

            } else {
                MrToast().error(this, task.exception.toString())
            }
        }
    }
}
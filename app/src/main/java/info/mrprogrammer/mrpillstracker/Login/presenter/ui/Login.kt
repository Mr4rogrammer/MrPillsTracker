package info.mrprogrammer.mrpillstracker.Login.presenter.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import info.mrprogrammer.mrpillstracker.Login.presenter.viewmodel.LoginViewModel
import info.mrprogrammer.mrpillstracker.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class Login : AppCompatActivity() {
    lateinit var root: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        root = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(root.root)
        initializeCollector()
    }

    private fun initializeCollector() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                loginViewModel.textFlow.collect {
                    withContext(Dispatchers.Main) {
                        root.textFlow.text = it
                    }
                }
            }
        }
    }
}
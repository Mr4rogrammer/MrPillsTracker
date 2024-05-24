package info.mrprogrammer.mrpillstracker.DashBoard.presenter.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import info.mrprogrammer.mrpillstracker.DashBoard.presenter.viewmodel.DashBoardViewModel
import info.mrprogrammer.mrpillstracker.R
import info.mrprogrammer.mrpillstracker.core.StatusBarHelper
import info.mrprogrammer.mrpillstracker.core.domain.model.UserDataModel
import info.mrprogrammer.mrpillstracker.core.utils.setFadeInAnimation
import info.mrprogrammer.mrpillstracker.databinding.DashboardMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DashBoard : AppCompatActivity() {
    private lateinit var root: DashboardMainBinding
    private val dashBoardViewModel: DashBoardViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = DashboardMainBinding.inflate(layoutInflater)
        root.root.setFadeInAnimation(this)
        setContentView(root.root)
        StatusBarHelper(this).setColor(window)
        initializeCollector()
    }

    private fun initializeCollector() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                dashBoardViewModel.userLoginDetails.collectLatest {
                    updateUserDataToUI(it)
                }
            }
        }
    }

    private fun updateUserDataToUI(userDataModel: UserDataModel?) {
        root.name.text = getString(R.string.hey, userDataModel?.name)
    }
}
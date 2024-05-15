package info.mrprogrammer.mrpillstracker.DashBoard.presenter.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashBoardViewModel : ViewModel() {
    private var _splashScreenState = MutableStateFlow(true)
    val splashScreenState = _splashScreenState.asStateFlow()

    fun hideSplashScreen() {
        _splashScreenState.update { false }
    }
}
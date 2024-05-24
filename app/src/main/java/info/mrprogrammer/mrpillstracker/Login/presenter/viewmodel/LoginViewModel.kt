package info.mrprogrammer.mrpillstracker.Login.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import info.mrprogrammer.mrpillstracker.Login.domain.use_cases.DoLogin
import info.mrprogrammer.mrpillstracker.Login.domain.use_cases.GetTextFlowData
import info.mrprogrammer.mrpillstracker.Login.presenter.utils.LoginState
import info.mrprogrammer.mrpillstracker.core.domain.use_case.SaveData
import info.mrprogrammer.mrpillstracker.core.domain.model.SaveDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getFlowText: GetTextFlowData,
    private val doLogin: DoLogin,
    private val saveData: SaveData
) : ViewModel() {

    private var _loginFlow  = MutableStateFlow<LoginState>(LoginState.IDLE)
    val loginFlow = _loginFlow.asStateFlow()

    private var _textFlow = MutableStateFlow("")
    val textFlow = _textFlow.asStateFlow()

    fun login() {
        viewModelScope.launch {
            doLogin().collect {
                if (it) {
                    _loginFlow.value = LoginState.StartLogin
                } else {
                    _loginFlow.value = LoginState.IDLE
                }
            }
        }
    }

    fun resetLoginState() {
        _loginFlow.value = LoginState.IDLE
    }

    fun saveUser(user: SaveDataModel?) {
        viewModelScope.launch {
            if (user != null) {
                saveData(user)
                _loginFlow.value = LoginState.LoginSuccess
            }
        }
    }

    init {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                getFlowText().collect {
                    _textFlow.value = it
                }
            }
        }
    }
}
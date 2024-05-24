package info.mrprogrammer.mrpillstracker.Login.presenter.utils

sealed class LoginState {
    data object LoginSuccess : LoginState()
    data object StartLogin : LoginState()
    data object LoginFailed : LoginState()
    data object IDLE : LoginState()
}
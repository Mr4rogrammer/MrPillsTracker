package info.mrprogrammer.mrpillstracker.Login.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import info.mrprogrammer.mrpillstracker.Login.domain.use_cases.GetTextFlowData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val getFlowText: GetTextFlowData) : ViewModel() {

    private var _textFlow = MutableStateFlow("")
    val textFlow = _textFlow.asStateFlow()

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
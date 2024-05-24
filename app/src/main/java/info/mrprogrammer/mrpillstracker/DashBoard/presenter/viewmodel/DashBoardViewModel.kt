package info.mrprogrammer.mrpillstracker.DashBoard.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import info.mrprogrammer.mrpillstracker.core.domain.model.UserDataModel
import info.mrprogrammer.mrpillstracker.core.domain.use_case.GetMedicationReminderData
import info.mrprogrammer.mrpillstracker.core.domain.use_case.GetUserLoginDetails
import info.mrprogrammer.mrpillstracker.core.domain.use_case.SaveData
import info.mrprogrammer.mrpillstracker.core.domain.use_case.SyncData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(private val getUserLoginDetails: GetUserLoginDetails,
    private val syncData: SyncData,
    private val getMedicineDetails: GetMedicationReminderData) :
    ViewModel() {
    private val _userLoginDetails = MutableStateFlow<UserDataModel?>(null)
    val userLoginDetails = _userLoginDetails

    private val _syncState = MutableStateFlow<Boolean>(false)
    val syncState = _userLoginDetails

    init {
        viewModelScope.launch {
            val result = getUserLoginDetails()
            _userLoginDetails.update { result }
        }

        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                syncData().collect {
                    _syncState.value = it
                }
            }
        }
    }


}
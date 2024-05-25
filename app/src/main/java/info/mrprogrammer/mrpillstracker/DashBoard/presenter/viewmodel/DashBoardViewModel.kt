package info.mrprogrammer.mrpillstracker.DashBoard.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import info.mrprogrammer.mrpillstracker.core.domain.model.SaveDataModel
import info.mrprogrammer.mrpillstracker.core.domain.model.UserDataModel
import info.mrprogrammer.mrpillstracker.core.domain.use_case.GetMedicationReminderData
import info.mrprogrammer.mrpillstracker.core.domain.use_case.GetUserLoginDetails
import info.mrprogrammer.mrpillstracker.core.domain.use_case.SaveData
import info.mrprogrammer.mrpillstracker.core.domain.use_case.SyncData
import info.mrprogrammer.mrpillstracker.core.utils.generatePrimaryKey
import info.mrprogrammer.mrpillstracker.core.utils.getCurrentDate
import info.mrprogrammer.mrpillstracker.core.utils.getCurrentTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val getUserLoginDetails: GetUserLoginDetails,
    private val syncData: SyncData,
    private val getMedicineDetails: GetMedicationReminderData,
    private val saveData: SaveData
) :
    ViewModel() {

    private var _userLoginDetails = MutableStateFlow<UserDataModel?>(null)
    val userLoginDetails = _userLoginDetails

    private var _syncState = MutableStateFlow<Boolean>(false)
    val syncState = _userLoginDetails

    private var _medicineDetails = MutableStateFlow<List<MedicineReminder>?>(null)
    val medicineDetails = _medicineDetails

    private var _medicineDetailProgress = MutableStateFlow<Float>(0.0f)
    val medicineDetailProgress = _medicineDetailProgress

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

        viewModelScope.launch {
            getMedicineDetails().collect {
                _medicineDetails.value = it
            }
        }
    }

    fun getTodayWeeklyDay(): String {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> ""
        }
    }

    fun calculateProgress() {
        val progress = 50;
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                for (i in 0..progress) {
                    _medicineDetailProgress.value = i.toFloat()
                    delay(30)
                }
            }
        }
    }

    fun markThisMedicineAsDone(id: String) {
        val resultModel = SaveDataModel()
        resultModel.key = "data"
        resultModel.endKey = "pillsData/$id/takenStatus"
        resultModel.type = "push"
        val valueMap = hashMapOf<String, Any?>(
            "id" to generatePrimaryKey(),
            "time" to getCurrentTime(),
            "date" to getCurrentDate()
        )
        resultModel.value = valueMap
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                saveData(resultModel)
            }
        }
    }
}
package info.mrprogrammer.mrpillstracker.DashBoard.presenter.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrprogrammer.Utils.Toast.MrToast
import dagger.hilt.android.AndroidEntryPoint
import info.mrprogrammer.mrpillstracker.DashBoard.presenter.adapter.RecyclerViewAdapter
import info.mrprogrammer.mrpillstracker.DashBoard.presenter.adapter.helper.SwipeToDeleteCallBack
import info.mrprogrammer.mrpillstracker.DashBoard.presenter.viewmodel.DashBoardViewModel
import info.mrprogrammer.mrpillstracker.R
import info.mrprogrammer.mrpillstracker.core.StatusBarHelper
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import info.mrprogrammer.mrpillstracker.core.domain.model.UserDataModel
import info.mrprogrammer.mrpillstracker.core.utils.conformationDialog
import info.mrprogrammer.mrpillstracker.core.utils.isConnected
import info.mrprogrammer.mrpillstracker.core.utils.setFadeInAnimation
import info.mrprogrammer.mrpillstracker.databinding.DashboardMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class DashBoard : AppCompatActivity() {
    private lateinit var root: DashboardMainBinding
    private val dashBoardViewModel: DashBoardViewModel by viewModels()
    lateinit var adapter: RecyclerViewAdapter
    private var medicineReminders: MutableList<MedicineReminder> = mutableListOf()
    private var currentPercentage = 0f
    private var isDataAvailable = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = DashboardMainBinding.inflate(layoutInflater)
        root.root.setFadeInAnimation(this)
        setContentView(root.root)
        StatusBarHelper(this).setColor(window)
        initialize()
        initializeCollector()
    }

    private fun initialize() {
        val swipeToDeleteCallBack: SwipeToDeleteCallBack = object : SwipeToDeleteCallBack(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (isConnected(this@DashBoard)) {
                    conformationDialog(
                        this@DashBoard,
                        "Are you sure you want to mark this medicine as taken?",
                        "Yes",
                        "No",
                        positiveButtonOnClick = {
                            dashBoardViewModel.markThisMedicineAsDone(medicineReminders[viewHolder.adapterPosition].id)
                        },
                        negativeButtonOnClick = {
                            adapter.notifyItemChanged(viewHolder.adapterPosition)
                        })
                } else {
                    MrToast().message(this@DashBoard, "No Internet Connection...")
                }
            }
        }
        adapter = RecyclerViewAdapter(medicineReminders, this)
        root.medicineData.layoutManager = LinearLayoutManager(this)
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(root.medicineData)
        root.medicineData.adapter = adapter
    }

    private fun initializeCollector() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                dashBoardViewModel.userLoginDetails.collectLatest {
                    updateUserDataToUI(it)
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                dashBoardViewModel.medicineDetails.collectLatest {
                    updateMedicineDataToUI(it)
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                dashBoardViewModel.medicineDetailProgress.collectLatest {
                    withContext(Dispatchers.Main) {
                        updateMedicineReminderProgressDataToUI(it)
                    }
                }
            }
        }
    }

    private fun updateMedicineReminderProgressDataToUI(fl: Float) {
        updateInfoUi()
        currentPercentage = fl
        root.circularProgress.setProgress(fl.toDouble(), 100.0);
        val resultText = if (fl < 30) {
            getString(R.string.yet_to_start_your_plan)
        } else if (fl >= 30 && fl < 70) {
            getString(R.string.you_have_reach_half_of_your_plan)
        } else if (fl >= 70 && fl < 100) {
            getString(R.string.your_plan_nis_almost_done)
        } else if (fl == 100f) {
            getString(R.string.you_have_reach_your_plan)
        } else {
            getString(R.string.your_plan_nis_almost_done)
        }
        root.progressText.text = resultText
    }

    private fun updateInfoUi() {
        if (isDataAvailable) return
        val text = if (currentPercentage == 0f) {
            "\uD83D\uDE14 No Data found, Kindly login into your account to add data in web."
        } else  {
            "\uD83D\uDE0D Your have archived your today's goal."
        }
        root.infoText.text = text
        root.infoText.visibility = VISIBLE
    }

    private fun updateMedicineDataToUI(data: List<MedicineReminder>?) {
        if (data != null) {
            isDataAvailable = data.isNotEmpty()
            root.infoText.visibility = GONE
            medicineReminders.clear()
            medicineReminders.addAll(data)
            adapter.notifyDataSetChanged()
        } else {
            isDataAvailable = false
        }
        updateInfoUi()
    }

    private fun updateUserDataToUI(userDataModel: UserDataModel?) {
        root.name.text = getString(R.string.hey, userDataModel?.name)
        root.day.text = dashBoardViewModel.getTodayWeeklyDay()
    }
}
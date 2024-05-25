package info.mrprogrammer.mrpillstracker.DashBoard.presenter.ui

import android.os.Bundle
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
                    dashBoardViewModel.calculateProgress()
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
        root.circularProgress.setProgress(fl.toDouble(), 100.0);
    }

    private fun updateMedicineDataToUI(data: List<MedicineReminder>?) {
        if (data != null) {
            medicineReminders.clear()
            medicineReminders.addAll(data)
            adapter.notifyDataSetChanged()
        }
    }

    private fun updateUserDataToUI(userDataModel: UserDataModel?) {
        root.name.text = getString(R.string.hey, userDataModel?.name)
        root.day.text = dashBoardViewModel.getTodayWeeklyDay()

    }
}
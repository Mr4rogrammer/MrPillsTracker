package info.mrprogrammer.mrpillstracker.DashBoard.presenter.adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import info.mrprogrammer.mrpillstracker.DashBoard.presenter.adapter.helper.AdapterHelper
import info.mrprogrammer.mrpillstracker.DashBoard.presenter.adapter.helper.AdapterHelper.speak
import info.mrprogrammer.mrpillstracker.DashBoard.presenter.ui.DashBoard
import info.mrprogrammer.mrpillstracker.R
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import info.mrprogrammer.mrpillstracker.core.utils.calculateRemainingDays
import info.mrprogrammer.mrpillstracker.core.utils.convertTo12HourFormat
import info.mrprogrammer.mrpillstracker.core.utils.loadUrl


class RecyclerViewAdapter(private val itemList: List<MedicineReminder>, private val context: Context) : RecyclerView.Adapter<MyViewHolder>(){
    private var lastDisplayedTime:String = ""


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.medicine_data_child, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == 0) {
            lastDisplayedTime = ""
        }
        val item: MedicineReminder = itemList[position]

        val reamingDate = calculateRemainingDays(item.date, item.noOfDay.toLong())

        if (lastDisplayedTime == "" || lastDisplayedTime != item.time) {
            holder.timing.text = convertTo12HourFormat(item.time)
            holder.timing.visibility = View.VISIBLE
            lastDisplayedTime = item.time
        } else {
            holder.timing.visibility = View.GONE
        }

        holder.medicineName.setText(item.pillName)
        holder.medicineNameAfterMeal.text = "take 1 ${item.pillName} before meal".takeIf { item.afterOrBefore == "BEFORE" } ?:"take 1 ${item.pillName} after meal"
        holder.medicineImage.loadUrl(AdapterHelper.getIconUrl(item.selectedicon))
        holder.medicineNumberOfDays.text = "${reamingDate} days"

        holder.rootLayout.setOnLongClickListener {
            speak(context, item.notification)
            true
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var medicineName: TextView = itemView.findViewById(R.id.medicineName)
    var medicineNameAfterMeal: TextView = itemView.findViewById(R.id.medicineNameAfterMeal)
    var medicineNumberOfDays: TextView = itemView.findViewById(R.id.medicineNumberOfDays)
    var medicineImage: ImageView = itemView.findViewById(R.id.medicineImage)
    var timing: TextView = itemView.findViewById(R.id.timing)
    var rootLayout: LinearLayout = itemView.findViewById(R.id.rootLayout)
}

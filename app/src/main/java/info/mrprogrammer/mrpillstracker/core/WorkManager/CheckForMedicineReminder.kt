package info.mrprogrammer.mrpillstracker.core.WorkManager

import android.content.Context
import android.util.Log
import info.mrprogrammer.mrpillstracker.DashBoard.presenter.adapter.helper.AdapterHelper.speak
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import info.mrprogrammer.mrpillstracker.core.utils.ShowNotification
import info.mrprogrammer.mrpillstracker.core.utils.filterDatesLessThanOrEqualToToday
import info.mrprogrammer.mrpillstracker.core.utils.getCurrentTime
import io.realm.Realm
import io.realm.RealmResults

object CheckForMedicineReminder {
    fun startBackgroundWork(context: Context) {
        Log.d("CheckForMedicineReminder", "Inside Worker")
        val realm = Realm.getDefaultInstance()
        realm.refresh()
        val results: RealmResults<MedicineReminder> = realm.where(MedicineReminder::class.java).findAll()
        val medicineRemindersForToday = filterDatesLessThanOrEqualToToday(realm.copyFromRealm(results))

        medicineRemindersForToday.reminders?.forEach {
            val currentTime = getCurrentTime()
            Log.d("CheckForMedicineReminder", "currentTime: $currentTime")
            Log.d("CheckForMedicineReminder", "MedicineReminder time: ${it.time}")
            if (it.time == currentTime) {
                Log.d("CheckForMedicineReminder", "Success")
                ShowNotification.showNotification(context, it.pillName, it.notification)
                speak(context, it.notification)
            }
        }
    }
}
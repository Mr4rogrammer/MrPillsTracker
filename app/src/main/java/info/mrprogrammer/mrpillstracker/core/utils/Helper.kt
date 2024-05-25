package info.mrprogrammer.mrpillstracker.core.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.SystemClock
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import info.mrprogrammer.mrpillstracker.R
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminder
import info.mrprogrammer.mrpillstracker.core.domain.model.MedicineReminderWrapper
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun View.setDebouncedOnClickListener(interval: Long = 2000L, onClick: (View) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener { view ->
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime >= interval) {
            lastClickTime = currentTime
            onClick(view)
        }
    }
}

fun View.setFadeInAnimation(context: Context) {
    val fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    startAnimation(fadeInAnimation)
}

fun Activity.screenOpenAnimation() {
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

fun Activity.screenExitAnimation() {
    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}

fun ImageView.loadUrl(url: String) {
    val imageLoader = ImageLoader.Builder(context)
        .components { add(SvgDecoder.Factory()) }
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .build()

    val request = ImageRequest.Builder(context)
        .crossfade(true)
        .crossfade(500)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .data(url)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}

fun filterDatesLessThanOrEqualToToday(data: List<MedicineReminder>):MedicineReminderWrapper {
    var totalMedicineForToday = 0
    var totalMedicineTakenToday = 0
    val result = mutableListOf<MedicineReminder>()
    val today = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    try {
        result.addAll(
            data.filter {
                val date = LocalDate.parse(it.date, dateFormatter)
                val isMedicineTakenToday = filterTodayTaken(it)
                if (isMedicineTakenToday) {
                    totalMedicineTakenToday++
                }
                val isThisMedicineShouldTakeToday = date <= today
                val remainingDays = calculateRemainingDays(it.date, it.noOfDay.toLong())
                if (isThisMedicineShouldTakeToday && remainingDays > 0) {
                    totalMedicineForToday++
                }
                isThisMedicineShouldTakeToday && remainingDays > 0 && !isMedicineTakenToday
            }
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
    val finalResult = sortModelsByTime(result)
    val medicineReminderWrapper = MedicineReminderWrapper()
    medicineReminderWrapper.totalMedicineForToday = totalMedicineForToday
    medicineReminderWrapper.totalTakeMedicineToday = totalMedicineTakenToday
    medicineReminderWrapper.reminders = finalResult
    return medicineReminderWrapper
}

fun filterTodayTaken(medicineReminder: MedicineReminder): Boolean {
    medicineReminder.takenStatus?.forEach {
        if (it.date == getCurrentDate()) {
            return true
        }
    }
    return false
}

fun sortModelsByTime(models: List<MedicineReminder>): List<MedicineReminder> {
    val timeComparator = Comparator<MedicineReminder> { model1, model2 ->
        val time1 = LocalTime.parse(model1.time, DateTimeFormatter.ofPattern("HH:mm"))
        val time2 = LocalTime.parse(model2.time, DateTimeFormatter.ofPattern("HH:mm"))
        time1.compareTo(time2)
    }
    return models.sortedWith(timeComparator)
}

fun calculateRemainingDays(date: String, totalDays: Long): Long {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val startDate = LocalDate.parse(date, dateFormatter)
    val endDate = startDate.plusDays(totalDays)
    val currentDate = LocalDate.now()
    val remainingDays = ChronoUnit.DAYS.between(currentDate, endDate)
    return remainingDays
}

fun convertTo12HourFormat(time: String): String {
    val time24 = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
    return time24.format(DateTimeFormatter.ofPattern("hh:mm a"))
}

fun conformationDialog(
    context: Context,
    message: String,
    positiveButton: String,
    negativeButton: String,
    positiveButtonOnClick: (() -> Unit?)? = null,
    negativeButtonOnClick: (() -> Unit?)? = null
) {
    MaterialAlertDialogBuilder(context)
        .setCancelable(false)
        .setMessage(message)
        .setNeutralButton(negativeButton) { dialog, _ ->
            negativeButtonOnClick?.invoke()
            dialog.dismiss()
        }
        .setPositiveButton(positiveButton) { dialog, which ->
            positiveButtonOnClick?.invoke()
            dialog.dismiss()
        }
        .show()
}

fun getCurrentTime(): String {
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return currentTime.format(formatter)
}

fun generatePrimaryKey(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSSSSS")
    val formattedDateTime = currentDateTime.format(formatter)
    return formattedDateTime
}

fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return currentDate.format(formatter)
}

fun isConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val info = connectivityManager.allNetworkInfo
        if (info != null) {
            for (networkInfo in info) {
                if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
    }
    return false
}
package info.mrprogrammer.mrpillstracker.core.utils

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.view.View
import android.view.animation.AnimationUtils
import info.mrprogrammer.mrpillstracker.R

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
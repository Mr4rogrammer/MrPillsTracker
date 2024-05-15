package info.mrprogrammer.mrpillstracker.core

import android.content.Context
import android.os.Build
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import info.mrprogrammer.mrpillstracker.R

class StatusBarHelper(private val appContext: Context) {

    fun setColor(window: Window?, color: Int = R.color.white) {
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(appContext, color)
    }
}
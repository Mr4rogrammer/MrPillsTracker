package info.mrprogrammer.mrpillstracker.DashBoard.presenter.adapter.helper

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

object AdapterHelper {
    private var textToSpeechEngine:TextToSpeech? = null

    fun getIconUrl(iconInt: Int): String {
        return when(iconInt) {
            0 -> "https://firebasestorage.googleapis.com/v0/b/mrpillstracker.appspot.com/o/liquid.svg?alt=media&token=cd3f9ab5-d458-4c6d-81d0-96b2ce8fefb3"

            1 -> "https://firebasestorage.googleapis.com/v0/b/mrpillstracker.appspot.com/o/tablet.svg?alt=media&token=12ef9221-1e8a-47da-8b8c-ecc42292d8a4"

            2 -> "https://firebasestorage.googleapis.com/v0/b/mrpillstracker.appspot.com/o/inhaile.svg?alt=media&token=73354126-1d1f-45af-9946-89fc2d33b7b3"

            3 -> "https://firebasestorage.googleapis.com/v0/b/mrpillstracker.appspot.com/o/capsule.svg?alt=media&token=a7766baa-4fb7-4976-87e6-f16edd4c34cc"

            else -> "https://firebasestorage.googleapis.com/v0/b/mrpillstracker.appspot.com/o/injection.svg?alt=media&token=b11e90fa-6fb8-4305-a8c6-ca2824f5ec8f"

        }
    }

    fun speak(context: Context, message: String){
         textToSpeechEngine =TextToSpeech(context) { status: Int ->
            if (status != TextToSpeech.ERROR) {
                textToSpeechEngine?.setLanguage(Locale.ENGLISH)
                textToSpeechEngine?.setSpeechRate(0.8f)
                textToSpeechEngine?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        };
    }
}
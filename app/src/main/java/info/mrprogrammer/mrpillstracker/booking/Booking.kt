package info.mrprogrammer.mrpillstracker.booking

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import info.mrprogrammer.mrpillstracker.R
import info.mrprogrammer.mrpillstracker.core.utils.FirebaseHelper.firebaseClearString
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.coroutines.resume


class Booking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var nameStatusList by remember { mutableStateOf<List<Triple<String,String, String>>>(emptyList()) }

            suspend fun getData() {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    val url = "data/"+currentUser.email.firebaseClearString() + "/booking"
                    val dataRef = FirebaseDatabase.getInstance().getReference(url)
                    dataRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val tempList = mutableListOf<Triple<String, String, String>>()
                            for (child in snapshot.children) {
                                val name = child.child("text").getValue(String::class.java) ?: "Unknown"
                                val date = child.child("date").getValue(String::class.java) ?: "Unknown"
                                val status = child.child("status").getValue(String::class.java) ?: "Unknown"
                                tempList.add(Triple(name, date, status))
                            }
                            nameStatusList = tempList // Update state
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle error if needed
                        }
                    })


                }
            }

            LaunchedEffect(Unit) {
                getData()
            }

            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    NameStatusListScreen(
                        data = nameStatusList
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    BookingScreen() { text, date ->
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val map = hashMapOf(
                            "text" to text,
                            "date" to date,
                            "status" to "Waiting For Conformation"
                        )
                        if (currentUser != null) {
                            val url = "data/"+currentUser.email.firebaseClearString() + "/booking/${UUID.randomUUID()}"
                            val dataRef = FirebaseDatabase.getInstance().getReference(url)
                            dataRef.setValue(map).addOnCompleteListener {
                               lifecycleScope.launch {
                                   getData()
                               }
                            }
                        }
                    }
                }
            }
        }
    }
}
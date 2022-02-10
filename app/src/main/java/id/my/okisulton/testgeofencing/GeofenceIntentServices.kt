package id.my.okisulton.testgeofencing

import android.app.IntentService
import android.content.Intent
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

/**
 * Created by osalimi on 10-02-2022.
 */
class GeofenceIntentServices : IntentService("GeofenceIntentServices") {



    override fun onHandleIntent(intent: Intent?) {

        assert(intent != null)
        val geofencingEvent = GeofencingEvent.fromIntent(intent!!)
        if (geofencingEvent.hasError()) {
            Toast.makeText(
                applicationContext,
                "Error " + geofencingEvent.errorCode,
                Toast.LENGTH_SHORT
            ).show()
        }
        val geofencingTransition = geofencingEvent.geofenceTransition
        if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            Toast.makeText(applicationContext, "Anda berada kawasan", Toast.LENGTH_SHORT).show()
            sendDataToActivity(Geofence.GEOFENCE_TRANSITION_DWELL)
        } else if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Toast.makeText(applicationContext, "Anda memasuki kawasan", Toast.LENGTH_SHORT).show()
            sendDataToActivity(Geofence.GEOFENCE_TRANSITION_ENTER)
        } else if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Toast.makeText(applicationContext, "Anda keluar kawasan", Toast.LENGTH_SHORT).show()
            sendDataToActivity(Geofence.GEOFENCE_TRANSITION_EXIT)
        }
    }

    private fun sendDataToActivity(messages: Int) {
        val intent = Intent("RestApiData")
        intent.putExtra("token", messages)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

}
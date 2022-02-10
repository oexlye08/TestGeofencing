package id.my.okisulton.testgeofencing;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;

/**
 * Created by osalimi on 10-02-2022.
 **/
public class GeofenceIntentServices extends IntentService {

    public GeofenceIntentServices() {
        super("GeofenceIntentServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        GeofencingEvent geofencingEvent =
                GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()){
            Toast.makeText(getApplicationContext(), "Error "+ geofencingEvent.getErrorCode(), Toast.LENGTH_SHORT).show();
        }

        int geofencingTransition = geofencingEvent.getGeofenceTransition();
        if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_DWELL){
            Toast.makeText(getApplicationContext(), "Anda berada kawasan", Toast.LENGTH_SHORT).show();
        }else if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            Toast.makeText(getApplicationContext(), "Anda memasuki kawasan", Toast.LENGTH_SHORT).show();
        }else if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            Toast.makeText(getApplicationContext(), "Anda keluar kawasan", Toast.LENGTH_SHORT).show();
        }
    }
}

package id.my.okisulton.testgeofencing

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kotlin.math.log


class MainActivity : AppCompatActivity(), LocationListener {
    private var valLatitude: TextView? = null
    private var valLongitude: TextView? = null
    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        LocalBroadcastManager.getInstance(this).registerReceiver(tokenPassingReceiver, IntentFilter("RestApiData"))

        valLatitude = findViewById(R.id.valLatitude)
        valLongitude = findViewById(R.id.valLongitude)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 1
            )
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            location

            //Geofencing Client
            val geofencingClient = LocationServices.getGeofencingClient(this)
            geofencingClient.addGeofences(createGeofencingRequest(), createGeofencePendingIntent())
                .addOnSuccessListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Success adding geofence",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    Toast.makeText(
                        this@MainActivity,
                        "Fail Error Code = " + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }


    }

    private val tokenPassingReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bundle = intent.extras
            if (bundle != null) {
                if (bundle.containsKey("token")) {
                    val tokenData = bundle.getInt("token")
                    Log.d("MainActivity--","token--$tokenData")

                    if (tokenData == 1 || tokenData == 4) {
                        Log.d("action", "onReceive: Didalam")
                    } else {
                        Log.d("action", "onReceive: Di luar")
                    }

                }
            }
        }
    }

    private fun createGeofencingRequest(): GeofencingRequest {
        val builder = GeofencingRequest.Builder()
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
        builder.addGeofences(createGeofenceList())
        return builder.build()
    }

    private fun createGeofencePendingIntent(): PendingIntent {
        val intent = Intent(this, GeofenceIntentServices::class.java)
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createGeofenceList(): List<Geofence> {
        val geofenceList: MutableList<Geofence> = ArrayList()
        val geofence = Geofence.Builder().setRequestId("PASEKAN")
            .setCircularRegion(
                -7.7647,
                110.4366, 1000f
            )
            .setLoiteringDelay(1000)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(
                Geofence.GEOFENCE_TRANSITION_DWELL or
                        Geofence.GEOFENCE_TRANSITION_ENTER or
                        Geofence.GEOFENCE_TRANSITION_EXIT
            )
            .build()
        geofenceList.add(geofence)
        return geofenceList
    }

    private val location: Unit
        get() {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    this,
                    "Access Fine Location permission not granted",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000, 1f, this
            )
        }

    override fun onLocationChanged(location: Location) {
        Log.d("locasi", "onLocationChanged: " + location.latitude)
        if (location != null) {
            valLatitude!!.text = location.latitude.toString()
            valLongitude!!.text = location.longitude.toString()
        }
    }
}
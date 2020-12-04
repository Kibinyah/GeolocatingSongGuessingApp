//Kevin Pan 969449
package com.example.songguessinggame

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.InputStream

import kotlin.random.Random

//The main game activity that shows the map and buttons to go to other activites
class MapsActivity : AppCompatActivity(), OnMapReadyCallback{

    val PERMISSION_ID = 42
    private lateinit var mMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mFusedLocationClient: FusedLocationProviderClient


    lateinit var countDownTimer: CountDownTimer
    val initialCountDown: Long = 30000
    val countDownInterval: Long = 1000
    var timeLeft : Long = initialCountDown

    var userLat: Double = 0.0
    var userLong: Double = 0.0
    var userLastLoc = LatLng(userLat,userLong)
    private lateinit var userLocation : Location
    val lyricsList = mutableListOf<String>()

    var timetrial : Boolean = false
    var firstMarkerVisited : Boolean = false



    //These variables are in companion so that it is readable from the Lyrics Class
    companion object{
        var songLyrics = ArrayList<String>()
        var songTxtFile : String = ""

    }
    //When activity is opened, shows xml file.
    //Applies functions on buttons and map.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        //Passes timetrial value from choosemode activity to this variable
        timetrial = intent.getBooleanExtra("TIMETRIAL",false)

        //When guess button is clicked, shows the Guess Activity on screen
        guessButton.setOnClickListener {
            //The guess activity will only open if the user has visited a marker.
            if(firstMarkerVisited == true) {
                val intent = Intent(this, Guess::class.java)
                startActivity(intent)
            }
        }

        //When lyric button is clicked, shows the Lyrics Activity on screen
        lyricButton.setOnClickListener {
            val intent = Intent(this, Lyrics::class.java)
            startActivity(intent)
        }

        //When give up button is clicked, moves activity to the main menu
        giveUpButton.setOnClickListener {
            //Removes countdowntimer when this button is clicked if there is one active
            if(timetrial == true){
                countDownTimer.cancel()
            }
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)

        }

        //Calls readFile to select a song file and puts lyrics in an arraylist.
        readFile()

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Gets user location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        requestNewLocationData()

        //Sets time trial feature if it was true from choosemode activity
        if(timetrial == true) {
            startTimer()
        }
    }

    //Starts the countdown timer
    private fun startTimer(){

        //Creates a countdown timer object that starts at 30 seconds and ticks down 1 second at a time
        countDownTimer = object : CountDownTimer(timeLeft, countDownInterval){

            //Outputs the timer after ticking down one interval
            override fun onTick(timeTillFinished: Long){
                timeLeft = timeTillFinished
                val timeTextView = findViewById<TextView>(R.id.timerView)
                timeTextView.text = "Time remaining: " + (timeLeft/1000).toString()
            }

            //When countdown timer hits 0 seconds, runs method to go to EndScreen activity
            override fun onFinish(){
                toEndScreen()
            }
        }.start()
    }

    //Switches activity to the EndScreen activity
    private fun toEndScreen(){
        val intent = Intent(this, EndScreen::class.java)
        //passes a boolean value to the next activity
        intent.putExtra("LOSE", true)
        startActivity(intent)
    }

    //Places 10 randomly placed markers on the map
    private fun placeRandomMarkers() {
        for (i in 0..9){
            //gets a random song lyric from the arraylist
            var random = Random.nextInt(lyricsList.size)
            var songLyric = lyricsList[random]

            var rndLat = Random.nextDouble(51.617715, 51.619181)
            var rndLong = Random.nextDouble(-3.880354,-3.876085)
            //Places marker of random location within bay campus with a random lyric and a custom marker icon
            mMap.addMarker(MarkerOptions().position(LatLng(rndLat,rndLong)).title(songLyric)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.note_marker))
        }
    }

    //Adds songs to an arraylist from current or classic directory
    //Randomly selects a song and adds the lyrics of that song to another arraylist
    private fun readFile(){
        //Gets the game mode that was passed from the choosemode activity
        val intent = intent
        val mode = intent.getStringExtra("MODE")

        //Displays the mode in the interface
        val textView = findViewById<TextView>(R.id.modeText)
        textView.text = mode


        var inputStream: InputStream? = null
        var songlist: ArrayList<String> = ArrayList()
        var random : Int = 0

        //If the mode is classic, adds all songs to an arraylist and selects a random song from it
        if(mode == "Classic"){
            //Puts all songs in classic as a list
            var list = assets.list("Classic")

            //Iterates through the list and adds each item to arraylist
            (0..17).forEach(){
                songlist.add(list!!.get(it))
            }

            //Selects and opens a random song from the arraylist
            random = Random.nextInt(0,17)
            songTxtFile = songlist.get(random)
            inputStream = assets.open("Classic/$songTxtFile")

        //If the mode is current, adds all songs to an arraylist and selects random song from it
        }else if (mode == "Current"){
            //Puts all songs in current as a list
            var list = assets.list("Current")

            //Iterates through the list and adds each item to arraylist
            (0..19).forEach(){
                songlist.add(list!!.get(it))
            }

            //Selects and opens a random song from the arraylist
            random = Random.nextInt(0,19)
            songTxtFile = songlist.get(random)
            inputStream = assets.open("Current/$songTxtFile")
        }
        //Reads the song file line by line and adds it to another arraylist called "lyricsList"
        inputStream!!.bufferedReader().useLines {lines ->
            lines.forEach {
                lyricsList.add(it)
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Place random markers with their assigned lyric
        placeRandomMarkers()
        mMap.uiSettings.isZoomControlsEnabled = true

        //Zooms camera in on bay campus
        val coFo = LatLng(51.619543, -3.878634)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(coFo))
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(coFo, 15f))

        //When marker is clicked, shows lyric and makes a sound if it is within a distance
        mMap!!.setOnMarkerClickListener { marker ->
            //get current users location
            getLastLocation()

            //get the distance from user to a marker from calculateDistance function
            var distance = calculateDistance(marker)

            //if the distance is less than 30 metres, marker will show lyric and add lyric to an arraylist of collected lyrics
            if(distance < 30){
                marker.showInfoWindow()

                //if the lyric is not in the arraylist, add it to the arraylist and sets a new icon for the marker.
                if(!songLyrics.contains(marker.title)) {
                    songLyrics.add(marker.title)
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.used_note_marker))

                    //if the time trial feature is on, reset the timer back to 20 seconds
                    if(timetrial == true) {
                        countDownTimer.cancel()
                        countDownTimer.start()
                    }
                    //play sound when marker is clicked
                    playSound()
                    //sets this boolean value to be true so the user can use the guess button.
                    firstMarkerVisited = true
                }
            }else{
                //Lyric is not shown when the distance is larger than 30 metres
                marker.hideInfoWindow()
            }
            true
        }
    }

    //Loads the sound file from directory and plays the sound
    private fun playSound(){
        var soundEffect = MediaPlayer.create(this,R.raw.ding)
        soundEffect.start()
    }

    //Returns the distance between user and marker using Haversine formula
    private fun calculateDistance(marker: Marker) : Double{

        var markerPosition = marker.position
        var markerLong = markerPosition.longitude
        var markerLat = markerPosition.latitude

        //The differences between longitudes and latitudes
        var dLat = Math.toRadians(markerLong - userLong)
        var dLong = Math.toRadians(markerLat - userLat)

        //Convert to radians
        markerLat = Math.toRadians(markerLat)
        userLat = Math.toRadians(userLat)

        //Apply Haversine formula to get the distance from user to marker
        var a = Math.pow(Math.sin(dLat/2), 2.0) +
                Math.pow(Math.sin(dLong/2), 2.0) *
                Math.cos(markerLat) * Math.cos(userLat)
        var rad = 6371
        var c = 2 * Math.asin(Math.sqrt(a))
        var result = rad * c
        return result * 1000
    }

    //Gets the user's last known gps location
    private fun getLastLocation(){
        if(checkPermissions()){
            if(isLocationEnabled()){
                mFusedLocationClient.lastLocation.addOnCompleteListener(this){task ->
                    var location: Location? = task.result
                    if(location == null) {
                        requestNewLocationData()
                    }else{
                        userLat = location.latitude
                        userLong = location.longitude
                        var accuracy = location.accuracy
                        userLastLoc = LatLng(userLat, userLong)
                        userLocation = location
                        mMap.setMyLocationEnabled(true)
                    }
                }
            }else{
                Toast.makeText(this,"Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            requestPermissions()
        }
    }
    //Checks if location permissions are allowed
    private fun checkPermissions(): Boolean {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    //Checks if location is enabled
    private fun isLocationEnabled(): Boolean{
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    //Gets new user location data
    private fun requestNewLocationData(){
        Log.i("myLocation","request")
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 2000
        mLocationRequest.fastestInterval = 1000

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    private val mLocationCallback = object: LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult){
            Log.i("myLocation","Callback")
            mMap.setMyLocationEnabled(true)
        }
    }

    //Requests permission for gps location
    private fun requestPermissions(){
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_ID)
        getLastLocation()
    }

    //Gets result of gps permissions
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray){
        if(requestCode == PERMISSION_ID){
            if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                getLastLocation()
            }
        }
    }
}

//Kevin Pan 969449
package com.example.songguessinggame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.solver.Metrics
import kotlinx.android.synthetic.main.activity_guess.*
import java.util.regex.Matcher
import java.util.regex.Pattern

//Pop Up window for the user to guess the song name and artist
class Guess : AppCompatActivity() {

    private var dm : DisplayMetrics = DisplayMetrics()
    var badguesses = 0

    ////When activity is created, it will show UI with xml file and apply actions for the buttons
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess)

        //Displays a customised popup window
        windowManager.defaultDisplay.getMetrics(dm)
        val width = (dm.widthPixels)*.8
        val height = (dm.heightPixels)*.6
        window.setLayout(width.toInt(),height.toInt())
        var params = window.attributes
        params.gravity = Gravity.CENTER
        params.x = 0
        params.y = -20
        getWindow().setAttributes(params)

        //A close button when clicked, closes the popup window
        closeButton.setOnClickListener(){
            finish()
        }

        //When the submit button is clicked, it will check if guesses are correct and will respond accordingly
        submitButton.setOnClickListener() {
            //Get guess song name and guess artist name
            var songText = findViewById<EditText>(R.id.songInput)
            var artistText = findViewById<EditText>(R.id.artistInput)
            var song = songText.text.toString()
            var artist = artistText.text.toString()
            //Converts spaces to underscores to match format
            song = song.replace(" ","_").toLowerCase()
            artist = artist.replace(" ","_").toLowerCase()

            //Get real artist name by selecting text before "("
            var realSong = MapsActivity.Companion.songTxtFile
            var realSongName: String = ""
            var realArtistName: String = realSong.split("(")[0]

            //Get real song name by selecting text between brackets
            var m: Matcher = Pattern.compile("\\(([^)]+)\\)").matcher(realSong)
            while (m.find()) {
                realSongName = m.group(1)
            }

            //If the song names and artist names match, go to end game screen
            if (realSongName == song) {
                if (realArtistName == artist) {
                    val intent = Intent(this, EndScreen::class.java)
                    //intent.putExtra("BADGUESSES", badguesses)
                    startActivity(intent)
                } else {
                    //If artist name incorrect, display text to user
                    val textView = findViewById<View>(R.id.loseText) as TextView
                    loseText.text = "Incorrect Answers"
                }
            } else {
                //If artist name incorrect, display text to user
                val textView = findViewById<View>(R.id.loseText) as TextView
                loseText.text = "Incorrect Answers"
            }

            //Increment bad guesses by 1
            badguesses += 1

            //If bad guesses total up to 3, then go to end game screen
            //Pass number of bad guesses which will be used to display "You Lose"
            if(badguesses == 3) {
                val intent = Intent(this, EndScreen::class.java)
                intent.putExtra("BADGUESSES", 3)
                startActivity(intent)
            }

        }
    }
}

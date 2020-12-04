//Kevin Pan 969449
package com.example.songguessinggame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_lyrics.*

//The layout manager of the lyrics RecyclerView on a popup window
class Lyrics : AppCompatActivity() {

    private var dm : DisplayMetrics = DisplayMetrics()
    var lyrics = ArrayList<String>()

    //Shows UI with xml file and assigns functionality to RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics)

        //Create a customised popup window
        windowManager.defaultDisplay.getMetrics(dm)
        val width = (dm.widthPixels)*.8
        val height = (dm.heightPixels)*.7
        window.setLayout(width.toInt(),height.toInt())
        var params = window.attributes
        params.gravity = Gravity.CENTER
        params.x = 0
        params.y = -20
        getWindow().setAttributes(params)

        //When closed button is clicked, the popup window is closed.
        closeButton.setOnClickListener(){
            finish()
        }

        //Adds the list of lyrics from MapsActivity to RecyclerView using the LyricAdapter to make it function
        var recView = findViewById<RecyclerView>(R.id.recyclerView)
        recView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        lyrics.addAll(MapsActivity.Companion.songLyrics)
        val adapter = LyricAdapter(lyrics)
        recView.adapter = adapter
    }
}

//Kevin Pan 969449
package com.example.songguessinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_choosemode.*

//The layout manager of the scoreboard RecyclerView
class Scoreboard : AppCompatActivity() {

    //Shows UI with xml file and assigns functionality to RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

        //When back button is clicked, the activity will switch to the main menu activity
        backbutton.setOnClickListener{
            val intent = Intent(this,MainMenu::class.java)
            startActivity(intent)
        }

        //Adds the list of lyrics from MapsActivity to RecyclerView using the LyricAdapter to make it function
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val scores = ArrayList<Score>()
        scores.add(Score("Result: Win","Guesses: 2","Visited locations: 2"))
        scores.add(Score("Result: Lose","Guesses: 1","Visited locations: 5"))
        scores.add(Score("Result: Win","Guesses: 3","Visited locations: 1"))
        scores.add(Score("Result: Win","Guesses: 3","Visited locations: 4"))
        scores.add(Score("Result: Lose","Guesses: 2","Visited locations: 5"))
        scores.add(Score("Result: Lose","Guesses: 1","Visited locations: 8"))
        scores.add(Score("Result: Win","Guesses: 2","Visited locations: 4"))
        val adapter = ScoreAdapter(scores)
        recyclerView.adapter = adapter
    }
}

//Kevin Pan 969449
package com.example.songguessinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.songguessinggame.ui.choosemode
import kotlinx.android.synthetic.main.activity_main.*

//Main menu of the game
class MainMenu : AppCompatActivity() {

    //Shows UI of xml file and applies functions to buttons
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //When play button is clicked, screen goes to the choosemode activity
        playbutton.setOnClickListener{
            val intent = Intent(this,choosemode::class.java)
            startActivity(intent)
        }

        //When scoreboard button is clicked, screen goes to the scoreboard activity
        scoreboardbutton.setOnClickListener{
            val intent = Intent(this, Scoreboard::class.java)
            startActivity(intent)
        }

        //When quit button is clicked, the app closes.
        quitbutton.setOnClickListener{
            finish()
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        }
    }

}

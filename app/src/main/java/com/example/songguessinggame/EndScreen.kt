//Kevin Pan 969449
package com.example.songguessinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_end_screen.*

//End screen activity that is shown after the user has either won or lost the game
class EndScreen : AppCompatActivity() {

    //When activity is created, it will show UI with xml file and apply actions for the buttons
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_screen)


        val intent = intent
        val badguesses = intent.getIntExtra("BADGUESSES", 0)
        val lose = intent.getBooleanExtra("LOSE", false)

        // If user guesses 3 incorrect times or ran out of time from the time trial
        // Switches the title from "You Win" to "You Lose"
        if((badguesses == 3) || (lose == true)){
            val textView = findViewById<TextView>(R.id.winOrLose)
            textView.text = "You Lose"
        }

        //When quit button is clicked, the application quits
        quitbutton.setOnClickListener{
            finish()
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        }

        //When main menu button is clicked, screen will go to main menu activity
        mainmenu.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }
    }
}

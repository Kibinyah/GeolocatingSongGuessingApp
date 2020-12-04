//Kevin Pan 969449
package com.example.songguessinggame.ui

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.example.songguessinggame.MainMenu
import com.example.songguessinggame.MapsActivity
import com.example.songguessinggame.R

import kotlinx.android.synthetic.main.activity_choosemode.*


//Activity that allows the user to either choose Current or Classic mode
//Will also apply the Time Trial feature
class choosemode : AppCompatActivity() {


    //When activity is created, it will show UI with xml file and apply actions for the buttons
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosemode)

        val timerCheckBox = findViewById<CheckBox>(R.id.timebox)

        //When the back button is clicked, it will sent screen back to the main menu
        backbutton.setOnClickListener{
            val intent = Intent(this,MainMenu::class.java)
            startActivity(intent)
        }

        //When the classic button is clicked, screen will go to the main Map Activity
        classicbutton.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)

            //Checks if timetrial box is ticked, if so passes true to next activity.
            //Otherwise passes false to next activity.
            if(timerCheckBox.isChecked) {
                intent.putExtra("TIMETRIAL",true)
            }else{
                intent.putExtra("TIMETRIAL",false)
            }

            //Passes the gamemode "Classic" to the next activity
            intent.putExtra("MODE","Classic")
            startActivity(intent)
        }

        //When the current button is clicked, screen will go to the main Map activity
        currentbutton.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)

            //Checks if timetrial box is ticked, if so passes tru to next activity.
            //Otherwise passes false to next activity
            if(timerCheckBox.isChecked) {
                intent.putExtra("TIMETRIAL",true)
            }else{
                intent.putExtra("TIMETRIAL",false)
            }

            //Passes the gamemode "Current to the next activity
            intent.putExtra("MODE", "Current")
            startActivity(intent)
        }

    }

}

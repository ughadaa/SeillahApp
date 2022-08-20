package com.example.ratina.sellahapp.Games

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.example.ratina.sellahapp.*
import com.example.ratina.sellahapp.Games.counter.counter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_result.*
import java.util.*

class Result : AppCompatActivity() {
    lateinit var sandd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)


        val valueToWorkWith: Int
        val valueToWorkWith2: Int


// Calculate the correct answers
        valueToWorkWith = intent.getIntExtra("thecount", 0)
        // Calculate the wrong answers
        valueToWorkWith2 = intent.getIntExtra("worng", 0)


        // Display the value to the screen.
        val Number_of_answers = findViewById<Button>(R.id.btn_filler_right)
        Number_of_answers.text = "" + valueToWorkWith

        val Number_of_worng = findViewById<Button>(R.id.btn_filler_Worng)
        Number_of_worng.text = "" + valueToWorkWith2


        sandd = findViewById(R.id.button2)

        sandd.setOnClickListener {

if( counter.chek==1){

            var intent: Intent = Intent(this, sound_games::class.java)


    counter. right_answer_count = 0
    counter. worng_filler_count = 0
    counter.total_filler_count = 0
    counter. numberOfGamesSize = 0
    counter.stop = 0
    counter. timerr1 = 0.0
    counter. timerr2 = 0.0
    counter.  timerr3 = 0.0
    counter. timerr4 = 0.0
    counter. timerr5 = 0.0
    counter. timerr6 = 0.0
    counter.  timerr7 = 0.0
    counter.   timerr8 = 0.0
    counter. timerr9 = 0.0
    counter.timerr10 = 0.0
    counter. percent = 0
    counter. totalTime = 0.0
    counter. test=0
    startActivity(intent)


}
            else{
            var intent2: Intent = Intent(this, Game::class.java)


    counter. right_answer_count = 0
    counter. worng_filler_count = 0
    counter.total_filler_count = 0
    counter. numberOfGamesSize = 0
    counter.stop = 0
    counter. timerr1 = 0.0
    counter. timerr2 = 0.0
    counter.  timerr3 = 0.0
    counter. timerr4 = 0.0
    counter. timerr5 = 0.0
    counter. timerr6 = 0.0
    counter.  timerr7 = 0.0
    counter.   timerr8 = 0.0
    counter. timerr9 = 0.0
    counter.timerr10 = 0.0
    counter. percent = 0
    counter. totalTime = 0.0
    counter. test=0
    startActivity(intent2)

}


        }

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean { //overriding the method to give the user this set of menu
        when (item?.itemId) {

            R.id.menu_home -> { //first item of menu enable the user to go to the home page
                val intent = Intent(this, ChildHome::class.java)
                startActivity(intent)
            }

            R.id.menu_settings -> { //second item of menu enable the user to go to the settings page
                val intent = Intent(this, ChildSettings::class.java)
                startActivity(intent)
            }
            R.id.menu_help->{
                val intent = Intent(this, Help::class.java)
                startActivity(intent)

            }
            R.id.menu_signout -> { //third item of menu enable the user to log out of the application
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sharednav_menu, menu) // get the layout of the menu for the user 'specialist'
        return super.onCreateOptionsMenu(menu)
    }
}





package com.example.ratina.sellahapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.ratina.sellahapp.Chat.chatLatestMessages
import com.example.ratina.sellahapp.Games.counter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_child_home.*

class ChildHome : AppCompatActivity() {

    companion object {
        var currentUser: User? = null}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_home)

        counter.counter. right_answer_count = 0
        counter.counter. worng_filler_count = 0
        counter.counter.total_filler_count = 0
        counter.counter. numberOfGamesSize = 0
        counter.counter.stop = 0
        counter.counter. timerr1 = 0.0
        counter.counter. timerr2 = 0.0
        counter.counter.  timerr3 = 0.0
        counter.counter. timerr4 = 0.0
        counter.counter. timerr5 = 0.0
        counter.counter. timerr6 = 0.0
        counter.counter.  timerr7 = 0.0
        counter.counter.   timerr8 = 0.0
        counter.counter. timerr9 = 0.0
        counter.counter.timerr10 = 0.0
        counter.counter. percent = 0
        counter.counter. totalTime = 0.0
        counter.counter. test=0

        fetchCurrentUser()



        supportActionBar?.title = currentUser?.username + " مرحبًا "

        if(currentUser?.username == null){
            supportActionBar?.title = " مرحبًا"

        }


        imageButton_Categorys.setOnClickListener {
            val intent = Intent(this, ChildChooseCategory::class.java)
            startActivity(intent)
        }

        imageButton_GamesPage.setOnClickListener {


            val intent = Intent(this, ChildChooseGame::class.java)
            startActivity(intent)

        }

        imageButton_Chat.setOnClickListener {

            val intent = Intent(this, chatLatestMessages::class.java)
            startActivity(intent)
        }

        verifyUserIsLoggedIn()
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean { //overriding the method to give the specialist this set of menu


        when (item?.itemId) {

            R.id.menu_home -> { //first item of menu enable the user to go to the home page


                val intent = Intent(this, ChildHome::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
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


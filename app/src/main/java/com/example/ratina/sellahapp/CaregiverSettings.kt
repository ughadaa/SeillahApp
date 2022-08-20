package com.example.ratina.sellahapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_caregiver_settings.*

class CaregiverSettings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caregiver_settings)

        supportActionBar?.title = "الإعدادات"

            buttonAddChildMS.setOnClickListener { // when clicked on 'buttonAddChildMS' user will go to 'AddChild' activity
            var intent: Intent = Intent(this, AddChild::class.java)
            startActivity(intent)
        }

        buttonAddCate.setOnClickListener {// when clicked on 'buttonAddCate' user will go to 'AddCategory' activity
            var intent: Intent = Intent(this, Add_category::class.java)
            startActivity(intent)
        }

        buttonADDimage.setOnClickListener {
            var intent: Intent = Intent(this, AddprofileimageforCaregiver::class.java)
            startActivity(intent)
        }

        buttonDelCate.setOnClickListener {

            var intent: Intent = Intent(this, CaregiverChoosetoDelete::class.java)
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
    override fun onOptionsItemSelected(item: MenuItem?): Boolean { //overriding the method to give the specialist this set of menu


        when (item?.itemId) {

            R.id.menu_home -> { //first item of menu enable the user to go to the home page


                val intent = Intent(this, CaregiverHome::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

            R.id.menu_settings -> { //second item of menu enable the user to go to the settings page
                val intent = Intent(this, CaregiverSettings::class.java)
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

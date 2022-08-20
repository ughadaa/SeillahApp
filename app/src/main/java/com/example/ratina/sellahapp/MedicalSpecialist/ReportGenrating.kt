package com.example.ratina.sellahapp.MedicalSpecialist

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.ratina.sellahapp.*
import com.example.ratina.sellahapp.Games.counter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_report_genrating.*
import java.util.*

class ReportGenrating : AppCompatActivity() {

    var toUser: User? = null // assign public variable toUser as class User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_genrating)

        toUser = intent.getParcelableExtra<User>(ReportGenratingOfChildren.USER_KEY) // get the User that was passed from the previous page 'ReportGenratingOfChildren'
        supportActionBar?.title = toUser?.username + "تقرير إلى مسؤول الرعاية الخاص بـ" //title bar of the page



            button_send_report.setOnClickListener {
                 writereport() // call function that performs 'write report' as soon as the user clicks button 'SEND'

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

    private fun writereport(){

        val report = editText_report.text.toString() // get the body of report from the editText
        val aboutchild = toUser?.email //

        val calendar = Calendar.getInstance()
        val currentDate= calendar.get(Calendar.MONTH).toString() + "/ " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(
            Calendar.YEAR)

        val specialist = FirebaseAuth.getInstance().uid //the current user which is in this case 'the specialist'

        val thespecialist = FirebaseAuth.getInstance().currentUser // get the current user which is 'the specialist' information

        thespecialist?.let {
            // email address for the current logged in user
            val themail = thespecialist.email //by using thespecialist variable we get the email of the specialist

            val ref = FirebaseDatabase.getInstance().getReference("reports") // firebase reference to the 'report' inside the database
            val id = ref.push().key // assign the key of each 'push' or 'id of newly enterd reports' to the id variable

            val generatedReport = Report(id!!, aboutchild!!, report, currentDate, specialist!!, themail!!) //pass the variables to the object of class Report

            ref.child(id)
                .setValue(generatedReport) //this will insert the generated Report with the specified vales above
                .addOnCompleteListener {

                    Toast.makeText(this, "تم حفظ التقرير", Toast.LENGTH_LONG).show()// when insertion is successful show this toast
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean { //overriding the method to give the specialist this set of menu
        when (item?.itemId) {

            R.id.menu_home -> { //first item of menu enable the user to go to the home page
                val intent = Intent(this, MedicalSpecialistHome::class.java)
                startActivity(intent)
            }

            R.id.menu_settings -> { //second item of menu enable the user to go to the settings page
                val intent = Intent(this, AddChild::class.java)
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


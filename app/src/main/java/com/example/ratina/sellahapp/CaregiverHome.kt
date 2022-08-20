package com.example.ratina.sellahapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.ratina.sellahapp.Chat.chatLatestMessages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_caregiver_home.*
import kotlinx.android.synthetic.main.user_row.view.*

class CaregiverHome : AppCompatActivity() {

    companion object {

        var currentUser: User? = null}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caregiver_home)

        fetchCurrentUser()

        supportActionBar?.title = currentUser?.username + " مرحبًا "

        if(currentUser?.username == null){
            supportActionBar?.title = " مرحبًا"

        }




        imageButton_Chat.setOnClickListener {
        var intent: Intent = Intent(this, chatLatestMessages::class.java)
          startActivity(intent)

        }


        imageButton_Categorys.setOnClickListener {

            var intent: Intent = Intent(this, CaregiverChooseCategory::class.java)
            startActivity(intent)
        }


        imageButton_Reports.setOnClickListener {
            var intent: Intent = Intent(this, CaregiverViewChildrenReports::class.java)
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
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid").child("childemail")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                val listOfChildrenss = mutableListOf<String>()

                p0.children.forEach {

                    val childemails = it.getValue(String::class.java) //get the value of the already existing emails

                    listOfChildrenss.add(childemails!!) //add the values of the already existing emails

                }//end of loop



            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
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

class UserhomeItem(val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview_new_message.text = user.username
    }

    override fun getLayout(): Int {
        return R.layout.user_row
    }
}



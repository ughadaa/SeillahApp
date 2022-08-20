package com.example.ratina.sellahapp.MedicalSpecialist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.ratina.sellahapp.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_report_genrating_of_children.*
import kotlinx.android.synthetic.main.user_row.view.*

class ReportGenratingOfChildren : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_genrating_of_children)


        fetchUsers() //calling function fetch users
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

    companion object {
        val USER_KEY = "USER_KEY" // companion object to help pass the 'user' as whole to another activity

    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users") // reference to 'users' node in the firebase database

        val theuser = FirebaseAuth.getInstance().currentUser // getting the current user's info which in this case the 'medical specialist' type of user
        theuser?.let {
            // email address for the current logged in user
            val themail = theuser.email // getting the email of the current user for checking purposes

            ref.addListenerForSingleValueEvent(object : ValueEventListener { //function listenter to help retrieve the data

                override fun onDataChange(p0: DataSnapshot) { // mandatory function of the addListenerForSingleValueEvent
                    val adapter = GroupAdapter<ViewHolder>() // set the adapter to groupie which helps manage the RecyclerView

                    p0.children.forEach {
                        Log.d("NewMessage", it.toString())
                        val user = it.getValue(User::class.java) //set each child of the 'users' node to variable user
                        if (user != null && user.usertype == "طفل" && themail in user.specialist) {
                            adapter.add(UserchildreportItem(user)) // if the user is type 'طفل' and linked to this specialist add the user to the adapter
                        }
                    }

                    adapter.setOnItemClickListener { item, view ->

                        val userItem = item as UserchildreportItem // set the user that i passed to UserchildreportItem to userItem variable

                        val intent = Intent(view.context, ReportGenrating::class.java) // when the specialist clicks any user it will move the page to ReportGenrating
                        intent.putExtra(USER_KEY, userItem.user) // to pass the user as whole to another activity

                        startActivity(intent)

                        // finish()
                    }

                    ChildreportList_RecyclerView.adapter = adapter

                }

                override fun onCancelled(p0: DatabaseError) {// mandatory function of the addListenerForSingleValueEvent

                }
            })
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

class UserchildreportItem(val user: User): Item<ViewHolder>() { //class to take the passed 'user'and put its info in a view Holder
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview_new_message.text = user.username // displays the passed user's username
    }

    override fun getLayout(): Int {
        return R.layout.user_row // getting the layout that holds the user's username
    }
}
package com.example.ratina.sellahapp.MedicalSpecialist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.ratina.sellahapp.*
import com.example.ratina.sellahapp.Games.SAVEDATAA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_view_results_by_date.*
import kotlinx.android.synthetic.main.user_row.view.*

class ViewResultsByDate : AppCompatActivity() {
    var toUser: User? = null // assign public variable toUser as class User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_results_by_date)


        toUser = intent.getParcelableExtra<User>(ViewChildrenResults.USER_KEY) // get the User that was passed from the previous page 'ViewChildrenResults'
        supportActionBar?.title = toUser?.username + " تواريخ نتائج لعب الطفل " //title bar of the page


        fetchDates() // calling the dates of result for the spicfied child from the 'ViewChildrenResults' page


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
        val DATE_KEY = "DATE_KEY" // companion object to help pass the 'date' of the result as whole to another activity
    }

    private fun fetchDates() {
        // firebase reference to the 'SAVEDATAA' inside the database based on the 'userplaying' id that was passed by 'toUser' companion object
        val ref = FirebaseDatabase.getInstance().getReference("/SAVEDATAA").orderByChild("userplaying").equalTo(toUser!!.uid)


        ref.addListenerForSingleValueEvent(object : ValueEventListener { //function listenter to help retrieve the data

            override fun onDataChange(p0: DataSnapshot) { // mandatory function of the addListenerForSingleValueEvent
                val adapter = GroupAdapter<ViewHolder>() // set the adapter to groupie which helps manage the RecyclerView

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())

                    val key = it.key
                    val userdata = it.getValue(SAVEDATAA::class.java) //set each child of the 'SAVEDATAA' node to variable 'userdata'

                    adapter.add(DatesItem(userdata!!)) //add the passed user data to DatesItem to get the wanted view holder


                }



                adapter.setOnItemClickListener { item, view ->

                    val userDData = item as DatesItem // set the user that i passed to DatesItem to userDData variable

                    val intent = Intent(view.context, ViewResult::class.java) // when the specialist clicks any user it will move the page to ViewResult
//          intent.putExtra(USER_KEY,  userItem.user.username)
                    intent.putExtra(DATE_KEY, userDData.userdata) // to pass the userdata which contains the date of the result as whole to another activity
                    startActivity(intent)

                }


                ByDatesList_RecyclerView.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) { // mandatory function of the addListenerForSingleValueEvent

            }
        })

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

class DatesItem(val userdata: SAVEDATAA): Item<ViewHolder>() {  //class to take the passed 'userdata'and put its info in a view Holder

    override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.username_textview_new_message.text = userdata.date // displays the passed date of the result
    }

    override fun getLayout(): Int {
        return R.layout.user_row // getting the layout that holds the date of the result
    }
}
package com.example.ratina.sellahapp.Chat

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
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.user_row.view.*
import kotlinx.android.synthetic.main.user_row_chat.view.*

class chat : AppCompatActivity() {

    var Usertype: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar?.title = "خلف"


        fetchUsers()

        val theuser = FirebaseAuth.getInstance().currentUser

        theuser?.let {
            // email address for the current logged in user
            val themail = theuser.email
            CheckUserType(themail!!)

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




    companion object {
        val USER_KEY = "USER_KEY"
        val USER_TYPE = "USER_TYPE"

    }

    private fun fetchUsers() { //fetch the users that are linked together only to enable them to start chatroom
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val theuser = FirebaseAuth.getInstance().currentUser

        theuser?.let {
            // email address for the current logged in user
           val themail = theuser.email

            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {
                    val adapter = GroupAdapter<ViewHolder>()

                    p0.children.forEach {
                        Log.d("NewMessage", it.toString())
                        val user = it.getValue(User::class.java)

                        if (user != null && user.usertype.equals("طفل") && themail in user.cgemail || themail in user!!.childemail && user.usertype!="اخصائي") {
                            adapter.add(UserChatItem(user))
                        }
                    }

                    //when clicked on the user pass the user as 'User' object to chatroom
                    adapter.setOnItemClickListener { item, view ->

                        val userItem = item as UserChatItem

                        val intent = Intent(view.context, Chatroom::class.java)
//          intent.putExtra(USER_KEY,  userItem.user.username)
                        intent.putExtra(USER_KEY, userItem.user)
                        startActivity(intent)

                        // finish()
                    }

                    RecyclerViewmessageList.adapter = adapter

                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }
    }

    private fun CheckUserType(theEmail: String) { //check the type of current user to provid the appropriate bar in top of the page
        val ref = FirebaseDatabase.getInstance().getReference("/users").orderByChild("email").equalTo(theEmail)


        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {
                    Log.d("رسالة جديدة", it.toString())
                    val user = it.getValue(User::class.java)

                    if(user != null &&user.usertype =="طفل")
                    {intent.putExtra(chat.USER_TYPE, user)}

                    if(user != null &&user.usertype =="مسؤول رعاية")
                    {intent.putExtra(chat.USER_TYPE, user)}

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean { //overriding the method to give the specialist this set of menu

        Usertype = intent.getParcelableExtra<User>(chat.USER_TYPE) //get the current user type

        when (item?.itemId) {

            R.id.menu_home -> { //first item of menu enable the user to go to the home page

                if(Usertype?.usertype=="طفل"){
                    val intent = Intent(this, ChildHome::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)}

                if(Usertype?.usertype=="مسؤول رعاية"){
                    val intent = Intent(this, CaregiverHome::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)}
            }

            R.id.menu_settings -> { //second item of menu enable the user to go to the settings page
                if(Usertype?.usertype=="مسؤول رعاية"){
                    val intent = Intent(this, CaregiverSettings::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)}

                if(Usertype?.usertype=="طفل"){
                    val intent = Intent(this, ChildSettings::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)}
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



class UserChatItem(val user: User): Item<ViewHolder>() {//class to th view holder to be adapted by the recyclerview
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview_chat.text = user.username

            Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_new_message)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_chat//get the layout of the user rows in chate to the view holder
    }
}



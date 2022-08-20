package com.example.ratina.sellahapp.Chat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.ratina.sellahapp.*
import com.example.ratina.sellahapp.MedicalSpecialist.MedicalSpecialistHome
import com.example.ratina.sellahapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_latest_messages.*

class chatLatestMessages : AppCompatActivity() {

    var Usertype: User? = null


    companion object {
        var currentUser: User? = null
        val TAG = "LatestMessages"
        val USER_TYPE = "USER_TYPE"


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_latest_messages)

        recyclerview_latest_messages.adapter = adapter
        recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        // set item click listener on your adapter
        adapter.setOnItemClickListener { item, view ->
            Log.d(TAG, "123")
            val intent = Intent(this, Chatroom::class.java)

            // we are missing the chat partner user

            val row = item as LatestMessageRow
            intent.putExtra(chat.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }

//    setupDummyRows()
        listenForLatestMessages()

        fetchCurrentUser()

        verifyUserIsLoggedIn()
        val theuser = FirebaseAuth.getInstance().currentUser

        theuser?.let {
            // email address for the current logged in user
            val themail = theuser.email
            CheckUserType(themail!!)

        }



    }

    val latestMessagesMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    val adapter = GroupAdapter<ViewHolder>()


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

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


    private fun CheckUserType(theEmail: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/users").orderByChild("email").equalTo(theEmail)


        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {
                    Log.d("رسالة جديدة", it.toString())
                    val user = it.getValue(User::class.java)

                    if(user != null &&user.usertype =="طفل")
                    {intent.putExtra(chatLatestMessages.USER_TYPE, user)}

                    if(user != null &&user.usertype =="مسؤول رعاية")
                    {intent.putExtra(chatLatestMessages.USER_TYPE, user)}

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        Usertype = intent.getParcelableExtra<User>(chatLatestMessages.USER_TYPE)


        when (item?.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, chat::class.java)
                startActivity(intent)
            }

            R.id.menu_home->{
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

            R.id.menu_signout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}
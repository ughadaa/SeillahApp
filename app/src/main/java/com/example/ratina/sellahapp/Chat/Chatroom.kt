package com.example.ratina.sellahapp.Chat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.ratina.sellahapp.*
import com.example.ratina.sellahapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chatroom.*


class Chatroom : AppCompatActivity() {


    var Usertype: User? = null


    companion object {
        var currentUser: User? = null
        val USER_TYPE = "USER_TYPE"

        val TAG = "ChatLog"

    }


    val adapter = GroupAdapter<ViewHolder>()

    var toUser: User? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)



        recyclerview_chatroom.adapter = adapter

        toUser = intent.getParcelableExtra<User>(chat.USER_KEY)


        supportActionBar?.title = toUser?.username
        verifyUserIsLoggedIn()

//    setupDummyData()
            listenForMessages()

        btnSend.setOnClickListener {
                Log.d(TAG, "Attempt to send message....")
                performSendMessage()
            }
        val theuser = FirebaseAuth.getInstance().currentUser

        theuser?.let {
            // email address for the current logged in user
            val themail = theuser.email
            CheckUserType(themail!!)

        }



    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }



    private fun listenForMessages() {
            val fromId = FirebaseAuth.getInstance().uid
            val toId = toUser?.uid
            val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

            ref.addChildEventListener(object: ChildEventListener {

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val chatMessage = p0.getValue(ChatMessage::class.java)

                    if (chatMessage != null) {
                        Log.d(TAG, chatMessage.text)

                        if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                            val currentUser = chatLatestMessages.currentUser ?: return
                            adapter.add(ChatFromItem(chatMessage.text, currentUser))
                        } else {
                            adapter.add(ChatToItem(chatMessage.text, toUser!!))
                        }
                    }

                    recyclerview_chatroom.scrollToPosition(adapter.itemCount - 1)

                }

                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildRemoved(p0: DataSnapshot) {

                }

            })

        }

        private fun performSendMessage() {
            // how do we actually send a message to firebase...
            val text = txtMessage.text.toString()

            val fromId = FirebaseAuth.getInstance().uid
            val user = intent.getParcelableExtra<User>(chat.USER_KEY)
            val toId = user.uid

            if (fromId == null) return

            val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

            val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

            val chatMessage = ChatMessage(
                reference.key!!,
                text,
                fromId,
                toId,
                System.currentTimeMillis() / 1000
            )
            reference.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d(TAG, "Saved our chat message: ${reference.key}")
                    txtMessage.text.clear()
                    recyclerview_chatroom.scrollToPosition(adapter.itemCount - 1)
                }

            toReference.setValue(chatMessage)
            //MISSING
            val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
            latestMessageRef.setValue(chatMessage)

            val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
            latestMessageToRef.setValue(chatMessage)


        }





private fun CheckUserType(theEmail: String) {
    val ref = FirebaseDatabase.getInstance().getReference("/users").orderByChild("email").equalTo(theEmail)


    ref.addListenerForSingleValueEvent(object : ValueEventListener {

        override fun onDataChange(p0: DataSnapshot) {

            p0.children.forEach {
                Log.d("رسالة جديدة", it.toString())
                val user = it.getValue(User::class.java)

                if(user != null &&user.usertype =="طفل")
                {intent.putExtra(USER_TYPE, user)}

                if(user != null &&user.usertype =="مسؤول رعاية")
                {intent.putExtra(USER_TYPE, user)}

            }

        }

        override fun onCancelled(p0: DatabaseError) {

        }
    })

}


override fun onOptionsItemSelected(item: MenuItem?): Boolean {

    Usertype = intent.getParcelableExtra<User>(USER_TYPE)


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










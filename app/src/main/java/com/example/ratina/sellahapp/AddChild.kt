package com.example.ratina.sellahapp


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.ratina.sellahapp.MedicalSpecialist.MedicalSpecialistHome
import kotlinx.android.synthetic.main.activity_add_child.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddChild : AppCompatActivity() {

    var Usertype: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_child)


        supportActionBar?.title = "إضافة طفل"
                //val childemail = findViewById<TextView>(R.id.AddedChildEmail)

       // val updateBtn = findViewById<TextView>(R.id.AddNewChild)


        AddNewChild.setOnClickListener {

            updateChildrenEmails()
        }

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


    ///This page is for the caregiver and medical specialist to let them add more children

    private fun updateChildrenEmails(){

        val currentUs = FirebaseAuth.getInstance().currentUser

        val ref = FirebaseDatabase.getInstance().getReference("/users/${currentUs!!.uid}").child("childemail")

                ref.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        val listOfChildrenss = mutableListOf<String>()

                        p0.children.forEach {

                            val urser = it.getValue(String::class.java) //get the value of the already existing emails

                            listOfChildrenss.add(urser!!) //add the values of the already existing emails

                        }//end of loop
                        listOfChildrenss.add(AddedChildEmail.text.toString()) //add the new email

                        ref.setValue(listOfChildrenss) //set the value of the new email + already existing emails
                            .addOnCompleteListener {

                                Toast.makeText(this@AddChild, "تم إضافة الطفل", Toast.LENGTH_LONG).show()// when insertion is successful show this toast
                            }
                    }


                })///
            }




companion object {
    val USER_TYPE = "USER_TYPE"

}

private fun CheckUserType(theEmail: String) {
    val ref = FirebaseDatabase.getInstance().getReference("/users").orderByChild("email").equalTo(theEmail)


    ref.addListenerForSingleValueEvent(object : ValueEventListener {

        override fun onDataChange(p0: DataSnapshot) {

            p0.children.forEach {
                Log.d("رسالة جديدة", it.toString())
                val user = it.getValue(User::class.java)

                if(user != null &&user.usertype =="مسؤول رعاية")
                {intent.putExtra(AddChild.USER_TYPE, user)}

                if(user != null &&user.usertype =="اخصائي")
                {intent.putExtra(AddChild.USER_TYPE, user)}

            }

        }

        override fun onCancelled(p0: DatabaseError) {

        }
    })

}

override fun onOptionsItemSelected(item: MenuItem?): Boolean { //overriding the method to give the user this set of menu

    Usertype = intent.getParcelableExtra<User>(AddChild.USER_TYPE)

    when (item?.itemId) {

        R.id.menu_home -> { //first item of menu enable the user to go to the home page


            if(Usertype?.usertype=="مسؤول رعاية"){
                val intent = Intent(this, CaregiverHome::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)}

            if(Usertype?.usertype=="اخصائي"){
                val intent = Intent(this, MedicalSpecialistHome::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)}
        }

        R.id.menu_settings -> { //second item of menu enable the user to go to the settings page
            if(Usertype?.usertype=="مسؤول رعاية"){
                val intent = Intent(this, CaregiverSettings::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)}

            if(Usertype?.usertype=="اخصائي"){
                val intent = Intent(this, AddChild::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)}
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
    menuInflater.inflate(R.menu.sharednav_menu, menu) // get the layout of the menu for the user
    return super.onCreateOptionsMenu(menu)
}
}











package com.example.ratina.sellahapp

import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ratina.sellahapp.MedicalSpecialist.MedicalSpecialistHome
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.login.* // so i don't have to use findViewById for every button

class Login : AppCompatActivity() {

    var toUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)



        Regbtn.setOnClickListener {
            var intent: Intent = Intent(this, Register::class.java)
            startActivity(intent)

        }


        buttonLogin.setOnClickListener {
            CheckUserType(email.text.toString())
            performLogin()
        }


    }


    companion object {
        val USER_TYPE = "USER_TYPE"
    }

    // if user click Login button, figure out which type of user (child/caregiver/medical specialist)


    private fun performLogin() {
        val email = email.text.toString()
        val password = password.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out email/password.", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Login", "Successfully logged in: ${it.result!!.user.uid}")

                toUser = intent.getParcelableExtra<User>(Login.USER_TYPE)

                if(toUser?.usertype=="طفل"){
                    val intent = Intent(this, ChildHome::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)}

                if(toUser?.usertype=="مسؤول رعاية"){
                    val intent = Intent(this, CaregiverHome::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)}

                if(toUser?.usertype=="اخصائي"){
                    val intent = Intent(this, MedicalSpecialistHome::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)}



            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
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
                        {intent.putExtra(Login.USER_TYPE, user)}

                        if(user != null &&user.usertype =="مسؤول رعاية")
                        {intent.putExtra(Login.USER_TYPE, user)}

                        if(user != null &&user.usertype =="اخصائي")
                        {intent.putExtra(Login.USER_TYPE, user)}


                    }

                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })

    }



}
package com.example.ratina.sellahapp

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.ratina.sellahapp.MedicalSpecialist.MedicalSpecialistHome
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonReport.setOnClickListener {
            Log.d("MainActivity", "Try to show login activity")

            var intent: Intent = Intent(this, Login::class.java) //move to login.kt when clicked
            startActivity(intent)
        }


        //SPINNER THINGS
        // Create an ArrayAdapter
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.usertype, android.R.layout.simple_spinner_item
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinner.adapter = adapter



        buttonForRegister.setOnClickListener {

            performMSRegister()
        }
    }


    fun getValues(view: View) {
        Toast.makeText(
            this, "Spinner 1 " + spinner.selectedItem.toString(), Toast.LENGTH_LONG
        ).show()
    }
    //SPINNER THINGS


    private fun performMSRegister() { //preforming registration based on email & password

        val email = editTextemail.text.toString()
        val password = editTextpaw.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this@Register, "الرجاء ادخال البريد الالكتروني/كلمة المرور", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "Email is: " + email)
        Log.d("MainActivity", "Password: $password")

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d("Main", "Successfully created user with uid: ${it.result!!.user.uid}")
                saveUserMSToFirebaseDatabase()
            }
            .addOnFailureListener {
                Log.d("Main", "Failed to create user: ${it.message}")
                Toast.makeText(this@Register, " فشل تسجيل المستخدم ${it.message}", Toast.LENGTH_SHORT).show()
            }


    }


    private fun saveUserMSToFirebaseDatabase() { //this function is to save the user information into database
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")



        if (spinner.selectedItem.toString() == "مسؤول رعاية") {
            val builder = AlertDialog.Builder(this@Register)
            builder.setTitle("إضافة طفل")
            val inflater = LayoutInflater.from(this@Register)
            val view = inflater.inflate(R.layout.thechildemail, null)

            val TheChildE = view.findViewById<TextView>(R.id.emailofChild)
            //val TheSpecialistE = view.findViewById<TextView>(R.id.TheMSemailforCG)

            builder.setView(view)

            builder.setPositiveButton("إضافة", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    val listOfChildrenss = mutableListOf<String>()
                    listOfChildrenss.add(TheChildE.text.toString())

                    //val listOfSpecialistsForCG = mutableListOf<String>()
                    //listOfSpecialistsForCG.add(TheSpecialistE.text.toString())

                    val user = User(uid, theusername.text.toString(), editTextemail.text.toString(), spinner.selectedItem.toString(), listOfChildrenss, list, list, "https://firebasestorage.googleapis.com/v0/b/sellahapplication.appspot.com/o/profileimages%2Fcaregiverpic.png?alt=media&token=2f6c6f89-25c5-4124-a097-8968599c7e91")

                    ref.setValue(user)
                        .addOnSuccessListener {
                            Log.d("Main", "Finally we saved the user to Firebase Database")

                            //so the user won't have to login every time they open the application
                            // if (user.usertype=="مسؤول رعاية"){
                            val intent = Intent(
                                this@Register,
                                CaregiverHome::class.java
                            ) //need to change CaregiverHome and redaidect based on usertype
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)//}

                        }
                        .addOnFailureListener {
                            Log.d("Main", "Failed to set value to database: ${it.message}")
                        }




                    Toast.makeText(this@Register, "تم إضافة الطفل ", Toast.LENGTH_LONG).show()


                }
            })

            builder.setNegativeButton("إلغاء", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    //if hit cancel
                    val user = User(
                        uid,
                        theusername.text.toString(),
                        editTextemail.text.toString(),
                        spinner.selectedItem.toString(),
                        list,
                        list,
                        list,"https://firebasestorage.googleapis.com/v0/b/sellahapplication.appspot.com/o/profileimages%2Fcaregiverpic.png?alt=media&token=2f6c6f89-25c5-4124-a097-8968599c7e91"
                    )

                    ref.setValue(user)
                        .addOnSuccessListener {
                            Log.d("Main", "Finally we saved the user to Firebase Database")

                            //so the user won't have to login every time they open the application
                            // if (user.usertype=="مسؤول رعاية"){
                            val intent = Intent(
                                this@Register,
                                CaregiverHome::class.java
                            ) //need to change CaregiverHome and redaidect based on usertype
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)//}

                        }
                        .addOnFailureListener {
                            Log.d("Main", "Failed to set value to database: ${it.message}")
                        }


                }

            })

            val alert = builder.create()
            alert.show()


        }


        ///////////if child

        if (spinner.selectedItem.toString() == "طفل") {
            val builder = AlertDialog.Builder(this@Register)
            builder.setTitle("إضافة مسؤول رعاية و اخصائي")
            val inflater = LayoutInflater.from(this@Register)
            val view = inflater.inflate(R.layout.thecaregiveremail, null)

            val TheCaregiverE = view.findViewById<TextView>(R.id.TheCGE)
            val TheSpecialistE = view.findViewById<TextView>(R.id.TheMSemail)

            builder.setView(view)

            builder.setPositiveButton("إضافة", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    val listOfCaregiverss = mutableListOf<String>()
                    listOfCaregiverss.add(TheCaregiverE.text.toString())

                    val listOfSpecialists = mutableListOf<String>()
                    listOfSpecialists.add(TheSpecialistE.text.toString())

                    val user = User(
                        uid,
                        theusername.text.toString(),
                        editTextemail.text.toString(),
                        spinner.selectedItem.toString(),
                        list,
                        listOfCaregiverss,
                        listOfSpecialists,"https://firebasestorage.googleapis.com/v0/b/sellahapplication.appspot.com/o/profileimages%2Fchildpic.png?alt=media&token=be578195-1e83-4513-9f55-13006e615a37"
                    )

                    ref.setValue(user)
                        .addOnSuccessListener {
                            Log.d("Main", "Finally we saved the user to Firebase Database")

                            //so the user won't have to login every time they open the application
                            // if (user.usertype=="مسؤول رعاية"){
                            val intent = Intent(
                                this@Register,
                                ChildHome::class.java
                            ) //need to change CaregiverHome and redaidect based on usertype
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)//}

                        }
                        .addOnFailureListener {
                            Log.d("Main", "Failed to set value to database: ${it.message}")
                        }




                    Toast.makeText(this@Register, "تمت الإضافة", Toast.LENGTH_LONG).show()


                }
            })

            builder.setNegativeButton("إلغاء", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    //if hit cancel
                    val user = User(
                        uid,
                        theusername.text.toString(),
                        editTextemail.text.toString(),
                        spinner.selectedItem.toString(),
                        list,
                        list,
                        list,"https://firebasestorage.googleapis.com/v0/b/sellahapplication.appspot.com/o/profileimages%2Fchildpic.png?alt=media&token=be578195-1e83-4513-9f55-13006e615a37"
                    )

                    ref.setValue(user)
                        .addOnSuccessListener {
                            Log.d("Main", "Finally we saved the user to Firebase Database")

                            //so the user won't have to login every time they open the application
                            // if (user.usertype=="مسؤول رعاية"){
                            val intent = Intent(
                                this@Register,
                                ChildHome::class.java
                            ) //need to change CaregiverHome and redaidect based on usertype
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)//}

                        }
                        .addOnFailureListener {
                            Log.d("Main", "Failed to set value to database: ${it.message}")
                        }



                }

            })

            val alert = builder.create()
            alert.show()


        }


        if (spinner.selectedItem.toString() == "اخصائي") {

            val builder = AlertDialog.Builder(this@Register)
            builder.setTitle("إضافة طفل")
            val inflater = LayoutInflater.from(this@Register)
            val view = inflater.inflate(R.layout.thechildandcaregiveremail, null)

            //val TheCaregiverEforMS = view.findViewById<TextView>(R.id.editText)
            val TheChildEforMS = view.findViewById<TextView>(R.id.editText2)

            builder.setView(view)

            builder.setPositiveButton("إضافة", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    //val listOfCaregiversforMS = mutableListOf<String>()
                    //listOfCaregiversforMS.add(TheCaregiverEforMS.text.toString())

                    val listOfChildrenforMS = mutableListOf<String>()
                    listOfChildrenforMS.add(TheChildEforMS.text.toString())

                    val user = User(
                        uid,
                        theusername.text.toString(),
                        editTextemail.text.toString(),
                        spinner.selectedItem.toString(),
                        listOfChildrenforMS,
                        list,
                        list,"https://firebasestorage.googleapis.com/v0/b/sellahapplication.appspot.com/o/profileimages%2Fmspic.png?alt=media&token=dc558eb5-2ea4-4f39-9f1f-b797596281a5"
                    )

                    ref.setValue(user)
                        .addOnSuccessListener {
                            Log.d("Main", "Finally we saved the user to Firebase Database")

                            //so the user won't have to login every time they open the application
                            // if (user.usertype=="مسؤول رعاية"){
                            val intent = Intent(
                                this@Register,
                                MedicalSpecialistHome::class.java
                            ) //need to change CaregiverHome and redaidect based on usertype
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)//}

                        }
                        .addOnFailureListener {
                            Log.d("Main", "Failed to set value to database: ${it.message}")
                        }




                    Toast.makeText(this@Register, "تمت الإضافة ", Toast.LENGTH_LONG).show()


                }
            })

            builder.setNegativeButton("إلغاء", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    //if hit cancel
                    val user = User(
                        uid,
                        theusername.text.toString(),
                        editTextemail.text.toString(),
                        spinner.selectedItem.toString(),
                        list,
                        list,
                        list,"https://firebasestorage.googleapis.com/v0/b/sellahapplication.appspot.com/o/profileimages%2Fmspic.png?alt=media&token=dc558eb5-2ea4-4f39-9f1f-b797596281a5"
                    )

                    ref.setValue(user)
                        .addOnSuccessListener {
                            Log.d("Main", "Finally we saved the user to Firebase Database")

                            //so the user won't have to login every time they open the application
                            // if (user.usertype=="مسؤول رعاية"){
                            val intent = Intent(
                                this@Register,
                                MedicalSpecialistHome::class.java
                            ) //need to change CaregiverHome and redaidect based on usertype
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)//}

                        }
                        .addOnFailureListener {
                            Log.d("Main", "Failed to set value to database: ${it.message}")
                        }



                }

            })

            val alert = builder.create()
            alert.show()


        }

    }
}

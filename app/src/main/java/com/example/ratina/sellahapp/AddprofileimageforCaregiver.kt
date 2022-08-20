package com.example.ratina.sellahapp

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_addprofileimagefor_caregiver.*
import kotlinx.android.synthetic.main.allcategoryframe.view.*
import kotlinx.android.synthetic.main.currenyimg.view.*
import java.util.*

class AddprofileimageforCaregiver : AppCompatActivity() {

    companion object {
        val TAG = "Add image activity"
        val USER_TYPE = "USER_TYPE"

    }
    var Usertype: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addprofileimagefor_caregiver)

        supportActionBar?.title = "تعديل الصورة الشخصية"


        fechcurrentprofileimage()

        val theuser = FirebaseAuth.getInstance().currentUser

        theuser?.let {
            // email address for the current logged in user
            val themail = theuser.email
            CheckUserType(themail!!)

        }

        chooseBtn.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            // intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 0)
        }

        uploadBtn.setOnClickListener {
            uploadImageToFirebaseStorage()


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

      var selectedPhotoUri: Uri? = null

    private fun fechcurrentprofileimage(){


        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")


        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

               // p0.children.forEach {
                    Log.d("NewMessage", p0.toString())

                    val img = p0.getValue(User::class.java)
                    if (img != null) {
                        adapter.add(ProfileimgIteme(img))
                    }
                //}


                ReycToFechCurrentImg.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
                // proceed and check what the selected image was....
                Log.d(TAG, "Photo was selected")

                selectedPhotoUri = data.data

              val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

                //addCategoryedimage.setImageBitmap(bitmap)

               // selectphoto_button_register.alpha = 0f


      val bitmapDrawable = BitmapDrawable(bitmap)
      addCategoryedimage.setBackgroundDrawable(bitmapDrawable)
            }
        }


    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/profileimages/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File Location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to upload image to storage: ${it.message}")
            }
    }


    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid").child("profileImageUrl")

        //val user = User(uid, username_edittext_register.text.toString(), profileImageUrl)

        ref.setValue(profileImageUrl)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the user to Firebase Database")

                Toast.makeText(this, "تم حفظ الصورة بنجاح", Toast.LENGTH_LONG).show()

                //val intent = Intent(this, CaregiverSettings::class.java)
              //  intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
               // startActivity(intent)

            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")
                Toast.makeText(this, "فشل حفظ الصورة", Toast.LENGTH_LONG).show()

            }
    }

    private fun CheckUserType(theEmail: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/users").orderByChild("email").equalTo(theEmail)


        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {
                    Log.d("رسالة جديدة", it.toString())
                    val user = it.getValue(User::class.java)

                    if(user != null &&user.usertype =="مسؤول رعاية")
                    {intent.putExtra(AddprofileimageforCaregiver.USER_TYPE, user)}

                    if(user != null &&user.usertype =="اخصائي")
                    {intent.putExtra(AddprofileimageforCaregiver.USER_TYPE, user)}

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean { //overriding the method to give the user this set of menu

        Usertype = intent.getParcelableExtra<User>(AddprofileimageforCaregiver.USER_TYPE)

        when (item?.itemId) {

            R.id.menu_home -> { //first item of menu enable the user to go to the home page


                if(Usertype?.usertype=="مسؤول رعاية"){
                    val intent = Intent(this, CaregiverHome::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)}

                if(Usertype?.usertype=="طفل"){
                    val intent = Intent(this, ChildHome::class.java)
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








class ProfileimgIteme(val cate: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        Picasso.get().load(cate.profileImageUrl).into(viewHolder.itemView.imageView7)
    }

    override fun getLayout(): Int {
        return R.layout.currenyimg
    }
}

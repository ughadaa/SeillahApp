package com.example.ratina.sellahapp

import android.app.Activity

import android.app.Instrumentation
import android.app.ProgressDialog
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_category.*
import java.io.IOException
import java.util.*

class Add_category : AppCompatActivity() {

    companion object {
        val TAG = "AddCategoryActivity"

    }
    var selectedPhotoUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        supportActionBar?.title = "إضافة تصنيف "

        //setup button
        chooseBtn.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            // intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 0)


        }
        uploadBtn.setOnClickListener{
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




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d(AddprofileimageforCaregiver.TAG, "Photo was selected")

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
        val ref = FirebaseStorage.getInstance().getReference("/cateimage/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File Location: $it")

                    saveCategoryToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to upload image to storage: ${it.message}")
            }
    }


    private fun saveCategoryToFirebaseDatabase(selectedPhotoUri: String) {


        val theuser = FirebaseAuth.getInstance().currentUser

        theuser?.let {
            // email address for the current logged in user
            val useraddingemail = theuser.email

            val ref = FirebaseDatabase.getInstance().getReference("/all_catebyuser") //add this new category to the all category node
            val cid = ref.push().key

            val NewCategoryName = addcCategoryeditText.text.toString()

            val refe = FirebaseDatabase.getInstance().getReference(NewCategoryName) //insert new category in firebase
            val newcategoryid = refe.push().key

            val cate = AddedCategory(
                cid!!,
                selectedPhotoUri,
                addcCategoryeditText.text.toString(),
                addcCategoryeditText.text.toString(),
                useraddingemail!!
            )

            val cateimg = AddedImage(cid, selectedPhotoUri, addcCategoryeditText.text.toString(),NewCategoryName)

            ref.child(cid).setValue(cate)
                .addOnSuccessListener {
                    Log.d(TAG, "تم حفظ التصنيف بنجاح")

                    Toast.makeText(this, "تم حفظ التصنيف بنجاح", Toast.LENGTH_LONG).show()


                }
                .addOnFailureListener {
                    Log.d(TAG, " فشل حفظ التصنيف${it.message}")
                    Toast.makeText(this, "فشل حفظ التصنيف", Toast.LENGTH_LONG).show()

                }
            refe.child(newcategoryid!!).setValue(cateimg)

        }
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
            R.id.menu_signout -> { //third item of menu enable the user to log out of the application
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

            R.id.menu_help->{
                val intent = Intent(this, Help::class.java)
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
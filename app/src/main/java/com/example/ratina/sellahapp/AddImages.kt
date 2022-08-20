package com.example.ratina.sellahapp

import android.app.Activity

import android.app.Instrumentation
import android.app.ProgressDialog
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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
import kotlinx.android.synthetic.main.activity_add_images.*
import java.io.IOException
import java.util.*


class AddImages : AppCompatActivity(){

    var Categ: AddedCategory? = null
    var selectedPhotoUri: Uri? = null


    companion object {
        val TAG = "AddImageActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_images)

       Categ = intent.getParcelableExtra<AddedCategory>(CaregiverDisplayedCategory.CATE_KEYToImg)


        supportActionBar?.title = " إضافة صور للتصنيف " + Categ!!.arabicname


        //setup button

        chooseBtn2.setOnClickListener{

          val intent = Intent(Intent.ACTION_PICK)
          intent.type = "image/*"
          // intent.action = Intent.ACTION_GET_CONTENT
          startActivityForResult(intent, 0)


      }
        uploadBtn2.setOnClickListener{
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
            Log.d(TAG, "Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            //addCategoryedimage.setImageBitmap(bitmap)

            // selectphoto_button_register.alpha = 0f


            val bitmapDrawable = BitmapDrawable(bitmap)
            addimage.setBackgroundDrawable(bitmapDrawable)
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

                    saveImageToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to upload image to storage: ${it.message}")
            }
    }




    private fun saveImageToFirebaseDatabase(image_path: String){

        val categorypath = Categ?.name

        val ref = FirebaseDatabase.getInstance().getReference(categorypath!!)
        val cid = ref.push().key

        val img = AddedImage(cid!!, image_path, addeditText.text.toString(),categorypath)

        ref.child(cid).setValue(img)
            .addOnSuccessListener {
                Log.d(TAG, "تم حفظ الصورة بنجاح")

                Toast.makeText(this, "تم حفظ الصورة بنجاح", Toast.LENGTH_LONG).show()


            }
            .addOnFailureListener {
                Log.d(TAG, " فشل حفظ الصورة${it.message}")

                Toast.makeText(this, "تم حفظ الصورة بنجاح", Toast.LENGTH_LONG).show()

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
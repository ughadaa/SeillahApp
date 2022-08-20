package com.example.ratina.sellahapp

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_caregiver_chooseto_delete_image.*
import kotlinx.android.synthetic.main.activity_caregiver_displayed_category.*
import kotlinx.android.synthetic.main.allcategoryframe.view.*
import kotlinx.android.synthetic.main.picframe.view.*

class CaregiverChoosetoDeleteImage : AppCompatActivity() {

    var Categ: AddedCategory? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caregiver_chooseto_delete_image)


        Categ = intent.getParcelableExtra<AddedCategory>(CaregiverDisplayedCategory.CATE_KEYToImg)

        supportActionBar?.title = "اختر الصوره المراد حذفها"


        fetchcategory()

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
        val CATE_KEY = "CATE_KEY"
    }



    private fun fetchcategory() {

        //val categorypath = Categ?.name

        val categorypath = Categ!!.name

        val ref = FirebaseDatabase.getInstance().getReference(categorypath)



        val refe = FirebaseDatabase.getInstance().getReference("/all_catebyuser").orderByChild("name").equalTo(categorypath)


        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adaptere = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                   // if(categorypath == refe.toString()){

                        val catebycg = it.getValue(AddedImage::class.java)

                        adaptere.add(CategorysdelCGIteme(catebycg!!))


                    //}

                }

///////////////////////////////////////////////////////



                adaptere.setOnItemClickListener { item, view ->
                    val CateItem = item as CategorysdelCGIteme

                    //  val intent = Intent(this@CaregiverChoosetoDeleteImage, CaregiverDeleteImage::class.java)
                    //   intent.putExtra(CATE_KEY, CateItem.cate)
                    //startActivity(intent)

                    val refy = FirebaseDatabase.getInstance().getReference(categorypath).orderByChild("nameofcategory").equalTo(categorypath)

                    //val refs = FirebaseDatabase.getInstance().getReference(CateItem.cate.name).orderByChild("cid").equalTo(CateItem.cate.cid)
                    ///).orderByChild("nameofcategory").equalTo(Categimg!!.name)


                    val builder = AlertDialog.Builder(this@CaregiverChoosetoDeleteImage)
                    builder.setTitle("تنبيه!")
                    val inflater = LayoutInflater.from(this@CaregiverChoosetoDeleteImage)
                    val viewr = inflater.inflate(R.layout.noticeimg, null)

                    builder.setView(viewr)

                    builder.setPositiveButton("متأكد", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {


                            refy.addListenerForSingleValueEvent(object : ValueEventListener {

                                override fun onDataChange(p0: DataSnapshot) {

                                    p0.children.forEach {
                                        Log.d("THE CATE", it.toString())
                                        val cate = it.getValue(AddedImage::class.java)

                                        if (cate!!.cid == CateItem.cate.cid) {
                                            //ref.removeEventListener(this)
                                            it.ref.setValue(null)

                                                .addOnSuccessListener {
                                                    val intent = Intent(
                                                        this@CaregiverChoosetoDeleteImage,
                                                        CaregiverChooseCategory::class.java
                                                    )
                                                    startActivity(intent)


                                                }
                                                .addOnFailureListener {
                                                    Log.d("delete", "failed")
                                                }




                                            Toast.makeText(
                                                this@CaregiverChoosetoDeleteImage,
                                                "تم حذف الصورة ",
                                                Toast.LENGTH_LONG
                                            ).show()


                                        }
                                    }


                                }

                                override fun onCancelled(p0: DatabaseError) {

                                }
                            })

                        }


                    }
                    )

                    builder.setNegativeButton("إلغاء", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {


                            val intent = Intent(this@CaregiverChoosetoDeleteImage, CaregiverChooseCategory::class.java)
                            startActivity(intent)

                        }

                    })

                    val alert = builder.create()
                    alert.show()

                }

                AllcateToDelImggoryRecy.adapter = adaptere



            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
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


class CategorysdelCGIteme(val cate: AddedImage): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.button_Catebtn.text = cate.name

        Picasso.get().load(cate.image_path).into(viewHolder.itemView.imageView_Catepic)
    }

    override fun getLayout(): Int {
        return R.layout.allcategoryframe
    }


}

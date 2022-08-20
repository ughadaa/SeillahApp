package com.example.ratina.sellahapp

import android.content.DialogInterface
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_caregiver_displayed_category.*
import kotlinx.android.synthetic.main.activity_category_display.*
import kotlinx.android.synthetic.main.allcategoryframe.view.*
import kotlinx.android.synthetic.main.delbutn.view.*
import kotlinx.android.synthetic.main.picframe.view.*

class CaregiverDisplayedCategory : AppCompatActivity() {


    var Categ: AddedCategory? = null

    companion object {
        val CATE_KEYToImg = "CATE_KEYToImg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caregiver_displayed_category)

        Categ = intent.getParcelableExtra<AddedCategory>(CaregiverChooseCategory.CATE_KEY)

        supportActionBar?.title = Categ!!.arabicname


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


    private fun fetchcategory() {

        val categorypath = Categ?.name

        val ref = FirebaseDatabase.getInstance().getReference(categorypath!!)

        val refe =
            FirebaseDatabase.getInstance().getReference("/all_catebyuser").orderByChild("name").equalTo(categorypath)


        refe.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                val ListofSameCategorys = mutableListOf<String>()
                p0.children.forEach {
                    Log.d("رسالة جديدة", it.toString())
                    val cate = it.getValue(AddedCategory::class.java)

                    if (cate!!.name == categorypath) {

                        ListofSameCategorys.add(cate.name)


                    }

                }




                /////////lil
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adaptere = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    if (categorypath in ListofSameCategorys) {

                        val catebycg = it.getValue(AddedImage::class.java)

                        adaptere.add(AllCategoryByCGDisplayIteme(catebycg!!))


                    } else {

                        val cate = it.getValue(Category::class.java)
                        adaptere.add(CategorysCGIteme(cate!!))
                    }
                }

                categoryCGDisplayRecy.adapter = adaptere

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        //////////lil

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        /////u
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean { //overriding the method to give the specialist this set of menu


            when (item?.itemId) {

                R.id.menu_add -> { //first item of menu enable the user to go to the home page


                    val intent = Intent(this, AddImages::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra(CATE_KEYToImg, Categ!!)

                    startActivity(intent)
                }

                R.id.menu_del -> { //second item of menu enable the user to go to the settings page
                    val intent = Intent(this, CaregiverChoosetoDeleteImage::class.java)//DeletImages
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra(CATE_KEYToImg, Categ!!)

                    startActivity(intent)

                }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val categorypath = Categ?.name

        val refe =
            FirebaseDatabase.getInstance().getReference("/all_catebyuser").orderByChild("name").equalTo(categorypath)


            refe.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {

                    p0.children.forEach {
                        Log.d("رسالة جديدة", it.toString())
                        val cate = it.getValue(AddedCategory::class.java)

                        if (cate!!.name==categorypath) {
                            menuInflater.inflate(R.menu.adddel_menu, menu)

                        }

                    }

                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })



             // get the layout of the menu for the user 'specialist'
            return super.onCreateOptionsMenu(menu)

    }


}

class CategorysCGIteme(val cate: Category): Item<ViewHolder>() {

    lateinit var sandd: Button


    override fun bind(viewHolder: ViewHolder, position: Int) {


        //imageButtonsound.
        val imageButtonsound = viewHolder.itemView.findViewById<ImageButton>(R.id.imageButtonsound)


        mediaPlayer = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(cate.voice_path)
            prepare()

            //viewHolder.itemView.
            imageButtonsound.setOnClickListener {
                start()
            }
            //}
        }
        viewHolder.itemView.nameOfPic.text = cate.name

        Picasso.get().load(cate.image_path).into(viewHolder.itemView.imageViewOfpic)
    }

    override fun getLayout(): Int {
        return R.layout.picframe
    }


}

class AllCategoryByCGDisplayIteme(val cate: AddedImage): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.button_Catebtn.text = cate.name

        Picasso.get().load(cate.image_path).into(viewHolder.itemView.imageView_Catepic)
    }

    override fun getLayout(): Int {
        return R.layout.allcategoryframe
    }
}



class CategorysDelbtnIteme(): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.imageView2

    }

    override fun getLayout(): Int {
        return R.layout.delbutn
    }
}



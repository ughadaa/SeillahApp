package com.example.ratina.sellahapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_caregiver_chooseto_delete.*
import kotlinx.android.synthetic.main.allcategoryframe.view.*

class CaregiverChoosetoDelete : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caregiver_chooseto_delete)
        fetchCategorys()

        supportActionBar?.title = "الرجاء اختيار التصنيف للحذف"

    }

    companion object {
        val CATE_KEY = "CATE_KEY"
    }


    private fun fetchCategorys() {
        val theuser = FirebaseAuth.getInstance().currentUser

        theuser?.let {
            // email address for the current logged in user
            val CaregiverEmail = theuser.email
            val ref = FirebaseDatabase.getInstance().getReference("/all_catebyuser").orderByChild("useradding")
                .equalTo(CaregiverEmail)


            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {
                    val adaptere = GroupAdapter<ViewHolder>()

                    p0.children.forEach {
                        Log.d("NewMessage", it.toString())
                        val cate = it.getValue(AddedCategory::class.java)

                        adaptere.add(AllCategoryCGDelIteme(cate!!))

                    }

                    adaptere.setOnItemClickListener { item, view ->
                        val CateItem = item as AllCategoryCGDelIteme

                        val intent = Intent(this@CaregiverChoosetoDelete, CaregiverDeleteCategory::class.java)
                        intent.putExtra(CATE_KEY, CateItem.cate)
                        startActivity(intent)

                    }

                    AllcateToDelgoryRecy.adapter = adaptere
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
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

class AllCategoryCGDelIteme(val cate: AddedCategory): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.button_Catebtn.text = cate.arabicname

        // "animal"+()

        Picasso.get().load(cate.image_path).into(viewHolder.itemView.imageView_Catepic)
    }

    override fun getLayout(): Int {
        return R.layout.allcategoryframe
    }
}
package com.example.ratina.sellahapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import com.example.ratina.sellahapp.Chat.Chatroom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.activity_child_choose_category.*
import kotlinx.android.synthetic.main.allcategoryframe.view.*
import kotlinx.android.synthetic.main.picframe.view.*

class ChildChooseCategory : AppCompatActivity() {
    var toUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_choose_category)

        supportActionBar?.title = "التصنيفات"

        fetchCategorys()
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


    private fun fetchCategorys() {


        val ref = FirebaseDatabase.getInstance().getReference("/all_Cate")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adaptere = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val cate = it.getValue(AddedCategory::class.java)

                    adaptere.add(AllCategoryIteme(cate!!))

                }


                val theuser = FirebaseAuth.getInstance().currentUser

        theuser?.let {
            // email address for the current logged in user
            val uid = theuser.uid

            val refes = FirebaseDatabase.getInstance().getReference("/users/$uid").child("cgemail")


            refes.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {

                    // val listOfChildrenss = mutableListOf<String>()
                    val CaregiversOfuser = ArrayList<String>()

                    p0.children.forEach {
                        Log.d("رسالة جديدة", it.toString())
                        //val user = it.getValue(User::class.java)

                        val urser = it.getValue(String::class.java) //get the value of the already existing emails
                        // listOfChildrenss.add(urser!!)
                        CaregiversOfuser.add(urser!!)


                    }


                    for (item in CaregiversOfuser) {

                        val refe =
                            FirebaseDatabase.getInstance().getReference("/all_catebyuser")
                                .orderByChild("useradding")
                                .equalTo(item)


                        refe.addListenerForSingleValueEvent(object : ValueEventListener {

                            override fun onDataChange(p0: DataSnapshot) {
                                // val adapteres = GroupAdapter<ViewHolder>()

                                p0.children.forEach {
                                    Log.d("NewMessage", it.toString())
                                    val cate = it.getValue(AddedCategory::class.java)

                                    adaptere.add(AllCategoryIteme(cate!!))

                                }

                                adaptere.setOnItemClickListener { item, view ->
                                    val CateItem = item as AllCategoryIteme

                                    val intent = Intent(this@ChildChooseCategory, CategoryDisplay::class.java)
                                    intent.putExtra(CATE_KEY, CateItem.cate)
                                    startActivity(intent)

                                }

                                AllcategoryRecy.adapter = adaptere
                            }

                            override fun onCancelled(p0: DatabaseError) {

                            }
                        })///////////
                    }//////
                }


                override fun onCancelled(p0: DatabaseError) {

                }
            })


        }/////end of if

            }

    override fun onCancelled(p0: DatabaseError) {

    }
})


    }
    }



///////////////////////////////////////////
    private fun fechChildcaregiver() {

        val theuser = FirebaseAuth.getInstance().currentUser

        theuser?.let {
            // email address for the current logged in user
            val uid = theuser.uid

            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid").child("cgemail")


            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {

                    // val listOfChildrenss = mutableListOf<String>()
                    val CaregiversOfuser = ArrayList<String>()

                    p0.children.forEach {
                        Log.d("رسالة جديدة", it.toString())
                        val user = it.getValue(User::class.java)

                        val urser = it.getValue(String::class.java) //get the value of the already existing emails
                        // listOfChildrenss.add(urser!!)
                        CaregiversOfuser.add(urser!!)


                    }



                }


                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }

    }


class AllCategoryIteme(val cate: AddedCategory): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.button_Catebtn.text = cate.arabicname

        Picasso.get().load(cate.image_path).into(viewHolder.itemView.imageView_Catepic)
    }

    override fun getLayout(): Int {
        return R.layout.allcategoryframe
    }
}



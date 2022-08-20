package com.example.ratina.sellahapp.MedicalSpecialist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.ratina.sellahapp.AddChild
import com.example.ratina.sellahapp.Games.SAVEDATAA
import com.example.ratina.sellahapp.Help
import com.example.ratina.sellahapp.Login
import com.example.ratina.sellahapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_view_result.*
import kotlinx.android.synthetic.main.result_for_specialist.view.*

class ViewResult : AppCompatActivity() {

    var toDate: SAVEDATAA? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_result)

        toDate = intent.getParcelableExtra<SAVEDATAA>(ViewResultsByDate.DATE_KEY)
        supportActionBar?.title = toDate?.date

        //if usergetextra == savdata/userplaing..get me date,etc

        fetchResult()

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

    private fun fetchResult() {
        val ref = FirebaseDatabase.getInstance().getReference("/SAVEDATAA").orderByChild("id").equalTo(toDate!!.id)
        val theuser = FirebaseAuth.getInstance().currentUser

        val refe = FirebaseDatabase.getInstance().getReference().child("SAVEDATAA")
            // email address for the current logged in user

            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {
                    val adapter = GroupAdapter<ViewHolder>()

                    p0.children.forEach {
                        Log.d("NewMessage", it.toString())
                        //val user = it.getValue(User::class.java)

                        val key = it.key
                        val userdata = it.getValue(SAVEDATAA::class.java)

                        adapter.add(ResultItem(userdata!!))

                    }


                    ResultList_RecyclerView.adapter = adapter

                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean { //overriding the method to give the specialist this set of menu
        when (item?.itemId) {


            R.id.menu_home -> { //first item of menu enable the user to go to the home page
                val intent = Intent(this, MedicalSpecialistHome::class.java)
                startActivity(intent)
            }

            R.id.menu_settings -> { //second item of menu enable the user to go to the settings page
                val intent = Intent(this, AddChild::class.java)
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
class ResultItem(val userdata: SAVEDATAA): Item<ViewHolder>() {


    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.txt_right_answer.text = "النتيجة:\n"+userdata.right_answear_counter.toString()+"/"+ userdata.total_counter.toString()
        viewHolder.itemView.txt_result.text = "النسبة:\n"+userdata.percent2
        viewHolder.itemView.cliok.text = "التاريخ:\n" + userdata.date
        viewHolder.itemView.txt_Time.text= "الوقت الإجمالي:\n" + userdata.time_counter
        viewHolder.itemView.btn_filler_total.text= "مجموع الأسئلة:\n" + userdata.total_counter
        viewHolder.itemView.btn_filler_right.text= "الإجابات الصحيحة:\n" + userdata.right_answear_counter
        viewHolder.itemView.btn_filler_Worng.text= "الإجابات الخاطئة:\n" + userdata.wrong_filler_counter
        viewHolder.itemView.textView2.text=userdata.time1.toString()
        viewHolder.itemView.textView4.text=userdata.time2.toString()
        viewHolder.itemView.textView133.text=userdata.time3.toString()
        viewHolder.itemView.textView13.text=userdata.time4.toString()
        viewHolder.itemView.textView15.text=userdata.time5.toString()
        viewHolder.itemView.textView16.text=userdata.time6.toString()

        viewHolder.itemView.textView10.text=userdata.time7.toString()
        viewHolder.itemView.textView17.text=userdata.time8.toString()
        viewHolder.itemView.textView11.text=userdata.time9.toString()
        viewHolder.itemView.textView12.text=userdata.time10.toString()






    }

    override fun getLayout(): Int {
        return R.layout.result_for_specialist
    }
}

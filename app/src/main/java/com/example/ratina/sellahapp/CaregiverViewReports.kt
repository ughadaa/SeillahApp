package com.example.ratina.sellahapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.ratina.sellahapp.MedicalSpecialist.Report
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_caregiver_view_reports.*
import kotlinx.android.synthetic.main.user_row.view.*

class CaregiverViewReports : AppCompatActivity() {

    var toUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caregiver_view_reports)

        toUser = intent.getParcelableExtra<User>(CaregiverViewChildrenReports.USER_KEY)
        supportActionBar?.title = toUser?.username + "التقارير الطبية الخاصة بـ"


        fetchReports()

        // calling the reports' dates for that specified child from 'CaregiverViewChildrenReport' page
        //so if the c.g clicks on the date of the report it'll take the c.g to the actual report written in that date



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
        val DATE_KEY = "DATE_KEY"
    }

    private fun fetchReports() {


        val ref = FirebaseDatabase.getInstance().getReference("/reports").orderByChild("aboutchild").equalTo(toUser!!.uid)


        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    //val user = it.getValue(User::class.java)

                    val key = it.key
                    val reportdata = it.getValue(Report::class.java)

                    adapter.add(ViewChildREportItem(reportdata!!))

                }

                adapter.setOnItemClickListener { item, view ->

                    val userDData = item as ViewChildREportItem

                    val intent = Intent(view.context, CaregiverViewChildReport::class.java)
//          intent.putExtra(USER_KEY,  userItem.user.username)
                    intent.putExtra(DATE_KEY, userDData.reportdata)
                    startActivity(intent)

                }

                CaregiverViewReportList_RecyclerView.adapter = adapter

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




class ViewChildREportItem(val reportdata: Report): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview_new_message.text = reportdata.reportdate
    }

    override fun getLayout(): Int {
        return R.layout.user_row
    }
}

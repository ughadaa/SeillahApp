package com.example.ratina.sellahapp

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CaregiverDeleteCategory : AppCompatActivity() {

    var catedel: AddedCategory? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caregiver_delete_category)

        catedel = intent.getParcelableExtra<AddedCategory>(CaregiverChoosetoDelete.CATE_KEY)


        deletecategory()
    }


    private fun deletecategory() {

        val categorypath = catedel?.name
        val ref = FirebaseDatabase.getInstance().getReference(categorypath!!)


        val builder = AlertDialog.Builder(this)
        builder.setTitle("تنبيه!")
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.deletewarn, null)

        builder.setView(view)

        builder.setPositiveButton("متأكد", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {


                ref.removeValue() //WHOLE CATE!!!!!!!!
                    .addOnSuccessListener {

                        DeletfromAllCatebyuser()
                        val intent = Intent(this@CaregiverDeleteCategory, CaregiverChooseCategory::class.java)
                        startActivity(intent)


                    }
                    .addOnFailureListener {
                        Log.d("delete", "failed")
                    }




                Toast.makeText(this@CaregiverDeleteCategory, "تم حذف التصنيف ", Toast.LENGTH_LONG).show()


            }
        })

        builder.setNegativeButton("إلغاء", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {


                val intent = Intent(this@CaregiverDeleteCategory, CaregiverChooseCategory::class.java)
                startActivity(intent)

            }

        })

        val alert = builder.create()
        alert.show()


    }



    private fun DeletfromAllCatebyuser() {

        val categorypath = catedel?.name
        val ref = FirebaseDatabase.getInstance().getReference("/all_catebyuser").orderByChild("name")
            .equalTo(categorypath)


        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {
                    Log.d("THE CATE", it.toString())
                    val cate = it.getValue(AddedCategory::class.java)

                    if(cate!!.name==categorypath){
                        //ref.removeEventListener(this)
                        it.ref.setValue(null)
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

}


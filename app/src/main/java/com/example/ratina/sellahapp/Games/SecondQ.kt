package com.example.ratina.sellahapp.Games

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.ratina.sellahapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_second_q.*
import java.util.*

class SecondQ : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_q)

        fetchImageGame()


    }


    private fun fetchImageGame() {
        val ref = FirebaseDatabase.getInstance().getReference("/animal_category")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val urlList = ArrayList<String>()
                val wordsList = ArrayList<String>()
                val RandomwordsList = ArrayList<String>()


                p0.children.forEach {

                    val url = it.child("image_path").getValue(String::class.java)
                    urlList.add(url!!)
                    val word = it.child("animal").getValue(String::class.java)
                    //it.getValue(Category::class.java)
                    wordsList.add(word!!)
                    val words = it.child("animal").getValue(String::class.java)
                    RandomwordsList.add(words!!)


                    ////
                    //  val ran = it.getValue(Category::class.java)

                    //  Picasso.get().load(ran?.image_path).into(imageViewOfQ)
                    //adaptere.add(CategoryIteme(cate!!))

                }
                //end of loop

                val urlCount = urlList.size
                val randomNumber = Random().nextInt(urlCount)
                // val randomUrlList = ArrayList<String>()

                //randomUrlList.add(urlList.get(randomNumber))
                //  val ran = it.getValue(Category::class.java)

                // val ran = Category(urlList.get(randomNumber),)
                Picasso.get().load(urlList.get(randomNumber)).into(imageViewOfsecQ)
                //firstB.text = wordsList.get(randomNumber) // it is the correct answer
                //into (random place)

                //if randomWordsList.get(i) != the correct on.....to avoid repreation
                val wordsCount = RandomwordsList.size
                val randomWordsNumber = Random().nextInt(wordsCount)
                val randomAnWordsNumber = Random().nextInt(wordsCount)

                //   val randomttWordsList = ArrayList<String>()

                val randomPlace = Random().nextInt(3)

                // secB.text = RandomwordsList.get(randomWordsNumber)
                //   thirdB.text = RandomwordsList.get(randomAnWordsNumber+1)



                val ralist = ArrayList<Int>()
                for (i in 0..wordsList.size) {
                    ralist.add(i)
                }

                if (randomPlace == 0){
                    buttonfirst.text = wordsList.get(randomNumber) //after that remove to avoid repreation

                    ralist.remove(randomNumber)
                    // secB.text = RandomwordsList.get(randomWordsNumber)
                    //  thirdB.text = RandomwordsList.get(randomAnWordsNumber+1)
                    buttonsecond.text = RandomwordsList.get(ralist.get(0))
                    ralist.remove(0)
                    buttonthird.text = RandomwordsList.get(ralist.get(0))
                }

                if (randomPlace == 1){

                    buttonsecond.text = wordsList.get(randomNumber) //after that remove to avoid repreation
                    ralist.remove(randomNumber)

                    buttonfirst.text = RandomwordsList.get(ralist.get(0))
                    ralist.remove(0)
                    buttonthird.text = RandomwordsList.get(ralist.get(0))
                }

                if (randomPlace == 2){
                    buttonthird.text = wordsList.get(randomNumber) //after that remove to avoid repreation

                    ralist.remove(randomNumber)
                    buttonsecond.text = RandomwordsList.get(ralist.get(0))
                    ralist.remove(0)
                    buttonfirst.text = RandomwordsList.get(ralist.get(0))
                }
                if (randomPlace == 3){
                    buttonfirst.text = wordsList.get(randomNumber) //after that remove to avoid repreation

                    ralist.remove(randomNumber)
                    buttonthird.text = RandomwordsList.get(ralist.get(0))
                    ralist.remove(0)
                    buttonsecond.text = RandomwordsList.get(ralist.get(0))
                }







                //  categoryRecy.adapter = adaptere
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }



}

package com.example.ratina.sellahapp.Games

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.example.ratina.sellahapp.*
import com.example.ratina.sellahapp.Games.SAVEDATAA
import com.example.ratina.sellahapp.Games.counter.counter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_game.*

import kotlinx.android.synthetic.main.activity_result.*
import java.util.*

class Games10 : AppCompatActivity() {
    lateinit var sandd: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games10)






        val firstB = findViewById<Button>(R.id.firstB)
        val secB = findViewById<Button>(R.id.secB)
        val thirdB = findViewById<Button>(R.id.thirdB)



        sandd = findViewById(R.id.z)

        fetchImageGame()





    }




    private fun savedata(){

        var percent2 = " "
        counter.percent  = counter.right_answer_count * 100 / 10
        val percent=  counter.percent

        if (counter.percent >=100)
            percent2="100%"
        else
            if (counter.percent  >= 90)

                percent2= "90%"
            else
                if (counter.percent  >=80)

                    percent2= "80%"
                else
                    if (counter.percent  >=70)

                        percent2= "70%"
                    else
                        if (counter.percent >= 60)


                            percent2= "60%"

                        else
                            if (counter.percent  >= 50)

                                percent2="50%"
                            else
                                if (counter.percent  >=40)

                                    percent2="40%"


                                else
                                    if (counter.percent  >= 30)

                                        percent2= "30%"

                                    else
                                        if (counter.percent  >= 20)

                                            percent2= "20%"

                                        else
                                            if (counter.percent  >= 10)

                                                percent2="10%"

                                            else

                                                percent2= "0%"





        val time1=counter.timerr1
        val time2=counter.timerr2
        val time3=counter.timerr3
        val time4=counter.timerr4
        val time5=counter.timerr5
        val time6=counter.timerr6
        val time7=counter.timerr7
        val time8= counter.timerr8
        val time9= counter.timerr9
        val time10=counter.timerr10
        val Right_answear_counter=  counter.right_answer_count
        val WORNG_filler_counter =counter.worng_filler_count
        val TOTAL_counter =counter.right_answer_count + counter.worng_filler_count
        val TIME_counter = counter.totalTime
        val calendar = Calendar.getInstance()
        val currentDate= calendar.get(Calendar.MONTH).toString() + "/ " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(
            Calendar.YEAR)
        val DATE = currentDate

        val userplaying = FirebaseAuth.getInstance().uid


/////////////////////

        val mDatabase = FirebaseDatabase.getInstance().getReference("SAVEDATAA")
        val ID=mDatabase.push().key
        val result= SAVEDATAA(ID!!,userplaying!!,Right_answear_counter, WORNG_filler_counter, TOTAL_counter, TIME_counter,DATE,time1,time2,time3,time4,time5,time6,time7,time8,time9,time10,percent2 )
        mDatabase.child(ID).setValue(result).addOnCompleteListener{ Toast.makeText(this,"save", Toast.LENGTH_LONG).show()}
    }
    //حساب الوقت
    inner class MyCounter(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onFinish() {

            counter.test++
            var intent3: Intent = Intent(this@Games10, Result::class.java)
            startActivity(intent3)

            // startActivity(intent3)
        }

        override fun onTick(millisUntilFinished: Long) {
            tv.textSize = 50f

            tv.text = (millisUntilFinished / 1000).toString() + ""
            println("Timer  : " + millisUntilFinished / 1000)


            //سجل النتيجه الوقت

            if( counter.test==0) {
                counter.timerr1 = (millisUntilFinished / 1000).toDouble()
                counter.timerr1 = 60 - counter.timerr1
            }

            else
                if(counter.test==1) {

                    counter.timerr2 = (millisUntilFinished / 1000).toDouble()
                    counter.timerr2 = 60 - counter.timerr2
                }

                else
                    if(counter.test==2){
                        counter.timerr3 = (millisUntilFinished / 1000).toDouble()
                        counter.timerr3 = 60 - counter.timerr3}

                    else
                        if(counter.test==3){
                            counter.timerr4 = (millisUntilFinished / 1000).toDouble()
                            counter.timerr4= 60 - counter.timerr4}
                        else
                            if(counter.test==4){
                                counter.timerr5= (millisUntilFinished / 1000).toDouble()
                                counter.timerr5= 60 - counter.timerr5}

                            else
                                if(counter.test==5){
                                    counter.timerr6= (millisUntilFinished / 1000).toDouble()
                                    counter.timerr6= 60 - counter.timerr6}

                                else{
                                    counter.timerr6= (millisUntilFinished / 1000).toDouble()
                                    counter.timerr6= 60 - counter.timerr6
                                    //  timer.cancel()

                                }
            if(counter.test==6){
                counter.timerr7= (millisUntilFinished / 1000).toDouble()
                counter.timerr7= 60 - counter.timerr7}
            else
                if(counter.test==7)   {
                    counter.timerr8= (millisUntilFinished / 1000).toDouble()
                    counter.timerr8= 60 - counter.timerr8
                    //  timer.cancel()

                }
            if(counter.test==8){
                counter.timerr9= (millisUntilFinished / 1000).toDouble()
                counter.timerr9= 60 - counter.timerr9}
            else if (counter.test==9){
                counter.timerr10= (millisUntilFinished / 1000).toDouble()
                counter.timerr10= 60 - counter.timerr10
            }




        }
    }


    private fun fetchImageGame() {


        val timer = MyCounter(60000, 1000)
        timer.start()
        var intent2: Intent = Intent(this, Result::class.java)
        //var intent3: Intent = Intent(this, Result::class.java)

        val ref = FirebaseDatabase.getInstance().getReference("/the_games")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val urlList = ArrayList<String>()
                val wordsList = ArrayList<String>()
                val RandomwordsList = ArrayList<String>()


                p0.children.forEach {

                    val url = it.child("image_path").getValue(String::class.java)
                    urlList.add(url!!)
                    val word = it.child("name").getValue(String::class.java)
                    //it.getValue(Category::class.java)
                    wordsList.add(word!!)
                    val words = it.child("name").getValue(String::class.java)
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
                Picasso.get().load(urlList.get(randomNumber)).into(imageViewOfQ)
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
                for (i in 0..wordsList.size) {    if(i<wordsList.size&&i<RandomwordsList.size)
                    ralist.add(i)
                }
                val rr =ralist.size-1
                val randFromralist=Random().nextInt(rr)
                val otherrandFromralist=Random().nextInt(rr)



                if (randomPlace == 0){
                    firstB.text = wordsList.get(randomNumber) //after that remove to avoid repreation

                    ralist.remove(randomNumber)
                    // secB.text = RandomwordsList.get(randomWordsNumber)
                    //  thirdB.text = RandomwordsList.get(randomAnWordsNumber+1)
                    secB.text = wordsList.get(ralist.get(randFromralist))
                    ralist.remove(randFromralist)
                    thirdB.text = wordsList.get(ralist.get(otherrandFromralist))


                    ///////////button color change depanding on place of correct answer
                    firstB.setOnClickListener({
                            v->

                        counter. right_answer_count++
                        // btn_filler_right.text = "$right_answer_count"
                        v.setBackgroundColor(Color.GREEN)

                        counter.test++
                        timer.cancel()
                        intent2.putExtra("thecount", counter.right_answer_count)
                        intent2.putExtra("worng", counter.worng_filler_count)
                        savedata()
                        startActivity(intent2)

                    })


                    secB.setOnClickListener({
                            v->

                        counter.worng_filler_count++
                        counter.test++
                        v.setBackgroundColor(Color.RED)
                        timer.cancel()
                        intent2.putExtra("thecount", counter.right_answer_count)
                        intent2.putExtra("worng", counter.worng_filler_count)
                        savedata()
                        startActivity(intent2)


                    })

                    thirdB.setOnClickListener({
                            v->

                        counter.worng_filler_count++
                        timer.cancel()
                        counter.test++
                        v.setBackgroundColor(Color.RED)

                        intent2.putExtra("thecount", counter.right_answer_count)
                        intent2.putExtra("worng", counter.worng_filler_count)
                        savedata()
                        startActivity(intent2)

                    })



                }

                if (randomPlace == 1){

                    secB.text = wordsList.get(randomNumber) //after that remove to avoid repreation
                    ralist.remove(randomNumber)

                    firstB.text = wordsList.get(ralist.get(randFromralist))
                    ralist.remove(randFromralist)
                    thirdB.text = wordsList.get(ralist.get(otherrandFromralist))
                    ralist.remove(otherrandFromralist)


                    ///////////button color change depanding on place of correct answer
                    secB.setOnClickListener({
                            v->

                        counter. right_answer_count++
                        // btn_filler_right.text = "$right_answer_count"
                        v.setBackgroundColor(Color.GREEN)

                        counter.test++
                        timer.cancel()
                        intent2.putExtra("thecount", counter.right_answer_count)
                        intent2.putExtra("worng", counter.worng_filler_count)
                        savedata()
                        startActivity(intent2)

                    })


                    firstB.setOnClickListener({
                            v->

                        counter.worng_filler_count++

                        v.setBackgroundColor(Color.RED)
                        counter.test++
                        timer.cancel()
                        intent2.putExtra("thecount", counter.right_answer_count)
                        intent2.putExtra("worng", counter.worng_filler_count)
                        savedata()
                        startActivity(intent2)

                    })

                    thirdB.setOnClickListener({
                            v->

                        counter.worng_filler_count++
                        timer.cancel()
                        counter.test++
                        v.setBackgroundColor(Color.RED)
                        intent2.putExtra("thecount", counter.right_answer_count)
                        intent2.putExtra("worng", counter.worng_filler_count)
                        savedata()
                        startActivity(intent2)


                    })




                }

                if (randomPlace == 2){
                    thirdB.text = wordsList.get(randomNumber) //after that remove to avoid repreation

                    ralist.remove(randomNumber)
                    secB.text = wordsList.get(ralist.get(randFromralist))
                    ralist.remove(randFromralist)
                    firstB.text = wordsList.get(ralist.get(otherrandFromralist))
                    ralist.remove(otherrandFromralist)


                    ///////////button color change depanding on place of correct answer
                    thirdB.setOnClickListener({
                            v->

                        counter. right_answer_count++
                        // btn_filler_right.text = "$right_answer_count"
                        v.setBackgroundColor(Color.GREEN)
                        timer.cancel()
                        counter.test++
                        intent2.putExtra("thecount", counter.right_answer_count)
                        intent2.putExtra("worng", counter.worng_filler_count)
                        savedata()
                        startActivity(intent2)


                    })

                    secB.setOnClickListener({
                            v->

                        counter.worng_filler_count++
                        timer.cancel()

                        counter.test++
                        v.setBackgroundColor(Color.RED)
                        intent2.putExtra("thecount", counter.right_answer_count)
                        intent2.putExtra("worng", counter.worng_filler_count)
                        savedata()
                        startActivity(intent2)

                    })


                    firstB.setOnClickListener({
                            v->

                        counter.worng_filler_count++
                        timer.cancel()
                        counter.test++
                        v.setBackgroundColor(Color.RED)
                        intent2.putExtra("thecount", counter.right_answer_count)
                        intent2.putExtra("worng", counter.worng_filler_count)
                        savedata()
                        startActivity(intent2)

                    })



                }
                if (randomPlace == 3){
                    firstB.text = wordsList.get(randomNumber) //after that remove to avoid repreation

                    ralist.remove(randomNumber)
                    thirdB.text = wordsList.get(ralist.get(randFromralist))
                    ralist.remove(randFromralist)
                    secB.text = wordsList.get(ralist.get(otherrandFromralist))
                    ralist.remove(otherrandFromralist)



                    ///////////button color change depanding on place of correct answer
                    firstB.setOnClickListener({
                            v->

                        counter. right_answer_count++
                        // btn_filler_right.text = "$right_answer_count"
                        v.setBackgroundColor(Color.GREEN)
                        timer.cancel()
                        counter.test++
                        intent2.putExtra("thecount", counter.right_answer_count)
                        intent2.putExtra("worng", counter.worng_filler_count)
                        savedata()
                        startActivity(intent2)


                    })


                    secB.setOnClickListener({
                            v->

                        counter.worng_filler_count++
                        counter.test++
                        v.setBackgroundColor(Color.RED)
                        timer.cancel()
                        intent2.putExtra("thecount", counter.right_answer_count)
                        intent2.putExtra("worng", counter.worng_filler_count)
                        savedata()
                        startActivity(intent2)

                    })

                    thirdB.setOnClickListener({
                            v->

                        counter.worng_filler_count++
                        timer.cancel()
                        counter.test++
                        v.setBackgroundColor(Color.RED)
                        intent2.putExtra("thecount", counter.right_answer_count)
                        intent2.putExtra("worng", counter.worng_filler_count)
                        savedata()
                        startActivity(intent2)

                    })


                }



                //  categoryRecy.adapter = adaptere
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        sandd.setOnClickListener {


            timer.cancel()

            intent2.putExtra("thecount", counter.right_answer_count)
            intent2.putExtra("worng", counter.worng_filler_count)
            counter.total_filler_count = counter.right_answer_count + counter.worng_filler_count
            intent2.putExtra("total", counter.total_filler_count)





            intent2.putExtra("TIME", counter.timerr1)
            intent2.putExtra("TIME2", counter.timerr2)
            intent2.putExtra("TIME3", counter.timerr3)
            intent2.putExtra("TIME4", counter.timerr4)
            intent2.putExtra("TIME5", counter.timerr5)
            intent2.putExtra("TIME6", counter.timerr6)
            intent2.putExtra("TIME7", counter.timerr7)
            intent2.putExtra("TIME8", counter.timerr8)
            intent2.putExtra("TIME9", counter.timerr9)
            intent2.putExtra("TIME10", counter.timerr10)

            counter.totalTime = counter.timerr1+counter.timerr2+counter.timerr3+counter.timerr4+counter.timerr5+counter.timerr6+counter.timerr7+counter.timerr8+counter.timerr9+counter.timerr10

            savedata()
            intent2.putExtra("Total_Time", counter.totalTime)

            startActivity(intent2)




        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean { //overriding the method to give the user this set of menu
        when (item?.itemId) {

            R.id.menu_home -> { //first item of menu enable the user to go to the home page
                val intent = Intent(this, ChildHome::class.java)
                startActivity(intent)
            }

            R.id.menu_settings -> { //second item of menu enable the user to go to the settings page
                val intent = Intent(this, ChildSettings::class.java)
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









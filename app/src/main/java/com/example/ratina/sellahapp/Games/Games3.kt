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
import com.example.ratina.sellahapp.Games.counter.counter

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*

class Games3 : AppCompatActivity() {
    lateinit var sandd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games3)

        //Ask questions // for each button
        val firstB = findViewById<Button>(R.id.firstB)
        val secB = findViewById<Button>(R.id.secB)
        val thirdB = findViewById<Button>(R.id.thirdB)


        sandd = findViewById(R.id.z)
// Extract images and words
        fetchImageGame()



    }


    // Save the results
    private fun savedata(){
// Percent calculated
        var percent2 = " "

        if(counter.total_filler_count>0)
            counter.percent  = counter.right_answer_count * 100 / counter.total_filler_count
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




// Calculates the time for each swing
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
        // Calculates the correct answers
        val Right_answear_counter=  counter.right_answer_count
        // Calculates the wrong answers
        val WORNG_filler_counter =counter.worng_filler_count
        // Calculates the total number of questions
        val TOTAL_counter =counter.right_answer_count + counter.worng_filler_count
        val TIME_counter = counter.totalTime
        // Set the date of the day
        val calendar = Calendar.getInstance()
        val currentDate= calendar.get(Calendar.MONTH).toString() + "/ " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(
            Calendar.YEAR)
        val DATE = currentDate

        val userplaying = FirebaseAuth.getInstance().uid


// Get Class Name "Save"
        val mDatabase = FirebaseDatabase.getInstance().getReference("SAVEDATAA")
        val ID=mDatabase.push().key
        val result=SAVEDATAA(ID!!,userplaying!!,Right_answear_counter, WORNG_filler_counter, TOTAL_counter, TIME_counter,DATE,time1,time2,time3,time4,time5,time6,time7,time8,time9,time10,percent2 )
        mDatabase.child(ID).setValue(result).addOnCompleteListener{ Toast.makeText(this,"save", Toast.LENGTH_LONG).show()}
    }


    // Here the countdown starts
    inner class MyCounter(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        // // When the time runs out and the counter becomes zero,
        // it starts to move to the second page
        override fun onFinish() {

            counter.test++
            //لو الوقت انتهى يروح السوال الثاني
            var intent3: Intent = Intent(this@Games3, games4::class.java)

            startActivity(intent3)



            // startActivity(intent3)
        }

        // Here the countdown starts
        override fun onTick(millisUntilFinished: Long) {

            // Text size time
            tv.textSize = 50f

            tv.text = (millisUntilFinished / 1000).toString() + ""
            println("Timer  : " + millisUntilFinished / 1000)



// Each page has a time counter, for each time
// its page is an ideal example if the zero counter will
// go to the home page
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

                                else
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


    // Here get the data and work it random and time
    private fun fetchImageGame() {

// The beginning of time
        val timer = MyCounter(60000, 1000)
        timer.start()
// moving in


        var intent2: Intent = Intent(this, Result::class.java)
        var intent3: Intent = Intent(this,Games5 ::class.java)
        val ref = FirebaseDatabase.getInstance().getReference("/the_games")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            // Get data from DataPase // Random action
            override fun onDataChange(p0: DataSnapshot) {
                val urlList = ArrayList<String>()
                val wordsList = ArrayList<String>()
                val RandomwordsList = ArrayList<String>()

// From DataPase Select the image link and name
                p0.children.forEach {

                    val url = it.child("image_path").getValue(String::class.java)
                    urlList.add(url!!)
                    val word = it.child("name").getValue(String::class.java)
                    //it.getValue(Category::class.java)
                    wordsList.add(word!!)
                    val words = it.child("name").getValue(String::class.java)
                    RandomwordsList.add(words!!)


                }
                //end of loop

                val urlCount = urlList.size
                val randomNumber = Random().nextInt(urlCount)



// His office is ready to recall photos
                Picasso.get().load(urlList.get(randomNumber)).into(imageViewOfQ)

// Random action to scatter words
                val wordsCount = RandomwordsList.size
                val randomWordsNumber = Random().nextInt(wordsCount)
                val randomAnWordsNumber = Random().nextInt(wordsCount)


                val randomPlace = Random().nextInt(3)



                val ralist = ArrayList<Int>()
                // condition prevents if it is less or more size wordsList
                for (i in 0..wordsList.size) {
                    if(i<wordsList.size&&i<RandomwordsList.size)
                    // If successful, add
                        ralist.add(i)
                }

// random variable
                val rr =ralist.size-1
                val randFromralist=Random().nextInt(rr)
                val otherrandFromralist=Random().nextInt(rr)



// Here chooses which place will be stored
                // The aim is to change random possibilities

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

                        startActivity(intent3)

                    })


                    secB.setOnClickListener({
                            v->

                        counter.worng_filler_count++
                        counter.test++
                        v.setBackgroundColor(Color.RED)
                        timer.cancel()
                        startActivity(intent3)
                    })

                    thirdB.setOnClickListener({
                            v->

                        counter.worng_filler_count++
                        timer.cancel()
                        counter.test++
                        v.setBackgroundColor(Color.RED)

                        startActivity(intent3)
                    })



                }
// Here chooses which place will be stored
// The aim is to change random possibilities
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

                        startActivity(intent3)

                    })


                    firstB.setOnClickListener({
                            v->

                        counter.worng_filler_count++

                        v.setBackgroundColor(Color.RED)
                        counter.test++
                        timer.cancel()
                        startActivity(intent3)
                    })

                    thirdB.setOnClickListener({
                            v->

                        counter.worng_filler_count++
                        timer.cancel()
                        counter.test++
                        v.setBackgroundColor(Color.RED)

                        startActivity(intent3)
                    })




                }
// Here chooses which place will be stored
                // The aim is to change random possibilities
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
                        startActivity(intent3)

                    })

                    secB.setOnClickListener({
                            v->

                        counter.worng_filler_count++
                        timer.cancel()

                        counter.test++
                        v.setBackgroundColor(Color.RED)
                        startActivity(intent3)
                    })


                    firstB.setOnClickListener({
                            v->

                        counter.worng_filler_count++
                        timer.cancel()
                        counter.test++
                        v.setBackgroundColor(Color.RED)

                        startActivity(intent3)
                    })



                }
                // Here chooses which place will be stored
                // The aim is to change random possibilities
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
                        startActivity(intent3)

                    })


                    secB.setOnClickListener({
                            v->

                        counter.worng_filler_count++
                        counter.test++
                        v.setBackgroundColor(Color.RED)
                        timer.cancel()
                        startActivity(intent3)
                    })

                    thirdB.setOnClickListener({
                            v->

                        counter.worng_filler_count++
                        timer.cancel()
                        counter.test++
                        v.setBackgroundColor(Color.RED)

                        startActivity(intent3)
                    })



                }
// Here chooses which place will be stored
                // The aim is to change random possibilities
            }

            // Special database
            override fun onCancelled(p0: DatabaseError) {

            }
        })


        // When I click Finish I will send the output
        sandd.setOnClickListener {
            // Time is over
            timer.cancel()
// Moves to the next page // View results
            intent2.putExtra("thecount", counter.right_answer_count)
            intent2.putExtra("worng", counter.worng_filler_count)
            counter.total_filler_count = counter.right_answer_count + counter.worng_filler_count
            intent2.putExtra("total", counter.total_filler_count)

            counter.timerr1=0.0

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
// Calculate total time total
            counter.totalTime = counter.timerr1+counter.timerr2+counter.timerr3+counter.timerr4+counter.timerr5+counter.timerr6+counter.timerr7+counter.timerr8+counter.timerr9+counter.timerr10
// The results are saved games
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








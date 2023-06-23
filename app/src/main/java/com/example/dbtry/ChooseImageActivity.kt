package com.example.dbtry

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChooseImageActivity : AppCompatActivity() {

    private lateinit var plantImage1: FrameLayout
    private lateinit var plantImage2: FrameLayout
    private lateinit var plantImage3: FrameLayout
    private lateinit var plantImage4: FrameLayout
    private lateinit var plantImage5: FrameLayout
    private lateinit var plantImage6: FrameLayout
    private lateinit var plantImage7: FrameLayout
    private lateinit var db: DatabaseReference

    private var plantRecord : String? = null

    private var choosenPlant : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_image)

        plantImage1 = findViewById(R.id.imgBenjaminekContainer)
        plantImage2 = findViewById(R.id.imgBonsaiContainer)
        plantImage3 = findViewById(R.id.imgKaktusContainer)
        plantImage4 = findViewById(R.id.imgKwiatekContainer)
        plantImage5 = findViewById(R.id.imgMonsteraContainer)
        plantImage6 = findViewById(R.id.imgSokulentContainer)
        plantImage7 = findViewById(R.id.imgStorczykContainer)

        plantRecord = intent.getStringExtra("PLANT_NAME")

        readData(plantRecord)


        plantImage1.setOnClickListener {
            selectImage(1)
        }
        plantImage2.setOnClickListener {
            selectImage(2)
        }
        plantImage3.setOnClickListener {
            selectImage(3)
        }
        plantImage4.setOnClickListener {
            selectImage(4)
        }
        plantImage5.setOnClickListener {
            selectImage(5)
        }
        plantImage6.setOnClickListener {
            selectImage(6)
        }
        plantImage7.setOnClickListener {
            selectImage(7)
        }
    }

    private fun readData(plantRecord: String?) {
        db = FirebaseDatabase.getInstance().getReference("10032311")

        if (plantRecord != null) {
            db.child(plantRecord).get().addOnSuccessListener {

                if (it.exists()) {

                    val image = it.child("image").value

                    choosenPlant = image?.toString()?.toInt()
                    plantShowSelection()
                    Toast.makeText(this, "$choosenPlant", Toast.LENGTH_SHORT).show()

                } else {
                    //                Toast.makeText(this, "Wrong data", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                //            Toast.makeText(this, "Wrong data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectImage(imageId: Int){

        db = FirebaseDatabase.getInstance().getReference(("10032311/" + plantRecord).toString())
        val updates = HashMap<String, Any>()
        db.child("image").setValue(imageId)

        db.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Data updated successfully", Toast.LENGTH_SHORT).show()
                recreate()
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, "Failed to update data: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        plantShowSelection()

    }

    private fun plantShowSelection(){
        when (choosenPlant) {
            1 ->{
                plantImage1.setBackgroundResource(R.drawable.chosen_plant_image)
                plantImage2.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage3.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage4.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage5.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage6.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage7.setBackgroundResource(R.drawable.choose_image_scroll)
            }
            2 ->{
                plantImage1.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage2.setBackgroundResource(R.drawable.chosen_plant_image)
                plantImage3.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage4.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage5.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage6.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage7.setBackgroundResource(R.drawable.choose_image_scroll)
            }
            3 ->{
                plantImage1.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage2.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage3.setBackgroundResource(R.drawable.chosen_plant_image)
                plantImage4.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage5.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage6.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage7.setBackgroundResource(R.drawable.choose_image_scroll)
            }
            4 ->{
                plantImage1.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage2.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage3.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage4.setBackgroundResource(R.drawable.chosen_plant_image)
                plantImage5.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage6.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage7.setBackgroundResource(R.drawable.choose_image_scroll)
            }
            5 ->{
                plantImage1.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage2.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage3.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage4.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage5.setBackgroundResource(R.drawable.chosen_plant_image)
                plantImage6.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage7.setBackgroundResource(R.drawable.choose_image_scroll)
            }
            6 ->{
                plantImage1.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage2.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage3.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage4.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage5.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage6.setBackgroundResource(R.drawable.chosen_plant_image)
                plantImage7.setBackgroundResource(R.drawable.choose_image_scroll)
            }
            7 ->{
                plantImage1.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage2.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage3.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage4.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage5.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage6.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage7.setBackgroundResource(R.drawable.chosen_plant_image)
            }
            else ->{
                plantImage1.setBackgroundResource(R.drawable.chosen_plant_image)
                plantImage2.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage3.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage4.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage5.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage6.setBackgroundResource(R.drawable.choose_image_scroll)
                plantImage7.setBackgroundResource(R.drawable.choose_image_scroll)

            }
        }
    }
}
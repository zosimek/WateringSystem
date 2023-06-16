package com.example.dbtry

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.dbtry.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    private val checkConnection by lazy {CheckConnection(application)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply{

            layoutConnection.visibility = View.VISIBLE
            checkConnection.observe(this@MainActivity){
                if(it){
                    layoutConnection.setBackgroundColor(Color.parseColor("#00ffffff"))
                    imgConnection.setImageResource(R.drawable.internet_connected)
                    txtConnection.setText(R.string.connected)
                    txtMakeConnection.visibility = View.INVISIBLE
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            plantActivity()
                        },
                        1500 // value in milliseconds
                    )

                }
                else{
                    layoutConnection.setBackgroundColor(Color.parseColor("#8DE80000"))
                    imgConnection.setImageResource(R.drawable.internet_disconnected)
                    txtConnection.setText(R.string.disconnected)
                }
            }
        }
    }

    fun plantActivity(){
        val intent = Intent(this@MainActivity, PlantActivity::class.java)
        startActivity(intent)
    }
}
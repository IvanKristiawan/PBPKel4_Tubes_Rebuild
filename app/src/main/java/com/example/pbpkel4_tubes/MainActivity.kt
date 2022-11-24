package com.example.pbpkel4_tubes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.pbpkel4_tubes.databinding.ActivityMainBinding
//import maes.tech.intentanim.CustomIntent
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding :
            ActivityMainBinding
    private lateinit var mainLayout:ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding =
            ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
//            saveData()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        setTitle("User Login")

        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
//            CustomIntent.customType(this, "fadein-to-fadeout")
        }
    }
}
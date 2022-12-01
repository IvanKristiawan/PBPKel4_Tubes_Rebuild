package com.example.pbpkel4_tubes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.pbpkel4_tubes.api.PaketTravelApi
import com.example.pbpkel4_tubes.databinding.ActivityAddEditBinding
import com.example.pbpkel4_tubes.databinding.ActivityMainBinding
import com.example.pbpkel4_tubes.models.PaketTravel
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets
//import maes.tech.intentanim.CustomIntent
import java.util.*

class MainActivity : AppCompatActivity() {
    private var txtEmail: EditText? = null
    private var txtPassword: EditText? = null
    private  var binding : ActivityMainBinding? = null
//    private lateinit var mainLayout:ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        txtEmail = findViewById(R.id.txt_email)
        txtPassword = findViewById(R.id.txt_password)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        btnLogin.setOnClickListener { createUser() }
//        binding.btnLogin.setOnClickListener {
//
////            saveData()
////            val intent = Intent(this, HomeActivity::class.java)
////            startActivity(intent)
//        }
        setTitle("User Login")

        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
//            CustomIntent.customType(this, "fadein-to-fadeout")
        }
    }


    private fun createUser(){

        if(txtEmail!!.text.toString().isEmpty()){
            Toast.makeText(this@MainActivity, "Email Tidak Boleh Kosong!!!", Toast.LENGTH_SHORT).show()
        }else if (txtPassword!!.text.toString().isEmpty()){
            Toast.makeText(this@MainActivity, "Password Tidak Boleh Kosong!!!", Toast.LENGTH_SHORT).show()
        }else {
//            val user = User(
//                txtEmail!!.setText(user.email)
//            )
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
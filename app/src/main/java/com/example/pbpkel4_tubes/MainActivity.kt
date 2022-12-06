package com.example.pbpkel4_tubes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.RequestQueue
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.pbpkel4_tubes.api.PaketTravelApi
import com.example.pbpkel4_tubes.api.UserApi
import com.example.pbpkel4_tubes.databinding.ActivityAddEditBinding
import com.example.pbpkel4_tubes.databinding.ActivityMainBinding
import com.example.pbpkel4_tubes.models.PaketTravel
import com.example.pbpkel4_tubes.user.User
import com.example.pbpkel4_tubes.user.UserDB
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets
//import maes.tech.intentanim.CustomIntent
import java.util.*

class MainActivity : AppCompatActivity() {
    private var txtEmail: EditText? = null
    private var txtPassword: EditText? = null
    private  var binding : ActivityMainBinding? = null
    private var queue: RequestQueue? = null
    lateinit var vEmail : String
    lateinit var vPassword : String
    lateinit var  mBundle: Bundle
    val db by lazy { UserDB(this) }
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

        val email=txtEmail!!.text.toString()
        val password=txtPassword!!.text.toString()

        val mBundle = Bundle()
        mBundle.putString("username", txtEmail!!.text.toString())
        mBundle.putString("password", txtPassword!!.text.toString())
        intent.putExtra("register", mBundle)

        CoroutineScope(Dispatchers.IO).launch {
            db.noteDao().addNote(
                User(
                    (Math.random() * (10000 - 3 + 1)).toInt(), txtEmail!!.text.toString(),
                    txtPassword!!.text.toString()
                )
            )
            finish()
        }

//        getBundle()

        if(txtEmail!!.text.toString().isEmpty()){
            Toast.makeText(this@MainActivity, "Email Tidak Boleh Kosong!!!", Toast.LENGTH_SHORT).show()
        }else if (txtPassword!!.text.toString().isEmpty()){
            Toast.makeText(this@MainActivity, "Password Tidak Boleh Kosong!!!", Toast.LENGTH_SHORT).show()
        }else {
            val mahasiswa = User(
                1,
                txtEmail!!.text.toString(),
                txtPassword!!.text.toString(),
            )

            val stringRequest: StringRequest =
                object : StringRequest(Method.POST, UserApi.LOGIN_URL, Response.Listener { response ->
                    val gson = Gson()
                    var mahasiswa = gson.fromJson(response, User::class.java)

                    if(mahasiswa != null)
                        Toast.makeText( this@MainActivity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()

                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }, Response.ErrorListener { error ->
                    try {
                        val respondBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(respondBody)
                        Toast.makeText(
                            this@MainActivity, errors.getString("messsage"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception){
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Accept"] = "application/json"
                        return headers
                    }

                    @Throws(AuthFailureError::class)
                    override fun getBody(): ByteArray {
                        val gson = Gson()
                        val requestBody = gson.toJson(mahasiswa)
                        return requestBody.toByteArray(StandardCharsets.UTF_8)
                    }

                    override fun getBodyContentType(): String {
                        return "application/json"
                    }
                }
//            queue!!.add(stringRequest)
        }
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
    }

    fun getBundle() {
        mBundle = intent.getBundleExtra("register")!!
        vEmail = mBundle.getString("email")!!
        vPassword = mBundle.getString("password")!!
    }
}
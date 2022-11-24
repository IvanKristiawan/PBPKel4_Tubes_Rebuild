package com.example.pbpkel4_tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pbpkel4_tubes.api.UserApi
import com.example.pbpkel4_tubes.models.User
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class Register : AppCompatActivity() {
    private var etUsername: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        queue = Volley.newRequestQueue(this)
        etUsername = findViewById(R.id.txt_username)
        etEmail = findViewById(R.id.txt_email)
        etPassword = findViewById(R.id.txt_password)

        val btnSave = findViewById<Button>(R.id.btnRegister)
        btnSave.setOnClickListener { createUser() }
    }

    private fun createUser(){

        val mahasiswa = User(
            etUsername!!.text.toString(),
            etEmail!!.text.toString(),
            etPassword!!.text.toString(),
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, UserApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var mahasiswa = gson.fromJson(response, User::class.java)

                if(mahasiswa != null)
                    Toast.makeText( this@Register, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

            }, Response.ErrorListener { error ->
                try {
                    val respondBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(respondBody)
                    Toast.makeText(
                        this@Register, errors.getString("messsage"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@Register, e.message, Toast.LENGTH_SHORT).show()
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
        queue!!.add(stringRequest)
    }
}
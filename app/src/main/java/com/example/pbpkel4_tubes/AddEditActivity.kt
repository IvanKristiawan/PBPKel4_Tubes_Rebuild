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
import com.example.pbpkel4_tubes.api.PaketTravelApi
import com.example.pbpkel4_tubes.models.PaketTravel
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddEditActivity : AppCompatActivity() {
    private var etIdPaket: EditText? = null
    private var etNamaPaket: EditText? = null
    private var etTujuan: EditText? = null
    private var etAsal: EditText? = null
    private var etHarga: EditText? = null
    private var etJam: EditText? = null
    private var etDurasi: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        queue = Volley.newRequestQueue(this)
        etIdPaket = findViewById(R.id.et_id_paket)
        etNamaPaket = findViewById(R.id.et_nama_paket)
        etTujuan = findViewById(R.id.et_tujuan)
        etAsal = findViewById(R.id.et_asal)
        etHarga = findViewById(R.id.et_harga)
        etJam = findViewById(R.id.et_jam)
        etDurasi = findViewById(R.id.et_durasi)
        layoutLoading = findViewById(R.id.layout_loading)

        val btnCancel = findViewById<Button>(R.id.btn_cancle)
        btnCancel.setOnClickListener{ finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getStringExtra("id")
        if(id == null){
            tvTitle.setText("Tambah Paket Travel")
            btnSave.setOnClickListener { createPaketTravel() }
        } else  {
            tvTitle.setText("Edit Paket Travel")
            getPaketTravelByid(id)

            btnSave.setOnClickListener { updatePaketTravel(id) }
        }
    }

    private fun getPaketTravelByid(id: String?){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PaketTravelApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val mahasiswa = gson.fromJson(response, PaketTravel::class.java)

                etIdPaket!!.setText(mahasiswa.idPaket)
                etNamaPaket!!.setText(mahasiswa.namaPaket)
                etTujuan!!.setText(mahasiswa.tujuan)
                etAsal!!.setText(mahasiswa.asal)
                etHarga!!.setText(mahasiswa.harga)
                etJam!!.setText(mahasiswa.jam)
                etDurasi!!.setText(mahasiswa.durasi)

                Toast.makeText(this@AddEditActivity, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headres = HashMap<String, String>()
                headres["Accept"] = "application/json"
                return headres
            }
        }
        queue!!.add(stringRequest)
    }

    private fun createPaketTravel(){
        setLoading(true)

        val mahasiswa = PaketTravel(
            etIdPaket!!.text.toString(),
            etNamaPaket!!.text.toString(),
            etTujuan!!.text.toString(),
            etAsal!!.text.toString(),
            etHarga!!.text.toString(),
            etJam!!.text.toString(),
            etDurasi!!.text.toString(),
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, PaketTravelApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var mahasiswa = gson.fromJson(response, PaketTravel::class.java)

                if(mahasiswa != null)
                    Toast.makeText( this@AddEditActivity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val respondBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(respondBody)
                    Toast.makeText(
                        this@AddEditActivity, errors.getString("messsage"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun updatePaketTravel(id:String?){
        setLoading(true)

        val mahasiswa = PaketTravel(
            etIdPaket!!.text.toString(),
            etNamaPaket!!.text.toString(),
            etTujuan!!.text.toString(),
            etAsal!!.text.toString(),
            etHarga!!.text.toString(),
            etJam!!.text.toString(),
            etDurasi!!.text.toString(),
        )

        val stringRequest : StringRequest = object :
            StringRequest(Method.PUT, PaketTravelApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()

                var mahasiswa = gson.fromJson(response, PaketTravel::class.java)

                if(mahasiswa != null)
                    Toast.makeText(this@AddEditActivity, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
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
                return  requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue!!.add(stringRequest)
    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }

}
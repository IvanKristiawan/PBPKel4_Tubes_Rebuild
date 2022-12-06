package com.example.pbpkel4_tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pbpkel4_tubes.adapters.PaketTravelAdapter
import com.example.pbpkel4_tubes.api.PaketTravelApi
import com.example.pbpkel4_tubes.models.PaketTravel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class HomeActivity : AppCompatActivity() {

    private var srMahasiswa: SwipeRefreshLayout? = null
    private var adapter: PaketTravelAdapter? = null
    private var svMahasiswa: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        queue = Volley.newRequestQueue(this )
        layoutLoading = findViewById(R.id.layout_loading)
        srMahasiswa = findViewById(R.id.sr_mahasiswa)
        svMahasiswa =findViewById(R.id.sv_mahasiswa)

        srMahasiswa?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allPaketTravel() })
        svMahasiswa?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener{
            val i = Intent(this@HomeActivity, AddEditActivity::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val fabLibrary = findViewById<FloatingActionButton>(R.id.fab_library)
        fabLibrary.setOnClickListener{
            val i = Intent(this@HomeActivity, AllFeatureActivity::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val fabUser = findViewById<FloatingActionButton>(R.id.fab_user)
        fabUser.setOnClickListener{
            val i = Intent(this@HomeActivity, ShowUser::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_mahasiswa)
        adapter = PaketTravelAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allPaketTravel()
    }

    private fun allPaketTravel(){
        srMahasiswa!!.isRefreshing  = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PaketTravelApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                var mahasiswa : Array<PaketTravel> = gson.fromJson(response, Array<PaketTravel>::class.java)

                adapter!!.setPaketTravelList(mahasiswa)
                adapter!!.filter.filter(svMahasiswa!!.query)
                srMahasiswa!!.isRefreshing = false

                if(!mahasiswa.isEmpty())
                    Toast.makeText(this@HomeActivity, "Data berhasil diambil", Toast.LENGTH_SHORT)
                        .show()
                else
                    Toast.makeText(this@HomeActivity, "Data kosong!", Toast.LENGTH_SHORT)
                        .show()
            }, Response.ErrorListener { error ->
                srMahasiswa!!.isRefreshing = false
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@HomeActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@HomeActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }

        queue!!.add(stringRequest)
    }

    fun deletePaketTravel(id: String){
        setLoading(true)
        val stringRequest: StringRequest = object:
            StringRequest(Method.DELETE, PaketTravelApi.DELETE_URL + id, Response.Listener { response ->
                setLoading(false)

                val gson = Gson()
                var mahasiswa = gson.fromJson(response, PaketTravel::class.java)
                if(mahasiswa != null)
                    Toast.makeText(this@HomeActivity, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                allPaketTravel()
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@HomeActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: java.lang.Exception){
                    Toast.makeText(this@HomeActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allPaketTravel()
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
            layoutLoading!!.visibility = View.GONE
        }
    }
}
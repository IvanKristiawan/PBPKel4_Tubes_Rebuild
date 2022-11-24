package com.example.pbpkel4_tubes.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pbpkel4_tubes.AddEditActivity
import com.example.pbpkel4_tubes.Register
//import com.example.pbpkel4_tubes.AddEditActivity
import com.example.pbpkel4_tubes.HomeActivity
import com.example.pbpkel4_tubes.R
//import com.example.pbpkel4_tubes.models.PaketTravel
import com.example.pbpkel4_tubes.models.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter(private var userList: List<User>, context: Context) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>(), Filterable {

    private var filteredUserList: MutableList<User>
    private val context: Context

    init {
        filteredUserList = ArrayList(userList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredUserList.size
    }

    fun setUserList(userList: Array<User>){
        this.userList = userList.toList()
        filteredUserList = userList.toMutableList()
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int){
        val user = filteredUserList[position]
        holder.tvUsername.text = user.username
        holder.tvEmail.text = user.email
        holder.tvPassword.text = user.password

        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data User ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){ _, _ ->
                    if(context is HomeActivity) user.email?.let { it1 ->
                        context.deletePaketTravel(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvMahasiswa.setOnClickListener{
            val i = Intent(context, AddEditActivity::class.java)
            i.putExtra("id", user.email)
            if(context is HomeActivity)
                context.startActivityForResult(i, HomeActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<User> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(userList)
                }else{
                    for (mahasiswa in userList){
                        if(mahasiswa.email.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(mahasiswa)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredUserList.clear()
                filteredUserList.addAll((filterResults.values as List<User>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvUsername: TextView
        var tvEmail: TextView
        var tvPassword: TextView
        var btnDelete: ImageButton
        var cvMahasiswa: CardView

        init {
            tvUsername = itemView.findViewById(R.id.tv_username)
            tvEmail = itemView.findViewById(R.id.tv_email)
            tvPassword = itemView.findViewById(R.id.tv_password)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvMahasiswa = itemView.findViewById(R.id.cv_mahasiswa)
        }
    }
}
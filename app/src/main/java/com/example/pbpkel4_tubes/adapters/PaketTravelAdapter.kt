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
import com.example.pbpkel4_tubes.HomeActivity
import com.example.pbpkel4_tubes.R
import com.example.pbpkel4_tubes.models.PaketTravel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class PaketTravelAdapter(private var paketTravelList: List<PaketTravel>, context: Context) :
    RecyclerView.Adapter<PaketTravelAdapter.ViewHolder>(), Filterable {

    private var filteredPaketTravelList: MutableList<PaketTravel>
    private val context: Context

    init {
        filteredPaketTravelList = ArrayList(paketTravelList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_paket_travel, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredPaketTravelList.size
    }

    fun setPaketTravelList(mahasiswaList: Array<PaketTravel>){
        this.paketTravelList = mahasiswaList.toList()
        filteredPaketTravelList = mahasiswaList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val paketTravel = filteredPaketTravelList[position]
        holder.tvIdPaket.text = paketTravel.idPaket
        holder.tvNamaPaket.text = paketTravel.namaPaket
        holder.tvTujuan.text = paketTravel.tujuan
        holder.tvAsal.text = paketTravel.asal
        holder.tvHarga.text = paketTravel.harga
        holder.tvJam.text = paketTravel.jam
        holder.tvDurasi.text = paketTravel.durasi

        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data Paket Travel ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){ _, _ ->
                    if(context is HomeActivity) paketTravel.idPaket?.let { it1 ->
                        context.deletePaketTravel(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvMahasiswa.setOnClickListener{
            val i = Intent(context, AddEditActivity::class.java)
            i.putExtra("id", paketTravel.idPaket)
            if(context is HomeActivity)
                context.startActivityForResult(i, HomeActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<PaketTravel> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(paketTravelList)
                }else{
                    for (mahasiswa in paketTravelList){
                        if(mahasiswa.namaPaket.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(mahasiswa)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredPaketTravelList.clear()
                filteredPaketTravelList.addAll((filterResults.values as List<PaketTravel>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvIdPaket: TextView
        var tvNamaPaket: TextView
        var tvTujuan: TextView
        var tvAsal: TextView
        var tvHarga: TextView
        var tvJam: TextView
        var tvDurasi: TextView
        var btnDelete: ImageButton
        var cvMahasiswa: CardView

        init {
            tvIdPaket = itemView.findViewById(R.id.tv_id_paket)
            tvNamaPaket = itemView.findViewById(R.id.tv_nama_paket)
            tvTujuan = itemView.findViewById(R.id.tv_tujuan)
            tvAsal = itemView.findViewById(R.id.tv_asal)
            tvHarga = itemView.findViewById(R.id.tv_harga)
            tvJam = itemView.findViewById(R.id.tv_jam)
            tvDurasi = itemView.findViewById(R.id.tv_durasi)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvMahasiswa = itemView.findViewById(R.id.cv_mahasiswa)
        }
    }

}
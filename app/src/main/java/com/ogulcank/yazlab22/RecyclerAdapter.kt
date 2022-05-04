package com.ogulcank.yazlab22

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_row.view.*

class RecyclerAdapter(val List: ArrayList<String>,val ListVal: ArrayList<Int>): RecyclerView.Adapter<RecyclerAdapter.AVH>() {
    class AVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AVH {
        //inflater,layoutInflater,MenuInflater
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.recycler_row,parent,false)
        return AVH(itemView)
    }

    override fun onBindViewHolder(holder: AVH, position: Int) {
        holder.itemView.recyclerViewTextView.text=List.get(position)+" "+ListVal.get(position)


    }

    override fun getItemCount(): Int {
        return List.size
    }
}
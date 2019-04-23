package com.example.zoomparallax.Adapters

import android.media.Image
import android.provider.ContactsContract
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.zoomparallax.R

class RecyclerAdapter(private val photos : ArrayList<Int>, private val names:ArrayList<String>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.content1_recyclerview,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text =  names[position]
        holder.photo.setImageResource(photos[position])

    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.name)
        val photo : ImageView = itemView.findViewById(R.id.image_show)
    }

}


//Kevin Pan 969449
package com.example.songguessinggame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//An adapter for the Lyric RecyclerViewer that takes out data from MapsActivity and passes it to Lyrics class
class LyricAdapter(val lyricList: ArrayList<String>) : RecyclerView.Adapter<LyricAdapter.ViewHolder>() {

    //Keeps textViewLyric from main UI thread to improve performance for RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewLyric = itemView.findViewById(R.id.textViewLyric)  as TextView

    }

    //Creates a ViewHolder using the list_layout.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_layout,parent,false)
        return ViewHolder(view)
    }

    //Gets the size of the total list
    override fun getItemCount(): Int {
        return lyricList.size
    }

    //Recycles an old view and rebinds it with new data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lyric: String = lyricList[position]
        holder?.textViewLyric.text = lyric.toString()

    }
}
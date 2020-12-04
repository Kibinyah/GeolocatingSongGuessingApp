//Kevin Pan 969449
package com.example.songguessinggame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//An adapter for the Scoreboard RecyclerViewer
class ScoreAdapter (val scoreList: ArrayList<Score>) : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {

    //Keeps textViewLyric from main UI thread to improve performance for RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewResult = itemView.findViewById(R.id.gameResult)  as TextView
        val textViewGuesses = itemView.findViewById(R.id.numGuesses)  as TextView
        val textViewPoints = itemView.findViewById(R.id.numPoints)  as TextView

    }
    //Creates a ViewHolder using the scoreboard_layout.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.scoreboard_layout,parent,false)
        return ViewHolder(view)
    }
    //Gets the size of the total list
    override fun getItemCount(): Int {
        return scoreList.size
    }

    //Recycles an old view and rebinds it with new data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val score: Score = scoreList[position]
        holder?.textViewResult?.text = score.result
        holder?.textViewGuesses?.text = score.guess
        holder?.textViewPoints?.text = score.points

    }
}
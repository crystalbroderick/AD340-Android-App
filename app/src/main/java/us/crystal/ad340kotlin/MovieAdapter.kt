package us.crystal.ad340kotlin

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_movie_details.view.*
import kotlinx.android.synthetic.main.movie_row.*
import kotlinx.android.synthetic.main.movie_row.view.movie_title
import kotlinx.android.synthetic.main.movie_row.view.movie_year
import org.w3c.dom.Text

class MovieAdapter(
    private val movieList: ArrayList<Movie>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    //view holder: initializes view without binding data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_row, parent, false)
        return MovieViewHolder(itemView)
    }
    // fetches data for view holder
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentMovie = movieList[position]
        holder.movie_title.text = currentMovie.movie_title
        holder.movie_year.text = currentMovie.movie_year
        holder.movie_desc.text = currentMovie.movie_desc
        holder.director.text = currentMovie.director

    }
    //size of data set
    override fun getItemCount() = movieList.size

    //view holder for all single movie items
    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val movie_desc: TextView = itemView.findViewById(R.id.movie_desc)
        val movie_title: TextView = itemView.findViewById(R.id.movie_title)
        val movie_year: TextView = itemView.findViewById(R.id.movie_year)
        val director: TextView = itemView.findViewById(R.id.director)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View?) {
            //prevents crash if item not full off screen
            val position:Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
            listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}


package us.crystal.ad340kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.movie_row.*


class MovieActivity : AppCompatActivity(), MovieAdapter.OnItemClickListener {

    private lateinit var movieList : ArrayList<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        movieList = MovieData.movieData()
        recycler.adapter = MovieAdapter(movieList, this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)


    }

    override fun onItemClick(position:Int){

       Toast.makeText(this, "$position clicked", Toast.LENGTH_SHORT).show()
        val movie_title = movieList[position].movie_title
        val movie_year = movieList[position].movie_year
       val movie_desc = movieList[position].movie_desc
        val director = movieList[position].director
        val intent = Intent(this, MovieDetails::class.java)
        intent.putExtra("movie_title", movie_title)
        intent.putExtra("movie_year", movie_year)
        intent.putExtra("movie_desc", movie_desc)
        intent.putExtra("director", director)

        startActivity(intent)

    }
}


package us.crystal.ad340kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.activity_movie_details.view.*

class MovieDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        //Intent intent = getIntent()
        val movie_title = intent.extras?.getString("movie_title")
        val movie_year = intent.extras?.getString("movie_year")
        val movie_desc = intent.extras?.getString("movie_desc")
        val director = intent.extras?.getString("director")

        findViewById<TextView>(R.id.movie_title).text = movie_title
        findViewById<TextView>(R.id.movie_year).text = movie_year
        findViewById<TextView>(R.id.movie_desc).text = movie_desc
        findViewById<TextView>(R.id.director).text = director
        supportActionBar?.setTitle(movie_title)
             //= message.toString()
        //val movie_title = intent.getStringArrayExtra(movie_title.toString())
        //findViewById<TextView>(R.id.movie_title).text = movie_title.toString()

        //supportActionBar?.title = message?.get(0).toString()
        //findViewById<TextView>(R.id.movie_title).text
        //findViewById<TextView>(R.id.movie_year).text
        //findViewById<TextView>(R.id.movie_desc).text

    }
}
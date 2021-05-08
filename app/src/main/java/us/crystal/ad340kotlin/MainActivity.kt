package us.crystal.ad340kotlin

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

const val EXTRA_MESSAGE = "us.crystal.ad340kotlin.MESSAGE"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editText)
        val message = editText.text.toString()
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    /**Called when user presses Movies button */
    fun moviesBtn(view:View){
        val intent = Intent(this, MovieActivity::class.java)
        startActivity(intent)
    }

    fun btnClick(view:View) {
        val button = view as Button
        Toast.makeText(applicationContext, button.text, Toast.LENGTH_LONG).show()
    }
}

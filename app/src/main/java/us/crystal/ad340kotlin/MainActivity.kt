package us.crystal.ad340kotlin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import java.util.regex.Pattern


const val EXTRA_MESSAGE = "us.crystal.ad340kotlin.MESSAGE"
const val PREFS_FILE = "us.crystal.ad340kotlin"
class MainActivity : AppCompatActivity() {
    private lateinit var mUsername: EditText
    private lateinit var mEmail: EditText
    private lateinit var mPassword: EditText

    private lateinit var mPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mUsername = findViewById(R.id.editTextUserName)
        mEmail = findViewById(R.id.editTextEmail)
        mPassword = findViewById(R.id.editTextPassword)

        mPreferences = this.getSharedPreferences(PREFS_FILE, MODE_PRIVATE)

        //mUsername.setText(mPreferences.getString(name))
        mUsername.setText(mPreferences.getString("name", "Enter name"))
        mEmail.setText(mPreferences.getString("email", "Enter email"))
        mPassword.setText(mPreferences.getString("password", ""))

    }

    /** Called when the user taps the send button */
    /*fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editTextUserName)
        val message = editText.text.toString()
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }*/



    fun loginBtn(view: View) {
        Log.e("FIREBASE", "logging in....")
        signIn()
    }

    /**Called when user presses Movies button */
    fun moviesBtn(view: View) {
        val intent = Intent(this, MovieActivity::class.java)
        startActivity(intent)
    }

    fun trafficCamBtn(view: View) {
        val intent = Intent(this, TrafficCamerasActivity::class.java)
        startActivity(intent)
    }

    fun btnClick(view: View) {
        val button = view as Button
        Toast.makeText(applicationContext, button.text, Toast.LENGTH_LONG).show()
    }

    fun message(view: View) {
        val button = view as Button
        Toast.makeText(applicationContext, EXTRA_MESSAGE, Toast.LENGTH_LONG).show()
    }

    fun mapBtn(view: View) {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }

    private fun emailValidation(email: String) : Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private fun loginValidation(name: String, email: String, password: String): Boolean {
        if (name.isEmpty()) {
            mUsername?.error = "Field cannot be empty"
            return false
        } else if (name.length < 6) {
            mUsername?.error = "Must be at least 6 characters"
            return false
        }
        if (email.isEmpty()) {
            mEmail?.error = "Field cannot be empty"
        } else if (!emailValidation(email)) {
            mEmail?.error = "Enter valid email address"
            return false
        }
        if(password.isEmpty() || password.length < 6) {
            mPassword?.error = "Enter valid password"
            return false
        }
        return true
    }
//Store valid field entries to shared preferences;
    private fun preferences(name: String, email: String, password: String) {
        var editor = mPreferences.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("password", password)
        editor.commit()
    }

    private fun signIn() {
        Log.d("FIREBASE", "signIn")

        // 1 - validate display name, email, and password entries
        var name : String = mUsername.text.toString()
        var email : String = mEmail.text.toString()
        var password : String = mPassword.text.toString()

        if(!loginValidation(name, email, password)) {
            return
        }
        // 2 - save valid entries to shared preferences
        preferences(name, email, password)

        // 3 - sign into Firebase
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                Log.d("FIREBASE", "signIn:onComplete:" + task.isSuccessful)
                if (task.isSuccessful) {
                    // update profile. displayname is the value entered in UI
                    val user = FirebaseAuth.getInstance().currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("FIREBASE", "User profile updated.")
                                // Go to FirebaseActivity
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        FirebaseActivity::class.java
                                    )
                                )
                            }
                        }
                } else {
                    Log.d("FIREBASE", "sign-in failed")
                    Toast.makeText(
                        this@MainActivity, "Sign In Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}

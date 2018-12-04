package com.coinz.app.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.coinz.app.R
import com.coinz.app.utils.AppLog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_log_in.*

/**
 * Activity managing the log in screen.
 *
 * Provides user with an interface to log into the app using Firebase, i.e. takes an email and a
 * password and handles log in process. Also provides a link to the create account screen should
 * the user not already have one.
 */
class LogInActivity : AppCompatActivity() {

    companion object {
        // Tag used to identify the log output of this activity.
        const val tag = "LogInActivity"
    }

    // Firebase authenticator object, used to access Firbase's account management.
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        AppLog(tag, "onCreate", "called")

        auth = FirebaseAuth.getInstance()

        log_in_button.setOnClickListener { _ -> logUserIn() }

        log_in_create_account_button.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
            finish() // NOTE: not sure if we should call finish() in this case.
        }
    }

    override fun onStart() {
        super.onStart()

        AppLog(tag, "onStart", "checking if user is logged in.")
        if (auth.currentUser != null) {
            // We have a current user, no need to login, go to map.
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    /**
     * Attempt to log user in.
     *
     * Take log in data (email, password) from appropriate layout elements and attempt to log the
     * user in. Notify if log in form has been filled in insufficiently. Notify user about outcome
     * of login.
     *
     * If log in succeeds, turn over to the main activity.
     */
    private fun logUserIn() {
        val email: String? = log_in_email.text.toString()
        val password: String? = log_in_password.text.toString()

        // Ensure user provides an email.
        if (email.isNullOrEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.log_in_enter_email_prompt),
                           Toast.LENGTH_SHORT).show()
            return
        }

        // ensure user provides a password.
        if (password.isNullOrEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.log_in_enter_password_prompt),
                    Toast.LENGTH_SHORT).show()
            return
        }

        // At this point we have assured neither email nor password are empty, so we can use safely
        // use !!.
        // Log the user in using Firebase.
        auth.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener(this) {task ->
            if (task.isSuccessful) {
                // Log in succeeded, let user know and go to main activity.
                Toast.makeText(applicationContext, getString(R.string.log_in_success_prompt),
                        Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                // Log in failed, let user know.
                Toast.makeText(applicationContext, getString(R.string.log_in_failure_prompt),
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

}
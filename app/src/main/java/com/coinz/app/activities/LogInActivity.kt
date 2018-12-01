package com.coinz.app.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.coinz.app.R
import com.coinz.app.utils.AppLog
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {

    companion object {
        const val tag = "LogInActivity"
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        AppLog(tag, "onCreate", "called")

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        AppLog(tag, "onStart", "checking if user is logged in.")
        if (auth.currentUser != null) {
            // We have a current user, no need to login, go to map.
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}
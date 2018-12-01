package com.coinz.app.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.coinz.app.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    companion object {
        const val tag = "CreateAccountActivity"
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        auth = FirebaseAuth.getInstance()

        create_acc_log_in_button.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish() // NOTE: not sure if we should be calling finish() here.
        }
    }

}
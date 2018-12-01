package com.coinz.app.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.coinz.app.R
import com.coinz.app.utils.AppConsts
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

        create_acc_button.setOnClickListener { _ -> createAccount() }

        create_acc_log_in_button.setOnClickListener {
            finish() // User remembered they have an account, go back to login screen.
        }
    }

    /**
     * Create user account
     *
     * Extract all necessary information (email, password) automatically from layout and check
     * their validity.
     */
    private fun createAccount() {
        val email: String? = create_acc_email.text.toString()
        val password: String? = create_acc_password.text.toString()

        // TODO: Should check that it's actually an email!
        if (email.isNullOrEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.create_acc_enter_email_prompt),
                           Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isNullOrEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.create_acc_enter_password_prompt),
                    Toast.LENGTH_SHORT).show()
            return
        }

        // From here on we can use !! for both email and password as we've ensured that neither
        // will be null by the above checks.

        if (password!!.length < AppConsts.minPasswordLength) {
            Toast.makeText(applicationContext, getString(R.string.create_acc_password_short_prompt),
                    Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email!!, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Account created, tell user and go to main activity now.
                Toast.makeText(applicationContext, getString(R.string.create_acc_success_prompt),
                        Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                // Account creation failed, let user know.
                Toast.makeText(applicationContext, getString(R.string.create_acc_failure_prompt),
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

}
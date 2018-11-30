package com.coinz.app.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import com.coinz.app.adapters.CoinListAdapter
import com.coinz.app.R

import kotlinx.android.synthetic.main.activity_local_wallet.*
import kotlinx.android.synthetic.main.content_local_wallet.*

class LocalWalletActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_local_wallet)
        setSupportActionBar(toolbar)

        coin_recyclerview.adapter = CoinListAdapter(this)
        coin_recyclerview.layoutManager = LinearLayoutManager(this)

        /*
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        */
    }

}

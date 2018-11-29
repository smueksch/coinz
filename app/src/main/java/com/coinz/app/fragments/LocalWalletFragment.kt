package com.coinz.app.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.coinz.app.R

class LocalWalletFragment : Fragment() {

    companion object {
        fun newInstance(): LocalWalletFragment {
            return LocalWalletFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.activity_local_wallet, container, false)
    }

}
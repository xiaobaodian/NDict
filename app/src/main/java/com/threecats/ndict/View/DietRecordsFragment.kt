package com.threecats.ndict.View


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.threecats.ndict.R
import kotlinx.android.synthetic.main.fragment_diet_records.*


class DietRecordsFragment : Fragment() {

    private val mOnClickListener = View.OnClickListener { _ ->
        date.text = "后天"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_diet_records, container, false)
        //date.text = "后天"
        button.setOnClickListener(mOnClickListener)
        return view
    }

}

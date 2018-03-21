package com.threecats.ndictdataset.View


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BNutrient
import com.threecats.ndictdataset.Helper.ErrorMessage

import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.fragment_trace_element.*
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 */
class TraceElementFragment : Fragment() {

    private val shareSet = BDM.ShareSet!!

    private var nutrientList: MutableList<BNutrient>? = null
    private var nutrientRView: RecyclerView? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_trace_element, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nutrientRView = NutrientRView
        nutrientRView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        if (nutrientList == null) {
            queryAllNutrient()
        } else {
            bindNutrientList()
        }
    }

    private fun queryAllNutrient() {
        val query = BmobQuery<BNutrient>()
        query.findObjects(object : FindListener<BNutrient>() {
            override fun done(nutrients: MutableList<BNutrient>?, e: BmobException?) {
                if (e == null) {
                    progressBarNutrient.visibility = View.GONE
                    nutrientList = nutrients
                    if (nutrientRView == null) {
                        context.toast("Nutrient is null ")
                    }
                    if (nutrientList != null) {
                        nutrientRView?.adapter = NutrientsAdapter(nutrientList!!, context)
                    }
                } else {
                    //message.text = e.message
                    if (view != null) {
                        //context.toast("${e.message}")
                        ErrorMessage(context, e)
                    }
                }
            }
        })
    }

    private fun bindNutrientList(){
        nutrientRView?.adapter = NutrientsAdapter(nutrientList!!, context)
        progressBarNutrient.visibility = View.GONE
    }

}// Required empty public constructor

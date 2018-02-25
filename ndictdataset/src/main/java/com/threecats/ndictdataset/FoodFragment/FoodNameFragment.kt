package com.threecats.ndictdataset.FoodFragment


import android.os.Bundle
import android.os.Debug
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.BuildConfig.DEBUG
import com.threecats.ndictdataset.Helper.CheckTextHelper
import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.fragment_food_name.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


/**
 * A simple [Fragment] subclass.
 */
class FoodNameFragment: FoodPropertyFragment() {

    val currentFood = BDM.ShareSet!!.CurrentFood!!
    val checkTextHelper = CheckTextHelper()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_food_name, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkTextHelper.addEditBox(NameIEditText)
        with (NameIEditText) {
            text.append(currentFood.name)
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 0) {
                        NameILayout.error = "食材名称不能为空"
                        NameILayout.isErrorEnabled = true
                    } else {
                        NameILayout.isErrorEnabled = false
                    }
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        checkTextHelper.addEditBox(AliasIEditText)
        with (AliasIEditText) {
            text.append(currentFood.alias)
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun afterTextChanged(s: Editable?) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
        checkTextHelper.initHash()
    }
}// Required empty public constructor

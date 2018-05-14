package com.threecats.ndictdataset.FoodFragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.fragment_food_name.*


/**
 * A simple [Fragment] subclass.
 */

class FoodNameFragment: FoodPropertyFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_food_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (NameIEditText) {
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
        with (AliasIEditText) {
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun afterTextChanged(s: Editable?) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if (initFields) {
            initFields = false
            shareSet.editorFood.item?.let { itemToViewFields(it) }
        }
    }

    override fun itemToViewFields(food: BFood) {
        //val logshow = AnkoLogger("NDIC")

        with (foodEditTextHelper) {
            textBoxs.clear()
            addEditBox(NameIEditText, food.name)
            addEditBox(AliasIEditText, food.alias)
        }
    }

    override fun viewFieldsToItem(food: BFood) {
        foodEditTextHelper.checkNull("")
        foodEditTextHelper.textBoxs.forEach {
            when (it.editBox){
                NameIEditText -> food.name = it.editBox.text.toString()
                AliasIEditText -> food.alias = it.editBox.text.toString()
            }
        }
    }

    override fun firstEditTextFocus(){
        with (NameIEditText){
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }
    }

}// Required empty public constructor

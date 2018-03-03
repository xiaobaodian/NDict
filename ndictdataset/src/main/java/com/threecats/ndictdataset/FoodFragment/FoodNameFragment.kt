package com.threecats.ndictdataset.FoodFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Enum.ChangeBlock
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.View.FoodEditerActivity
import kotlinx.android.synthetic.main.fragment_food_name.*


/**
 * A simple [Fragment] subclass.
 */
class FoodNameFragment: FoodPropertyFragment() {

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

    override fun onDestroyView() {
        super.onDestroyView()
        assemblyFields(currentFood)
    }

    override fun BlockChangeState(parent: FoodEditerActivity) {
        val changeNumber = checkTextHelper.ChangeNumber()
        if (changeNumber > 0) {
            parent.addChangeBlock(ChangeBlock.Food)
        }
    }

    private fun assemblyFields(food: BFood){
        checkTextHelper.CheckNull()
        checkTextHelper.textBoxs.forEach {
            when (it.editBox){
                NameIEditText -> food.name = it.editBox.text.toString()
                AliasIEditText -> food.alias = it.editBox.text.toString()
            }
        }
    }

}// Required empty public constructor

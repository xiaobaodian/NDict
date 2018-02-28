package com.threecats.ndictdataset.FoodFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Bmob.BFoodVitamin
import com.threecats.ndictdataset.Enum.EditerState
import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.fragment_food_vitamin.*
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 */

class FoodVitaminFragment : FoodPropertyFragment() {

    var vitaminItem: BFoodVitamin? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_food_vitamin, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assignFields()
    }

    override fun getFoodFields(): Int {
        val changeNumber = checkTextHelper.ChangeNumber()
        if (changeNumber > 0) {
            assemblyFields()
        }
        return changeNumber
    }

    private fun assignFields(){

        with (checkTextHelper) {
            addEditBox(VitaminAIEditText, currentVitamin.VitaminA.toString())
            addEditBox(CaroteneIEditText, currentVitamin.Carotene.toString())
            addEditBox(VitaminB1IEditText, currentVitamin.VitaminB1.toString())
            addEditBox(VitaminB2IEditText, currentVitamin.VitaminB2.toString())
            addEditBox(NiacinIEditText, currentVitamin.Niacin.toString())
            addEditBox(VitaminB6IEditText, currentVitamin.VitaminB6.toString())
            addEditBox(PantothenicAcidIEditText, currentVitamin.PantothenicAcid.toString())
            addEditBox(VitaminHIEditText, currentVitamin.VitaminH.toString())
            addEditBox(FolicAcidIEditText, currentVitamin.FolicAcid.toString())
            addEditBox(VitaminB12IEditText, currentVitamin.VitaminB12.toString())
            addEditBox(CholineIEditText, currentVitamin.Choline.toString())
            addEditBox(VitaminCIEditText, currentVitamin.VitaminC.toString())
            addEditBox(VitaminDIEditText, currentVitamin.VitaminD.toString())
            addEditBox(VitaminEIEditText, currentVitamin.VitaminE.toString())
            addEditBox(VitaminKIEditText, currentVitamin.VitaminK.toString())
            addEditBox(VitaminPIEditText, currentVitamin.VitaminP.toString())
            addEditBox(InositolIEditText, currentVitamin.Inositol.toString())
            addEditBox(PABAIEditText, currentVitamin.PABA.toString())
            initHash()
        }

    }

    private fun assemblyFields(){

        checkTextHelper.textBoxs.forEach {
            when (it.editBox){

                VitaminAIEditText -> currentVitamin.VitaminA = it.editBox.text.toString().toFloat()
                CaroteneIEditText -> currentVitamin.Carotene = it.editBox.text.toString().toFloat()
                VitaminB1IEditText -> currentVitamin.VitaminB1 = it.editBox.text.toString().toFloat()
                VitaminB2IEditText -> currentVitamin.VitaminB2 = it.editBox.text.toString().toFloat()
                NiacinIEditText -> currentVitamin.Niacin = it.editBox.text.toString().toFloat()
                VitaminB6IEditText -> currentVitamin.VitaminB6 = it.editBox.text.toString().toFloat()
                PantothenicAcidIEditText -> currentVitamin.PantothenicAcid = it.editBox.text.toString().toFloat()
                VitaminHIEditText -> currentVitamin.VitaminH = it.editBox.text.toString().toFloat()
                FolicAcidIEditText -> currentVitamin.FolicAcid = it.editBox.text.toString().toFloat()
                VitaminB12IEditText -> currentVitamin.VitaminB12 = it.editBox.text.toString().toFloat()
                CholineIEditText -> currentVitamin.Choline = it.editBox.text.toString().toFloat()
                VitaminCIEditText -> currentVitamin.VitaminC = it.editBox.text.toString().toFloat()
                VitaminDIEditText -> currentVitamin.VitaminD = it.editBox.text.toString().toFloat()
                VitaminEIEditText -> currentVitamin.VitaminE = it.editBox.text.toString().toFloat()
                VitaminKIEditText -> currentVitamin.VitaminK = it.editBox.text.toString().toFloat()
                VitaminPIEditText -> currentVitamin.VitaminP = it.editBox.text.toString().toFloat()
                InositolIEditText -> currentVitamin.Inositol = it.editBox.text.toString().toFloat()
                PABAIEditText -> currentVitamin.PABA = it.editBox.text.toString().toFloat()

            }
        }

    }

}// Required empty public constructor

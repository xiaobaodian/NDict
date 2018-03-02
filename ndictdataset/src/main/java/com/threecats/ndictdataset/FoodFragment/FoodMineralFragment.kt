package com.threecats.ndictdataset.FoodFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.Bmob.BFoodMineral
import com.threecats.ndictdataset.Bmob.BFoodMineralExt
import com.threecats.ndictdataset.Enum.ChangeBlock
import com.threecats.ndictdataset.Helper.EditTextHelper

import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.View.FoodEditerActivity
import kotlinx.android.synthetic.main.fragment_food_mineral.*


/**
 * A simple [Fragment] subclass.
 */
class FoodMineralFragment : FoodPropertyFragment() {

    val checkTextExtHelper = EditTextHelper()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_food_mineral, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        assignFields(currentFood.Mineral!!, currentFood.MineralExt!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //BlockChangeState()
        assemblyFields(currentFood.Mineral!!, currentFood.MineralExt!!)
    }

    override fun BlockChangeState(parent: FoodEditerActivity) {
        val changeNumber = checkTextHelper.ChangeNumber()
        val extChangeNumber = checkTextExtHelper.ChangeNumber()
        if (changeNumber > 0) {
            parent.addChangeBlock(ChangeBlock.Mineral)
        }
        if (extChangeNumber > 0) {
            parent.addChangeBlock(ChangeBlock.MineralExt)
        }
    }

    private fun assignFields(mineral: BFoodMineral, mineralext: BFoodMineralExt){

        with (checkTextHelper) {
            addEditBox(KIEditText, mineral.mK.toString())
            addEditBox(NIEditText, mineral.mN.toString())
            addEditBox(CaIEditText, mineral.mCa.toString())
            addEditBox(MgIEditText, mineral.mMg.toString())
            addEditBox(FeIEditText, mineral.mFe.toString())
            addEditBox(MnIEditText, mineral.mMn.toString())
            addEditBox(ZnIEditText, mineral.mZn.toString())
            addEditBox(CuIEditText, mineral.mCu.toString())
            addEditBox(SeIEditText, mineral.mSe.toString())
            addEditBox(PIEditText, mineral.mP.toString())
            addEditBox(IIEditText, mineral.mI.toString())
            addEditBox(MoIEditText, mineral.mMo.toString())
            addEditBox(CrIEditText, mineral.mCr.toString())
            addEditBox(CeIEditText, mineral.mCe.toString())
            addEditBox(CoIEditText, mineral.mCo.toString())
            addEditBox(SnIEditText, mineral.mSn.toString())
            addEditBox(NiIEditText, mineral.mNi.toString())
            addEditBox(VIEditText, mineral.mV.toString())
            addEditBox(SiIEditText, mineral.mSi.toString())
            initHash()
        }
        with (checkTextExtHelper) {
            addEditBox(AiIEditText, mineralext.mAi.toString())
            addEditBox(CiIEditText, mineralext.mCi.toString())
            addEditBox(FIEditText, mineralext.mF.toString())
            addEditBox(PbIEditText, mineralext.mPb.toString())
            addEditBox(SIEditText, mineralext.mS.toString())
            initHash()
        }

    }

    private fun assemblyFields(mineral: BFoodMineral, mineralext: BFoodMineralExt){

        checkTextHelper.textBoxs.forEach {
            when (it.editBox){
                  KIEditText    ->  mineral.mK  = it.editBox.text.toString().toFloat()
                  NIEditText    ->  mineral.mN  = it.editBox.text.toString().toFloat()
                  CaIEditText   ->  mineral.mCa = it.editBox.text.toString().toFloat()
                  MgIEditText   ->  mineral.mMg = it.editBox.text.toString().toFloat()
                  FeIEditText   ->  mineral.mFe = it.editBox.text.toString().toFloat()
                  MnIEditText   ->  mineral.mMn = it.editBox.text.toString().toFloat()
                  ZnIEditText   ->  mineral.mZn = it.editBox.text.toString().toFloat()
                  CuIEditText   ->  mineral.mCu = it.editBox.text.toString().toFloat()
                  SeIEditText   ->  mineral.mSe = it.editBox.text.toString().toFloat()
                  PIEditText    ->  mineral.mP  = it.editBox.text.toString().toFloat()
                  IIEditText    ->  mineral.mI  = it.editBox.text.toString().toFloat()
                  MoIEditText   ->  mineral.mMo = it.editBox.text.toString().toFloat()
                  CrIEditText   ->  mineral.mCr = it.editBox.text.toString().toFloat()
                  CeIEditText   ->  mineral.mCe = it.editBox.text.toString().toFloat()
                  CoIEditText   ->  mineral.mCo = it.editBox.text.toString().toFloat()
                  SnIEditText   ->  mineral.mSn = it.editBox.text.toString().toFloat()
                  NiIEditText   ->  mineral.mNi = it.editBox.text.toString().toFloat()
                  VIEditText    ->  mineral.mV  = it.editBox.text.toString().toFloat()
                  SiIEditText   ->  mineral.mSi = it.editBox.text.toString().toFloat()
            }
        }
        checkTextExtHelper.textBoxs.forEach {
            when (it.editBox){
                CiIEditText   ->  mineralext.mCi = it.editBox.text.toString().toFloat()
                SIEditText    ->  mineralext.mS = it.editBox.text.toString().toFloat()
                FIEditText    ->  mineralext.mF = it.editBox.text.toString().toFloat()
                AiIEditText   ->  mineralext.mAi = it.editBox.text.toString().toFloat()
                PbIEditText   ->  mineralext.mPb = it.editBox.text.toString().toFloat()
            }
        }

    }

}// Required empty public constructor

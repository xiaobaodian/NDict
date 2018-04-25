package com.threecats.ndict.View


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndict.Models.DataSet
import com.threecats.ndict.Models.FoodCategory
import com.threecats.ndict.R
import kotlinx.android.synthetic.main.fragment_foods.*


/**
 * A simple [Fragment] subclass.
 */
class FoodsFragment : Fragment() {

    lateinit var categorys: List<FoodCategory>
    var categoryFoodFragments = mutableListOf<CategoryFoodsFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categorys = DataSet.foodCategoryQuery.find()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_foods, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryFoodFragments.clear()
        categorys.forEach { categoryFoodFragments.add(CategoryFoodsFragment(it)) }
        viewPager.adapter = CategoryFoodFragmentAdapter(childFragmentManager, categoryFoodFragments)
        tabs.setupWithViewPager(viewPager)
    }
}

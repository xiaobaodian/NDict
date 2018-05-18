package com.threecats.ndict.View


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndict.Models.DataSet
import com.threecats.ndict.Models.Category
import com.threecats.ndict.R
import com.threecats.ndict.Shells.TabViewShell.TabViewShell
import kotlinx.android.synthetic.main.fragment_foods.*


/**
 * A simple [Fragment] subclass.
 */
class FoodsFragment : Fragment() {

    lateinit var categories: List<Category>
    var foodFragments = TabViewShell()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categories = DataSet.categoryQuery.find()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_foods, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foodFragments.tab(tabs).viewPage(viewPager).parent(this)
        categories.forEach { foodFragments.addFragment(CategoryFoodsFragment(it), requireNotNull(it.shortTitle)) }
        foodFragments.link()

    }
}

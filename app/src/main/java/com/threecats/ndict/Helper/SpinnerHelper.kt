package com.threecats.ndict.Helper

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter

/**
 * 由 zhang 于 2018/1/25 创建
 */
class SpinnerHelper(val context: Context, val spinner: Spinner) {

    var items: List<String>? = null
        set(value) {items = value}

    var spinneLayout: Int = 0
        set(value) {spinneLayout = value}

    private var dropdownLayout: Int = 0
    set(value) {dropdownLayout = value}

    private var adapter: ArrayAdapter<String>? = null
    //val

    fun bind() {
        if (spinneLayout == 0) spinneLayout = android.R.layout.simple_spinner_item
        if (dropdownLayout == 0) dropdownLayout = android.R.layout.simple_spinner_dropdown_item
        items?.let { items = arrayListOf("你","忘记了","加入数据") }
        adapter = ArrayAdapter<String>(context, spinneLayout, items)
        adapter!!?.setDropDownViewResource(dropdownLayout)
        spinner.adapter = adapter
    }

//    ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
//    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////绑定 Adapter到控件
//    spinner .setAdapter(adapter);
//    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view,
//                int pos, long id) {
//
//            String[] languages = getResources().getStringArray(R.array.languages);
//            Toast.makeText(MainActivity.this, "你点击的是:"+languages[pos], 2000).show();
//        }
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//            // Another interface callback
//        }
//    });
}
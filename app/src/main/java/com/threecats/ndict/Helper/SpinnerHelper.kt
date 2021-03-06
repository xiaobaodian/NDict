package com.threecats.ndict.Helper

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner

/**
 * 由 zhang 于 2018/1/25 创建
 */
class SpinnerHelper(val context: Context, val spinner: Spinner) {

    var items: List<String> = arrayListOf("你","忘记了","加入数据")
        //set(value) {items = value}

    var spinneLayout: Int = android.R.layout.simple_spinner_item
        //set(value) {spinneLayout = value}

    var dropDownLayout: Int = android.R.layout.simple_spinner_dropdown_item
    //set(value) {dropDownLayout = value}

    private var adapter: ArrayAdapter<String>? = null
    //val

    fun bind() {
        adapter = ArrayAdapter<String>(context, spinneLayout, items)
        if (adapter != null) {
            adapter!!?.setDropDownViewResource(dropDownLayout)
            spinner.adapter = adapter
        }

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
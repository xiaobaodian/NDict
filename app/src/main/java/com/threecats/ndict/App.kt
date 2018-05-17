package com.threecats.ndict

import android.app.Application
import com.threecats.MyObjectBox
import com.threecats.ndict.Models.DataSet
//import com.threecats.ndict.Models.MyObjectBox
import io.objectbox.BoxStore

/**
 * 由 zhang 于 2018/1/6 创建
 */
class App: Application() {

    companion object Constants {
        const val TAG = "NDict"
        const val EXTERNAL_DIR = false
    }

    lateinit var boxStore: BoxStore
        private set

    override fun onCreate() {
        super.onCreate()

        //        if (EXTERNAL_DIR) {
        //            // Example how you could use a custom dir in "external storage"
        //            // (Android 6+ note: give the app storage permission in app info settings)
        //            File directory = new File(Environment.getExternalStorageDirectory(), "objectbox-notes");
        //            boxStore = MyObjectBox.builder().androidContext(App.this).directory(directory).build();
        //        } else {
        // This is the minimal setup required on Android

        boxStore = MyObjectBox.builder().androidContext(this).build()

        //        }    }
    }

}
package com.threecats.javatest.ndictdataset

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import com.threecats.javatest.ndictdataset.Bmob.FoodCategory
import kotlinx.android.synthetic.main.activity_main.*
import cn.bmob.v3.listener.SaveListener


class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                var categoryQuery: BmobQuery<FoodCategory> = BmobQuery<FoodCategory>()
                var title = "aaa"
                categoryQuery.getObject("kuS0D55D", object : QueryListener<FoodCategory>() {
                    override fun done(category: FoodCategory, e: BmobException?) {
                        if (e == null) {
                            message.text = category.LongTitle
                        } else {
                            message.text = "nonononon !"
                        }
                    }
                })

                //message.setText(title)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {

                var category = FoodCategory(20,"这是长标题","短标题")
                category.save(object : SaveListener<String>() {
                    override fun done(objectId: String, e: BmobException?) {
                        if (e == null) {
                            message.text = "添加数据成功，返回objectId为：" + objectId
                        } else {
                            message.text = "创建数据失败：" + e.message
                        }
                    }
                })


                //message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //第一：默认初始化
        Bmob.initialize(this, "92c3bbfbecf25dd1991485ee41597f1a");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);

        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}

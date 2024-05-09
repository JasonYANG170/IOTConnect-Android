package com.example.IOTsmart
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import kotlin.concurrent.thread

class DBStart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dbtest)

        val show = findViewById<TextView>(R.id.show)
        val addButton = findViewById<Button>(R.id.add)
        val queryButton = findViewById<Button>(R.id.query)

        addButton.setOnClickListener {
            addUser()
        }

        queryButton.setOnClickListener {
            val showText = queryUser()
            show.text = showText
        }
    }

    fun addUser() {
        // 启动一个子线程
        thread {
            // 从数据库类中获取Dao接口，再用Dao访问数据库
            val dao = MyDataBase.getDataBase(this).userDao()
         //   val user = Userx(Name = "xiao", Userid = 123, Password = "123123", LoginStats = 0, Image = "s")
          //  val userx = Driver(Driverbyuserid = 123, Drivername = "sds1", DiverId = 1)
           // val userx1 = Driver(Driverbyuserid = 1234, Drivername = "sds2", DiverId = 2)
           // val userx2 = Driver(Driverbyuserid = 123, Drivername = "sds3", DiverId = 3)
           // dao.insertUser(user,userx,userx1,userx2)
        }
    }

    fun queryUser(): String {
        var showText = ""
        // 启动一个子线程
        thread {
            // 从数据库类中获取Dao接口，再用Dao访问数据库
            val dao = MyDataBase.getDataBase(this).userDao()
            val list = dao.getUserbydriver()
            //  for (user in list) {
            showText += "名字：$list"
            // }
        }.join()  // 为了获取子线程中查询到的结果，此处简单的使用join等待子线程完成，再结束函数
        return showText
    }
}
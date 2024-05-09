package com.example.IOTsmart
import MyDatabase
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.IOTsmart.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseActivity() : AppCompatActivity() {
    private lateinit var myDatabase: MyDatabase

    fun setupBottomNavigationView(selectedItemId: Int) {
        myDatabase = MyDatabase(this)
        var id = -1
        GlobalScope.launch(Dispatchers.IO) {
            val userList = myDatabase.retrieveData()
            val filteredList = userList.filter { it.login == 0 }

            if (filteredList.isNotEmpty()) {
                val userData = filteredList.first()
                val message = "用户ID：${userData.id}\n用户名：${userData.name}\n密码：${userData.password}"

                withContext(Dispatchers.Main) {
                   /* val alertDialog = AlertDialog.Builder(this@BaseActivity)
                        .setTitle("登录为0的用户信息")
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .create()
                    alertDialog.show()
                    */

                    id = 0
                }
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = selectedItemId
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_1 -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    true
                }
                R.id.item_2 -> {
                    if (id == -1) {
                        startActivity(Intent(this, login::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    } else {
                        // 修改Login字符串资源的值
                        startActivity(Intent(this, Userinfo::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    true
                }
                else -> false
            }
        }
    }
}
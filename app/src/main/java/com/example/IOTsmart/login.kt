package com.example.IOTsmart
import MyDatabase
import android.content.Intent
import android.os.Bundle

import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import kotlin.concurrent.thread

class login : BaseActivity() {

    private lateinit var myDatabase: MyDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.signlayout)
        myDatabase = MyDatabase(this)
        setupBottomNavigationView(R.id.item_2)

        val button4 = findViewById<Button>(R.id.button4)
        button4.setOnClickListener {
            val userLayout = findViewById<TextInputLayout>(R.id.textField)
            val userEditText = userLayout.editText
            val user = userEditText?.text?.toString()

            val passwordLayout = findViewById<TextInputLayout>(R.id.textField2)
            val passwordEditText = passwordLayout.editText
            val password = passwordEditText?.text?.toString()

            val isUserExist = myDatabase.isUserExist(user)

            if (isUserExist) {
                val message = "用户名已存在！请尝试其他用户名或输入密码后登录。"
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("注册失败")
                    .setMessage(message)
                    .setPositiveButton("确定", null)
                    .create()
                alertDialog.show()
                return@setOnClickListener
            }

            val maxId = myDatabase.getMaxId()
            val newId = maxId + 1
            val message = "注册成功！"
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("用户ID：$newId 用户名：$user 密码：$password")
                .setMessage(message)
                .setPositiveButton("确定", null)
                .create()
            alertDialog.show()
            myDatabase.insertData(newId, user, password, -1, 0)
        }

        val button5 = findViewById<Button>(R.id.button5)
        button5.setOnClickListener {
            val userLayout = findViewById<TextInputLayout>(R.id.textField)
            val userEditText = userLayout.editText
            val user2 = userEditText?.text?.toString()
            myDatabase.updateUserLoginStatus(user2, 0)

            val passwordLayout = findViewById<TextInputLayout>(R.id.textField2)
            val passwordEditText = passwordLayout.editText
            val password2 = passwordEditText?.text?.toString()

            var isUserFound = false
            val userData = myDatabase.getUserData(user2)

            if (userData != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("id", userData.id)
                intent.putExtra("user", userData.name)
                intent.putExtra("password", userData.password)
                intent.putExtra("login", userData.login)
                startActivity(intent)

                val intent2 = Intent(this, BaseActivity::class.java)
                intent2.putExtra("id", userData.id)
                intent2.putExtra("user", userData.name)
                intent2.putExtra("password", userData.password)
                intent2.putExtra("login", userData.login)
                startActivity(intent2)
            } else {
                val message = "未在数据库中找到对应的账户数据！\n请先完成账户注册！"
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("登录失败")
                    .setMessage(message)
                    .setPositiveButton("确定", null)
                    .create()
                alertDialog.show()
            }
        }
    }
}
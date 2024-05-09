package com.example.IOTsmart
import MyDatabase
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import io.github.cdimascio.dotenv.dotenv
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import io.github.cdimascio.dotenv.dotenv

class Userinfo : BaseActivity() {
    private var locatversion1="V3.0.0"//可尝试传值
    private var Onlineversion1: String = ""
    private var uplog1: String = ""
    private lateinit var myDatabase: MyDatabase
    private lateinit var imageView: ImageView
    private fun update(){
        val dotenv = dotenv {
            directory = "/assets"
            filename = "env" // instead of 'env', use 'env'
        }
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(dotenv["APP_UPDATA_URL"])

            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // 网络请求失败，处理错误逻辑
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                // 解析 responseData 并从中获取到土壤湿度数据
                try{
                val jsonObject = JSONObject(responseData)
                val infoObject = jsonObject.getJSONObject("data")
                val nightObject = infoObject.getString("content")
                // 解析 responseData 并从中获取到土壤湿度数据



                Onlineversion1 = nightObject.substring(0, 6)
                uplog1= nightObject.substring(6).replace("#", "\n")

                println("newstring: $Onlineversion1")
                println("ddString: $uplog1")
                // 更新 UI，在主线程中更新 TextView 的显示
                //  runOnUiThread {
                //     yinyu.text = "$nightObject"
                // }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // 处理 JSON 解析异常
                }
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userinfo)

        imageView = findViewById(R.id.imageView)
        myDatabase = MyDatabase(this)

        update()

        val client = OkHttpClient()

        runOnUiThread {
            val users = myDatabase.getUserByLogin(0)

            val user = findViewById<TextView>(R.id.user)
            users?.let {
                user?.text = "用户名：" + users.name
            } ?: run {
                println("User not found")
            }
        }

        val img = myDatabase.getImgByLogin(0)
        val request = Request.Builder()
            .url("https://q4.qlogo.cn/headimg_dl?dst_uin=$img&spec=640")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // Handle network request failure
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.bytes()

                runOnUiThread {
                    Glide.with(this@Userinfo)
                        .load(responseData)
                        .into(imageView)
                }
            }
        })

        setupBottomNavigationView(R.id.item_2)
        val btnLogout = findViewById<MaterialButton>(R.id.out)
        btnLogout.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("退出登录")
                .setMessage("确认退出登录吗？")
                .setPositiveButton("确定") { dialog, _ ->
                    myDatabase.updateLoginValues()
                    dialog.dismiss()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()
        }

        val btnImg = findViewById<MaterialButton>(R.id.img)
        btnImg.setOnClickListener {
            val editText = EditText(this)

            val alertDialog = AlertDialog.Builder(this)
                .setTitle("输入文本")
                .setView(editText)
                .setPositiveButton("确定") { dialog, _ ->
                    myDatabase.updateimg(editText.text.toString().toInt())
                    val inputText = editText.text.toString()

                    dialog.dismiss()

                    val newRequest = Request.Builder()
                        .url("https://q4.qlogo.cn/headimg_dl?dst_uin=$inputText&spec=640")
                        .build()

                    client.newCall(newRequest).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val responseData = response.body?.bytes()

                            runOnUiThread {
                                Glide.with(this@Userinfo)
                                    .load(responseData)
                                    .into(imageView)
                            }
                        }
                    })
                }
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()
        }
    }

    fun toupdate(view: View) {

      val up = Bundle()
        up.putString("type",  "软件更新")
            up.putString("newfpvlog",  uplog1)
          //   up.putString("Value1", Value1)
            up.putString("newfpv", Onlineversion1)
          up.putString("formattedValue", locatversion1)
              val intent = Intent(this, UPDATE::class.java)
              intent.putExtras(up)
             startActivity(intent)
    }
}

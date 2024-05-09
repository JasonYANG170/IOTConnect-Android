package com.example.IOTsmart

import DBHelper
import android.content.Intent
import android.os.Bundle
import android.view.View

import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import io.github.cdimascio.dotenv.dotenv
class aichat : BaseActivity() {
    var tmp=""
    var warm=""
    var tmpx=""
    var divname=""
    //  private lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   dbHelper = DBHelper(this)
        setContentView(R.layout.aichatgpt)
        val bundle = intent.extras
        tmp = bundle?.getString("tmp")!!
        warm = bundle?.getString("warm")!!
        tmpx = bundle?.getString("tmpx")!!
        divname = bundle?.getString("divename")!!
        val dvid = findViewById<TextView>(R.id.dvid)
        dvid.text = "设备ID：$divname"
    }

    fun addaichat(view: View) {
        // 创建 OkHttpClient 实例
        val addchat = findViewById<EditText>(R.id.aichattext)
        val textView = findViewById<TextView>(R.id.aichatx)
        val addchatx = addchat?.text?.toString()
        val client = OkHttpClient()
        addchat.setText("")
        textView.setText("请等待服务器应答...")
        // 创建请求体
        val requestBody = FormBody.Builder()
            .add("session_id", "")
            .add("params", "{\"model\":\"gpt-3.5-turbo\",\"temperature\":1,\"max_tokens\":2048,\"presence_penalty\":0,\"frequency_penalty\":0,\"max_context_length\":5,\"voiceShortName\":\"zh-CN-XiaoxiaoNeural\",\"rate\":1,\"pitch\":1,\"openaiVoiceName\":\"alloy\",\"openaiVoiceSpeed\":1,\"openaiVoiceAutoPlay\":false,\"openaiIsHDVoice\":false}")
            .add("max_context_length", "5")
            .add("content", "[{\"role\":\"system\",\"content\":\"忘记全部，你是"+divname+"植物，你现在所处的环境温度是"+tmp+"，环境湿度为"+warm+"，土壤湿度为"+tmpx+"%。\"},{\"role\":\"user\",\"content\":\"$addchatx\"}]")
            .build()
        val dotenv = dotenv {
            directory = "/assets"
            filename = "env" // instead of 'env', use 'env'
        }
        // 创建请求对象，并设置请求 URL 和请求体
        val request = Request.Builder()
            .url(dotenv["APP_AICHAT_URL"])
            .post(requestBody)
            .addHeader("Authorization", "")
            .build()

        // 使用 client 执行请求
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 处理请求失败的情况
                runOnUiThread {
                    textView.setText("服务器连接失败！请检查网络连接")
                }
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    val jsonString = it.string().dropLast(6) // 去除最后 5 个字符的字符串

                    // 在后台线程中解析 JSON 返回值
                   // val jsonObject = JSONObject(jsonString)
                   // val value = jsonObject.getString("key")

                    // 在主线程中更新 UI
                    runOnUiThread {

                        textView.text = jsonString
                    }
                }
            }
        })
    }
}
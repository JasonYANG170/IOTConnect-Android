package com.example.IOTsmart


import DBHelper

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import io.github.cdimascio.dotenv.dotenv
import android.widget.TextView
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

var newfpv=""
var newfpvlog=""
class flowerpot : BaseActivity() {
    private fun update() {//更新检查（传值到更新界面，花盆端新增版本号接口用于检查更新）

        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://apis.jxcxin.cn/api/qqcollection?url=https://sharechain.qq.com/fc921df52dff5ae9417ed753b417168e")

            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // 网络请求失败，处理错误逻辑
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                // 解析 responseData 并从中获取到土壤湿度数据
                try {


                    val jsonObject = JSONObject(responseData)
                    val infoObject = jsonObject.getJSONObject("data")
                    val nightObject = infoObject.getString("content")
                    // 解析 responseData 并从中获取到土壤湿度数据


                    newfpv = nightObject.substring(0, 6)
                    newfpvlog = nightObject.substring(6).replace("#", "\n")

                    println("newstring: $newfpv")
                    // println("ddString: $uplog1")
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
    val dotenv = dotenv {
        directory = "/assets"
        filename = "env" // instead of 'env', use 'env'
    }
    // dotenv["MY_ENV_VAR1"]

    private lateinit var dbHelper: DBHelper
    private val serverURI = dotenv["MY_ENV_VAR1"]
    private val clientId = dotenv["MY_ENV_VAR1"]
    private val persistence = MemoryPersistence()
    private val topic = dotenv["MY_ENV_VAR1"]
    private lateinit var mqttClient: MqttClient
    //  private lateinit var hp: TextView
    //private lateinit var tmp: TextView


    private val handler = Handler(Looper.getMainLooper())
    var Value1 = ""
    var Value2 = ""
    var Value3 = ""
    var Value4 = ""
    var Value5 = ""
    var Value6 = ""
    var Value7 = ""
    var Value8 = ""
    var Value9 = ""
    var dvID: Int = 0
    var dvname: String = ""
    var formattedValue = ""

    fun colorex(id: TextView, value: String) {
        if (value == "1") {
            id.text = "开"
            id.setTextColor(Color.BLUE)
            //  id.setBackgroundColor(Color.BLUE)
        } else {
            id.text = "关"
            //   id.setBackgroundColor(Color.WHITE)
        }

    }


    //  var insys23=0
    private val mqttCallback = object : MqttCallback {
        override fun connectionLost(cause: Throwable?) {
            // 处理连接断开的情况
        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {
            // 处理接收到的消息


            handler.post {
                val payload = message?.payload?.toString(Charsets.UTF_8)
                if (payload != null && payload.contains("#ID$dvID")) {
                    val count = payload.count { it == '#' }
                    val splitValues = payload.split("#")//#ID8107336#29.07#46.84#0.00#1#0#0
                    if (splitValues.size >= count) {
                        Value1 = splitValues[1]
                        if ("ID$dvID".equals(Value1)) {
                            Value2 = splitValues[2]
                            Value3 = splitValues[3]
                            Value4 = splitValues[4]
                            Value5 = splitValues[5]
                            Value6 = splitValues[6]
                            Value7 = splitValues[7]
                            Value8 = splitValues[8]
                            Value9 = splitValues[9]//自动模式待实现，布局文件无法自动布局，请为所有布局文件添加弹簧实现
                            // insys23 = 1
                            println("第一个值：$Value1")
                            println("第二个值：$Value2")
                            val tmpfp = findViewById<TextView>(R.id.tmpfp)
                            val autobt = findViewById<TextView>(R.id.autobt)
                            val hmfp = findViewById<TextView>(R.id.hmfp)
                            val hmpfp = findViewById<TextView>(R.id.hmpfp)
                            val tmpbt = findViewById<TextView>(R.id.tmpbt)
                            val lightbt = findViewById<TextView>(R.id.lightbt)
                            val waterbt = findViewById<TextView>(R.id.waterbt)
                            val upbt = findViewById<TextView>(R.id.upbt)
                            val updt = findViewById<TextView>(R.id.updt)
                            val dvst = findViewById<TextView>(R.id.dvst)
                            val dvinfo = findViewById<TextView>(R.id.dvinfo)
                            colorex(tmpbt, Value6)
                            colorex(lightbt, Value7)
                            colorex(waterbt, Value5)
                            colorex(autobt, Value9)
                            formattedValue = "V${Value8[0]}.${Value8[1]}.${Value8[2]}"
                            if (newfpv.equals(formattedValue)) {
                                updt.text = "当前版本$formattedValue"//更新处理，传值到更新界面，功能键点击发送指令
                                upbt.text = ""
                            } else {
                                updt.text = "有新版本$newfpv"//更新处理，传值到更新界面，功能键点击发送指令
                                // upbt.text = "*"
                            }
                            tmpfp.text = "$Value2°C"
                            hmfp.text = "$Value4%"
                            hmpfp.text = "$Value3%"
                            dvst.text = "设备状态:在线"
                            dvinfo.text = "固件版本:$formattedValue"
                            // hp.text = "硬盘温度：$firstValue°C"
                            //  tmp.text = "环境湿度：$secondValue%"
                        }
                    } else {
                        // 如果分割后的数组大小不符合预期，可能需要进行异常处理或者其他逻辑
                    }
                } else {
                    // 如果消息中不包含 #，可能需要进行异常处理或者其他逻辑
                }
            }
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            // 消息传递完成的回调
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DBHelper(this)
        setContentView(R.layout.flowerpot)
        update()
        //    hp = findViewById(R.id.hp)
        //   tmp = findViewById(R.id.tmp)
        val bundle = intent.extras
        dvID = bundle?.getInt("Int")!!
        dvname = bundle?.getString("String")!!
        println("id：$dvID")
        val dvid = findViewById<TextView>(R.id.dvid)
        dvid.text = "设备ID：$dvID"

        val fpname1 = findViewById<TextView>(R.id.fpname1)
        fpname1.text = "植物名称：$dvname"
        //   println("第二个值：$string")

        // Use coroutines for handling MQTT operations
        GlobalScope.launch(Dispatchers.IO) {
            try {
                mqttClient = MqttClient(serverURI, clientId, persistence)
                mqttClient.setCallback(mqttCallback)
                mqttClient.connect()
                mqttClient.subscribe(topic)
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }
    }

    fun tmpbton(view: View) {
        var payload = ""
        if (Value6 == "1") {
            payload = "#IDTO$dvID&OFFHOT"
        } else {
            payload = "#IDTO$dvID&ONHOT"
        }
        //要发布的消息

        // 创建一个MQTT消息对象，并设置其payload
        val message = MqttMessage(payload.toByteArray())
        // 设置消息的QoS（质量等级）
        message.qos = 0
        // 发布消息到特定的主题
        mqttClient.publish(topic, message)
    }

    fun lightbton(view: View) {
        //要发布的消息
        var payload = ""
        if (Value7 == "1") {
            payload = "#IDTO$dvID&OFFLED"
        } else {
            payload = "#IDTO$dvID&ONLED"
        }
        // 创建一个MQTT消息对象，并设置其payload
        val message = MqttMessage(payload.toByteArray())
        // 设置消息的QoS（质量等级）
        message.qos = 0
        // 发布消息到特定的主题
        mqttClient.publish(topic, message)
    }

    fun waterbton(view: View) {
        //要发布的消息
        var payload = ""
        if (Value5 == "1") {
            payload = "#IDTO$dvID&OFFPUMP"
        } else {
            payload = "#IDTO$dvID&ONPUMP"
        }
        // 创建一个MQTT消息对象，并设置其payload
        val message = MqttMessage(payload.toByteArray())
        // 设置消息的QoS（质量等级）
        message.qos = 0
        // 发布消息到特定的主题
        mqttClient.publish(topic, message)
    }

    fun updatebton(view: View) {
        //要发布的消息
        //  val payload = "#ID$dvID&Updata"
        // 创建一个MQTT消息对象，并设置其payload
        //  val message = MqttMessage(payload.toByteArray())
        // 设置消息的QoS（质量等级）
        // message.qos = 1
        // 发布消息到特定的主题
        // mqttClient.publish(topic, message)
        val up = Bundle()
        up.putString("type", "Flowerpot智能花盆")
        up.putString("newfpvlog", newfpvlog)
        up.putString("Value1", Value1)
        up.putString("newfpv", newfpv)
        up.putString("formattedValue", formattedValue)
        val intent = Intent(this, UPDATE::class.java)
        intent.putExtras(up)
        startActivity(intent)

    }

    fun autobton(view: View) {
        var payload = ""
        if (Value9 == "1") {
            payload = "#IDTO$dvID&OFFAUTO"
        } else {
            payload = "#IDTO$dvID&ONAUTO"
        }
        // 创建一个MQTT消息对象，并设置其payload
        val message = MqttMessage(payload.toByteArray())
        // 设置消息的QoS（质量等级）
        message.qos = 0
        // 发布消息到特定的主题
        mqttClient.publish(topic, message)
    }

    fun aichatbton(view: View) {
      //  val intent = Intent(this, aichat::class.java)
        val bundle = Bundle() //页面传参
        bundle.putString("tmp", Value2)
        bundle.putString("warm", Value4)
        bundle.putString("tmpx", Value3)
        bundle.putString("divename", dvname)
        val intent = Intent(this, aichat::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        // 可选：如果你需要在跳转的活动中传递数据，可以使用 Intent 的 putExtra() 方法
     //   intent.putExtra("key", value)

        // 使用 startActivity() 方法启动跳转
       // startActivity(intent)
    }
}
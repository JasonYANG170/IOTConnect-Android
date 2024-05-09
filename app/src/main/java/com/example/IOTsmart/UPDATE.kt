package com.example.IOTsmart
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import io.github.cdimascio.dotenv.dotenv
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
val dotenv = dotenv {
    directory = "/assets"
    filename = "env" // instead of 'env', use 'env'
}
// dotenv["MY_ENV_VAR1"]
private val serverURI = dotenv["MQTT_URL"]
private val clientId = dotenv["MQTT_CLIENTID"]
private val persistence = MemoryPersistence()
private val topic = dotenv["MQTT_TOPIC"]
var updivid:String?=""
var type33:String?=""
private lateinit var mqttClient: MqttClient

class UPDATE : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update)
        val bundlex = intent.extras
        updivid = bundlex?.getString("Value1")
        val upinfo = bundlex?.getString("newfpvlog")
        val upoline = bundlex?.getString("newfpv")
        var uplocation = bundlex?.getString("formattedValue")
      type33 = bundlex?.getString("type")
        val updvid1 = findViewById<TextView>(R.id.updvid)

     val uptitle = findViewById<TextView>(R.id. uptitle)
        val updvinfo2 = findViewById<TextView>(R.id.updateinfo)
        val updvid3 = findViewById<TextView>(R.id.uponline)
        val updvid4 = findViewById<TextView>(R.id.uplocation)
        val updvst = findViewById<TextView>(R.id.updvst)
        val locationv = findViewById<TextView>(R.id.updvinfo)
        val upbt = findViewById<TextView>(R.id.updatebt)
        if (upoline.equals(uplocation)){
            upbt.text = "暂无更新"
        }else{
            upbt.text = "立即更新"
        }
        updvid1.text = "设备ID：$updivid"
        updvinfo2.text = upinfo
        updvid3.text = upoline
        uptitle.text = type33

        updvid4.text = uplocation
       locationv.text = "固件版本：$uplocation"
        if (updivid!="") {
            updvst.text = "设备状态：在线"
        }else{
            updvst.text = "设备状态：离线"
            updvid4.text = "设备离线"
        }
        try {
            mqttClient = MqttClient(serverURI, clientId, persistence)
           // mqttClient.setCallback(mqttCallback)
            mqttClient.connect()
            mqttClient.subscribe(topic)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
        //    up.putString("newfpvlog", newfpvlog)
        //       up.putString("Value1", Value1)
        //        up.putString("newfpv", newfpv)
        //        up.putString("formattedValue", formattedValue)
    }

    fun update(view: View) {
        println(type33)
        if (updivid!=""&&type33=="Flowerpot智能花盆") {
            val resultString = updivid?.substring(2)
            val payload = "#IDTO$resultString&Updata"
            // 创建一个MQTT消息对象，并设置其payload
            val message = MqttMessage(payload.toByteArray())
            // 设置消息的QoS（质量等级）
            message.qos = 1
            // 发布消息到特定的主题
            mqttClient.publish(topic, message)
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("更新正在进行")
            alertDialog .setMessage(
                "  请等待设备重启即可\n"

            )

                .setPositiveButton("确定") { dialog, which ->
                    // 在这里编写按下确定按钮后的逻辑
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .create()
            alertDialog.show()
        }else if (updivid!=""&&type33=="软件更新") {
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("即将开始")
            alertDialog .setMessage(
                "  将启动系统浏览器下载\n" +
                        "  更新密钥：e7ur\n"+"  请手动下载并点击安装包完成更新。\n"
            )


                .setPositiveButton("确定") { dialog, which ->
                    // 在这里编写按下确定按钮后的逻辑
                    val url = dotenv["APP_UPGRADE_URL"] // 你要打开的网页链接
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }
                .create()
            alertDialog.show()

        }
    }
}

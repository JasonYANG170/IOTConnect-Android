package com.example.IOTsmart
import DBHelper
import MyDatabase
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.eclipse.paho.client.mqttv3.*
import org.json.JSONException
import android.Manifest
import io.github.cdimascio.dotenv.dotenv
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// 检查权限是否已授予
fun isPermissionGranted(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

// 在使用通知之前检查权限

var locatversion1="V3.0.0"
var Onlineversion1: String = ""
class MainActivity : BaseActivity() {
    val dotenv = dotenv {
        directory = "/assets"
        filename = "env" // instead of 'env', use 'env'
    }
 fun updatexxx(){//接口被删除，需要修复

        val client = OkHttpClient()

        val request = Request.Builder()
          .url(dotenv["APP_UPDATA_URL"])

           .build()
     client.newCall(request).enqueue(object : Callback {
         override fun onFailure(call: Call, e: IOException) {
// 处理请求失败的情况
             e.printStackTrace()
         }

         override fun onResponse(call: Call, response: Response) {
             response.body?.let {
                 val jsonString = it.string()
                 val jsonObject = JSONObject(jsonString)
                 val infoObject = jsonObject.getJSONObject("data")
                 val nightObject = infoObject.getString("content")
// 在后台线程中解析 JSON 返回值
// val jsonObject = JSONObject(jsonString)
// val value = jsonObject.getString("key")

// 在主线程中更新 UI


                 // 解析 responseData 并从中获取到土壤湿度数据


                 Onlineversion1 = nightObject.substring(0, 6)
                 uplog1= nightObject.substring(6).replace("#", "\n")
if( Onlineversion1!= locatversion1) {
    //    Onlineversion1 = nightObject.substring(0, 6)
    runOnUiThread {
        val alertDialog = AlertDialog.Builder(this@MainActivity) // 使用正确的上下文
            .setTitle("更新提示")
            .setMessage("发现新版本：$Onlineversion1$uplog1")

            .setPositiveButton("更新", DialogInterface.OnClickListener { dialog, which ->
                // 点击确定按钮后的逻辑
                val up = Bundle()
                up.putString("type",  "软件更新")
                up.putString("newfpvlog",  uplog1)
                //   up.putString("Value1", Value1)
                up.putString("newfpv", Onlineversion1)
                up.putString("formattedValue", locatversion1)
                val intent = Intent(this@MainActivity, UPDATE::class.java) // 使用正确的上下文和目标活动类
                intent.putExtras(up)
                startActivity(intent)
            })
            .setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
                // 点击取消按钮后的逻辑
            })

            .create()

        alertDialog.show()

        // 处理 JSON 解析异常
    }
}
             }
         }
     })

               // 更新 UI，在主线程中更新 TextView 的显示
               //  runOnUiThread {
                //     yinyu.text = "$nightObject"
                // }


    }
    private var uplog1: String = ""
    private lateinit var dbHelper: DBHelper
  //  private val serverURI = "tcp://bemfa.com:9501"
   // private val clientId = "46d36c5368444235903989ab8d581993"
   // private val persistence = MemoryPersistence()
   // private val topic = "Flowerpot"
   // private lateinit var mqttClient: MqttClient
   //private lateinit var hp: TextView
   // private lateinit var tmp: TextView

  //  private val handler = Handler(Looper.getMainLooper())
    //    var firstValue =""
    //    var secondValue =""
    //    var insys23=0
    //
    //   // private val mqttCallback = object : MqttCallback {
    //    //    override fun connectionLost(cause: Throwable?) {
    //            // 处理连接断开的情况
    //    //    }
    //
    //     //   override fun messageArrived(topic: String?, message: MqttMessage?) {
    //            // 处理接收到的消息
    //     //       handler.post {
    //      //          val payload = message?.payload?.toString(Charsets.UTF_8)
    //       //         if (payload != null && payload.contains("#")) {
    //       //             val splitValues = payload.split("#")
    //        //            if (splitValues.size >= 3) {
    //        //                firstValue = splitValues[1]
    //        //                secondValue = splitValues[2]
    //        //                insys23 = 1
    //        //                println("第一个值：$firstValue")
    //        //                println("第二个值：$secondValue")
    //
    //         //               hp.text = "硬盘温度：$firstValue°C"
    //         //               tmp.text = "环境湿度：$secondValue%"
    //         //           } else {
    //                        // 如果分割后的数组大小不符合预期，可能需要进行异常处理或者其他逻辑
    //          //          }
    //         //       } else {
    //                    // 如果消息中不包含 #，可能需要进行异常处理或者其他逻辑
    //          //      }
    //         //   }
    //      //  }
    //
    //        override fun deliveryComplete(token: IMqttDeliveryToken?) {
    //            // 消息传递完成的回调
    //        }
    //    }


    //private lateinit var soilHumidityTextView: TextView
    private lateinit var myDatabase: MyDatabase
   // private lateinit var userList: List<User>
    private fun tianqi(){
        val tianqi = findViewById<TextView>(R.id.tianqi)
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(dotenv["APP_API1"])

            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // 网络请求失败，处理错误逻辑
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                try {
                    // 解析 responseData 并从中获取到土壤湿度数据
                    val jsonObject = JSONObject(responseData)
                    val city = jsonObject.getString("city")


                    val infoObject = jsonObject.getJSONObject("info")
                    val date = infoObject.getString("date")
                    val week = infoObject.getString("week")
                    val type = infoObject.getString("type")
                    val low = infoObject.getString("low")
                    val high = infoObject.getString("high")
                    // 更新 UI，在主线程中更新 TextView 的显示

                    val nightObject = infoObject.getJSONObject("air")
                    val aqi_name = nightObject.getString("aqi_name")
                    runOnUiThread {
                        tianqi.text =
                            "$city $date $week $type\n温度：$low-$high  空气质量：$aqi_name   "
                    }

            } catch (e: JSONException) {
                e.printStackTrace()
                // 处理 JSON 解析异常
            }
            }
        })
    }
    private fun english1(){
        val yinyu = findViewById<TextView>(R.id.yinyu)
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(dotenv["APP_API2"])

            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // 网络请求失败，处理错误逻辑
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                try{
                // 解析 responseData 并从中获取到土壤湿度数据
                val jsonObject = JSONObject(responseData)
                val infoObject = jsonObject.getJSONObject("data")
                val nightObject = infoObject.getString("en")
                // 解析 responseData 并从中获取到土壤湿度数据


                // 更新 UI，在主线程中更新 TextView 的显示
                runOnUiThread {
                    yinyu.text = "$nightObject"
                }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // 处理 JSON 解析异常
                }
            }
        })
    }
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    // 在你想显示通知的地方调用这个函数
    fun showNotificationSafely(context: Context) {
        val permission = Manifest.permission.POST_NOTIFICATIONS

        if (isPermissionGranted(context, permission)) {
            showHelloNotification(context)
        } else {
            // 权限未授予，应该请求相应权限
            // 这里可以加入请求权限的逻辑
        }
    }
    fun showHelloNotification(context: Context) {
        val channelId = "hello_channel"

        // 创建通知渠道
        createNotificationChannel(context, channelId)

        // 构建通知
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.home)
            .setContentTitle("已为您浇水啦")
            .setContentText("多肉植物土壤湿度仅30%，已为您浇水")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // 显示通知
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Handling permission request logic
                return
            }
            notify(1, builder.build()) // Using a unique ID for each notification
        }
    }

    // 创建通知渠道
    fun createNotificationChannel(context: Context, channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Hello Channel"
            val descriptionText = "Say Hello Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            // 注册通知渠道
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       //updatexxx()需要修复，API失效
        dbHelper = DBHelper(this)
        myDatabase = MyDatabase(this)

     //  tianqi()//  API崩溃(用户无网络时应当进行错误处理)
       // hp = findViewById(R.id.hp)
       // tmp = findViewById(R.id.tmp)

       // try {
        //            mqttClient = MqttClient(serverURI, clientId, persistence)
        //            mqttClient.setCallback(mqttCallback)
        //            mqttClient.connect()
        //            mqttClient.subscribe(topic)
        //        } catch (e: MqttException) {
        //            e.printStackTrace()
        //        }

    english1()

        showToast(this, "Welcome")
        showHelloNotification(this)

            println("newstring: $Onlineversion1")
        println("newstring: $locatversion1")
           // if (Onlineversion1.equals(locatversion1)){
        //
        //            }else{
        //
        //                runOnUiThread {
        //
        //                    println("newstring: $Onlineversion1")
        //                    val alertDialog = AlertDialog.Builder(this)
        //                        .setTitle("发现新版本")
        //
        //                    alertDialog.setMessage(
        //                        "当前版本：$locatversion1\n新版本：$Onlineversion1\n请前往个人中心获取新版本！"
        //                    )
        //                        //
        //
        //                        .setPositiveButton("确定", null)
        //                        .create()
        //                    alertDialog.show()
        //                }
        //            }


        setupBottomNavigationView(R.id.item_1)
        val id = intent.getIntExtra("id", -1)
        val login = intent.getIntExtra("login", -1)    // 获取Intent中的id数据，默认值为-1
       val user = intent.getStringExtra("user") ?: "默认用户名"
        val age = intent.getStringExtra("password") ?: "默认密码"  // 获取Intent中的age数据
      //  val button6 = findViewById<Button>(R.id.button6)

        if (id != -1) {
            val message = "用户ID: $id\n" +
                    "用户名: $user\n" +
                    "密码: $age\n"+
            "login: $login"
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("登录成功")
                .setMessage(message)
                .setPositiveButton("确定", null)
                .create()
            alertDialog.show()



        }
        //button6.setOnClickListener {
        //    myDatabase.insertData(2, "yang", "19",-1,0)

       // }

       // val button7 = findViewById<Button>(R.id.button7)
       // button7.setOnClickListener {
        //            val userList = myDatabase.retrieveData()
        //            for (user in userList) {
        //                if (user.id == 2 && user.name == "yang" && user.password == "19"&& user.login == -1) {
        //                    // 找到对应的数据，可以执行相应的操作
        //                    // 在这里处理您需要的逻辑
        //                    // 例如：
        //                    val message = "找到对应的数据！"
        //                    val alertDialog = AlertDialog.Builder(this)
        //                        .setTitle("提示")
        //                        .setMessage(message)
        //                        .setPositiveButton("确定", null)
        //                        .create()
        //                    alertDialog.show()
        //                    break
        //                }
        //            }
        //        }

        myDatabase = MyDatabase(this)

        // 插入数据
       // myDatabase.insertData(1, "user", "18",-1,0)

        // 检索数据

        val driveData = dbHelper.getDriveIdsAndValuesForUserWithLoginZero()

        for (drive in driveData) {
            when (drive.second) {
                "fp" -> addMaterialCardView(findViewById(R.id.sda1q2), "Flowerpot",drive.first,drive.third)
                "ssd" -> addMaterialCardView(findViewById(R.id.sda1q2), "YANG-SSD",drive.first,drive.third)
                else -> {
                    // 如果不是 "fp" 或 "ssd"，则不做任何操作
                }
            }
        }

    }

  //  fun ss(view: View) {
  //
  //
  //        val alertDialog = AlertDialog.Builder(this)
  //            .setTitle("YANG SSD智能硬盘")
  //        if (insys23!=0) {
  //            alertDialog .setMessage(
  //                "  设备状态：当前在线\n" +
  //                        "  硬盘温度：$firstValue°C\n" +
  //                        "  环境湿度：$secondValue% \n"
  //                        + "  设备运维及开发：YANG\n"
  //                        + "  设备识别码：YANG +SSD-001\n"
  //            )
  //        } else {
  //            alertDialog .setMessage(
  //                "  设备状态：设备离线\n" +
  //                        "  硬盘温度：未知\n" +
  //                        "  环境湿度：未知\n"+
  //                        "  设备运维及开发：YANG\n"
  //                        + "  设备识别码：YANG +SSD-001\n"
  //            )
  //        }
  //
  //            .setPositiveButton("确定", null)
  //            .create()
  //        alertDialog.show()
  //    }
    fun adddive(view: View) {

     //   val userIds = dbHelper.getUserIdsWithLoginZero()
     //   val driveId = 123535
//
     //   for (userId in userIds) {
      //      dbHelper.addDriveIdForUserId(userId, driveId)
      //  }
         val intent = Intent(this,  addActivity::class.java)
               startActivity(intent)
    }

    fun sdsda(view: View) {

        val driveData = dbHelper.getDriveIdsAndValuesForUserWithLoginZero()
        val driveNames = driveData.joinToString(separator = "\n") { "Drive ID: ${it.first}, Drive Value: ${it.second}, Drive name: ${it.third}" }
        val totalDriveCount = driveData.size  // 获取 Drive ID 的数量

        val messageWithTitle = "Total Drive IDs: $totalDriveCount\n\n$driveNames"  // 包含 Drive ID 的数量在消息文本中

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Drive Information")
        builder.setMessage(messageWithTitle)
        builder.setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }
    fun View.setTopMargin(margin: Int) {
        val params = this.layoutParams as? ViewGroup.MarginLayoutParams ?: ViewGroup.MarginLayoutParams(this.layoutParams)
        params.topMargin = margin
        this.layoutParams = params
    }
    fun fp(view: View) {
    //   val intent = Intent(this,  flowerpot::class.java)
      //  startActivity(intent)
        //mqtt发送
      //  if (!::mqttClient.isInitialized) {
       //     Log.e("MQTT", "mqttClient is not initialized")
       //     return
      //  }

        // 要发布的消息
      //  val payload = "Hello, MQTT!"
        // 创建一个MQTT消息对象，并设置其payload
     //   val message = MqttMessage(payload.toByteArray())
        // 设置消息的QoS（质量等级）
     //   message.qos = 1

        // 发布消息到特定的主题

      //  mqttClient.publish(topic, message)
        
        //动态布局
      // addMaterialCardView(findViewById(R.id.sda1q2),"Flowerpot")
    }
//  <LinearLayout
//                                android:layout_width="match_parent"
//                                android:layout_height="match_parent"
//                                android:orientation="horizontal"
//                                >
private fun addMaterialCardView(parentLayout: LinearLayout, driveValue: String, driveID: Int,drivename:String) {
    val cardView = MaterialCardView(this)
    val cardLayoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    cardLayoutParams.setMargins(20, 20, 20, 20)
    cardView.layoutParams = cardLayoutParams
    // 设置点击事件
    cardView.setOnClickListener {
        if (driveValue == "Flowerpot") {
            GlobalScope.launch(Dispatchers.IO) {
                val bundle = Bundle() // 页面传参
                bundle.putInt("Int", driveID)
                bundle.putString("String", drivename)
                val intent = Intent(this@MainActivity, flowerpot::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        } else {
            // 其他操作
        }
    }

    val cardContentLayout = LinearLayout(this)
    cardContentLayout.layoutParams = LinearLayout.LayoutParams(
        470, // 设置为 169dp，与原始格式一致
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    cardContentLayout.orientation = LinearLayout.VERTICAL
    cardContentLayout.setPadding(35, 35, 35, 35)

    val titleTextView = TextView(this)
    titleTextView.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    titleTextView.text = driveValue
    titleTextView.setTextAppearance(this, android.R.style.TextAppearance_Medium)

    val hpTextView = TextView(this)
    hpTextView.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    val hpTextView2 = TextView(this)
    hpTextView2.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    hpTextView.text = "植物名称：$drivename"
    hpTextView2.text = "设备ID：$driveID"
    hpTextView.setTextAppearance(this, android.R.style.TextAppearance_Small)
    hpTextView.setTextColor(resources.getColor(android.R.color.secondary_text_dark))
    hpTextView2.setTextAppearance(this, android.R.style.TextAppearance_Small)
    hpTextView2.setTextColor(resources.getColor(android.R.color.secondary_text_dark))
    cardContentLayout.addView(titleTextView)
    cardContentLayout.addView(hpTextView)
    cardContentLayout.addView(hpTextView2)
    cardView.addView(cardContentLayout)

    var horizontalLayout: LinearLayout? = null

    for (i in 0 until parentLayout.childCount) {
        if (parentLayout.getChildAt(i) is LinearLayout) {
            val layout = parentLayout.getChildAt(i) as LinearLayout
            if (layout.orientation == LinearLayout.HORIZONTAL && layout.childCount % 2 != 0) {
                horizontalLayout = layout
                break
            }
        }
    }

    if (horizontalLayout == null) {
        horizontalLayout = LinearLayout(this)
        horizontalLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        horizontalLayout.orientation = LinearLayout.HORIZONTAL
        parentLayout.addView(horizontalLayout)
    }

    horizontalLayout.addView(cardView)
}

    private fun createTextView(text: String): TextView {
        val textView = TextView(this)
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.text = text
        return textView
    }
}

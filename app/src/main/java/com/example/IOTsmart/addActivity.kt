package com.example.IOTsmart

import DBHelper
import android.content.Intent
import android.os.Bundle
import android.view.View

import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class addActivity : BaseActivity() {
    private lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DBHelper(this)
        setContentView(R.layout.add)

    }
    fun separateStringAndNumber(input: String): Pair<String, Int>? {
        val regex = Regex("([a-zA-Z]+)(\\d+)")
        val matchResult = regex.find(input)

        matchResult?.let { result ->
            val textPart = result.groupValues[1] // 提取字母部分
            val numberPart = result.groupValues[2].toInt() // 提取数字部分并转为整数
            return Pair(textPart, numberPart)
        }

        return null
    }
    fun addid(view: View) {
        val addEditText = findViewById<EditText>(R.id.editTextText2231)
        val fped = findViewById<EditText>(R.id.fpeditTextText)
        val addid1 = addEditText?.text?.toString()
           val userIds = dbHelper.getUserIdsWithLoginZero()

        val (text, number) = separateStringAndNumber(addid1!!) ?: run {

            val alertDialog = AlertDialog.Builder(this)
                .setTitle("设备绑定失败")
            alertDialog .setMessage(
                "  设备ID错误，未知设备\n" +
                        "  请检查你的设备ID是否输入正确\n"
            )

                .setPositiveButton("确定", null)
                .create()
            alertDialog.show()
            println("未找到匹配的字符串")
            return
        }

        // 把值保存为对应的val
        val driverval: String = text
        var drv=""
        val fpname = fped?.text.toString()
var drivername=fpname
if (driverval=="fp"){
  drv="Flowerpot"
    val driveId: Int = number

    for (userId in userIds) {
        dbHelper.addDriveIdForUserId(userId, driveId,driverval,drivername)
    }
    val alertDialog = AlertDialog.Builder(this)
        .setTitle("设备绑定成功")
    alertDialog .setMessage(
        "  设备ID：$driveId\n" +
                "  设备类型：$drv\n"
    )

        .setPositiveButton("确定") { dialog, which ->
            // 在这里编写按下确定按钮后的逻辑
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        .create()
    alertDialog.show()

}else if (driverval=="ssd") {
    drv = "YANG-SSD"
    val driveId: Int = number

    for (userId in userIds) {
        dbHelper.addDriveIdForUserId(userId, driveId,driverval,"")
    }
    val alertDialog = AlertDialog.Builder(this)
        .setTitle("设备绑定成功")
    alertDialog .setMessage(
        "  设备ID：$driveId\n" +
                "  设备类型：$drv\n"
    )

        .setPositiveButton("确定") { dialog, which ->
            // 在这里编写按下确定按钮后的逻辑
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        .create()
    alertDialog.show()
}
        else{
    val driveId: Int = number

    for (userId in userIds) {
        dbHelper.addDriveIdForUserId(userId, driveId,driverval,"")
    }
    val alertDialog = AlertDialog.Builder(this)
        .setTitle("设备绑定失败")
    alertDialog .setMessage(
        "  设备ID错误.未知设备\n" +
                "  请检查你的设备ID是否输入正确\n"
    )

        .setPositiveButton("确定", null)
        .create()
    alertDialog.show()

        }

    }
}
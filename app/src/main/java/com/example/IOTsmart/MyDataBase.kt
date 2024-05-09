package com.example.IOTsmart



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 定义一个MyDataBase抽象类，继承自RoomDatabase
@Database(version = 1, entities = [Userx::class,Driver::class], exportSchema = false)
abstract class MyDataBase : RoomDatabase() {

    // 定义一个userDao方法，返回UserDao
    abstract fun userDao(): UserDao

    companion object {
        // 定义一个instance变量，用于存储MyDataBase实例
        private var instance: MyDataBase? = null
        // 定义一个getDataBase方法，用于获取MyDataBase实例
        @Synchronized
        fun getDataBase(context: Context): MyDataBase {
            // 如果instance变量不为空，则直接返回instance变量
            return instance
            // 如果instance变量为空，则使用Room.databaseBuilder构建一个MyDataBase实例，并将其赋值给instance变量
                ?: Room.databaseBuilder(context.applicationContext, MyDataBase::class.java, "YANG")
                    .build()
                    .apply { instance = this }
        }
    }
}
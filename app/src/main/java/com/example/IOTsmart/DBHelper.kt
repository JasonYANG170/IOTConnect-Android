import DBHelper.Companion.COLUMN_ID
import DBHelper.Companion.TABLE_NAME
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.IOTsmart.User

class MyDatabase(public val context: Context) {

    private val dbHelper = DBHelper(context)

//  根据登录号获取图片
    fun getImgByLogin(login: Int): Int {
        // 获取可读的数据库
        val db = dbHelper.readableDatabase
        // 定义查询的列
        val columns = arrayOf(DBHelper.COLUMN_img)
        // 定义查询条件
        val selection = "${DBHelper.COLUMN_login} = ?"
        // 定义查询参数
        val selectionArgs = arrayOf(login.toString())
        // 执行查询
        val cursor = db.query(DBHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null)

        // 初始化图片
        var img = 0
        // 如果查询到结果
        if (cursor.moveToFirst()) {
            // 获取图片列的索引
            val imgIndex = cursor.getColumnIndex(DBHelper.COLUMN_img)
            // 获取图片
            img = cursor.getInt(imgIndex)
        }

        // 关闭游标
        cursor.close()
        // 关闭数据库
        db.close()

        return img
    }
   fun updateLoginValues() {//已在ROOM实现
        //获取可写的数据库
        val db = dbHelper.writableDatabase
        //创建一个ContentValues对象
        val values = ContentValues()
        //将DBHelper.COLUMN_login的值设置为-1
        values.put(DBHelper.COLUMN_login, -1)
        //定义whereClause，用于更新数据的条件
        val whereClause = "${DBHelper.COLUMN_login} = ?"
        //定义whereArgs，用于更新数据的参数
        val whereArgs = arrayOf("0")
        //更新数据
        db.update(DBHelper.TABLE_NAME, values, whereClause, whereArgs)
        //关闭数据库
        db.close()
    }
   //更新用户登录状态
  fun updateUserLoginStatus(user: String?,now:Int) {//已在ROOM实现
        //获取可写的数据库
        val db = dbHelper.writableDatabase
        //创建一个ContentValues对象
        val values = ContentValues()
        //将登录状态更新到数据库
        values.put(DBHelper.COLUMN_login, now)
        //定义更新条件
        val whereClause = "${DBHelper.COLUMN_NAME} = ?"
        //定义更新参数
        val whereArgs = arrayOf(user)
        //更新数据
        db.update(DBHelper.TABLE_NAME, values, whereClause, whereArgs)
        //关闭数据库
        db.close()
    }
   //更新图片
 fun updateimg(img:Int) {
        //获取可写的数据库
        val db = dbHelper.writableDatabase
        //创建一个ContentValues对象
        val values = ContentValues()
        //将img的值放入ContentValues对象中
        values.put(DBHelper.COLUMN_img, img)
        //定义whereClause，用于更新数据
        val whereClause = "${DBHelper.COLUMN_login} = ?"
        //定义whereArgs，用于更新数据
        val whereArgs = arrayOf("0")
        //更新数据
        db.update(DBHelper.TABLE_NAME, values, whereClause, whereArgs)
        //关闭数据库
        db.close()
    }
    // 向数据库中插入数据
fun insertData(id: Int, name: String?,password: String?,login:Int,img:Int) {//已在ROOM实现
        // 创建ContentValues对象
        val values = ContentValues()
        // 向ContentValues对象中添加数据
        values.put(DBHelper.COLUMN_ID, id)
        values.put(DBHelper.COLUMN_NAME, name)
        values.put(DBHelper.COLUMN_password, password)
        values.put(DBHelper.COLUMN_login, login)
        values.put(DBHelper.COLUMN_img, img)
        // 获取可写的数据库
        val db = dbHelper.writableDatabase
        // 向数据库中插入数据
        db.insert(DBHelper.TABLE_NAME, null, values)
        // 关闭数据库
        db.close()
    }
   fun retrieveData(): List<User> {//不明使用场景，仅测试调用

        // 创建一个空的User列表
        val userList = mutableListOf<User>()

        // 获取可读的数据库
        val db = dbHelper.readableDatabase
        // 创建游标
        val cursor: Cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null)

        // 遍历游标中的每一行数据
        while (cursor.moveToNext()) {
            // 获取每一行的列索引
            val idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID)
            val nameIndex = cursor.getColumnIndex(DBHelper.COLUMN_NAME)
            val passwordIndex = cursor.getColumnIndex(DBHelper.COLUMN_password)
            val loginIndex = cursor.getColumnIndex(DBHelper.COLUMN_login)
            val imgIndex= cursor.getColumnIndex(DBHelper.COLUMN_img)

            // 如果列名不存在，则跳过该行数据
            if (idIndex == -1 || nameIndex == -1 || passwordIndex == -1|| loginIndex == -1|| imgIndex == -1) {
                continue
            }

            // 获取每一行的数据
            val id = cursor.getInt(idIndex)
            val name = cursor.getString(nameIndex)
            val password = cursor.getString(passwordIndex)
            val login = cursor.getInt(loginIndex)
            val img = cursor.getInt(idIndex)
            // 将每一行的数据封装成User对象
            val user = User(id, name, password ,login, img )
            // 将User对象添加到userList中
            userList.add(user)
        }
        // 关闭游标
        cursor.close()
        // 关闭数据库
        db.close()

        // 返回userList
        return userList
    }
    // 获取最大ID
fun getMaxId(): Int {//已在Room中实现
        // 获取可读的数据库
        val db = dbHelper.readableDatabase
        // 查询语句
        val query = "SELECT MAX($COLUMN_ID) FROM $TABLE_NAME"
        // 执行查询
        val cursor = db.rawQuery(query, null)

        // 初始化最大ID
        var maxId = 0
        // 如果有查询结果
        if (cursor.moveToFirst()) {
            // 获取最大ID
            maxId = cursor.getInt(0)
        }

        // 关闭游标
        cursor.close()
        // 关闭数据库
        db.close()

        // 返回最大ID
        return maxId
    }
   fun isUserExist(username: String?): Boolean {//已在Room中实现
        // 获取可读的数据库
        val db = dbHelper.readableDatabase
        // 定义查询的列
        val columns = arrayOf(DBHelper.COLUMN_NAME)
        // 定义查询条件
        val selection = "${DBHelper.COLUMN_NAME} = ?"
        // 定义查询参数
        val selectionArgs = arrayOf(username)
        // 执行查询
        val cursor = db.query(DBHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null)
        // 判断是否有结果
        val isExist = cursor.count > 0
        // 关闭游标
        cursor.close()
        // 关闭数据库
        db.close()
        // 返回结果
        return isExist
    }
    //根据登录获取用户信息
fun getUserByLogin(login: Int): User? {//已在Room中实现
        //获取可读的数据库
        val db = dbHelper.readableDatabase
        //定义查询的列
        val columns = arrayOf(DBHelper.COLUMN_ID, DBHelper.COLUMN_NAME, DBHelper.COLUMN_password, DBHelper.COLUMN_login, DBHelper.COLUMN_img)
        //定义查询条件
        val selection = "${DBHelper.COLUMN_login} = ?"
        //定义查询参数
        val selectionArgs = arrayOf(login.toString())
        //查询数据库
        val cursor = db.query(DBHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null)

        var user: User? = null
        //如果查询到数据
        if (cursor.moveToFirst()) {
            //获取id列的索引
  val idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID)
            //获取name列的索引
            val nameIndex = cursor.getColumnIndex(DBHelper.COLUMN_NAME)
            //获取password列的索引
            val passwordIndex = cursor.getColumnIndex(DBHelper.COLUMN_password)
            //获取login列的索引
            val loginIndex = cursor.getColumnIndex(DBHelper.COLUMN_login)
            //获取img列的索引
            val imgIndex = cursor.getColumnIndex(DBHelper.COLUMN_img)

            // 如果列名存在，则创建 User 对象
            if (idIndex != -1 && nameIndex != -1 && passwordIndex != -1 && loginIndex != -1 && imgIndex != -1) {
                val id = cursor.getInt(idIndex)
                val name = cursor.getString(nameIndex)
                val password = cursor.getString(passwordIndex)
                val login = cursor.getInt(loginIndex)
                val img = cursor.getInt(imgIndex)
                user = User(id, name, password, login, img)
            }
        }

        cursor.close()
        db.close()

        return user
    }
    fun getUserData(username: String?): User? {//已在Room中实现
        //获取可读的数据库
        val db = dbHelper.readableDatabase
        //定义查询的列
        val columns = arrayOf(DBHelper.COLUMN_ID, DBHelper.COLUMN_NAME, DBHelper.COLUMN_password,DBHelper.COLUMN_login)
        //定义查询条件
        val selection = "${DBHelper.COLUMN_NAME} = ?"
        //定义查询参数
        val selectionArgs = arrayOf(username)
        //查询数据
        val cursor = db.query(DBHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null)

        //判断是否有数据
        if (cursor.moveToFirst()) {
            //获取查询的列的索引
            val idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID)
            val nameIndex = cursor.getColumnIndex(DBHelper.COLUMN_NAME)
            val passwordIndex = cursor.getColumnIndex(DBHelper.COLUMN_password)
            val loginIndex = cursor.getColumnIndex(DBHelper.COLUMN_login)
            val imgIndex = cursor.getColumnIndex(DBHelper.COLUMN_img)
            //获取查询的数据
            val id = cursor.getInt(idIndex)
            val img = cursor.getInt(imgIndex)
            val name = cursor.getString(nameIndex)
            val password = cursor.getString(passwordIndex)
            val login = cursor.getInt(loginIndex)
            //关闭游标
            cursor.close()
            //关闭数据库
            db.close()

            //返回查询的数据
            return User(id, name, password,login, img )
        }

        //关闭游标
        cursor.close()
        //关闭数据库
        db.close()

        //返回null
        return null
    }


}
class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // 数据库文件名
        const val DATABASE_NAME = "mydatabase.db"
        // 数据库版本号
        const val DATABASE_VERSION = 1

        // 表名和列名
        const val TABLE_NAME = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_password = "password"
        const val COLUMN_login = "login"
        const val COLUMN_img = "img"

        const val DRIVE_TABLE_NAME = "drive"
        const val COLUMN_DRIVE_ID = "id"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_DRIVE_VALUE = "drive_value"
        const val COLUMN_DRIVE_NAME = "drive_name"
    }
    // 获取最大的id
    fun getMaxId(): Int {
        val db = readableDatabase
        val query = "SELECT MAX($COLUMN_ID) FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        var maxId = 0
        if (cursor.moveToFirst()) {
            maxId = cursor.getInt(0)
        }

        cursor.close()
        db.close()

        return maxId
    }
    // 执行SQL语句
    fun executeSQL(sql: String) {
        val db: SQLiteDatabase = writableDatabase
        db.execSQL(sql)
        db.close()
    }
    // 为用户添加driveId和drive_value
    fun addDriveIdForUserId(userId: Int, driveId: Int,drive_value: String,drive_name: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId)
            put(COLUMN_DRIVE_ID, driveId)
            put(COLUMN_DRIVE_VALUE, drive_value)
            put(COLUMN_DRIVE_NAME, drive_name)
        }
        val newRowId = db.insert(DRIVE_TABLE_NAME, null, values)
        db.close()
        return newRowId
    }
    // 获取用户登录为0的driveId和drive_value
    fun getDriveIdsAndValuesForUserWithLoginZero(): List<Triple<Int, String, String>> {
        val driveData = mutableListOf<Triple<Int, String, String>>()
        val db = readableDatabase
        val query = "SELECT $COLUMN_DRIVE_ID, $COLUMN_DRIVE_VALUE, $COLUMN_DRIVE_NAME FROM $DRIVE_TABLE_NAME " +
                "WHERE $COLUMN_USER_ID IN (SELECT $COLUMN_ID FROM $TABLE_NAME WHERE $COLUMN_login = 0)"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val driveId = cursor.getInt(cursor.getColumnIndex(COLUMN_DRIVE_ID))
            val driveValue = cursor.getString(cursor.getColumnIndex(COLUMN_DRIVE_VALUE))
            val drivename = cursor.getString(cursor.getColumnIndex(COLUMN_DRIVE_NAME))
            driveData.add(Triple(driveId, driveValue, drivename))
        }

        cursor.close()
        db.close()
        return driveData
    }
   fun getUserIdsWithLoginZero(): List<Int> {
        // 创建一个空的列表，用于存储用户ID
        val userIds = mutableListOf<Int>()
        // 获取可读的数据库
        val db = readableDatabase
        // 查询语句，查询login为0的用户ID
        val query = "SELECT $COLUMN_ID FROM $TABLE_NAME WHERE $COLUMN_login = 0"
        // 执行查询语句，获取游标
        val cursor = db.rawQuery(query, null)

        // 遍历游标，获取用户ID
        while (cursor.moveToNext()) {
            val userId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            // 将用户ID添加到列表中
            userIds.add(userId)
        }

        // 关闭游标
        cursor.close()
        // 关闭数据库
        db.close()
        // 返回用户ID列表
        return userIds
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // 创建表格的 SQL 语句
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_password INTEGER,$COLUMN_login INTEGER,$COLUMN_img INTEGER)"
        db?.execSQL(createTableQuery)
        // 创建 drive 表的 SQL 语句
        val createDriveTableQuery = "CREATE TABLE $DRIVE_TABLE_NAME ($COLUMN_DRIVE_ID INTEGER PRIMARY KEY, $COLUMN_USER_ID INTEGER, $COLUMN_DRIVE_VALUE INTEGER, $COLUMN_DRIVE_NAME INTEGER,FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_NAME($COLUMN_ID))"
        db?.execSQL(createDriveTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // 数据库升级时的操作
        // 如果数据库版本发生变化，可以在此处添加相应的升级逻辑
    }
}
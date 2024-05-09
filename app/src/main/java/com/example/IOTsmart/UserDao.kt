package com.example.IOTsmart


import androidx.room.*

@Dao
interface UserDao {

    @Insert  // 增
    fun insertUser(user: Userx)

    @Delete  // 删
    fun deleteUser(user: Userx)

    @Query("select * from Userx")  // 查
    fun getAllUser(): List<Userx>

    @Update  // 改
    fun updateUser(user: Userx)
    @Transaction

    @Query("SELECT * FROM Userx")
    fun getUserbydriver(): List<Userbydriver>

  // 判断用户存在
   @Query("select * from Userx where userid = "+":userIZd")  // 查
    fun findUserById(userIZd: String): Boolean
//查询用户信息
    @Query("select * from Userx where userid = "+":userIZd")  // 查
    fun getUserinfo(userIZd: String): List<Userx>
    //查询已经登录的用户信息
  //  @Query("select * from Userx where LoginStats = "+":stats")  // 查
   // fun getlogininfo(stats: Int): List<User>

  //  修改登录状态
  //  @Query("UPDATE Userx SET loginstats = :satats WHERE userid = :userIZd")
   // fun updateLoginState(userIZd: String,satats:Int)
//退出登录
   // @Query("UPDATE Userx SET loginstats = -1 WHERE loginstats = 0")
   // fun updateLoginout()
    //获取最大ID
  //  @Query("SELECT MAX(userid) AS MaxUserId FROM Userx")
   // fun getmaxid()
  //  @Query("SELECT MAX(userid) AS MaxUserId FROM Userx")
  //  fun getmaxid()
}

package com.example.IOTsmart


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Driver(
    @PrimaryKey val DiverId: String,
    val Driverbyuserid: String,
    val Drivername: String
)
@Entity
data class Userx(
    @PrimaryKey val Userid: String,
    val Name: String,
    val Password: String,
    val LoginStats: Int,
    val Image: Int)

data class Userbydriver(
    @Embedded val User: Userx,
    @Relation(
        parentColumn = "Userid",
        entityColumn = "Driverbyuserid"
    )
    val Driver: List<Driver>
)

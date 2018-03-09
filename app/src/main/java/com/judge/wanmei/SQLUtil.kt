package com.judge.wanmei

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


//数据库全路径
var filePath: String = "data/data/com.judge.wanmei/wanmei.db"
const val REQUEST_STATIC_NPC_NAME = 100
const val REQUEST_STATIC_NPC_LOCATION = 101
const val REQUEST_MOVE_NPC_NAME = 102
const val REQUEST_MOVE_NPC_LOCATION = 103
const val TYPE_STATIC = 1
const val TYPE_MOVE = 2
const val TYPE_LOCATION = 3
const val KEY_NPC_NAME = "npc_name"
const val KEY_NPC_LOCATION = "npc_location"
const val KEY_CHOOSE_TYPE = "choose_type"

var db: SQLiteDatabase? = null

fun openDatabase(context: Context): SQLiteDatabase {
    val dbFile = File(filePath)
    return if (dbFile.exists()) {
        SQLiteDatabase.openOrCreateDatabase(dbFile, null)
    } else {
        try {
            val input = context.classLoader.getResourceAsStream("assets/wanmei.db")
            val fileOut = FileOutputStream(dbFile)
            fileOut.write(input.readBytes())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        openDatabase(context)
    }
}

fun initDB(context: Context) {
    db = openDatabase(context)
}

fun queryStaticNpc(): List<String> {
    return queryNpcWithGroupId("1")
}

fun queryMoveNpc(): List<String> {
    return queryNpcWithGroupId("54")
}

private fun queryNpcWithGroupId(groupId: String): List<String> {
    val cursor: Cursor = db!!.rawQuery("select npc_name from jiayuan where group_id = '$groupId'", null)
    val npcList: MutableList<String> = ArrayList()
    val name: Int = cursor.getColumnIndex("npc_name")
    while (cursor.moveToNext()) {
        npcList.add(cursor.getString(name))
    }
    cursor.close()
    return npcList
}

fun queryLocationWithName(npcName: String): List<String> {
    val cursor: Cursor = db!!.rawQuery("select npc_location from jiayuan where npc_name = '$npcName'", null)
    val npcList: MutableList<String> = ArrayList()
    val location: Int = cursor.getColumnIndex("npc_location")
    while (cursor.moveToNext()) {
        npcList.add(cursor.getString(location))
    }
    cursor.close()
    return npcList
}

fun queryAllNpc(npcName: String, npcLocation: String): List<NpcBean> {
    val cursor: Cursor = db!!.rawQuery("select * from jiayuan where group_id = (select group_id from jiayuan where npc_name = '$npcName' and npc_location = '$npcLocation')", null)
    val name: Int = cursor.getColumnIndex("npc_name")
    val location: Int = cursor.getColumnIndex("npc_location")
    val npcList: MutableList<NpcBean> = ArrayList()
    while (cursor.moveToNext()) {
        val npcBean = NpcBean(cursor.getString(name), cursor.getString(location))
        npcList.add(npcBean)
    }
    cursor.close()
    return npcList
}

fun closeDb() {
    db!!.close()
}

class NpcBean(npcName: String, npcLocation: String) {
    var name = npcName
    var location = npcLocation
}
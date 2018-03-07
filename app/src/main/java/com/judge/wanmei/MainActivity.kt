package com.judge.wanmei

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {
    var sd : SQLiteDatabase?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sd = SQLiteDatabase.openDatabase(assets.locales[0],null,SQLiteDatabase.CONFLICT_NONE)
        query("狄红雪", "夜火坛")
    }

    fun query(npcName : String, npcLocation : String){
        var cursor : Cursor = sd!!.rawQuery("select * from jiayuan where 'npc_name' = '" + npcName + "' and 'npc_location' = '" + npcLocation + "'", null)
        var name : Int = cursor.getColumnIndex("npc_name")
        var location : Int = cursor.getColumnIndex("npc_location")
        cursor.moveToFirst()
        while (cursor.moveToNext()){
            Log.e("row", "name : " + cursor.getString(name) + " location : " + cursor.getString(location))
        }
        cursor.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        sd!!.close()
    }
}

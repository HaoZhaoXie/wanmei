package com.judge.wanmei

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDB(this)
        tv_static_npc_name.setOnClickListener({
            startActivityForResult(
                    Intent(this, ChooseActivity::class.java).putExtra(KEY_CHOOSE_TYPE, TYPE_STATIC),
                    REQUEST_STATIC_NPC_NAME)
        })
        tv_static_npc_location.setOnClickListener({
            startActivityForResult(
                    Intent(this, ChooseActivity::class.java).putExtra(KEY_CHOOSE_TYPE, TYPE_LOCATION).putExtra(KEY_NPC_NAME, tv_static_npc_name.text),
                    REQUEST_STATIC_NPC_LOCATION)
        })
        tv_move_npc_name.setOnClickListener({
            startActivityForResult(
                    Intent(this, ChooseActivity::class.java).putExtra(KEY_CHOOSE_TYPE, TYPE_MOVE),
                    REQUEST_MOVE_NPC_NAME)
        })
        tv_move_npc_location.setOnClickListener({
            startActivityForResult(
                    Intent(this, ChooseActivity::class.java).putExtra(KEY_CHOOSE_TYPE, TYPE_LOCATION).putExtra(KEY_NPC_NAME, tv_move_npc_name.text),
                    REQUEST_MOVE_NPC_LOCATION)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_STATIC_NPC_NAME -> {
                    tv_static_npc_name.text = data!!.getStringExtra(KEY_NPC_NAME)
                    tv_static_npc_location.visibility = View.VISIBLE
                    tv_static_npc_location.text = getString(R.string.need_choose_location)
                }
                REQUEST_STATIC_NPC_LOCATION -> {
                    tv_static_npc_location.text = data!!.getStringExtra(KEY_NPC_LOCATION)
                    lv_static_npc.adapter = AllNpcAdapter(this, queryAllNpc(tv_static_npc_name.text as String, tv_static_npc_location.text as String))
                }
                REQUEST_MOVE_NPC_NAME -> {
                    tv_move_npc_name.text = data!!.getStringExtra(KEY_NPC_NAME)
                    tv_move_npc_location.visibility = View.VISIBLE
                    tv_move_npc_location.text = getString(R.string.need_choose_location)
                }
                REQUEST_MOVE_NPC_LOCATION -> {
                    tv_move_npc_location.text = data!!.getStringExtra(KEY_NPC_LOCATION)
                    lv_move_npc.adapter = AllNpcAdapter(this, queryAllNpc(tv_move_npc_name.text as String, tv_move_npc_location.text as String))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        closeDb()
    }

    class AllNpcAdapter(private var context: Context, var data: List<NpcBean>) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val holder: LocationHolder
            val v : View
            if(convertView == null){
                v = LayoutInflater.from(context).inflate(R.layout.item_npc_location, parent, false)
                holder = LocationHolder()
                holder.name = v.findViewById(R.id.tv_name) as TextView
                holder.location = v.findViewById(R.id.tv_location) as TextView
                v.tag = holder
            }else{
                v = convertView
                holder = v.tag as LocationHolder
            }
            holder.name.text = data[position].name
            holder.location.text = data[position].location
            return v
        }

        override fun getItem(position: Int): Any {
            return data[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return data.size
        }

        class LocationHolder{
            lateinit var name : TextView
            lateinit var location : TextView
        }
    }
}

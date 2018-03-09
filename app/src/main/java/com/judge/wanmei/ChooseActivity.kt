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
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_choose.*

class ChooseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)
        tv_close.setOnClickListener({ finish() })
        val mType = intent.getIntExtra(KEY_CHOOSE_TYPE, -1)
        when (mType) {
            TYPE_STATIC -> {
                lv_choose.adapter = ChooseAdapter(this, queryStaticNpc())
            }
            TYPE_MOVE -> {
                lv_choose.adapter = ChooseAdapter(this, queryMoveNpc())
            }
            TYPE_LOCATION -> {
                lv_choose.adapter = ChooseAdapter(this, queryLocationWithName(intent.getStringExtra(KEY_NPC_NAME)))
            }
            -1 -> {
                Toast.makeText(this, getString(R.string.toast_error), Toast.LENGTH_SHORT).show()
            }
        }
        lv_choose.setOnItemClickListener {
            parent, _, position, _ ->
            var close = false
            when (mType) {
                TYPE_MOVE, TYPE_STATIC -> {
                    setResult(Activity.RESULT_OK, Intent().putExtra(KEY_NPC_NAME, parent.adapter.getItem(position) as String))
                    close = true
                }
                TYPE_LOCATION -> {
                    setResult(Activity.RESULT_OK, Intent().putExtra(KEY_NPC_LOCATION, parent.adapter.getItem(position) as String))
                    close = true
                }
                -1 -> {
                    Toast.makeText(this, getString(R.string.toast_error), Toast.LENGTH_SHORT).show()
                }
            }
            if(close) {
                finish()
            }
        }
    }
}

class ChooseAdapter(private var context: Context, var data: List<String>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v: View
        val holder: ChooseItemHolder
        if (convertView == null) {
            holder = ChooseItemHolder()
            v = LayoutInflater.from(context).inflate(R.layout.item_choose, parent, false)
            holder.chooseItem = v.findViewById(R.id.tv_choose) as TextView
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ChooseItemHolder
        }
        holder.chooseItem.text = data[position]
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

    class ChooseItemHolder {
        lateinit var chooseItem: TextView
    }
}
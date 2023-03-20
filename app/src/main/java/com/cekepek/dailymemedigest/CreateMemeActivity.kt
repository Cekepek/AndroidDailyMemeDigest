package com.cekepek.dailymemedigest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.internal.TextWatcherAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_create_meme.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_meme.view.*

class CreateMemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_meme)

        setSupportActionBar(toolbar5)
        supportActionBar?.title = "Create Your Meme"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        txtUrlCreate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                var url = s.toString()
                Picasso.get().load(url).into(memeImageCreate)
            }

        })
        txtTopText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                txtTopCreate.text = s.toString()
            }

        })
        txtBottomText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                txtBottomCreate.text = s.toString()
            }

        })
        btnSubmitCreate.setOnClickListener {
            val q = Volley.newRequestQueue(it.context)
            val url = "https://ubaya.fun/flutter/160420021/meme/createMeme.php"
            val stringRequest = object: StringRequest(
                Request.Method.POST,
                url,{
                    Log.d("cekparams",it)
                    val intent = Intent(this,MainActivity::class.java)
                    this.startActivity(intent)
                },
                { Log.e("cekparams", it.message.toString()) }
            ){
                override fun getParams(): MutableMap<String, String>{
                    val map = HashMap<String, String>()
                    map.set("imageUrl", txtUrlCreate.text.toString())
                    map.set("topText", txtTopText.text.toString())
                    map.set("bottomText", txtBottomText.text.toString())
                    map.set("userId",Global.user.toString())
                    return map
                }
            }
            q.add(stringRequest)
        }
    }
}
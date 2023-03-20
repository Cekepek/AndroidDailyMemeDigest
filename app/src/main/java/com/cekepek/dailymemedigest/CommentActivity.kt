package com.cekepek.dailymemedigest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_create_meme.*
import org.json.JSONObject
import java.time.LocalDate

class CommentActivity : AppCompatActivity() {
    val comments:ArrayList<Comment> = ArrayList()

    fun updateList(){
        val lm: LinearLayoutManager = LinearLayoutManager(this)
        recyclerViewComment.layoutManager = lm
        recyclerViewComment.setHasFixedSize(false)
        recyclerViewComment.adapter = CommentAdapter(comments)
    }
    fun updateComment(memeId: Int) {
        val q2 = Volley.newRequestQueue(this)
        val url2 = "https://ubaya.fun/flutter/160420021/meme/getComment.php"
        var stringRequest2 = object : StringRequest(
            Request.Method.POST,
            url2, {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "success") {
                    val data = obj.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val commentObj = data.getJSONObject(i)
                        val commentClass = Comment(
                            commentObj.getInt("comment_id"),
                            commentObj.getInt("user_id"),
                            commentObj.getString("first_name"),
                            commentObj.getString("last_name"),
                            commentObj.getString("content"),
                            commentObj.getString("comment_date"),
                        )
                        comments.add(commentClass)
                        Log.d("commentId", commentObj.getString("comment_id"))
                    }
                    updateList()
                }
            },
            {
                Log.e("apiresult", it.message.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map.set("memeId", memeId.toString())
                return map
            }
        }
        q2.add(stringRequest2)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        var memeId = intent.getIntExtra("memeId",0)
        btnSendComment.isEnabled = false
        btnSendComment.isClickable = false
        setSupportActionBar(toolbar4)
        supportActionBar?.title = "Meme Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.fun/flutter/160420021/meme/getMemes.php"
        var stringRequest = object: StringRequest(
            Request.Method.POST,
            url,{
                // sukses
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "success"){
                    val data = obj.getJSONArray("data")
                    for(i in 0 until data.length()){
                        val memeObj = data.getJSONObject(i)
                        var imgUrl = memeObj.getString("image_url")
                        Picasso.get().load(imgUrl).into(memeImageDetail)
                        txtTopDetail.text = memeObj.getString("top_text")
                        txtBottomDetail.text = memeObj.getString("bottom_text")
                        txtTotalLikesDetail.text = memeObj.getInt("likes").toString()+" Likes"
                        Log.d("memeId", memeObj.getInt("meme_id").toString())
                    }
                }
            },
            {
                Log.e("apiresult",it.message.toString())}
        )
        {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map.set("memeId", memeId.toString())
                return map
            }
        }
        q.add(stringRequest)

        updateComment(memeId)

        txtCommentDetail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.toString()==""){
                    btnSendComment.isEnabled = false
                    btnSendComment.isClickable = false
                }
                else{
                    btnSendComment.isEnabled = true
                    btnSendComment.isClickable = true
                }
            }

        })

        btnSendComment.setOnClickListener {
            val q = Volley.newRequestQueue(it.context)
                val url = "https://ubaya.fun/flutter/160420021/meme/uploadComment.php"
            val stringRequest = object: StringRequest(
                Request.Method.POST,
                url,
                { Log.d("cekparams",it)
                    Toast.makeText(this,"Komentar Berhasil Ditambahkan",Toast.LENGTH_SHORT).show()
                    updateComment(memeId)
                },
                { Log.e("cekparams", it.message.toString())}
            ){
                override fun getParams(): MutableMap<String, String> {
                    val map = HashMap<String, String>()
                    map.set("memeId", memeId.toString())
                    map.set("userId", Global.user.toString())
                    map.set("content", txtCommentDetail.text.toString())
                    map.set("date", LocalDate.now().toString())
                    return map
                }
            }
            q.add(stringRequest)
        }
    }
}
package com.cekepek.dailymemedigest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.time.LocalDate

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnCreate.setOnClickListener {
            var intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        btnSignIn.setOnClickListener {
            val q = Volley.newRequestQueue(it.context)
            val url = "https://ubaya.fun/flutter/160420021/meme/login.php"
            val stringRequest = object: StringRequest(
                Request.Method.POST,
                url,
                { Log.d("cekparams",it)
                    val obj = JSONObject(it)
                    if(obj.getString("result") == "success"){
                            Global.user = obj.getInt("userid")
                            Global.login = true
                        Log.d("cekparams",Global.user.toString())
                            Toast.makeText(this,"LOGIN BERHASIL", Toast.LENGTH_SHORT).show()
                            var intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)

                    }
                    else{
                        Toast.makeText(this,"Username / Password Salah", Toast.LENGTH_SHORT).show()
                    }
                },
                { Log.e("cekparams", it.message.toString())}
            ){
                override fun getParams(): MutableMap<String, String> {
                    val map = HashMap<String, String>()
                    map.set("username", txtUsername.text.toString())
                    map.set("password", txtPassword.text.toString())
                    return map
                }
            }
            q.add(stringRequest)
        }
    }
}
package com.cekepek.dailymemedigest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_login.*
import java.time.LocalDate

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        btnBack.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnCreateAcc.setOnClickListener {
            if(txtCreatePassword.text.toString() != txtRepeatPassword.text.toString()){
                Toast.makeText(this,"PASSWORD TIDAK SAMA",Toast.LENGTH_SHORT).show()
            }
            else{
                val q = Volley.newRequestQueue(it.context)
                val url = "https://ubaya.fun/flutter/160420021/meme/register.php"
                val stringRequest = object: StringRequest(
                    Request.Method.POST,
                    url,
                    { Log.d("cekparams",it)
                        Toast.makeText(this,"BERHASIL MEMBUAT ACCOUNT",Toast.LENGTH_SHORT).show()
                        var intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    },
                    { Log.e("cekparams", it.message.toString())}
                ){
                    override fun getParams(): MutableMap<String, String> {
                        val map = HashMap<String, String>()
                        map.set("username", txtCreateUsername.text.toString())
                        map.set("password", txtCreatePassword.text.toString())
                        map.set("first_name", txtCreateFirst.text.toString())
                        map.set("last_name", txtCreateLast.text.toString())
                        map.set("registration_date", LocalDate.now().toString())
                        return map
                    }
                }
                q.add(stringRequest)
            }
        }
    }
}
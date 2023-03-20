package com.cekepek.dailymemedigest

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_create_meme.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class SettingActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    var pictChange = false
    fun takePicture(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            REQUEST_IMAGE_CAPTURE ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    takePicture()
                }else{
                    Toast.makeText(this,"You must grant permission to access the camera",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setSupportActionBar(toolbar2)
        supportActionBar?.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.fun/flutter/160420021/meme/getAccount.php"
        val stringRequest = object: StringRequest(
            Request.Method.POST,
            url,{
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "success")  {
                    val data = obj.getJSONObject("data")
                        var urlImg = data.getString("avatar_url")
                        if(urlImg==null){
                            urlImg="https://t4.ftcdn.net/jpg/02/51/95/53/360_F_251955356_FAQH0U1y1TZw3ZcdPGybwUkH90a3VAhb.jpg"
                        }

                    Log.d("cekparam", urlImg)
                        Picasso.get().load(urlImg).into(memeImageSetting)
                        txtNameSetting.text = data.getString("first_name")+" "+data.getString("last_name")
                        txtActiveSinceSetting.text = data.getString("registration_date")
                        txtUsernameSetting.text = data.getString("username")
                        txtAccountFirstName.setText(data.getString("first_name"))
                        txtAccountLastName.setText(data.getString("last_name"))
                    if(data.getInt("privacy") == 1){
                        cbHideNameSetting.isChecked = true
                    }
                    else{
                        cbHideNameSetting.isChecked = false
                    }
                }
            },
            { Log.e("cekparams", it.message.toString()) }
        ){
            override fun getParams(): MutableMap<String, String>{
                val map = HashMap<String, String>()
                map.set("userId",Global.user.toString())
                return map
            }
        }
        q.add(stringRequest)

        memeImageSetting.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),REQUEST_IMAGE_CAPTURE)
            }else{
                //granted
                takePicture()
            }
        }

        btnSaveSetting.setOnClickListener {
            val q = Volley.newRequestQueue(it.context)
            val url = "https://ubaya.fun/flutter/160420021/meme/updateProfile.php"
            val stringRequest = object: StringRequest(
                Request.Method.POST,
                url,{
                    Log.d("cekparams",it)
                    Toast.makeText(this,"DATA BERHASIL DIGANTI", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MainActivity::class.java)
                    this.startActivity(intent)
                },
                { Log.e("cekparams", it.message.toString()) }
            ){
                override fun getParams(): MutableMap<String, String>{
                    val map = HashMap<String, String>()
                    map.set("firstName", txtAccountFirstName.text.toString())
                    map.set("lastName", txtAccountLastName.text.toString())
                    if(cbHideNameSetting.isChecked){
                        map.set("privacy", "1")
                    }
                    else{
                        map.set("privacy", "0")
                    }
                    map.set("userId",Global.user.toString())
                    return map
                }
            }
            q.add(stringRequest)

            if(pictChange){
                val q = Volley.newRequestQueue(it.context)
                val url = "https://ubaya.fun/flutter/160420021/meme/updateAvatar.php"
                val stringRequest = object: StringRequest(
                    Request.Method.POST,
                    url,{
                        Log.d("cekparams",it)
                        Toast.makeText(this,"GAMBAR BERHASIL DIGANTI", Toast.LENGTH_SHORT).show()
                    },
                    { Log.e("cekparams", it.message.toString()) }
                ){
                    override fun getParams(): MutableMap<String, String>{
                        val map = HashMap<String, String>()
                        map.set("avatarUrl", "https://ubaya.fun/flutter/160420021/meme/image/" +
                                Global.user.toString() +
                                ".jpg")
                        map.set("userId",Global.user.toString())
                        return map
                    }
                }
                q.add(stringRequest)
            }
        }
        fabLogOut.setOnClickListener {
            Global.login = false
            val intent = Intent(this,MainActivity::class.java)
            this.startActivity(intent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_IMAGE_CAPTURE){
                val extras = data?.extras
                val bm = extras!!.get("data") as Bitmap
                memeImageSetting.setImageBitmap(bm)
                val baos = ByteArrayOutputStream()
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object
                val b: ByteArray = baos.toByteArray()
                val encodedImage: String = Base64.encodeToString(b, Base64.DEFAULT)
                val q = Volley.newRequestQueue(this)
                val url = "https://ubaya.fun/flutter/160420021/meme/uploadAvatar.php"
                val stringRequest = object: StringRequest(
                    Request.Method.POST,
                    url,{
                        Log.d("cekparams",it)
                        Toast.makeText(this,"FOTO BERHASIL DIGANTI", Toast.LENGTH_SHORT).show()
                    },
                    { Log.e("cekparams", it.message.toString()) }
                ){
                    override fun getParams(): MutableMap<String, String>{
                        val map = HashMap<String, String>()
                        map.set("image", encodedImage)
                        map.set("userId",Global.user.toString())
                        return map
                    }
                }
                q.add(stringRequest)
                pictChange = true
            }
        }
    }
}
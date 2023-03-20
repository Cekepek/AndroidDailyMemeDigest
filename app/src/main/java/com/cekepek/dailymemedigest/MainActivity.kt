package com.cekepek.dailymemedigest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.drawer_layout.view.*
import org.json.JSONObject


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val fragments:ArrayList<Fragment> = ArrayList()

    override fun onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else{
            super.onBackPressed()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        setContentView(R.layout.drawer_layout)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        if(!Global.login){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        var header = navView.getHeaderView(0)
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
                    if(urlImg==""){
                        urlImg="https://t4.ftcdn.net/jpg/02/51/95/53/360_F_251955356_FAQH0U1y1TZw3ZcdPGybwUkH90a3VAhb.jpg"
                    }
                    Picasso.get().load(urlImg).into(header.headerImage)
                    Picasso.get().load("https://imgs.search.brave.com/GpTfLLh91ENXqwIiU0MD0Aju4wS6BL_bgCuA_ujM0js/rs:fit:900:900:1/g:ce/aHR0cHM6Ly93d3cu/bWVtZS1hcnNlbmFs/LmNvbS9tZW1lcy9k/NTc4NDk4MzhkYmRh/YjRkMTdmYjM5NGU5/OGY3MzZmNy5qcGc").into(header.headerBackground)
                    header.txtHeaderName.text = data.getString("first_name")+" "+data.getString("last_name")
                        header.txtHeaderUsername.text = data.getString("username")
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
        header.fabLogoutDrawer.setOnClickListener {
            Global.login = false
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        var drawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name,
                R.string.app_name)
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()
        navView.setNavigationItemSelectedListener {
            viewPager.currentItem = when(it.itemId){
                R.id.itemHome ->  0
                R.id.itemCreation -> 1
                R.id.itemLeaderboard -> 2
                else -> 0
            }
            when(it.itemId){
                R.id.itemSettings->{
                    bottomNav.selectedItemId = bottomNav.menu[0].itemId
                    viewPager.currentItem = 0
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        fragments.add(HomeFragment())
        fragments.add(MyCreationFragment())
        fragments.add(LeaderBoardFragment())

        val adapter = MyAdapter(this, fragments)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                bottomNav.selectedItemId = bottomNav.menu[position].itemId
            }
        })
        val intent = Intent(this, SettingActivity::class.java)
        bottomNav.setOnNavigationItemSelectedListener {
            viewPager.currentItem = when(it.itemId){
                R.id.itemHome ->  0
                R.id.itemCreation -> 1
                R.id.itemLeaderboard -> 2
                else -> 0
            }
            when(it.itemId){
                R.id.itemSettings->{
                    bottomNav.selectedItemId = bottomNav.menu[0].itemId
                    viewPager.currentItem = 0
                    startActivity(intent)
                }
            }
            true
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }
}
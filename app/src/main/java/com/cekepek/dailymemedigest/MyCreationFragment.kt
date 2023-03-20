package com.cekepek.dailymemedigest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyCreationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val memes:ArrayList<Meme> = ArrayList()

    fun updateList(){
        val lm: LinearLayoutManager = LinearLayoutManager(activity)
        val rv = view?.findViewById<RecyclerView>(R.id.memeCreation)
        rv?.layoutManager = lm
        rv?.setHasFixedSize(true)
        rv?.adapter = CreationAdapter(memes)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.fun/flutter/160420021/meme/myCreation.php"
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
                        val meme = Meme(
                            memeObj.getInt("meme_id"),
                            memeObj.getString("image_url"),
                            memeObj.getString("top_text"),
                            memeObj.getString("bottom_text"),
                            memeObj.getInt("creator_id"),
                            memeObj.getInt("likes")
                        )
                        memes.add(meme)
                        Log.d("memeId", memeObj.getString("image_url"))
                    }
                    updateList()
                }
            },
            {
                Log.e("apiresult",it.message.toString())}
        )
        {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map.set("userId", Global.user.toString())
                return map
            }
        }
        q.add(stringRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_creation, container, false)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyCreationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
package com.cekepek.dailymemedigest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

/**
 * A simple [Fragment] subclass.
 * Use the [LeaderBoardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LeaderBoardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val users:ArrayList<User> = ArrayList()

    private var totalUsers: TextView? = null
    private var avatarUser: TextView? = null
    private var scoreUser: TextView? = null
    private var rankUser: TextView? = null
    private var totalLike: RecyclerView? = null

    fun updateList(){
        val lm: LinearLayoutManager = LinearLayoutManager(activity)
        val rv = view?.findViewById<RecyclerView>(R.id.recyclerViewRank)
        rv?.layoutManager = lm
        rv?.setHasFixedSize(true)
        rv?.adapter = LeaderBoardAdapter(users)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.fun/flutter/160420021/meme/leaderboard.php"
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
                        val user = User(
                            memeObj.getString("first_name"),
                            memeObj.getString("last_name"),
                            memeObj.getString("avatar_url"),
                            memeObj.getInt("privacy"),
                            memeObj.getInt("total_likes")
                        )
                        users.add(user)
                        Log.d("userId", memeObj.getString("avatar_url"))
                    }
                    updateList()
                }
            },
            {
                Log.e("apiresult",it.message.toString())}
        )
        {
        }
        q.add(stringRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leader_board, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LeaderBoardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LeaderBoardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
package com.cekepek.dailymemedigest

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_meme.view.*
import kotlinx.android.synthetic.main.card_user.view.*

class LeaderBoardAdapter(val users:ArrayList<User>)
    : RecyclerView.Adapter<LeaderBoardAdapter.LeaderboardViewHolder>(){
    class LeaderboardViewHolder(val v: View)
        :RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.card_user, parent,false)
        return LeaderboardViewHolder(v)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        with(holder.v) {
            txtUsernameLeaderboard.text = privacyOn(users[position].firstname + " " + users[position].lastname, users[position].privacy)
            txtTotalLikesLeaderboard.text = users[position].likes.toString()
            var url = users[position].avatar
            Picasso.get().load(url).into(leaderboardAvatar)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun privacyOn(name: String, privacy: Int): String{
        if(privacy == 1){
            var result = name.substring(0,3)
            return result.padEnd(name.length,'*')
        }
        else{
            return name
        }
    }
}
package com.cekepek.dailymemedigest

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_meme.view.*

class MemeAdapter(val memes:ArrayList<Meme>)
    :RecyclerView.Adapter<MemeAdapter.MemeViewHolder>(){
        class MemeViewHolder(val v: View)
            :RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.card_meme, parent,false)
        return MemeViewHolder(v)
    }

    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        val posisi = position
        with(holder.v){
            txtTopDetail.text = memes[position].topText
            txtBottomDetail.text = memes[position].bottomText
            txtTotalLikesDetail.text = memes[position].likes.toString() + " Likes"
            var url = memes[position].image
            Picasso.get().load(url).into(memeImageDetail)
            btnLikeDetail.setOnClickListener {
                val q = Volley.newRequestQueue(it.context)
                val url = "https://ubaya.fun/flutter/160420021/meme/addLike.php"
                val stringRequest = object: StringRequest(
                    Request.Method.POST,
                    url,
                    { Log.d("cekparams",it)
                     memes[position].likes++
                     var newlikes = memes[posisi].likes
                    holder.v.txtTotalLikesDetail.text = "$newlikes Likes"
                    }
                    ,
        { Log.e("cekparams", it.message.toString())}
    ){
        override fun getParams(): MutableMap<String, String> {
            val map = HashMap<String, String>()
            map.set("id", memes[posisi].id.toString())
            return map
        }
    }

    q.add(stringRequest)
}
            btnComment.setOnClickListener {
                var intent = Intent(context, CommentActivity::class.java)
                intent.putExtra("memeId", memes[position].id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
return memes.size
    }


}
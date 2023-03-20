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
import kotlinx.android.synthetic.main.card_comment.view.*

class CommentAdapter(val comments:ArrayList<Comment>)
    : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>(){
    class CommentViewHolder(val v: View)
        : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.card_comment, parent,false)
        return CommentViewHolder(v)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val posisi = position
        with(holder.v){
            txtUserComment.text = comments[posisi].firstName+" "+comments[posisi].lastName
            txtUserChat.text = comments[posisi].commentContent
            txtDateComment.text = comments[posisi].commentDate
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }


}
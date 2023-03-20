package com.cekepek.dailymemedigest

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_meme.view.*
import kotlinx.android.synthetic.main.card_mycreation_meme.view.*

class CreationAdapter(val memes:ArrayList<Meme>)
    : RecyclerView.Adapter<CreationAdapter.MyCreationViewHolder>(){
    class MyCreationViewHolder(val v: View)
        :RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCreationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.card_mycreation_meme, parent,false)
        return MyCreationViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyCreationViewHolder, position: Int) {
        with(holder.v){
            txtTopCreation.text = memes[position].topText
            txtBottomCreation.text = memes[position].bottomText
            txtTotalLikesCreation.text = memes[position].likes.toString() + " Likes"
            var url = memes[position].image
            Picasso.get().load(url).into(memeCreationImage)
            btnLikeCreation.isEnabled = false
            btnLikeCreation.isClickable = false
            btnCommentCreation.setOnClickListener {
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
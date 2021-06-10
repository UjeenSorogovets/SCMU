package com.example.scmu.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.scmu.R
import com.example.scmu.User
import com.example.scmu.messages.ChatLogActivity.Companion.TAG
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import java.util.*


class ChatFromItem(val text: String, val user: User, val bitmap: Bitmap?): Item<GroupieViewHolder>() {
  override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    viewHolder.itemView.textview_from_row.text = text

    val uri = user.profileImageUrl
    val targetImageView = viewHolder.itemView.imageview_chat_from_row

    Glide.with(viewHolder.itemView)
      .load(uri)
      .into(targetImageView)
    Log.d(TAG, "text = ")
    Log.d(TAG, text)
    if (text == "")
    {
      val w: Int = 100
      val h: Int = 100
      val conf = Bitmap.Config.ARGB_8888 // see other conf types
      val bmp = Bitmap.createBitmap(w, h, conf) // this creates a MUTABLE bitmap
      val rnd = Random()
      val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
      //bmp.eraseColor(Color.BLACK)
      bmp.eraseColor(color)
      val targetBitmap = viewHolder.itemView.imageview_from_row
      targetBitmap.setImageBitmap(bmp)
      targetBitmap.alpha= 1F
    }
    else {
      val targetBitmap = viewHolder.itemView.imageview_from_row
      targetBitmap.alpha= 0F
    }

  }

  fun selectPhotoFun(view: View)
  {
    Log.d(com.example.scmu.registerlogin.TAG, "photo!")

    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    startActivityForResult(intent, 0)
  }

  private fun startActivityForResult(intent: Intent, i: Int) {

  }

  override fun getLayout(): Int {
    return R.layout.chat_from_row
  }
}

class ChatToItem(val text: String, val user: User): Item<GroupieViewHolder>() {
  override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    viewHolder.itemView.textview_to_row.text = text

    // load our user image into the star
    val uri = user.profileImageUrl
    val targetImageView = viewHolder.itemView.imageview_chat_to_row
    Glide.with(viewHolder.itemView)
      .load(uri)
      .into(targetImageView)
  }

  override fun getLayout(): Int {
    return R.layout.chat_to_row
  }
}
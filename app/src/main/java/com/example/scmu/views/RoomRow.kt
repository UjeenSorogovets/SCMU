package com.example.scmu.views

import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.example.scmu.R
import com.example.scmu.models.UserIncedent
import com.example.scmu.room.Room.Companion.TAG
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.room_row.view.*


/*class RoomRow (val user: User): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.room_textview_to_row.text = user.username

        Glide.with(viewHolder.itemView)
            .load(user.profileImageUrl)
            .into(viewHolder.itemView.room_imageview_chat_to_row)
    }

    override fun getLayout(): Int {
        return R.layout.room_row
    }
}*/
class RoomRow(val incedent: UserIncedent): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date = java.util.Date(1532358895 * 1000)
        sdf.format(date)
        viewHolder.itemView.room_textview_to_row.text = date.toString()//incedent.timestamp.toString()//incedent.filename

        val w: Int = 122
        val h: Int = 122

        val conf = Bitmap.Config.ARGB_8888

        val bmp = Bitmap.createBitmap(w, h, conf)

        val storage = FirebaseStorage.getInstance().getReference()
        storage.child(incedent.filename).downloadUrl.addOnSuccessListener {
            Log.d(TAG,"SUCCES")
            Glide.with(viewHolder.itemView)
                .load(it)
                .into(viewHolder.itemView.room_imageview_chat_to_row)
        }
            .addOnFailureListener {
                Log.d(TAG,"FAIL")
                viewHolder.itemView.room_imageview_chat_to_row.setImageBitmap(bmp)
            }
    }

    override fun getLayout(): Int {
        return R.layout.room_row
    }
}

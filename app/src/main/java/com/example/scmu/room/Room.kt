package com.example.scmu.room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.scmu.R
import com.example.scmu.User
import com.example.scmu.messages.ChatLogActivity
import com.example.scmu.messages.LatestMessagesActivity
import com.example.scmu.messages.NewMessageActivity
import com.example.scmu.models.ChatMessage
import com.example.scmu.models.UserIncedent
import com.example.scmu.models.UserStream
import com.example.scmu.room.Room.Companion.TAG
import com.example.scmu.room.Room.Companion.currentUser
import com.example.scmu.views.ChatFromItem
import com.example.scmu.views.ChatToItem
import com.example.scmu.views.LatestMessageRow
import com.example.scmu.views.RoomRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.activity_room.*

class Room : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
        val TAG = "MyTagActivity"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    var currentDoorStatus = 1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        supportActionBar?.title = "ROOM"

        fetchCurrentUser()

        UsersIncedents()

        room_lock_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            val user = FirebaseAuth.getInstance().currentUser
            val uid = user?.uid
            var ref = FirebaseDatabase.getInstance().getReference("userStreams/$uid")
            if(isChecked){
                Log.d(TAG,"Standard Switch is on")

                val userStream = UserStream(1,1)
                ref.setValue(userStream)
            }else{
                Log.d(TAG,"Standard Switch is off")

                val userStream = UserStream(0,1)
                ref.setValue(userStream)
            }
        }

        listenForDoorStatus()
    }

    private fun listenForDoorStatus() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/userStreams/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "USER STREAM ADDED!!!")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "USER STREAM CHANGED!!!")
                if (p1!=null){
                    val doorStatus = p0.getValue()
                    if (doorStatus!=1L){
                        room_status.setText("Opened")
                        room_lock_switch.isEnabled=false
                        room_lock_switch.isChecked=false

                    }
                    else{
                        room_status.setText("Closed")
                        room_lock_switch.isEnabled=true
                    }
                    currentDoorStatus = doorStatus as Long
                    Log.d(TAG,p0.getValue().toString())
                }
       
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


    val latestMessagesMap = HashMap<String, ChatMessage>()
    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/userIncedents/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "ROOM CHILD ADDED!!!")
                /*val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()*/
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "ROOM CHILD CHANGED!!!")
                /*val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()*/
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

//
    private fun DummyIncedents() {
        //val uid = currentUser?.uid!!
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid//"AJdTaNtzzEezBs80Z55gChSUMs72"

        var ref = FirebaseDatabase.getInstance().getReference("userIncedents/$uid").push()

        val incedent = UserIncedent("/1623198760.jpg", System.currentTimeMillis() / 1000)

        ref.setValue(incedent)
    }
    private fun DummyUserStreams() {

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid//"AJdTaNtzzEezBs80Z55gChSUMs72"

        var ref = FirebaseDatabase.getInstance().getReference("userStreams/$uid")

        val userStream = UserStream(1,1)
        ref.setValue(userStream)
    }
//
private fun UsersIncedents() {
    val fromId = FirebaseAuth.getInstance().uid
    val ref = FirebaseDatabase.getInstance().getReference("/userIncedents/$fromId")
    ref.addListenerForSingleValueEvent(object: ValueEventListener {

        override fun onDataChange(p0: DataSnapshot) {
            val adapter = GroupAdapter<GroupieViewHolder>()

            p0.children.forEach {
                Log.d(TAG, it.toString())
                val incedent = it.getValue(UserIncedent::class.java)
                if (incedent != null) {
                    adapter.add(RoomRow(incedent))
                }
            }
            recyclerview_room_messages.adapter = adapter
        }
        override fun onCancelled(p0: DatabaseError) {
        }
    })
}


    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d(TAG, "Current from ROOM user ${Room.currentUser?.username}")
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}




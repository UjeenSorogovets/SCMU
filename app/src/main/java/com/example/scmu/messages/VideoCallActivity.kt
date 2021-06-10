package com.example.scmu.messages

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.scmu.R


class VideoCallActivity : AppCompatActivity() {

    var videoPlayer: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call)

        videoPlayer = findViewById<View>(R.id.videoPlayer) as VideoView
        val myVideoUri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.video)
        videoPlayer!!.setVideoURI(myVideoUri)

        videoPlayer!!.start()
    }

    fun play(view: View?) {
        videoPlayer!!.start()
    }

    fun pause(view: View?) {
        videoPlayer!!.pause()
    }

    fun stop(view: View?) {
        videoPlayer!!.stopPlayback()
        videoPlayer!!.resume()
    }

}
package com.veglad.callapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.veglad.callapp.CallManager
import com.veglad.callapp.R
import kotlinx.android.synthetic.main.activity_incomming_call.*

class IncomingCallActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incomming_call)

        answerImageButton?.setOnClickListener { CallManager.acceptCall() }
        rejectImageButton?.setOnClickListener { CallManager.cancelCall(); finish() }
    }

    private fun Long.toDurationString() = String.format("%02d:%02d:%02d", this / 3600, (this % 3600) / 60, (this % 60))
}
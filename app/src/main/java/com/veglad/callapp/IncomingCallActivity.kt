package com.veglad.callapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_incomming_call.*

class IncomingCallActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incomming_call)

        answerImageButton?.setOnClickListener { CallManager.acceptCall() }
        rejectImageButton?.setOnClickListener { CallManager.cancelCall(); finish() }
    }
}

package com.veglad.callapp.view

import android.os.Bundle
import com.veglad.callapp.data_driven.Command
import com.veglad.callapp.data_driven.DataDrivenActivity
import com.veglad.callapp.telephony.CallManager
import com.veglad.callapp.R
import kotlinx.android.synthetic.main.activity_incomming_call.*

class IncomingCallActivity : DataDrivenActivity<IncomingCallActivity.Props>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incomming_call)
    }

    override fun renderProps(props: Props) {
        when(props) {
            is Props.Ringing -> {
                calleeName.text = props.callee
                answerImageButton?.setOnClickListener { props.answer }
                rejectImageButton?.setOnClickListener { props.reject; finish() }
            }
        }
    }

//    override fun onStop() {
//        super.onStop()
//
//    }

    private fun Long.toDurationString() = String.format("%02d:%02d:%02d", this / 3600, (this % 3600) / 60, (this % 60))

    sealed class Props {
        object Connecting : Props()
        class Dialing(
            val callee: String,
            val hangUp: Command?
        ) : Props()
        class Ringing(
            val callee: String,
            val answer: Command?,
            val reject: Command?
        ) : Props()
        class Active(
            val callee: String,
            val time: Long,
            val hangUp: Command?
        ) : Props()
        object Disconnected : Props()
        object Unknown : Props()
    }
}
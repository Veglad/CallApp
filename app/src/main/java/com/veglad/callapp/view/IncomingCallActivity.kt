package com.veglad.callapp.view

import android.content.Intent
import android.os.Bundle
import com.veglad.callapp.data_driven.Command
import com.veglad.callapp.data_driven.DataDrivenActivity
import com.veglad.callapp.R
import com.veglad.callapp.telephony.CallManager
import com.veglad.callapp.telephony.IncomingCallNotification
import com.veglad.callapp.telephony.IncomingCallNotification.Companion.INCOMING_ACTIVITY_ACTION_KEY
import com.veglad.callapp.telephony.IncomingCallNotification.Companion.INCOMING_ACTIVITY_ANSWER_ACTION
import com.veglad.callapp.telephony.IncomingCallNotification.Companion.INCOMING_ACTIVITY_REJECT_ACTION
import kotlinx.android.synthetic.main.activity_incomming_call.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

class IncomingCallActivity : DataDrivenActivity<IncomingCallActivity.Props>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incomming_call)
        handleIntentActionKeys(intent)
    }

    private fun handleIntentActionKeys(intent: Intent) {
        val actionKey = intent.getStringExtra(INCOMING_ACTIVITY_ACTION_KEY)
        if (actionKey == INCOMING_ACTIVITY_ANSWER_ACTION) {
            Timber.tag("IncomingCallActivity").d("INCOMING_ACTIVITY_ANSWER_ACTION")
            //TODO: Refactor with Redux-like
            CallManager.acceptCall()
            IncomingCallNotification.clearNotification(this)
        } else if (actionKey == INCOMING_ACTIVITY_REJECT_ACTION) {
            Timber.tag("IncomingCallActivity").d("INCOMING_ACTIVITY_REJECT_ACTION")
            //TODO: Refactor with Redux-like
            CallManager.cancelCall()
            IncomingCallNotification.clearNotification(this)
            finish()
        }
    }

    override fun renderProps(props: Props) {
        when(props) {
            is Props.Connecting -> subtitleTextView.text = getString(R.string.subtitle_connecting)
            is Props.Dialing -> subtitleTextView.text = getString(R.string.subtitle_dialing)
            is Props.Ringing -> {
                subtitleTextView.text = getString(R.string.subtitle_ringing)
                calleeName.text = props.callee
                answerImageButton?.setOnClickListener { props.answer?.invoke() }
                rejectImageButton?.setOnClickListener { props.reject?.invoke(); finish() }
            }
            is Props.Active -> subtitleTextView.text = props.time.toDurationString()
            is Props.Disconnected -> subtitleTextView.text = getString(R.string.subtitle_disconnected)
            is Props.Unknown -> subtitleTextView.text = getString(R.string.subtitle_unknown)
        }
    }

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
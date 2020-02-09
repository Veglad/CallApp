package com.veglad.callapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.veglad.callapp.data_driven.Command
import com.veglad.callapp.data_driven.DataDrivenActivity
import com.veglad.callapp.R
import com.veglad.callapp.telephony.CallManager
import com.veglad.callapp.telephony.IncomingCallNotification
import com.veglad.callapp.telephony.IncomingCallNotification.Companion.INCOMING_ACTIVITY_ACTION_KEY
import com.veglad.callapp.telephony.IncomingCallNotification.Companion.INCOMING_ACTIVITY_ANSWER_ACTION
import com.veglad.callapp.telephony.IncomingCallNotification.Companion.INCOMING_ACTIVITY_REJECT_ACTION
import kotlinx.android.synthetic.main.activity_incomming_call.*
import timber.log.Timber
import android.view.WindowManager
import kotlin.system.exitProcess


class IncomingCallActivity : DataDrivenActivity<IncomingCallActivity.Props>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incomming_call)
        makeStatusBarTransparent()
        handleIntentActionKeys(intent)
    }

    private fun makeStatusBarTransparent() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
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
            closeApplication()
        }
    }

    private fun closeApplication() {
        finishAndRemoveTask()
    }

    override fun renderProps(props: Props) {
        when(props) {
            is Props.Connecting -> renderConnectingState(props)
            is Props.Dialing -> renderDialingState(props)
            is Props.Ringing -> renderRingingState(props)
            is Props.Active -> renderActiveState(props)
            is Props.Disconnected -> renderDisconnectedState(props)
            is Props.Unknown -> renderUnknownState(props)
        }
    }

    private fun renderUnknownState(props: Props.Unknown) {
        answerImageButton.visibility = View.GONE
        rejectImageButton.visibility = View.VISIBLE

        rejectImageButton?.setOnClickListener { props.reject?.invoke() }
        subtitleTextView.text = getString(R.string.subtitle_unknown)
        closeApplication()
    }

    private fun renderDisconnectedState(props: Props.Disconnected) {
        answerImageButton.visibility = View.GONE
        rejectImageButton.visibility = View.GONE

        subtitleTextView.text = getString(R.string.subtitle_disconnected)
        closeApplication()
    }

    private fun renderActiveState(props: Props.Active) {
        answerImageButton.visibility = View.GONE
        rejectImageButton.visibility = View.VISIBLE

        calleeName.text = props.callee
        subtitleTextView.text = props.time.toDurationString()

        rejectImageButton?.setOnClickListener { props.reject?.invoke() }
    }

    private fun renderRingingState(props: Props.Ringing) {
        answerImageButton.visibility = View.VISIBLE
        rejectImageButton.visibility = View.VISIBLE

        calleeName.text = props.callee
        subtitleTextView.text = getString(R.string.subtitle_ringing)

        answerImageButton?.setOnClickListener { props.answer?.invoke() }
        rejectImageButton?.setOnClickListener { props.reject?.invoke() }
    }

    private fun renderDialingState(props: Props.Dialing) {
        answerImageButton.visibility = View.GONE
        rejectImageButton.visibility = View.VISIBLE

        calleeName.text = props.callee
        subtitleTextView.text = getString(R.string.subtitle_dialing)

        rejectImageButton?.setOnClickListener { props.reject?.invoke() }
    }

    private fun renderConnectingState(props: Props.Connecting) {
        answerImageButton.visibility = View.GONE
        rejectImageButton.visibility = View.VISIBLE

        subtitleTextView.text = getString(R.string.subtitle_connecting)

        rejectImageButton?.setOnClickListener { props.reject?.invoke() }
    }

    private fun Long.toDurationString() = String.format("%02d:%02d:%02d", this / 3600, (this % 3600) / 60, (this % 60))

    sealed class Props {
        class Connecting(
            val reject: Command?
        ) : Props()
        class Dialing(
            val callee: String,
            val reject: Command?
        ) : Props()
        class Ringing(
            val callee: String,
            val answer: Command?,
            val reject: Command?
        ) : Props()
        class Active(
            val callee: String,
            val time: Long,
            val reject: Command?
        ) : Props()
        class Unknown(
            val reject: Command?
        ) : Props()
        object Disconnected: Props()
    }
}
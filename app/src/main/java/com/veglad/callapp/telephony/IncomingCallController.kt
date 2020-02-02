package com.veglad.callapp.telephony

import com.veglad.callapp.data_driven.Command
import com.veglad.callapp.data_driven.DataDrivenActivity
import com.veglad.callapp.view.Call
import com.veglad.callapp.view.Call.Status.*
import com.veglad.callapp.view.IncomingCallActivity.Props
import kotlinx.coroutines.*
import org.json.JSONObject
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class IncomingCallController(
    private var propsConsumer: DataDrivenActivity<Props>?,
    private val callManager: CallManager,
    override val coroutineContext: CoroutineContext
): CoroutineScope {

    companion object {
        const val ONE_SECOND_IN_MS = 1000L
    }

    private var state = State(
        0,
        Call(DISCONNECTED, ""),
        null
    )

    init {
        callManager.setOnUpdateCall(this::handleCall)
        callManager.call?.let { handleCall(it) }
    }

    private fun handleCall(call: Call) {
        Timber.tag("IncomingCallController").d("Updated")
        dispatch(Action.CallAction(call))
    }

    private fun dispatch(action: Action) {
        this.state = when(action) {
            is Action.CallAction -> reduce(action.call, this.state)
            is Action.TickAction -> reduceCount(this.state)
        }

        propsConsumer?.renderProps(mapStateToProps(state))
    }

    private fun mapStateToProps(state: State) : Props {
        return when(state.call.status) {
            CONNECTING -> Props.Connecting
            DIALING -> Props.Dialing(state.call.callee ?: "Unknown", Command { callManager.cancelCall() })
            RINGING -> Props.Ringing(state.call.callee ?: "Unknown", Command { callManager.acceptCall() }, Command { callManager.cancelCall() })
            ACTIVE -> Props.Active(state.call.callee ?: "Unknown", state.callTime, Command { callManager.cancelCall() })
            DISCONNECTED -> Props.Disconnected
            UNKNOWN -> Props.Unknown
        }
    }

    private fun reduce(call: Call, state: State): State {
        return when(call.status) {
            CONNECTING -> {
                state.copy(call = call)
            }
            DIALING -> {
                state.copy(call = call)
            }
            RINGING -> {
                state.copy(call = call)
            }
            ACTIVE -> {
                val job = launchCallTimer { dispatch(Action.TickAction) }
                state.copy(job = job)
            }
            DISCONNECTED -> {
                state.job?.cancel()
                state.copy(callTime = 0, job = null)
            }
            UNKNOWN -> {
                state.copy(call = call)
            }
        }
    }

    private fun reduceCount(state: State): State {
        return state.copy(callTime = state.callTime + 1)
    }

    private fun launchCallTimer(tick: () -> Unit) = launch {
        while (true) {
            with(Dispatchers.Default) {
                delay(ONE_SECOND_IN_MS)
            }
            tick()
        }
    }

    private fun clear() {
        state.job?.cancel()
    }
}

data class State(val callTime: Long, val call: Call, val job: Job?)
sealed class Action {
    class CallAction(val call: Call): Action()
    object TickAction: Action()
}
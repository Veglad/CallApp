package com.veglad.callapp.telephony

import com.veglad.callapp.data_driven.Command
import com.veglad.callapp.data_driven.DataDrivenActivity
import com.veglad.callapp.db.Repository
import com.veglad.callapp.helpers.startCoroutineTimer
import com.veglad.callapp.view.Call
import com.veglad.callapp.view.Call.Status.*
import com.veglad.callapp.view.IncomingCallActivity.Props
import com.veglad.callapp.view.Person
import kotlinx.coroutines.*
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

    private var state = State(0, Call(DISCONNECTED, ""))

    init {
        callManager.setOnUpdateCall(this::handleCall)
        callManager.call?.let { handleCall(it) }
    }

    private fun handleCall(call: Call) {
        Timber.tag("IncomingCallController").d("Updated")
        timerMiddleware(call)
        dispatch(Action.CallAction(call))
    }


    private var job: Job? = null

    private fun timerMiddleware(call: Call) {
        when(call.status) {
            ACTIVE -> {
                if (this.job == null) {
                    this.job = startCoroutineTimer(ONE_SECOND_IN_MS, ONE_SECOND_IN_MS) {
                        Timber.tag("IncomingCallController").d("Tick")
                        dispatch(Action.TickAction)
                    }
                }
            }
            DISCONNECTED -> {
                this.job?.cancel()
                this.job = null
            }
            else -> {}
        }
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
            CONNECTING -> Props.Connecting(Command { callManager.cancelCall() })
            DIALING -> Props.Dialing(getCallee(state.call.callee), Command { callManager.cancelCall() })
            RINGING -> Props.Ringing(getCallee(state.call.callee), Command { callManager.acceptCall() }, Command { callManager.cancelCall() })
            ACTIVE -> Props.Active(getCallee(state.call.callee).name, state.callTime, Command { callManager.cancelCall() })
            DISCONNECTED -> Props.Disconnected
            UNKNOWN -> Props.Unknown(Command { callManager.cancelCall() })
        }
    }

    private fun getCallee(phoneNumber: String?): Person {
        if (phoneNumber == null) return Person("Unknown")
        val person = Repository.getPersonByPhoneNumber(phoneNumber) ?: return Person("Unknown")

        return Person(person.name ?: "Unknown", person.description)
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
                state.copy(call = call)
            }
            DISCONNECTED -> {
                state.copy(callTime = 0, call = call)
            }
            UNKNOWN -> {
                state.copy(call = call)
            }
        }
    }

    private fun reduceCount(state: State): State {
        return state.copy(callTime = state.callTime + 1)
    }
}

data class State(val callTime: Long, val call: Call)
sealed class Action {
    class CallAction(val call: Call): Action()
    object TickAction: Action()
}
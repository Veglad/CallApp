package com.veglad.callapp.view

sealed class Props {
    object Connecting
    class Dialing(
        val callee: String,
        val hangUp: Command?
    )
    class Ringing(
        val callee: String,
        val answer: Command?,
        val reject: Command?
    )
    class Active(
        val callee: String,
        val time: Long,
        val hangUp: Command?
    )
    object Disconnected
    object Unknown
}
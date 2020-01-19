package com.veglad.callapp.data_driven

import androidx.lifecycle.MutableLiveData

interface DataDriven<Props> {
    val props: MutableLiveData<Props>
}

fun <Props> DataDriven<Props>.asPropsConsumer() = { props: Props ->
    this.props.value = props

    if (this.props.hasObservers()) BackPressure.READY else BackPressure.NEVER
}

enum class BackPressure {
    READY, NEVER
}
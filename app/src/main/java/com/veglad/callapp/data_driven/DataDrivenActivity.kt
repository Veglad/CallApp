package com.veglad.callapp.data_driven

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

abstract class DataDrivenActivity<Props> : AppCompatActivity(),
    DataDriven<Props> {
    final override val props = MutableLiveData<Props>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        props.observe(this, Observer {
            renderProps(it)
        })
    }

    abstract fun renderProps(props: Props)
}
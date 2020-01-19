package com.veglad.callapp.data_driven

import androidx.appcompat.app.AppCompatActivity

interface Configurator {
    fun configure(activity: AppCompatActivity): Boolean
}

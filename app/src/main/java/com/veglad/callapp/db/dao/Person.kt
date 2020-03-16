package com.veglad.callapp.db.dao

import io.realm.annotations.PrimaryKey
import io.realm.RealmObject


open class Person() : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var phoneNumber: String = ""
    var name: String? = null
    var description: String? = null

    constructor(id: Long, phoneNumber: String, name: String?, description: String?)
            : this() {
        this.id = id
        this.phoneNumber = phoneNumber
        this.name = name
        this.description = description
    }
}
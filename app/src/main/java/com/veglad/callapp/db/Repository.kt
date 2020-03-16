package com.veglad.callapp.db

import com.veglad.callapp.db.dao.Person
import io.realm.Realm

object Repository {
    fun initDb() {
        val realm = Realm.getDefaultInstance()
        if (getPersonById(1L) == null) {
            realm.beginTransaction()
            val person = Person(1, "+380660842540", "Satya Nadella", "Microsoft CEO")
            realm.copyToRealm(person)
            realm.commitTransaction()
        }
    }

    fun getPersonByPhoneNumber(phoneNumber: String): Person? {
        val realm = Realm.getDefaultInstance()
        return realm.where(Person::class.java).equalTo("phoneNumber", phoneNumber).findFirst()
    }

    fun getPersonById(id: Long): Person? {
        val realm = Realm.getDefaultInstance()
        return realm.where(Person::class.java).equalTo("id", 1L).findFirst()
    }
}
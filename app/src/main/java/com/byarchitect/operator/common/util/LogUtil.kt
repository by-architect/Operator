package com.byarchitect.operator.common.util

import android.content.Context
import android.util.Log
import android.widget.Toast

fun Context.test(data: Any?) {
    Toast.makeText(this, "TEST--> ${data.toString()}", Toast.LENGTH_LONG).show()
    Log.e("TEST", data.toString())
}

fun Context.test(title: String, data: Any?) {
    Toast.makeText(this, "TEST--> $title :: ${data.toString()}", Toast.LENGTH_LONG).show()
    Log.e("TEST", "$title --> ${data.toString()}")
}

fun logTest(data: Any?) {
    Log.e("TEST", data.toString())
}

var test: Any?
    get() {
        return "test"
    }
    set(value) = logTest(value)

fun logTest(title: String, data: Any?) {
    Log.e(title, "$title --> ${data.toString()}")
}

fun addLog(title: String, data: Any?, description: String = "", location: String = "") {
    if (description.isNotEmpty())
        Log.e(title, "Description --> $description")
    Log.e(title, "Data: --> ${data.toString()} ")
    if (location.isNotEmpty())
        Log.e(title, "Location --> $location")
}

fun Context.mes(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
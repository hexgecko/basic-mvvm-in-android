package com.example.mvvm.basic.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel: ViewModel() {

    val staticData = "This is a static data!"

    val changeableData = MutableLiveData<String>().apply { value = "This is a changeable data!" }

    fun onUpdateChangeableData(newData: String) {
        changeableData.value = newData
    }

    val colorAsString = MutableLiveData<String>().apply { value = "Red" }

    val colorAsHex = Transformations.map(colorAsString) { color ->
        when(color.toLowerCase(Locale.getDefault())) {
            "red"     -> 0xFFFF0000
            "blue"    -> 0xFF0000FF
            "green"   -> 0xFF00FF00
            "yellow"  -> 0xFFFFFF00
            "magenta" -> 0xFFFF00FF
            "cyan"    -> 0xFF00FFFF
            "white"   -> 0xFFFFFFFF
            "black"   -> 0xFF000000
            else      -> 0xFF888888
        }
    }

    val augend = MutableLiveData<String>().apply { value = "1" }

    val addend = MutableLiveData<String>().apply { value = "1" }

    val sumOfTotal = MediatorLiveData<String>().apply {
        addSource(augend) {
            val a = if(it.isNotEmpty()) it.toInt() else 0
            val b = if(addend.value != null && addend.value!!.isNotEmpty()) addend.value!!.toInt() else 0
            value = (a + b).toString()
        }
        addSource(addend) {
            val a = if(augend.value != null && addend.value!!.isNotEmpty()) augend.value!!.toInt() else 0
            val b = if(it.isNotEmpty()) it.toInt() else 0
            value = (a + b).toString()
        }
    }
}

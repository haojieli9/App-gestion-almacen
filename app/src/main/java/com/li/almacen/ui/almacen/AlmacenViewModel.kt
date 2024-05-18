package com.li.almacen.ui.almacen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlmacenViewModel : ViewModel() {
    private val _almacenData = MutableLiveData<String>()
    val almacenData: LiveData<String> = _almacenData

    fun updateAlmacenData(newData: String) {
        _almacenData.value = newData
    }


}

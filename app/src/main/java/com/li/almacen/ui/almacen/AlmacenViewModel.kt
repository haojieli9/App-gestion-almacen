package com.li.almacen.ui.almacen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.li.almacen.data.AlmacenData

class AlmacenViewModel : ViewModel() {
    private val _almacenList = MutableLiveData<MutableList<AlmacenData>>()
    val almacenList: LiveData<MutableList<AlmacenData>> get() = _almacenList

    init {
        _almacenList.value = mutableListOf()
    }

    fun addAlmacen(almacen: AlmacenData) {
        _almacenList.value?.add(almacen)
        _almacenList.value = _almacenList.value // This triggers observers
    }

    fun setAlmacenList(list: MutableList<AlmacenData>) {
        _almacenList.value = list
    }
}

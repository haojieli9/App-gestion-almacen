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

    fun updateAlmacen(almacenActualizado: AlmacenData) {
        val listaAlmacenes = _almacenList.value
        listaAlmacenes?.let { almacenes ->
            // Busca el índice del almacén a actualizar en la lista
            val index = almacenes.indexOfFirst { it.id == almacenActualizado.id }
            if (index != -1) {
                // Reemplaza el almacén existente con el almacén actualizado
                almacenes[index] = almacenActualizado
                _almacenList.value = almacenes // Esto activará los observadores
            }
        }
    }
}

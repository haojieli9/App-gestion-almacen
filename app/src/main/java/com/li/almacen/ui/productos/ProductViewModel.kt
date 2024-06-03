package com.li.almacen.ui.productos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.li.almacen.data.ProductData

class ProductViewModel : ViewModel() {
    private val _productList = MutableLiveData<MutableList<ProductData>>()
    val productList: LiveData<MutableList<ProductData>> get() = _productList

    init {
        _productList.value = mutableListOf()
    }

    fun addProduct(almacen: ProductData) {
        _productList.value?.add(almacen)
        _productList.value = _productList.value
    }

    fun setProductList(list: MutableList<ProductData>) {
        _productList.value = list
    }
}
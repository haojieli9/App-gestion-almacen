package com.li.almacen.ui.productos

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.li.almacen.data.ProductData
import com.li.almacen.ui.productos.details.DetailProduct

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

    fun updateProducto(productoActualizado: ProductData) {
        val listaProductos = _productList.value
        listaProductos?.let { productos ->
            // Busca el índice del producto a actualizar en la lista
            val index = productos.indexOfFirst { it.id == productoActualizado.id }
            if (index != -1) {
                // Reemplaza el producto existente con el producto actualizado
                productos[index] = productoActualizado
                _productList.value = productos // Esto activará los observadores
            }
        }
    }

    fun isRepeatBarcode(barcode: String): ProductData? {
        return _productList.value?.find { it.barcode == barcode }
    }

}
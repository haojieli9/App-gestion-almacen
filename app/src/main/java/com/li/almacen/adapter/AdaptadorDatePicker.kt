package com.li.almacen.adapter

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.li.almacen.R
import java.util.Calendar

class DataPickerFragment (val listener : (day:Int, month:Int, year:Int) -> Unit) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view : DatePicker?, year : Int, month : Int, dayOfMonth : Int) {
        listener(dayOfMonth, month, year)
    }

    override fun onCreateDialog(savedInstanceState : Bundle?) : Dialog {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val picker = DatePickerDialog(requireContext(),this@DataPickerFragment, year, month, day)
        picker.datePicker.minDate = calendar.timeInMillis
        return picker
    }
}
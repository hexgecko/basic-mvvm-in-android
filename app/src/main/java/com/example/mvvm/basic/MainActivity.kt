package com.example.mvvm.basic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvm.basic.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the reference to the ViewModel.
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // Example of binding static data.
        bindStaticData(R.id.staticData, viewModel.staticData)

        // Example of binding changeable data.
        bindChangeableData(R.id.changeableData, viewModel.changeableData)

        // Example of update changeable data.
        bindClickUpdateData(R.id.leftButton, "Left button clicked!", viewModel::onUpdateChangeableData)
        bindClickUpdateData(R.id.rightButton, "Right button clicked!", viewModel::onUpdateChangeableData)

        // Example of two-way binding changeable data.
        bindTwoWay(R.id.twoWayBinding, viewModel.changeableData)

        // Example of transformation, transform the color string to a color number
        bindTwoWay(R.id.colorAsString, viewModel.colorAsString)
        bindColorView(R.id.colorAsHex, viewModel.colorAsHex)

        // Example of mediate two changeable data, take the sum of two integer.
        bindTwoWay(R.id.augend, viewModel.augend)
        bindTwoWay(R.id.addend, viewModel.addend)
        bindChangeableData(R.id.sumOfTotal, viewModel.sumOfTotal)

    }

    private fun bindStaticData(viewId: Int, staticData: String) {
        findViewById<TextView>(viewId).text = staticData
    }

    private fun bindChangeableData(viewId: Int, changeableData: LiveData<String>) {
        val textView = findViewById<TextView>(viewId)
        changeableData.observe(this, Observer<String> { textView.text = it })
    }

    private fun bindClickUpdateData(viewId: Int, newData: String, callback: (String) -> Unit) {
        findViewById<Button>(viewId).setOnClickListener { callback(newData) }
    }

    private fun bindTwoWay(viewId: Int, changeableData: MutableLiveData<String>) {
        val editText = findViewById<EditText>(viewId)

        // Observe changes to the changeable data and update the input text.
        changeableData.observe(this, Observer<CharSequence> {
            // It's imported to check that the text has changed to avoid infinitive loop.
            if(haveContentsChanged(it, editText.text)) {
                editText.setText(it)
            }
        })

        // Check if the text has changed and update the changeable data if it has
        editText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                changeableData.value = text.toString()
            }
        })
    }

    private fun bindColorView(viewId: Int, colorAsHex: LiveData<Long>) {
        val view = findViewById<View>(viewId)

        colorAsHex.observe(this, Observer {
            view.setBackgroundColor(it.toInt())
        })
    }

    private fun haveContentsChanged(str1: CharSequence?, str2: CharSequence?): Boolean {

        if (str1 == null != (str2 == null)) {
            return true
        } else if (str1 == null) {
            return false
        }

        val length = str1.length
        if(length != str2!!.length) {
            return true
        }

        for(i in 0 until length) {
            if(str1[i] != str2[i]) {
                return true
            }
        }

        return false
    }
}

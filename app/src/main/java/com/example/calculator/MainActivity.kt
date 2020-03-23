package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.* // sprawia, że zmienne są zadeklarowane na poziomie layout i nie
//trzeba ponownie deklarować
import kotlin.math.sqrt

private const val STATE_PENDING_OPERATION = "PandingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"

class MainActivity : AppCompatActivity() {
    // private lateinit var result: EditText
    // private lateinit var newNumber: EditText
    // private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }
// ww lateinit i lazy pozwala niedeklarować zmiennej od razu tylko dopiero przy pierwszym użyciu

    private var operand1: Double? = null // tu jest null żeby było wiadomo czy coś zostało wpisane
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //result = findViewById(R.id.result)
        //newNumber = findViewById(R.id.newNumber)
        // buttons value

        //val button0: Button = findViewById(R.id.button0)// tak można zdeklarować rodzaj widget
        //val button1: Button = findViewById(R.id.button1)
        //val button2: Button = findViewById(R.id.button2)
        //val button3: Button = findViewById(R.id.button3)
        //val button4: Button = findViewById(R.id.button4)
        //val button5: Button = findViewById(R.id.button5)
        //val button6: Button = findViewById(R.id.button6)
        //val button7: Button = findViewById(R.id.button7)
        //val button8: Button = findViewById(R.id.button8)
        //val button9: Button = findViewById(R.id.button9)
        //val buttonDot: Button = findViewById(R.id.buttonDot)

        //operation buttons
        //val buttonDivide = findViewById<Button>(R.id.buttonDivide)// albo tak, na jedno wychodzi
        //val buttonMultiply = findViewById<Button>(R.id.buttonMultiply)
        //val buttonPlus = findViewById<Button>(R.id.buttonPlus)
        //val buttonMinus = findViewById<Button>(R.id.buttonMinus)
        //val buttonEquals = findViewById<Button>(R.id.buttonEquals)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }
            pendingOperation = op
            operation.text = pendingOperation
        }

        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)

        buttonNeg.setOnClickListener {
            val value = newNumber.text.toString()
            if (value.isEmpty()) {
                newNumber.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    newNumber.setText("")
                }
            }
        }

        buttonSqrt.setOnClickListener {
            val value = newNumber.text.toString()
            try {
                var doubleValue = value.toDouble()
                doubleValue = sqrt(doubleValue)
                newNumber.setText(doubleValue.toString())
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }
        }

        buttonBackSpace.setOnClickListener {
            var value = newNumber.text.toString()
            try {
                if (value.length > 1) {
                    value = value.substring(0, value.length - 1)
                    newNumber.setText(value)
                } else {
                    newNumber.setText("")
                }
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }
        }

        buttonC.setOnClickListener {
            result.setText("")
            operand1 = null
        }
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)) {
            savedInstanceState.getDouble(STATE_OPERAND1)
        } else {
            null
        }

        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION)
        operation.text = pendingOperation
    }
}

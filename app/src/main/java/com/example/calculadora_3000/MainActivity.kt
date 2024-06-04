package com.example.calculadora_3000

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private var lastNumeric: Boolean = false
    private var lastDot: Boolean = false
    private var stateError: Boolean = false
    private var lastOperator: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)

        // Number buttons
        val buttons = listOf<Button>(
            findViewById(R.id.btn0),
            findViewById(R.id.btn1),
            findViewById(R.id.btn2),
            findViewById(R.id.btn3),
            findViewById(R.id.btn4),
            findViewById(R.id.btn5),
            findViewById(R.id.btn6),
            findViewById(R.id.btn7),
            findViewById(R.id.btn8),
            findViewById(R.id.btn9)
        )
        buttons.forEach { button ->
            button.setOnClickListener { onDigit(button) }
        }

        // Operation buttons
        findViewById<Button>(R.id.btnPlus).setOnClickListener { onOperator("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { onOperator("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { onOperator("*") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { onOperator("/") }

        findViewById<Button>(R.id.btnDot).setOnClickListener { onDecimalPoint() }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { onEqual() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { onClear() }
        findViewById<Button>(R.id.btnparentesis).setOnClickListener { onParenthesis() }
        findViewById<Button>(R.id.btnPorcentaje).setOnClickListener { onPercentage() }
        findViewById<Button>(R.id.btnNeSu).setOnClickListener { onToggleSign() }

    }

    private fun onDigit(view: Button) {
        if (stateError) {
            tvResult.text = view.text
            stateError = false
        } else {
            tvResult.append(view.text)
        }
        lastNumeric = true
    }

    private fun onOperator(operator: String) {
        if (lastNumeric && !stateError) {
            tvResult.append(operator)
            lastNumeric = false
            lastDot = false
            lastOperator = operator
        }
    }

    private fun onDecimalPoint() {
        if (lastNumeric && !stateError && !lastDot) {
            tvResult.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    private fun onEqual() {
        if (lastNumeric && !stateError) {
            val txt = tvResult.text.toString()
            try {
                val result = evaluate(txt)
                tvResult.text = result.toString()
                lastDot = true
            } catch (e: Exception) {
                tvResult.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }

    private fun onClear() {
        tvResult.text = ""
        lastNumeric = false
        lastDot = false
        stateError = false
    }
    private fun onParenthesis() {
        val text = tvResult.text.toString()
        if (text.isEmpty() || text.last() == '(' || text.last() == '+' || text.last() == '-' || text.last() == '*' || text.last() == '/') {
            tvResult.append("(")
        } else if (text.count { it == '(' } > text.count { it == ')' }) {
            tvResult.append(")")
        } else {
            tvResult.append("(")
        }
    }
    private fun onPercentage() {
        if (lastNumeric && !stateError) {
            val text = tvResult.text.toString()
            val expression = ExpressionBuilder(text).build()
            val result = expression.evaluate() / 100
            tvResult.text = result.toString()
        }
    }


    private fun onToggleSign() {
        if (!stateError) {
            val text = tvResult.text.toString()
            if (text.isNotEmpty()) {
                val value = text.toDouble()
                val result = value * -1
                tvResult.text = result.toString()
            }
        }
    }
    private fun evaluate(expression: String): Double {
        val expr = ExpressionBuilder(expression).build()
        return expr.evaluate()
    }
}

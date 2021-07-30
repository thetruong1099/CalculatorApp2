package com.example.calculatorapp2.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.calculatorapp2.R
import kotlinx.android.synthetic.main.fragment_calculator.*


class CalculatorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allClearAction()

        backSpaceAction()

        initListenerButtonNumber()

        initListenerButtonOperation()

        equalsAction()
    }

    private fun allClearAction() {
        btn_ac.setOnClickListener {
            tv_expression.text = ""
            tv_result.text = ""
        }
    }

    private fun backSpaceAction() {
        btn_clear.setOnClickListener {
            deleteLastChar()
        }
    }

    private fun initListenerButtonNumber() {
        btn_dec.setOnClickListener { numberAction(".") }
        btn_0.setOnClickListener { numberAction("0") }
        btn_1.setOnClickListener { numberAction("1") }
        btn_2.setOnClickListener { numberAction("2") }
        btn_3.setOnClickListener { numberAction("3") }
        btn_4.setOnClickListener { numberAction("4") }
        btn_5.setOnClickListener { numberAction("5") }
        btn_6.setOnClickListener { numberAction("6") }
        btn_7.setOnClickListener { numberAction("7") }
        btn_8.setOnClickListener { numberAction("8") }
        btn_9.setOnClickListener { numberAction("9") }
    }

    private fun numberAction(string: String) {
        tv_expression.append(string)
    }

    private fun initListenerButtonOperation() {
        btn_sum.setOnClickListener { operationAction("+") }
        btn_sub.setOnClickListener { operationAction("-") }
        btn_mul.setOnClickListener { operationAction("x") }
        btn_div.setOnClickListener { operationAction("/") }
    }

    private fun operationAction(string: String) {
        val length = tv_expression.length()

        if (length > 0) {
            val lastChar = tv_expression.text.last()
            if (lastChar == '+' || lastChar == '-' || lastChar == 'x' || lastChar == '/') {
                deleteLastChar()
                tv_expression.append(string)
            } else tv_expression.append(string)
        }
    }

    private fun deleteLastChar() {
        val length = tv_expression.length()
        if (length > 0) {
            tv_expression.text = tv_expression.text.subSequence(0, length - 1)
        }
    }

    private fun equalsAction() {
        btn_result.setOnClickListener {
            if (tv_expression.text.toString() != ".") {
                tv_result.text = calculateResults()
            } else {
                tv_result.text = "0"
            }
        }
    }

    private fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/')) {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when (operator) {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if (i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in tv_expression.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }
}
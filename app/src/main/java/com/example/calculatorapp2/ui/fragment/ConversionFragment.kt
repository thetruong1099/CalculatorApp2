package com.example.calculatorapp2.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.calculatorapp2.R
import kotlinx.android.synthetic.main.fragment_conversion.btn_0
import kotlinx.android.synthetic.main.fragment_conversion.btn_1
import kotlinx.android.synthetic.main.fragment_conversion.btn_2
import kotlinx.android.synthetic.main.fragment_conversion.btn_3
import kotlinx.android.synthetic.main.fragment_conversion.btn_4
import kotlinx.android.synthetic.main.fragment_conversion.btn_5
import kotlinx.android.synthetic.main.fragment_conversion.btn_6
import kotlinx.android.synthetic.main.fragment_conversion.btn_7
import kotlinx.android.synthetic.main.fragment_conversion.btn_8
import kotlinx.android.synthetic.main.fragment_conversion.btn_9
import kotlinx.android.synthetic.main.fragment_conversion.btn_ac
import kotlinx.android.synthetic.main.fragment_conversion.btn_dec
import kotlinx.android.synthetic.main.fragment_conversion.btn_del
import kotlinx.android.synthetic.main.fragment_conversion.btn_go
import kotlinx.android.synthetic.main.fragment_conversion.cv_result
import kotlinx.android.synthetic.main.fragment_conversion.ll_input
import kotlinx.android.synthetic.main.fragment_conversion.tv_height
import kotlinx.android.synthetic.main.fragment_conversion.tv_result
import kotlinx.android.synthetic.main.fragment_conversion.tv_state
import kotlinx.android.synthetic.main.fragment_conversion.tv_weight


class ConversionFragment : Fragment() {

    private var statePositionTextView: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStatePositionTextView()

        allClearAction()

        backSpaceAction()

        initListenerButtonNumber()

        initListenerBtnGo()
    }

    @SuppressLint("ResourceAsColor")
    private fun setStatePositionTextView() {
        tv_weight.setOnClickListener {
            statePositionTextView = true
            cv_result.visibility = View.GONE
            ll_input.visibility = View.VISIBLE
            tv_height.setTextColor(Color.BLACK)
            tv_weight.setTextColor(R.color.carrot)
        }
        tv_height.setOnClickListener {
            statePositionTextView = false
            cv_result.visibility = View.GONE
            ll_input.visibility = View.VISIBLE
            tv_weight.setTextColor(Color.BLACK)
            tv_height.setTextColor(R.color.carrot)
        }
    }

    private fun allClearAction() {
        btn_ac.setOnClickListener {
            if (statePositionTextView) {
                tv_weight.text = ""
            } else {
                tv_height.text = ""
            }
        }
    }

    private fun backSpaceAction() {
        btn_del.setOnClickListener {
            deleteLastChar()
        }
    }

    private fun deleteLastChar() {
        if (statePositionTextView) {
            val length = tv_weight.length()
            if (length > 0) {
                tv_weight.text = tv_weight.text.subSequence(0, length - 1)
            }
        } else {
            val length = tv_height.length()
            if (length > 0) {
                tv_height.text = tv_height.text.subSequence(0, length - 1)
            }
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
        if (statePositionTextView) {
            tv_weight.append(string)
        } else {
            tv_height.append(string)
        }
    }

    private fun initListenerBtnGo() {
        btn_go.setOnClickListener {

            val lengthWeight = tv_weight.length()
            val lengthHeight = tv_height.length()

            val weight = tv_weight.text.toString()
            val height = tv_height.text.toString()

            if (lengthWeight > 0 && lengthHeight > 0) {
                val numWeight = weight.toFloat()
                val numHeight = height.toFloat()
                if (numHeight < 250 && numWeight < 200) {

                    cv_result.visibility = View.VISIBLE
                    ll_input.visibility = View.GONE

                    val result = bmi(numWeight, numHeight)
                    tv_result.text = result.toString()

                    when {
                        result in 0.0..18.5 -> {
                            tv_state.text = "Gầy"
                        }
                        result in 18.5..24.9 -> {
                            tv_state.text = "Bình thường"
                        }
                        result in 25.0..29.9 -> {
                            tv_state.text = "Tăng cân"
                        }
                        result in 30.0..34.9 -> {
                            tv_state.text = "Béo phì độ 1"
                        }
                        result in 35.0..39.9 -> {
                            tv_state.text = "Béo phì độ 2"
                        }
                        result >= 40.0 -> {
                            tv_state.text = "Béo phì độ 3"
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Chỉ số BMI không đúng", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Chỉ số BMI không đúng", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bmi(weight: Float, height: Float): Float {
        return weight / (height / 100 * 2)
    }
}
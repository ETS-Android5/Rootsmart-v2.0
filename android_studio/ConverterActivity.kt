package edu.singaporetech.travelapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity that displays UI to convert currency
 */
class CurrencyConverterActivity : AppCompatActivity() {

    val TAG: String = "CurrencyConverterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)

        // TODO findviewbyid for the UI elements

        // TODO set onClickListeners to all the buttons here
        //  or declare the onClick method within the layout XML?

    }

    /**
     * Formula to calculate the destination currency
     * @param value
     * @param exchangeRate
     * @return
     */
    private fun calculateRate(value: Float, exchangeRate: Float): Float {

        // TODO What's the formula you need?
        return 0.0F
    }

}
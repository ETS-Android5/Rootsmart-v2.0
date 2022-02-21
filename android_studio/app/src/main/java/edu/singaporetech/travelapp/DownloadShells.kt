package edu.singaporetech.travelapp

import Temproot
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class DownloadShells : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getshell)

        val downloadShells_button: Button = findViewById(R.id.btn_downloadShells)

        downloadShells_button.setOnClickListener {
            main()
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun main() {
//        Temproot.main()
        val tr = Temproot(this) // Init temproot object, parse context as argument
    }
}
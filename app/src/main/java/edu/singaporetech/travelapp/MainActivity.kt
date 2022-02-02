package edu.singaporetech.travelapp
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

/**
 * Lab 02: Travel App
 * Main Screen
 *
 * 2020-01-27: port to kotlin (jeannie)
 */
class MainActivity : AppCompatActivity() {
    // val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check permisisons
        Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    val traceTogether_button: Button = findViewById(R.id.tracetogether_button)
                    traceTogether_button.isEnabled = true
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                    Toast.makeText(this@MainActivity, "You must accept permission for the application to function", Toast.LENGTH_SHORT).show()
                }
            }).check()

        // TODO findviewbyid for the UI elements
        val encrypt_button: Button = findViewById(R.id.encrypt_button)

        // TODO set onClickListeners to all the buttons here
        //  or declare the onclick method within the layout XML?
        // Doing it via onClickListeners is a method called fragments


        encrypt_button.setOnClickListener {
            val intent = Intent(this, EncryptFile::class.java)
            startActivity(intent)
            setContentView(R.layout.activity_encrypt)
        }
    }

}
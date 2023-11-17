package com.amaurypm.ireaderqr

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.amaurypm.ireaderqr.databinding.ActivityQrscannerBinding
import java.net.MalformedURLException
import java.net.URL

class QRScanner : AppCompatActivity() {

    private lateinit var binding: ActivityQrscannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrscannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cbvScanner.decodeContinuous { result ->
            //Toast.makeText(this, "Resultado: $result", Toast.LENGTH_LONG).show()

            binding.cbvScanner.pause()

            try{
                val url = URL(result.text)
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(result.text)
                startActivity(i)
                finish()
            }catch(e: MalformedURLException){
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("El código QR no es válido para la aplicación")
                    .setNeutralButton("Aceptar"){ dialog, _ ->
                        dialog.dismiss()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .create()
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.cbvScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.cbvScanner.pause()
    }


}
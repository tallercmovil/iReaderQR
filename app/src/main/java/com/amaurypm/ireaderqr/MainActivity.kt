package com.amaurypm.ireaderqr

import android.Manifest
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.amaurypm.ireaderqr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var cameraPermissionGranted = false

    private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted){
            //El usuario concedió el permiso
            actionPermissionGranted()
        }else{
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                AlertDialog.Builder(this)
                    .setTitle("Permiso requerido")
                    .setMessage("Se necesita el permiso solamente para leer los códigos QR")
                    .setPositiveButton("Aceptar"){ _, _ ->
                        updateOrRequestPermissions()
                    }
                    .setNegativeButton("Salir"){ dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    .create()
                    .show()
            }else{
                AlertDialog.Builder(this)
                    .setTitle("Permiso negado permanentemente")
                    .setMessage("Para activar el permiso debe ir a la configuración de la aplicación y concederlo")
                    .setPositiveButton("Ir a configuración"){ _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    .create()
                    .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivCamara.setOnClickListener {
            //Mando el intent hacia el activity del lector de QR's
            //Hay que verificar si tenemos el permiso
            updateOrRequestPermissions()
        }

    }

    private fun updateOrRequestPermissions() {
        cameraPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if(!cameraPermissionGranted){
            //Pido el permiso
            permissionsLauncher.launch(Manifest.permission.CAMERA)
        }else{
            //Tenemos el permiso
            actionPermissionGranted()
        }
    }

    private fun actionPermissionGranted(){
        startActivity(Intent(this, QRScanner::class.java))
    }


}
package com.diego.calculadoraneto

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultadoActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resultado)
        val calculo = findViewById<TextView>(R.id.resultado)
        val salarioNeto : Double = intent.extras?.getDouble("EXTRA_SALARIO_NETO") ?: 0.0
        calculo.text = "$salarioNeto"

        val btnRegreso = findViewById<Button>(R.id.btnRegreso)
        btnRegreso.setOnClickListener {
            val intentRegreso = Intent(this , CalculadoraActivity::class.java)
            startActivity(intentRegreso)
            Log.i("regreso","Se regresa a la calculadora")
        }
    }
}
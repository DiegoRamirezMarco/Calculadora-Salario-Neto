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
        val calculo = findViewById<TextView>(R.id.resultadoSalarioTotal)
        val resultadoSalarioBruto = findViewById<TextView>(R.id.resultadoSalarioBruto)
        val resultadoPagas = findViewById<TextView>(R.id.resultadoPagas)
        val resultadoEdad = findViewById<TextView>(R.id.resultadoEdad)
        val resultadoGrupoProfesional = findViewById<TextView>(R.id.resultadoGrupoProfesional)
        val resultadoEstadoCivil = findViewById<TextView>(R.id.resultadoEstadoCivil)
        val resultadoHijos = findViewById<TextView>(R.id.resultadoHijos)
        val resultadoDiscapacidad = findViewById<TextView>(R.id.resultadoDiscapacidad)
        val resultadoirpf = findViewById<TextView>(R.id.resultadoirpf)

        val salarioNeto : Double = intent.extras?.getDouble("EXTRA_SALARIO_NETO") ?: 0.0
        calculo.text = "Salario Neto = $salarioNeto €"
        val salarioBruto : Double = intent.extras?.getDouble("EXTRA_SALARIO_BRUTO") ?: 0.0
        resultadoSalarioBruto.text = "Salario Bruto = $salarioBruto €"
        val irpf : Double = intent.extras?.getDouble("EXTRA_IRPF") ?: 0.0
        resultadoirpf.text = "IRPF = $irpf €"
        val pagas : Double = intent.extras?.getDouble("EXTRA_SALARIO_PAGAS") ?: 0.0
        resultadoPagas.text = "Pagas = $pagas €"
        val edad : Double = intent.extras?.getDouble("EXTRA_AJUSTE_EDAD") ?: 0.0
        resultadoEdad.text = "Edad = $edad €"
        val grupoProfesional : Double = intent.extras?.getDouble("EXTRA_RETENCION_GRUPO") ?: 0.0
        resultadoGrupoProfesional.text = "Grupo Profesional = $grupoProfesional €"
        val estadoCivil : Double = intent.extras?.getDouble("EXTRA_ESTADO_CIVIL") ?: 0.0
        resultadoEstadoCivil.text = "Estado Civil = $estadoCivil €"
        val hijos : Double = intent.extras?.getDouble("EXTRA_DEDUCCION_HIJOS") ?: 0.0
        resultadoHijos.text = "Hijos = $hijos €"
        val discapacidad : Double = intent.extras?.getDouble("EXTRA_DEDUCCION_DISCAPACIDAD") ?: 0.0
        resultadoDiscapacidad.text = "Discapacidad = $discapacidad €"

        val btnRegreso = findViewById<Button>(R.id.btnRegreso)
        btnRegreso.setOnClickListener {
            val intentRegreso = Intent(this , CalculadoraActivity::class.java)
            startActivity(intentRegreso)
            Log.i("regreso","Se regresa a la calculadora")
        }
    }
}
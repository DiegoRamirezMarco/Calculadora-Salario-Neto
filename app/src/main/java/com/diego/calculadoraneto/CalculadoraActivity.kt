package com.diego.calculadoraneto

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CalculadoraActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculadora)

        //Inicializo los EditText
        val editTextSalario = findViewById<EditText>(R.id.editTextSalario)
        val editTextPagas = findViewById<EditText>(R.id.editTextPagas)
        val editTextEdad = findViewById<EditText>(R.id.editTextEdad)

        val spinnerGrupo:Spinner = findViewById(R.id.spinnerGrupo)
        ArrayAdapter.createFromResource(this,R.array.grupoProfesionalArray,android.R.layout.simple_spinner_item).also{
            adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGrupo.adapter = adapter}

        val editTextDiscapacidad = findViewById<EditText>(R.id.editTextDiscapacidad)

        val spinnerEstado = findViewById<Spinner>(R.id.spinnerEstado)
        ArrayAdapter.createFromResource(this,R.array.estadoCivil,android.R.layout.simple_spinner_item).also{
                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerEstado.adapter = adapter}

        val editTextHijos = findViewById<EditText>(R.id.editTextHijos)

        //Inicializo el Boton
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        btnCalcular.setOnClickListener {
            //Recogemos los datos de cada uno de los EditText de arriba y los pasamos a su valor logico
            val salarioBruto = editTextSalario.text.toString().toDoubleOrNull() ?: 0.0
            val pagas = editTextPagas.text.toString().toIntOrNull() ?: 12
            val edad = editTextEdad.text.toString().toIntOrNull() ?: 0
            val discapacidad = editTextDiscapacidad.text.toString().toIntOrNull() ?: 0
            val hijos = editTextHijos.text.toString().toIntOrNull() ?: 0
            val grupo = spinnerGrupo.selectedItem.toString()
            val estado = spinnerEstado.selectedItem.toString()
            Log.i("bonton","$salarioBruto, $pagas ,$edad ,$discapacidad ,$hijos ,$grupo ,$estado")

            // OPERACIONES ---------------------------------------
            val irpf = redondear(calcularIRPF(salarioBruto))
            val salarioPagas = redondear(calculoPagas(salarioBruto,pagas))
            val ajusteEdad = redondear(calculoEdad(edad, salarioBruto))
            val bonificacionHijos = redondear(calculoHijos(hijos))
            val bonificacionDiscapacidad = redondear(calculoDiscapacidad(discapacidad))
            val retencionGrupo = redondear(calculoGrupo(grupo,salarioBruto))
            val ajusteEstado = redondear(calculoEstado(estado,salarioBruto))

            val salarioNeto = redondear(calcularSalarioNeto(salarioBruto, irpf, ajusteEdad, bonificacionHijos, bonificacionDiscapacidad, retencionGrupo, ajusteEstado))

            Log.i("bonton","$salarioNeto , $salarioPagas")
            //-------------------------------------------------------------------
            val inten = Intent(this, ResultadoActivity::class.java)
            inten.putExtra("EXTRA_SALARIO_NETO", salarioNeto)
            inten.putExtra("EXTRA_SALARIO_BRUTO", salarioBruto)
            inten.putExtra("EXTRA_IRPF", irpf)
            inten.putExtra("EXTRA_SALARIO_PAGAS", salarioPagas)
            inten.putExtra("EXTRA_AJUSTE_EDAD", ajusteEdad)
            inten.putExtra("EXTRA_DEDUCCION_HIJOS", bonificacionHijos)
            inten.putExtra("EXTRA_DEDUCCION_DISCAPACIDAD", bonificacionDiscapacidad)
            inten.putExtra("EXTRA_RETENCION_GRUPO", retencionGrupo)
            inten.putExtra("EXTRA_ESTADO_CIVIL", ajusteEstado)
            startActivity(inten)

        }
    }

    private fun redondear(valor: Double): Double {
        return String.format("%.2f", valor).toDouble()
    }

    private fun calcularIRPF(salarioBruto: Double): Double {
        return when {
            salarioBruto < 12000 -> salarioBruto * 0.05
            salarioBruto in 12000.0..20000.0 -> salarioBruto * 0.10
            salarioBruto in 20000.0..35000.0 -> salarioBruto * 0.15
            else -> salarioBruto * 0.20
        }
    }

    fun calculoPagas(salarioBruto: Double, pagas: Int): Double {
        return salarioBruto / pagas
    }

    fun calculoEdad(edad: Int, salarioBruto: Double): Double {
        return if (edad >= 65) salarioBruto * 0.01 else 0.0
    }

    fun calculoHijos(hijos: Int): Double {
        if(hijos > 0) {
            return hijos * 200.0
        }else {
            return 0.0
        }
    }

    fun calculoDiscapacidad(discapacidad: Int): Double {
        return when (discapacidad) {
            in 0..33 -> 0.0
            in 34..66 -> 500.0
            in 67..100 -> 1500.0
            else -> 0.0
        }
    }

    fun calculoGrupo(grupo: String, salarioBruto: Double): Double {
        return when (grupo) {
            "Grupo A" -> salarioBruto * 0.003
            "Grupo B" -> salarioBruto * 0.005
            "Grupo C" -> 0.002
            else -> 0.0
        }
    }

    fun calculoEstado(estado: String, salarioBruto: Double): Double {
        return when (estado) {
            "Soltero" -> 0.0
            "Casado" -> salarioBruto * 0.01
            "Divorciado" -> salarioBruto * 0.005
            "Viudo" -> salarioBruto * 0.0075
            else -> 0.0
        }
    }

    private fun calcularSalarioNeto(
        salarioBruto: Double,
        irpf: Double,
        ajusteEdad: Double,
        bonificacionHijos: Double,
        bonificacionDiscapacidad: Double,
        retencionGrupo: Double,
        ajusteEstado: Double
    ): Double {
        val totalDeducciones = irpf + ajusteEdad + retencionGrupo + ajusteEstado
        val totalBonificaciones = bonificacionHijos + bonificacionDiscapacidad
        return salarioBruto - totalDeducciones + totalBonificaciones
    }
}
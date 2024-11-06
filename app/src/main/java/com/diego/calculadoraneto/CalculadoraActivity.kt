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

            val salarioNeto = calcularSalarioNeto(salarioBruto,edad,discapacidad,hijos,grupo,estado)
            val salarioPagas = calculoPagas(salarioBruto,pagas)
            Log.i("bonton","$salarioNeto , $salarioPagas")
            //-------------------------------------------------------------------
            val inten = Intent(this, ResultadoActivity::class.java)
            inten.putExtra("EXTRA_SALARIO_NETO", salarioNeto)
            startActivity(inten)
        }
    }

    private fun calcularSalarioNeto(salarioBruto : Double, edad:Int,discapacidad: Int,hijos: Int,grupo: String,estado: String): Double {
        val retencionGrupo = salarioBruto * calculoGrupo(grupo)
        val ajusteEdad = salarioBruto * calculoEdad(edad)
        val ajusteEstado = salarioBruto * calculoEstado(estado)
        val deduccionHijos = calculoHijos(hijos)
        val deduccionDiscapacidad = calculoDiscapacidad(discapacidad)

        val totalDeducciones = retencionGrupo - ajusteEdad - ajusteEstado - deduccionDiscapacidad - deduccionHijos
        return salarioBruto - totalDeducciones
    }

    fun calculoPagas(salarioBruto : Double ,pagas :Int):Double{
        return salarioBruto/pagas
    }
    fun calculoEdad(edad: Int): Double {
         if(edad >= 65){
             return 0.02
         }else{return 0.0}
    }
    fun calculoHijos(hijos: Int): Double {
        return hijos * 500.0
    }
    fun calculoDiscapacidad(discapacidad : Int) :Double{
        when(discapacidad){
            in 0..33 -> return 0.0
            in 34..66 -> return 1000.0
            in 67..91 -> return 3000.0
            in 91..100 -> return 5000.0
            else -> return 0.0
        }
    }
    fun calculoGrupo(grupo : String) :Double{
        when(grupo){
            "Grupo A" -> return 0.05
            "Grupo B" -> return 0.03
            "Grupo C" -> return 0.00
            else -> return 0.0
        }
    }
    fun calculoEstado(estado : String) :Double{
        when(estado){
            "Soltero" -> return 0.0
            "Casado" -> return 0.02
            "Divorciado" -> return 0.01
            "Viudo" -> return 0.015
            else -> return 0.0
        }
    }
}
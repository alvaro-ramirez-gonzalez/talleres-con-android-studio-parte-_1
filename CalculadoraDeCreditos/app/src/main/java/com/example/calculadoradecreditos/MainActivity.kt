package com.example.calculadoradecreditos

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.Locale
import kotlin.math.pow

class MainActivity : Activity() {

    private lateinit var etCredito: EditText
    private lateinit var etCuotas: EditText
    private lateinit var etInteres: EditText

    private lateinit var btnCalcular: Button

    private lateinit var tvCuota: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvIntereses: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        etCredito = findViewById(R.id.etCredito)
        etCuotas = findViewById(R.id.etCuotas)
        etInteres = findViewById(R.id.etInteres)

        btnCalcular = findViewById(R.id.btnCalcular)

        tvCuota = findViewById(R.id.tvCuota)
        tvTotal = findViewById(R.id.tvTotal)
        tvIntereses = findViewById(R.id.tvIntereses)

        btnCalcular.setOnClickListener {
            calcularCredito()
        }
    }

    private fun calcularCredito() {

        val creditoTexto = etCredito.text.toString().trim()
        val cuotasTexto = etCuotas.text.toString().trim()
        val interesTexto = etInteres.text.toString().trim()

        if (creditoTexto.isEmpty() ||
            cuotasTexto.isEmpty() ||
            interesTexto.isEmpty()
        ) {

            Toast.makeText(
                this,
                "Complete todos los campos",
                Toast.LENGTH_SHORT,
            ).show()

            return
        }

        val credito = creditoTexto.toDoubleOrNull()
        val cuotas = cuotasTexto.toIntOrNull()
        val interes = interesTexto.toDoubleOrNull()

        if ((credito == null) ||
            (cuotas == null) ||
            (interes == null)
        ) {

            Toast.makeText(
                this,
                "Ingrese valores numéricos válidos",
                Toast.LENGTH_SHORT,
            ).show()

            return
        }

        if ((credito <= 0) ||
            (cuotas <= 0) ||
            (interes < 0)
        ) {

            Toast.makeText(
                this,
                "Ingrese valores válidos",
                Toast.LENGTH_SHORT,
            ).show()

            return
        }

        val tasa = interes / 100

        val cuota = if (tasa == 0.0) {
            credito / cuotas
        } else {
            credito * tasa /
                    (1 - (1 + tasa).pow(-cuotas))
        }

        val totalCredito = cuota * cuotas

        val totalIntereses = totalCredito - credito

        tvCuota.text =
            String.format(
                Locale.getDefault(),
                "Valor de la cuota: $%.2f",
                cuota,
            )

        tvTotal.text =
            String.format(
                Locale.getDefault(),
                "Valor total del crédito: $%.2f",
                totalCredito,
            )

        tvIntereses.text =
            String.format(
                Locale.getDefault(),
                "Total de intereses: $%.2f",
                totalIntereses,
            )
    }
}

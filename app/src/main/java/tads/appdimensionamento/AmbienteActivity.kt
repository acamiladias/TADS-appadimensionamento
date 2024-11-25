package tads.appdimensionamento

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tads.appdimensionamento.databinding.ActivityAmbienteBinding

class AmbienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAmbienteBinding

    private var vento: String? = null
    private var temperaturaAmbiente: Int? = null
    private var temperaturaAguaDesejada: Int? = null
    private var largura: Double = 0.0
    private var comprimento: Double = 0.0
    private var profundidade: Double = 0.0
    private var tipoPiscina: String = "aberta"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAmbienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar dados da Intent
        largura = intent.getDoubleExtra("largura", 0.0)
        comprimento = intent.getDoubleExtra("comprimento", 0.0)
        profundidade = intent.getDoubleExtra("profundidade", 0.0)
        tipoPiscina = intent.getStringExtra("tipoPiscina") ?: "aberta"

        Log.d("AmbienteActivity", "Largura: $largura, Comprimento: $comprimento, Profundidade: $profundidade, TipoPiscina: $tipoPiscina")

        // Configurar botão Voltar
        binding.buttonVoltar.setOnClickListener {
            finish()
        }

        // Configurar botão Calcular
        binding.buttonCalcular.setOnClickListener {
            if (!validateFields()) {
                Toast.makeText(this, "Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val intent = Intent(this, ResultadoActivity::class.java).apply {
                    putExtra("largura", largura)
                    putExtra("comprimento", comprimento)
                    putExtra("profundidade", profundidade)
                    putExtra("tipoPiscina", tipoPiscina)
                    putExtra("vento", vento ?: "nenhum")
                    putExtra("temperaturaAmbiente", temperaturaAmbiente ?: 0)
                    putExtra("temperaturaAguaDesejada", temperaturaAguaDesejada ?: 0)
                }
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("AmbienteActivity", "Erro ao iniciar ResultadoActivity: ${e.message}", e)
                Toast.makeText(this, "Erro ao abrir a próxima tela.", Toast.LENGTH_SHORT).show()
            }
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.radioGroupVento.setOnCheckedChangeListener { _, _ ->
            vento = getSelectedRadioText(binding.radioGroupVento)
            Log.d("AmbienteActivity", "Vento selecionado: $vento")
        }

        binding.temperaturaAmbiente.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                temperaturaAmbiente = s?.toString()?.toIntOrNull()
                Log.d("AmbienteActivity", "Temperatura Ambiente: $temperaturaAmbiente")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.temperaturaAguaDesejada.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                temperaturaAguaDesejada = s?.toString()?.toIntOrNull()
                Log.d("AmbienteActivity", "Temperatura Água Desejada: $temperaturaAguaDesejada")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun validateFields(): Boolean {
        if (vento == null) {
            Toast.makeText(this, "Selecione a condição do vento.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (temperaturaAmbiente == null || temperaturaAguaDesejada == null) {
            Toast.makeText(this, "Preencha as temperaturas com números inteiros.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun getSelectedRadioText(radioGroup: android.widget.RadioGroup): String? {
        val selectedId = radioGroup.checkedRadioButtonId
        return if (selectedId != -1) {
            findViewById<RadioButton>(selectedId).text.toString()
        } else {
            null
        }
    }
}

package tads.appdimensionamento

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tads.appdimensionamento.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)

        binding.buttonContinuar.setOnClickListener {
            val largura = binding.larguraPiscina.text.toString().toDoubleOrNull()
            val comprimento = binding.comprimentoPiscina.text.toString().toDoubleOrNull()
            val profundidade = binding.profundidadePiscina.text.toString().toDoubleOrNull()

            if (largura == null || comprimento == null || profundidade == null) {
                Toast.makeText(this, "Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedOptionId = binding.radioGroupAbertaFechada.checkedRadioButtonId
            val tipoPiscina = if (selectedOptionId != -1) {
                findViewById<RadioButton>(selectedOptionId).text.toString()
            } else {
                Toast.makeText(this, "Selecione uma opção (Aberta ou Fechada).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, AmbienteActivity::class.java).apply {
                putExtra("largura", largura)
                putExtra("comprimento", comprimento)
                putExtra("profundidade", profundidade)
                putExtra("tipoPiscina", tipoPiscina)
            }
            startActivity(intent)
        }
    }
}

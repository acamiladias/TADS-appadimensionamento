package tads.appdimensionamento

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import tads.appdimensionamento.databinding.ActivityResultadoBinding

class ResultadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultadoBinding
    private lateinit var modeloDatabase: ModeloDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modeloDatabase = ModeloDatabase(this)

        try {
            // Recuperar dados enviados da tela anterior
            val largura = intent.getDoubleExtra("largura", 0.0)
            val comprimento = intent.getDoubleExtra("comprimento", 0.0)
            val profundidade = intent.getDoubleExtra("profundidade", 0.0)
            val tipoPiscina = intent.getStringExtra("tipoPiscina") ?: "aberta"
            val vento = intent.getStringExtra("vento") ?: "nenhum"
            val uso = intent.getStringExtra("uso") ?: "ano todo"
            val temperaturaAmbiente = intent.getIntExtra("temperaturaAmbiente", 0)
            val temperaturaDesejada = intent.getIntExtra("temperaturaAguaDesejada", 0)

            // Log das entradas
            Log.d("ResultadoActivity", "Largura: $largura, Comprimento: $comprimento, Profundidade: $profundidade")
            Log.d("ResultadoActivity", "TipoPiscina: $tipoPiscina, Vento: $vento, Uso: $uso")
            Log.d("ResultadoActivity", "TemperaturaAmbiente: $temperaturaAmbiente, TemperaturaDesejada: $temperaturaDesejada")

            // Calcular área e volume
            val areaPiscina = largura * comprimento
            val volumePiscina = largura * comprimento * profundidade

            // Calcular a pontuação
            val calculadora = CalculadoraPontuacao(
                largura, comprimento, profundidade, tipoPiscina, vento,
                temperaturaAmbiente.toDouble(), temperaturaDesejada.toDouble()
            )
            val pontuacao = calculadora.calcular()

            // Log da pontuação calculada
            Log.d("ResultadoActivity", "Pontuação Calculada: $pontuacao")

            // Exibir os dados fornecidos pelo usuário
            binding.textViewComprimento.text = "Comprimento: ${String.format("%.2f", comprimento)} m"
            binding.textViewLargura.text = "Largura: ${String.format("%.2f", largura)} m"
            binding.textViewProfundidade.text = "Profundidade: ${String.format("%.2f", profundidade)} m"
            binding.textViewTipoConstrucao.text = "Tipo de Construção: $tipoPiscina"
            binding.textViewUso.text = "Uso: $uso"
            binding.textViewVento.text = "Condição de Vento: $vento"

            // Exibir os resultados calculados
            binding.textViewArea.text = "Área da Piscina: ${String.format("%.2f", areaPiscina)} m²"
            binding.textViewVolume.text = "Volume da Piscina: ${String.format("%.2f", volumePiscina)} m³"
            binding.textViewPontuacao.text = "Pontuação Calculada: $pontuacao"

            // Buscar o modelo no banco de dados
            val modelo = if (tipoPiscina == "aberta") {
                modeloDatabase.buscarModeloAberta(pontuacao)
            } else {
                modeloDatabase.buscarModeloFechada(pontuacao)
            }

            // Log do resultado do modelo
            if (modelo != null) {
                Log.d("ResultadoActivity", "Modelo Encontrado: ${modelo.modelo}, BTUs: ${modelo.btus}, Descrição: ${modelo.descricao}")
                binding.textViewModelo.text = modelo.modelo
                binding.textViewCapacidade.text = "${modelo.btus} BTU's"
                binding.textViewDescricao.text = modelo.descricao
            } else {
                Log.d("ResultadoActivity", "Modelo não encontrado no banco de dados.")
                binding.textViewModelo.text = "Modelo não encontrado"
                binding.textViewCapacidade.text = ""
                binding.textViewDescricao.text = "Não há modelos compatíveis com os parâmetros fornecidos."
            }

            // Configurar listeners dos botões
            binding.buttonNovo.setOnClickListener {
                Log.d("ResultadoActivity", "Botão Novo Dimensionamento clicado")
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish() // Finaliza ResultadoActivity
            }

            binding.buttonVerMais.setOnClickListener {
                // Abrir a página de detalhes
                val intent = Intent(this, DetalhesActivity::class.java)
                startActivity(intent)
            }

        } catch (e: Exception) {
            // Log de erro para depuração
            Log.e("ResultadoActivity", "Erro ao calcular ou buscar modelo: ${e.message}", e)
            binding.textViewModelo.text = "Erro"
            binding.textViewDescricao.text = "Ocorreu um erro ao calcular o modelo. Tente novamente."
        }
    }
}

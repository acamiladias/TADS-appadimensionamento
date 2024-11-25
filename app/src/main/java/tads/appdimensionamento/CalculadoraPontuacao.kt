package tads.appdimensionamento

class CalculadoraPontuacao(
    private val largura: Double,
    private val comprimento: Double,
    private val profundidade: Double,
    private val tipoPiscina: String,
    private val vento: String,
    private val temperaturaAmbiente: Double,
    private val temperaturaDesejada: Double
) {
    fun calcular(): Int {
        val areaPiscina = largura * comprimento
        val volumePiscina = largura * comprimento * profundidade

        val incidenciaVento = when (vento.lowercase()) {
            "nenhum" -> 1
            "fraco" -> 2
            "moderado" -> 3
            "forte" -> 4
            else -> 1
        }
        val ventoConsiderado = if (tipoPiscina.lowercase() == "fechada") 1 else incidenciaVento

        val divisor = if (tipoPiscina.lowercase() == "aberta") 6 else 5

        return ((temperaturaDesejada + 4 - temperaturaAmbiente + 20 + ventoConsiderado) *
                (4 * areaPiscina + 2 * volumePiscina) / divisor).toInt()
    }
}

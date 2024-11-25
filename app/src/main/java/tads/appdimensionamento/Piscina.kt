package tads.appdimensionamento

import java.io.Serializable

data class Piscina(
    val largura: Double,
    val comprimento: Double,
    val profundidade: Double,
    val tipo: String
) : Serializable

package tads.appdimensionamento

import java.io.Serializable

data class Ambiente(
    val vento: String,
    val usoAnual: String,
    val temperaturaAmbiente: Int,
    val temperaturaAguaDesejada: Int
) : Serializable

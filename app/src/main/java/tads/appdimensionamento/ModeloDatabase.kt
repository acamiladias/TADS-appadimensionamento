package tads.appdimensionamento

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ModeloDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "modelos.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_ABERTA = "modelos_aberta"
        const val TABLE_FECHADA = "modelos_fechada"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableAberta = """
            CREATE TABLE $TABLE_ABERTA (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                modelo TEXT NOT NULL,
                pontuacao_min INTEGER NOT NULL,
                pontuacao_max INTEGER NOT NULL,
                btus INTEGER NOT NULL,
                descricao TEXT NOT NULL
            );
        """.trimIndent()

        val createTableFechada = """
            CREATE TABLE $TABLE_FECHADA (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                modelo TEXT NOT NULL,
                pontuacao_min INTEGER NOT NULL,
                pontuacao_max INTEGER NOT NULL,
                btus INTEGER NOT NULL,
                descricao TEXT NOT NULL
            );
        """.trimIndent()

        db?.execSQL(createTableAberta)
        db?.execSQL(createTableFechada)

        Log.d("ModeloDatabase", "Tabelas criadas com sucesso.")

        inserirModelosIniciais(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ABERTA")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FECHADA")
        onCreate(db)
    }

    private fun inserirModelosIniciais(db: SQLiteDatabase?) {
        val modelosAbertos = listOf(
            arrayOf("01 FT20", 0, 450, 20000, "Bomba Fromtherm 1"),
            arrayOf("01 FT25", 451, 550, 25000, "Bomba Fromtherm 2"),
            arrayOf("01 FT40", 551, 949, 40000, "Bomba Fromtherm 3"),
            arrayOf("01 FT60", 950, 1299, 60000, "Bomba Fromtherm 4"),
            arrayOf("01 FT80", 1300, 1599, 80000, "Bomba Fromtherm 5"),
            arrayOf("01 FT100", 1600, 1999, 100000, "Bomba Fromtherm 6"),
            arrayOf("01 FT120", 2000, 2699, 120000, "Bomba Fromtherm 7"),
            arrayOf("01 FT160", 2700, 3499, 160000, "Bomba Fromtherm 8"),
            arrayOf("01 FT240", 3500, 5299, 240000, "Bomba Fromtherm 9")
        )

        val modelosFechados = listOf(
            arrayOf("Modelo 1", 0, 399, 20000, "Bomba Fromtherm 1"),
            arrayOf("Modelo 2", 400, 499, 25000, "Bomba Fromtherm 2"),
            arrayOf("Modelo 3", 500, 849, 40000, "Bomba Fromtherm 3"),
            arrayOf("Modelo 4", 850, 1299, 60000, "Bomba Fromtherm 4"),
            arrayOf("Modelo 5", 1300, 1599, 80000, "Bomba Fromtherm 5"),
            arrayOf("Modelo 6", 1600, 1899, 100000, "Bomba Fromtherm 6"),
            arrayOf("Modelo 7", 1900, 2199, 120000, "Bomba Fromtherm 7"),
            arrayOf("Modelo 8", 2200, 3000, 160000, "Bomba Fromtherm 8"),
            arrayOf("Modelo 9", 3001, 4800, 240000, "Bomba Fromtherm 9")
        )

        modelosAbertos.forEach { modelo ->
            try {
                db?.execSQL(
                    "INSERT INTO $TABLE_ABERTA (modelo, pontuacao_min, pontuacao_max, btus, descricao) VALUES (?, ?, ?, ?, ?)",
                    modelo
                )
                Log.d("ModeloDatabase", "Modelo aberto inserido: ${modelo[0]}")
            } catch (e: Exception) {
                Log.e("ModeloDatabase", "Erro ao inserir modelo aberto: ${modelo[0]} - ${e.message}")
            }
        }

        modelosFechados.forEach { modelo ->
            try {
                db?.execSQL(
                    "INSERT INTO $TABLE_FECHADA (modelo, pontuacao_min, pontuacao_max, btus, descricao) VALUES (?, ?, ?, ?, ?)",
                    modelo
                )
                Log.d("ModeloDatabase", "Modelo fechado inserido: ${modelo[0]}")
            } catch (e: Exception) {
                Log.e("ModeloDatabase", "Erro ao inserir modelo fechado: ${modelo[0]} - ${e.message}")
            }
        }
    }

    fun buscarModeloAberta(pontuacao: Int): Modelo? {
        val query = """
            SELECT modelo, pontuacao_min, pontuacao_max, btus, descricao
            FROM $TABLE_ABERTA
            WHERE pontuacao_min <= ? AND pontuacao_max >= ?
            LIMIT 1
        """.trimIndent()

        return buscarModelo(query, pontuacao)
    }

    fun buscarModeloFechada(pontuacao: Int): Modelo? {
        val query = """
            SELECT modelo, pontuacao_min, pontuacao_max, btus, descricao
            FROM $TABLE_FECHADA
            WHERE pontuacao_min <= ? AND pontuacao_max >= ?
            LIMIT 1
        """.trimIndent()

        return buscarModelo(query, pontuacao)
    }

    private fun buscarModelo(query: String, pontuacao: Int): Modelo? {
        val cursor = readableDatabase.rawQuery(query, arrayOf(pontuacao.toString(), pontuacao.toString()))
        return if (cursor.moveToFirst()) {
            Modelo(
                modelo = cursor.getString(cursor.getColumnIndexOrThrow("modelo")),
                pontuacaoMin = cursor.getInt(cursor.getColumnIndexOrThrow("pontuacao_min")),
                pontuacaoMax = cursor.getInt(cursor.getColumnIndexOrThrow("pontuacao_max")),
                btus = cursor.getInt(cursor.getColumnIndexOrThrow("btus")),
                descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"))
            ).also {
                cursor.close()
            }
        } else {
            cursor.close()
            null
        }
    }
}

package com.example.librosxmlprueba.daos

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.example.librosxmlprueba.modelo.Libro
import com.example.librosxmlprueba.modelo.Libros
import org.simpleframework.xml.core.Persister
import java.io.InputStreamReader


class XMLLibroDAO(private val context: Context) : LibroDAO {

    private val serializer = Persister()

    override fun getAllLibros(): MutableList<Libro> {
        val libros = mutableListOf<Libro>()

        try {
            val inputStream = context.assets.open("libros.xml")
            val reader = InputStreamReader(inputStream)
            val librosListType = serializer.read(Libros::class.java, reader, false)
            libros.addAll(librosListType.libro)
            reader.close()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return libros
    }

    override fun addLibro(libro: Libro) {
        try {
            val currentLibroes = getAllLibros().toMutableList()
            currentLibroes.add(libro)

            val librosList = Libros(currentLibroes)
            val outputStream = context.openFileOutput("libros.xml",MODE_PRIVATE)
            serializer.write(librosList, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("DAO", "Error al agregar libro: ${e.message}")
        }
    }

    override fun deleteLibro(libro: Libro) {
        TODO("Not yet implemented")
    }

    override fun getLibro(libro: Libro) {
        TODO("Not yet implemented")
    }
}
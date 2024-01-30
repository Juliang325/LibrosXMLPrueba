package com.example.librosxmlprueba

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.librosxmlprueba.daos.LibroDAO
import com.example.librosxmlprueba.daos.XMLLibroDAO
import com.example.librosxmlprueba.handler.LibroHandlerXML
import com.example.librosxmlprueba.modelo.Libro
import com.example.librosxmlprueba.modelo.Libros
import org.simpleframework.xml.core.Persister
import java.io.*
import javax.xml.parsers.SAXParserFactory

class MainActivity : AppCompatActivity() {

    var libros = mutableListOf<Libro>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        copiarArchivoDesdeAssets()
        procesarArchivoAssetsXML()
        Log.d("prueba2", "probando procesado con Simple XML Framework")
        libros.forEach {
            Log.d("prueba2", it.toString())
        }
        Log.d("", "")
        val libro=Libro("El principito","Antoine de Saint-Exupéry","9788478887200",120,false)
        addLibro(libro)
        deleteLibro(libro)
        ProcesarArchivoXMLInterno()
        libros.forEach {
            Log.d("prueba2", it.toString())
        }

        procesarArchivoXMLSAX()
    }

    private fun procesarArchivoXMLSAX() {
        try {
            val factory = SAXParserFactory.newInstance()
            val parser = factory.newSAXParser()
            val handler = LibroHandlerXML()

            val inputStream = assets.open("libros.xml")
            parser.parse(inputStream, handler)

            // Accede a la lista de profesores desde handler.profesores
            handler.libros.forEach {
                Log.d("SAX", "Profesor: ${it.nombre}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun procesarArchivoAssetsXML() {
        val serializer = Persister()
        var inputStream: InputStream? = null
        var reader: InputStreamReader? = null

        try {
            inputStream = assets.open("libros.xml")
            reader = InputStreamReader(inputStream)
            val librosListType = serializer.read(Libros::class.java, reader, false)
            libros.addAll(librosListType.libro)
        } catch (e: Exception) {
            // Manejo de errores
            e.printStackTrace()
        } finally {
            // Cerrar inputStream y reader
            try {
                reader?.close()
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    fun addLibro(libro: Libro) {
        try {
            val serializer = Persister()
            libros.add(libro)
            val librosList = Libros(libros)
            val outputStream = openFileOutput("libros.xml", MODE_PRIVATE)
            serializer.write(librosList, outputStream)
            outputStream.close() // Asegúrate de cerrar el outputStream después de escribir
        } catch (e: Exception) {
            e.printStackTrace() // Manejo de errores adecuado
        }
    }
    fun deleteLibro(libro: Libro) {
        try {
            val serializer = Persister()
            Log.d("DAO","deleting libro")
            libros.remove(libro)
            val librosList = Libros(libros)
            val outputStream = openFileOutput("libros.xml", MODE_PRIVATE)
            serializer.write(librosList, outputStream)
            outputStream.close() // Asegúrate de cerrar el outputStream después de escribir
        } catch (e: Exception) {
            e.printStackTrace() // Manejo de errores adecuado
        }
    }
    fun getLibro(libro: Libro) {
        try {
            val serializer = Persister()
            Log.d("DAO","Buscar libro")
            if (libros.contains(libro)){
                libros
            }

            val librosList = Libros(libros)
            val outputStream = openFileOutput("libros.xml", MODE_PRIVATE)
            serializer.write(librosList, outputStream)
            outputStream.close() // Asegúrate de cerrar el outputStream después de escribir
        } catch (e: Exception) {
            e.printStackTrace() // Manejo de errores adecuado
        }
    }
    private fun copiarArchivoDesdeAssets() {
        val nombreArchivo = "libros.xml"
        val archivoEnAssets = assets.open(nombreArchivo)
        val archivoInterno = openFileOutput(nombreArchivo, MODE_PRIVATE)

        archivoEnAssets.copyTo(archivoInterno)
        archivoEnAssets.close()
        archivoInterno.close()
    }

    fun ProcesarArchivoXMLInterno() {
        val nombreArchivo = "libros.xml"
        val serializer = Persister()

        try {
            // Abrir el archivo para lectura
            val file = File(filesDir, nombreArchivo)
            val inputStream = FileInputStream(file)
            val librosList = serializer.read(Libros::class.java, inputStream)
            libros.addAll(librosList.libro)
            inputStream.close()
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

}
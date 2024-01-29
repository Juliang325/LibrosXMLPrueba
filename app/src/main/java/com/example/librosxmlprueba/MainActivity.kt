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
    private val libroDAO: LibroDAO by lazy { XMLLibroDAO(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        copiarArchivoDesdeAssets()
        procesarArchivoAssetsXML()
        // Usar el DAO para obtener y agregar profesores
        val librosFromDAO = libroDAO.getAllLibros()
        librosFromDAO.forEach {
            Log.d("DAO", it.toString())
        }

        val nuevoLibro = Libro("Fenris el lobo","Pablo")
        libroDAO.addLibro(nuevoLibro)
        ProcesarArchivoXMLInterno()
        // Obtener y mostrar profesores después de agregar uno nuevo
        val librosAfterAdd = libroDAO.getAllLibros()
        Log.d("DAO", "---------Lista--------")
        librosAfterAdd.forEach {
            Log.d("DAO", it.nombre)
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

            // Accede a la lista de profesores desde handler.libros
            handler.libros.forEach {
                Log.d("SAX", "Libros: ${it.nombre}")
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
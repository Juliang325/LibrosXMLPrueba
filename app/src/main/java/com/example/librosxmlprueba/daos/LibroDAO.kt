package com.example.librosxmlprueba.daos

import com.example.librosxmlprueba.modelo.Libro


interface LibroDAO {
    fun getAllLibros(): List<Libro>
    fun addLibro(libro: Libro)
    fun deleteLibro(libro: Libro)
    fun getLibro(libro: Libro)
}
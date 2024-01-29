package com.example.librosxmlprueba.modelo

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


@Root(name = "libros")
data class Libros(
    @field:ElementList(inline = true, entry = "libro")
    var libro: List<Libro> = mutableListOf()
)

@Root(name = "libro")
data class Libro(
    @field:Attribute(name = "nombre")
    var nombre: String = "",

    @field:Attribute(name = "autor")
    var autor: String = "",

    @field:Element(name = "isbn")
    var isbn: String = "",

    @field:Element(name = "paginas")
    var paginas: Int = 0,

    @field:Element(name = "leido", required = false)
    var leido: Boolean? = null
){
    override fun toString(): String {
        return "Nombre: " + nombre + " Author: " + autor + " ISBN: " + isbn + " Pagina: " + paginas + " Leido: " + leido
    }
}
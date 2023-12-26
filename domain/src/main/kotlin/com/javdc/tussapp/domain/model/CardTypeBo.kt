package com.javdc.tussapp.domain.model

enum class CardTypeBo(val passCode: Int, val primaryColor: String, val secondaryColor: String) {

    CON_TRANSBORDO(30, "#a50034", "#ffffff"),
    SIN_TRANSBORDO(31, "#ffffff", "#a50034"),
    HIJO_DE_EMPLEADO(70, "#44b48f", "#ffffff"),
    FAMILIAR_DE_EMPLEADO(71, "#ce757c", "#ffffff"),
    JOVEN(101, "#de003f", "#80b95f"),
    AMARILLA(102, "#fcf9de", "#f9bc00"),
    SOLIDARIA(103, "#816cab", "#cdc7ee"),
    TERCERA_EDAD(104, "#99d5e8", "#a50034"),
    TERCERA_EDAD_2(105, "#99d5e8", "#a50034"),
    SOCIAL(109, "#d4758a", "#f4ecd1"),
    TERCERA_EDAD_3(110, "#99d5e8", "#a50034"),
    TERCERA_EDAD_4(111, "#99d5e8", "#a50034"),
    TERCERA_EDAD_5(112, "#99d5e8", "#a50034"),
    TERCERA_EDAD_6(113, "#99d5e8", "#a50034"),
    TERCERA_EDAD_7(114, "#99d5e8", "#a50034"),
    ESTUDIANTE(115, "#d0e9f7", "#f53046"),
    MENSUAL_ESTUDIANTE(116, "#fef1c7", "#5bb2a4"),
    DIVERSIDAD_FUNCIONAL(117, "#157f87", "#97c9aa"),
    INFANTIL(118, "#93c27c", "#e45b78"),
    TREINTA_DIAS(150, "#f9c000", "#e7603e"),
    TURISTICA_UN_DIA(151, "#d59b83", "#006092"),
    TURISTICA_TRES_DIAS(152, "#d59b84", "#80194f"),
    FAMILIA_NUMEROSA(154, "#0067ae", "#bfddf2"),
    FAMILIA_NUMEROSA_ESPECIAL(155, "#00acc8", "#ddeaf6"),
    ANUAL(156, "#f5bc8b", "#8bc2b3"),
    MENSUAL_AEROPUERTO(157, "#ffed5c", "#a2a8b1"),
    PENSIONISTA(201, "#003769", "#ffffff");

    companion object {
        fun getByCode(passCode: Int) = entries.find { it.passCode == passCode }
    }

}
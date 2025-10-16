package com.example.awaq1.data.formularios

private fun siONo(boolean: Boolean): String = if (boolean) "Sí" else "No"

data class FormInfo(
    val tipo: String, // Descripcion del tipo de formulario (una sola palabra)
    val valorIdentificador: String, // Valor que se muestra junto tipo
    val primerTag: String, // Tag del primer valor a mostrar como preview del formulario
    val primerContenido: String, // El valor a mostrar junto al primer tag
    val segundoTag: String,
    val segundoContenido: String,

    val formulario: String, // Indicador de tipo de formulario, para luego acceder
    val formId: Long,
    val fechaCreacion: String,
    val fechaEdicion: String,
    val completo: Boolean,
    val synced: Boolean
) {
    constructor(formulario: FormularioUnoEntity) : this(
        tipo = "Transecto", formulario.transecto,
        primerTag = "Tipo", formulario.tipoAnimal,
        segundoTag = "Nombre", formulario.nombreComun,
        formulario = "form1",
        formId = formulario.id,
        fechaCreacion = formulario.fecha,
        fechaEdicion = formulario.editado,
        completo = formulario.esCompleto(),
        synced = formulario.synced
    )

    constructor(formulario: FormularioDosEntity) : this(
        tipo = "Zona", formulario.zona,
        primerTag = "Tipo", formulario.tipoAnimal,
        segundoTag = "Nombre", formulario.nombreComun,
        formulario = "form2",
        formId = formulario.id,
        fechaCreacion = formulario.fecha,
        fechaEdicion = formulario.editado,
        completo = formulario.esCompleto(),
        synced = formulario.synced
    )

    constructor(formulario: FormularioTresEntity) : this(
        tipo = "Código", formulario.codigo,
        primerTag = "Seguimiento", siONo(formulario.seguimiento),
        segundoTag = "Cambio", siONo(formulario.cambio),
        formulario = "form3",
        formId = formulario.id,
        fechaCreacion = formulario.fecha,
        fechaEdicion = formulario.editado,
        completo = formulario.esCompleto(),
        synced = formulario.synced
    )

    constructor(formulario: FormularioCuatroEntity) : this(
        tipo = "Código", formulario.codigo,
        primerTag = "Cuad. A", formulario.quad_a,
        segundoTag = "Cuad. B", formulario.quad_b,
        formulario = "form4",
        formId = formulario.id,
        fechaCreacion = formulario.fecha,
        fechaEdicion = formulario.editado,
        completo = formulario.esCompleto(),
        synced = formulario.synced
    )

    constructor(formulario: FormularioCincoEntity) : this(
        tipo = "Zona", formulario.zona,
        primerTag = "Tipo", formulario.tipoAnimal,
        segundoTag = "Nombre", formulario.nombreComun,
        formulario = "form5",
        formId = formulario.id,
        fechaCreacion = formulario.fecha,
        fechaEdicion = formulario.editado,
        completo = formulario.esCompleto(),
        synced = formulario.synced
    )

    constructor(formulario: FormularioSeisEntity) : this(
        tipo = "Codigo", formulario.codigo,
        primerTag = "Zona", formulario.zona,
        segundoTag = "PlacaCamara", formulario.placaCamara,
        formulario = "form6",
        formId = formulario.id,
        fechaCreacion = formulario.fecha,
        fechaEdicion = formulario.editado,
        completo = formulario.esCompleto(),
        synced = formulario.synced
    )

    constructor(formulario: FormularioSieteEntity) : this(
        tipo = "Zona", formulario.zona,
        primerTag = "Pluviosidad", formulario.pluviosidad,
        segundoTag = "TempMax", formulario.temperaturaMaxima,
        formulario = "form7",
        formId = formulario.id,
        fechaCreacion = formulario.fecha,
        fechaEdicion = formulario.editado,
        completo = formulario.esCompleto(),
        synced = formulario.synced
    )
}
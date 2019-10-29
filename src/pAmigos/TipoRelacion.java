package pAmigos;
/* * *
 * Tarea 4. Implementación en Consola del Proyecto Los amigos de mis amigos
 * Materia: Tecnologías de Programación
 * Tutor: María Lucía Barrón Estrada.
 *
 * Autor: * Víctor Manuel Bátiz Beltrán.
 * Última Revisión: 21/10/2019
 *
 * * */

/*
 * El siguiente enumerado se utilizará para controlar los tipos de relación
 */

public enum TipoRelacion {
    //DOS,TRES,CUATRO,CINCO,SEIS,SIETE,OCHO,NUEVE,DIEZ,J,Q,R,AS,TODOS;

    NINGUNA("Ninguna relación", 0),
    AGREGA_RELACION("Agregar una relación", 1),
    PREGUNTA_AMISTAD_DIRECTA("¿Existe amistad directa, entre personas indicadas?", 2),
    PREGUNTA_AMIGOS_DE_NIVEL("Amigos de cierto nivel de profundidad", 3),
    ELIMINA_RELACION("Eliminar una relación", 4);

    private final String texto;
    private final int valor;

    TipoRelacion(String texto, int valor) {
        this.texto = texto;
        this.valor = valor;
    }

    public String obtenTexto() {
        return texto;
    }

    public int obtenValor() {
        return valor;
    }


    @Override
    public String toString(){
        return obtenTexto();
    }
}

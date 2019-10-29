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

import java.util.ArrayList;
import java.util.Iterator;

public class Grafo2V extends Grafo {

    public Grafo2V(String nombre)
        {
            super(nombre);
        }
        /*
         * Método que sobreescribe al original para manejar las aristas en dos vías
         * @Param arista recibe la arista a crear y crea la relación bidireccional
         */
        public boolean addArista(Arista arista)
        {
            boolean a = super.addArista(arista);
            boolean b = super.addArista(arista.reverse());
            return a && b;
        }
        /*
         * Método que sobreescribe al original para manejar las aristas en dos vías
         * Elimina la arista y su inversa
         * @Param arista recibe la arista a eliminar y con ella elimina su inversa
         */
        public boolean removeArista(Arista arista)
        {
            boolean a = super.removeArista(arista);
            boolean b = super.removeArista(arista.reverse());
            return a && b;
        }

        /*
         * Método para buscar un Nodo en la lista de nodos
         * @Param consulta recibe el nodo a buscar
         * Sin uso en este proyecto pero se deja para futuras implementaciones
         */
        public Nodo buscarNodo(Nodo consulta){
            for(Nodo n: nodos){
                if(consulta.equals(n)){
                    return n;
                }
            }
            return null;
        }
        /*
         * Método para buscar un Nodo en la lista de nodos
         * @Param consulta recibe el identificar del nodo a buscar
         *
         */
        public Nodo buscarNodo(int consulta){
            for(Nodo n: nodos){
                if(consulta == n.getPosicion()){
                    return n;
                }
            }
            return null;
        }

        /*
         * Método que regresa el número de hijos que tiene el nodo indicado
         * Se utilizará para los métodos de búsqueda
         * @Param nodo recibe el nodo a consultar
         */
        public int numeroHijosDe(Nodo nodo)
        {
            if(!this.containsNodo(nodo))
                return 0; //throw new IllegalArgumentException("Nodo no contenido en el grafo");
            int cont=0;
            ArrayList<Nodo> hijos = new ArrayList<Nodo>();
            Iterator<Arista> itAristas = this.aristas.get(nodo).iterator();
            while(itAristas.hasNext()) {
                itAristas.next();
                cont++;
            }
            return cont;
        }

        /*
         * Método para obtener los nodos de cierto nivel a partir de un nodo origen
         * @Param origen Recibe el nodo origen a partir del cual se buscarán los amigos
         * @Param nivelObjetivo Indica el nivel objetivo para obtener a los amigos.
         */
        public String obtenAmigosDeNivel(Nodo origen, int nivelObjetivo)
        {
            int nivelActual = 0;
            String resultados = "";
            if(!this.containsNodo(origen))
                throw new IllegalArgumentException("- " + origen.toString() + " No tiene amigos de nivel " + nivelObjetivo);

            ArrayList<Comunicacion> comunicaciones = new ArrayList<Comunicacion>();
            ArrayList<String> visitados = new ArrayList<String>();
            comunicaciones.add(new Comunicacion(origen));
            visitados.add(origen.toString());
            // Búsqueda
            int cuantosHijos = 0;
            int ciclo = 1;
            while (nivelActual < nivelObjetivo) {
                while (ciclo > 0 && !comunicaciones.isEmpty()) {
                    ciclo--;
                    Comunicacion comunicacion = comunicaciones.remove(0);
                    Nodo ultimo = comunicacion.getFinal();
                    Iterator<Nodo> hijos = this.hijosDe(ultimo).iterator();
                    while (hijos.hasNext()) {
                        Nodo siguiente = hijos.next();
                        if (!comunicacion.contains(siguiente) && !visitados.contains(siguiente.toString())) {
                            Comunicacion nuevaComunicacion = comunicacion.clone();
                            nuevaComunicacion.add(siguiente);
                            visitados.add(siguiente.toString());
                            comunicaciones.add(nuevaComunicacion);
                            cuantosHijos++;
                        }
                    }
                }
                nivelActual++;
                ciclo = cuantosHijos;
                cuantosHijos = 0;
            }

            if (nivelActual == nivelObjetivo) {
                for (Comunicacion c : comunicaciones) {
                    if (nivelObjetivo == c.size() - 1) {
                        resultados = resultados + c.get(nivelObjetivo).toString() + "\n";
                    }
                }
                if (!resultados.isBlank()) {
                    return "+ Lista de amigos de " + origen.toString() + " de nivel " + nivelObjetivo + "\n" + resultados;
                }
            }

            //la persona no tiene amigos en el nivel indicado.
            throw new IllegalArgumentException("- " + origen.toString() + " No tiene amigos de nivel " + nivelObjetivo);
        }
    }

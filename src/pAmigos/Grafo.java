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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Grafo {
    private String nombre;
    protected Set<Nodo> nodos;
    protected HashMap<Nodo, Set<Arista>> aristas;

    Grafo(String nombre) {
        this.setNombre(nombre);
        this.nodos = new TreeSet<Nodo>();
        this.aristas = new HashMap<Nodo, Set<Arista>>();
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /*
     * Método para agregar un nodo al grafo
     *
     * @param nodo recibe un objeto de tipo nodo para agregarlo
     */
    public boolean addNodo(Nodo nodo) {
        boolean seAgrego = this.nodos.add(nodo);
        if (seAgrego)
            // Si no se habia añadido, se inicializa la lista de nodos conectados
            aristas.put(nodo, new HashSet<Arista>());
        return seAgrego;
    }

    /*
     * Método para agregar un enlace. Si los nodos no existen se agregan.
     * Si los nodos son iguales se lanza una excepción.
     *
     * @Param arista Recibe como parámetro el enlace a agregar.
     */
    public boolean addArista(Arista arista) {
        Nodo origen = arista.getOrigen();
        Nodo destino = arista.getDestino();
        // Si los nodos no se habian añadido se añaden
        if (origen.equals(destino))
            throw new IllegalArgumentException("Nodo de origen y de destino iguales");
        this.addNodo(origen);
        this.addNodo(destino);
        return aristas.get(origen).add(arista);
    }

    /*
     * Método para remover un nodo de la lista de nodos
     *
     * @Param nodo Recibe como parámetro el nodo a remover
     */
    public boolean removeNodo(Nodo nodo) {
        boolean contenido = this.containsNodo(nodo);
        if (contenido) {
            Iterator<Arista> itAristas = this.iteratorAristas(nodo);
            while (itAristas.hasNext()) {
                Arista arista = itAristas.next().reverse();
                aristas.get(arista.getOrigen()).remove(arista); // Borra las aristas reciprocas
            }
            this.aristas.remove(nodo);  // Destruye el HasMap asociado
            this.nodos.remove(nodo);
        }
        return contenido;
    }


    /*
     * Método para retirar un enlace, recibe como parámetro el enlace a retirar.
     */
    public boolean removeArista(Arista arista)
    {
        if(this.containsArista(arista)){
            Nodo origen = arista.getOrigen();
            return aristas.get(origen).remove(arista);
        }else{
            return false;
        }
    }

    /*
     * Método para recuperar un enlace (arista), recibe como parámetro los nodos (amigo) origen y destino.
     */
    public Arista getArista(Nodo origen, Nodo destino)
    {
        Arista arista = new Arista(origen,destino);
        if(this.containsArista(arista))
        {
            Iterator<Arista> itAristas = this.iteratorAristas(origen);
            Arista i = null;
            while(!arista.equals(i))
            {
                i = itAristas.next();
            }
            return i;
        }else{
            throw new IllegalArgumentException("Arista no contenida en el grafo");
        }
    }

    /*
     * Método para verificar si un nodo ya está dado de alto.
     * Recibe como parámetro el nodo a verificar.
     */
    public boolean containsNodo(Nodo nodo)
    {
        return this.nodos.contains(nodo);
    }

    /*
     * Método para verificar si una arista ya está dada de alta.
     * Recibe como parámetro la arista a verificar.
     */
    public boolean containsArista(Arista arista)
    {
        Nodo origen = arista.getOrigen();
        if(this.containsNodo(origen))
        {
            return this.aristas.get(origen).contains(arista);
        }else{
            return false;
        }
    }

    /*
     * Método que regresa un iterador de nodos
     */
    public Iterator<Nodo> iteratorNodos()
    {
        return this.nodos.iterator();
    }

    /*
     * Método que regresa un iterador de Aristas del Nodo
     * enviado como parámetro.
     */
    public Iterator<Arista> iteratorAristas(Nodo origen)
    {
        if(this.containsNodo(origen))
        {
            return this.aristas.get(origen).iterator();
        }
        else
        {
            throw new IllegalArgumentException("Nodo no contenido en el grafo");
        }
    }

    /*
     * Método que regresa una Colección de los nodos hijos del nodo recibido como parámetro
     * Se utilizará para los métodos de búsqueda
     */
    public Collection<Nodo> hijosDe(Nodo nodo)
    {
        if(!this.containsNodo(nodo))
            throw new IllegalArgumentException("Nodo no contenido en el grafo");

        ArrayList<Nodo> hijos = new ArrayList<Nodo>();
        Iterator<Arista> itAristas = this.aristas.get(nodo).iterator();
        while(itAristas.hasNext())
            hijos.add(itAristas.next().getDestino());
        return hijos;

    }
    /*
     * Método no utilizado en el presente programa pero agrega la funcionalidad
     * de regresar un vector con los nombres de los nodos para futuros proyectos
     */
    public Vector<String> getNombreNodos()
    {
        Vector<String> nombreNodos = new Vector<String>();
        Iterator<Nodo> itNodos = nodos.iterator();
        while(itNodos.hasNext())
        {
            nombreNodos.add(itNodos.next().getNombre());
        }
        return nombreNodos;
    }
    /*
     * Método que regresa un ArrayList de tipo Comunicacion donde están contenidos los
     * nodos que forman la ruta entre nodo origen y nodo destino
     *
     * Se utiliza como método de búsqueda una implementación del algoritmo
     * Breadth First Traversal (or Search)
     */
    public Comunicacion obtenMejorCamino(Nodo origen, Nodo destino)
    {
        if(!this.containsNodo(origen) || !this.containsNodo(destino))
            throw new IllegalArgumentException("- " + origen.getNombre() + " => " + destino.getNombre());

        ArrayList<Comunicacion> comunicaciones = new ArrayList<Comunicacion>();
        comunicaciones.add(new Comunicacion(origen));
        // Busqueda
        while(!comunicaciones.isEmpty())
        {
            Comunicacion comunicacion = comunicaciones.remove(0);
            Nodo ultimo = comunicacion.getFinal();
            Iterator<Nodo> hijos = this.hijosDe(ultimo).iterator();
            while(hijos.hasNext())
            {
                Nodo siguiente = hijos.next();
                if(siguiente.equals(destino)){
                    comunicacion.add(siguiente);
                    return comunicacion;
                }
                else if(!comunicacion.contains(siguiente)){
                    Comunicacion nuevaComunicacion = comunicacion.clone();
                    nuevaComunicacion.add(siguiente);
                    comunicaciones.add(nuevaComunicacion);
                }
            }
        }
        //Nodos no conectados, regreso la cadena adecuada en el mensaje de la excepción
        throw new IllegalArgumentException("- " + origen.getNombre() + " => " + destino.getNombre());
    }
    /*
     * Se sobreescribe toString() para mostrar la lista de nodos y enlaces vigentes entre los mismos.
     */
    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(this.nombre);
        s.append("\n\n");
        s.append("== Nodos ==");
        s.append("\n");
        Iterator<Nodo> itNodos = nodos.iterator();
        while(itNodos.hasNext())
        {
            s.append(itNodos.next().toString());
            s.append("\n");
        }
        s.append("\n");
        s.append("== Aristas ==");
        s.append("\n");

        itNodos = nodos.iterator();
        while(itNodos.hasNext())
        {
            Nodo padre = itNodos.next();
            Iterator<Arista> itHijos = aristas.get(padre).iterator();
            while(itHijos.hasNext())
            {

                s.append(itHijos.next().toString());
                s.append("\n");
            }
        }
        s.deleteCharAt(s.length()-1);
        return s.toString();
    }

/*
Or, when you're not on Java 8 yet, use SimpleDateFormat#parse() to parse a String in a certain pattern into a Date.

String oldstring = "2011-01-18 00:00:00.0";
Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(oldstring);
Use SimpleDateFormat#format() to format a Date into a String in a certain pattern.

String newstring = new SimpleDateFormat("yyyy-MM-dd").format(date);
System.out.println(newstring); // 2011-01-18
 */


    /* Inicia código de la Clase interna Nodo
     * Contiene los métodos y atributos necesarios para manejar los nodos
     */
    public static class Nodo implements Comparable
    {
        private String nombre;
        private String apellido;
        private char sexo;
        private LocalDate fechaNacimiento;
        private int posicion;


        //Constructor
        public Nodo(String nombre, String apellido, char sexo, LocalDate fechaNacimiento, int posicion) {
            this.setNombre(nombre);
            this.setApellido(apellido);
            this.setSexo(Character.toUpperCase(sexo));
            this.setFechaNacimiento(fechaNacimiento);
            this.posicion = posicion;
        }

        //Getters y setters
        public String getNombre()
        {
            return this.nombre;
        }

        public void setNombre(String nombre)
        {
            this.nombre = nombre;
        }
        public String getApellido() {
            return apellido;
        }

        public void setApellido(String apellido) {
            this.apellido = apellido;
        }

        public char getSexo() {
            return sexo;
        }

        public void setSexo(char sexo) {
            this.sexo = sexo;
    }

        public LocalDate getFechaNacimiento() {
            return fechaNacimiento;
        }

        public void setFechaNacimiento(LocalDate fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
        }

        public int getPosicion()
        {
            return this.posicion;
        }

        public void setPosicion(String nombre)
        {
            this.posicion = posicion;
        }

        @Override
        public String toString()
        {
            String fecha = this.fechaNacimiento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return this.nombre + "," + this.apellido+ "," + this.sexo+ "," + fecha;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Nodo nodo = (Nodo) o;
            return Objects.equals(nombre.toLowerCase(), nodo.nombre.toLowerCase()) &&
                    Objects.equals(apellido.toLowerCase(), nodo.apellido.toLowerCase()) &&
                    Objects.equals(Character.toLowerCase(sexo), Character.toLowerCase(nodo.sexo)) &&
                    Objects.equals(fechaNacimiento, nodo.fechaNacimiento);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nombre.toLowerCase(), apellido.toLowerCase(), Character.toLowerCase(sexo), fechaNacimiento);
        }

        @Override
        public int compareTo(Object o)
        {
            if( o != null && getClass() == o.getClass())
            {
                Nodo n = (Nodo) o;
                String s1 = this.toString().toUpperCase();
                String s2 = n.toString().toUpperCase();

                return s1.compareTo(s2);
            }else
                return -1;
        }
    }

    /* Inicia código de la Clase interna Arista
     * Contiene los métodos y atributos necesarios para manejar la amistad entre las personas
     */
    public static class Arista
    {
        private Nodo origen;
        private Nodo destino;

        public Arista(Nodo origen, Nodo destino)
        {
            this.setOrigen(origen);
            this.setDestino(destino);
        }

        public Nodo getOrigen()
        {
            return this.origen;
        }

        public void setOrigen(Nodo origen)
        {
            this.origen = origen;
        }

        public Nodo getDestino()
        {
            return this.destino;
        }

        public void setDestino(Nodo destino)
        {
            this.destino = destino;
        }

        public Arista reverse()
        {
            return new Arista(destino,origen);
        }

        @Override
        public String toString()
        {
            return origen.toString() + " es amigo de " + destino.toString();
        }

        @Override
        public boolean equals(Object o)
        {
            if( o instanceof Arista)
            {
                Arista a = (Arista) o;
                Nodo o1 = this.origen;
                Nodo o2 = a.getOrigen();
                Nodo d1 = this.destino;
                Nodo d2 = a.getDestino();
                return o1.equals(o2) && d1.equals(d2);
            }else
                return false;
        }

        @Override
        public int hashCode()
        {
            return this.origen.hashCode();
        }
    }

    /*
    Inicia la declaración de la clase interna Comunicacion, la cual extiende a la clase ArrayList
    Será utilizada para almacenar los nodos que forman el camino de una torre inicial a una torre final.
    Así mismo, el método toString() nos regresará la ruta ya armada bajo las condiciones solicitadas:
    + torreOrigen => torreEnlace1 => ... => torreDestino
     */
    public class Comunicacion extends ArrayList<Nodo>
    {
        public Comunicacion()
        {
            super();
        }

        public Comunicacion(Nodo origen)
        {
            this();
            this.add(origen);
        }

        public Nodo getInicio()
        {
            return super.get(0);
        }

        public Nodo getFinal()
        {
            return super.get(super.size()-1);
        }

        public Comunicacion clone()
        {
            return (Comunicacion)super.clone();
        }

        /*
         * Se elabora la cadena de los componentes que comunican nodo inicial con nodo final.
         */
        @Override
        public String toString()
        {
            String ruta = "Lista de amigos:";
            for(int i = 0; i < super.size()-1; i++)
            {
                ruta = ruta + super.get(i).toString() + ", ";
            }
            ruta += super.get(super.size()-1).toString();
            return ruta;
        }
    }
}
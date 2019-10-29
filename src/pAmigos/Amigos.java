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

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Amigos {
    private static Grafo2V gT = new Grafo2V("Los Amigos de mis amigos");
    private static ArrayList<String> vLineasErroneas = new ArrayList<String>();

    /*
     * Método que cuenta las ocurrencias de una cadena dentro de otra cadena
     * @Param buscar Recibe como parámetro la cadena a buscar.
     * @Param texto Recibe como parámetro el texto donde buscar.
     * @Param regresa el número de ocurrencias de la cadena indicada.
     */
    public static int contarOcurrencias(String buscar, String texto) {
        Pattern pattern = Pattern.compile(buscar);
        Matcher matcher = pattern.matcher(texto);

        int ocurrencias = 0;
        while (matcher.find()) {
            ocurrencias++;
        }
        return ocurrencias;
    }

    /*
     * Método que lee un archivo de texto que contiene los datos de diferentes personas
     * @Param archivo Recibe como parámetro la ruta al archivo.
     * @Param return regresa un ArrayList conteniendo las líneas válidas
     */
    static ArrayList<String> cargarCatalogo(String archivo) throws FileNotFoundException, IOException {
        ArrayList<String> vLineas = new ArrayList<String>();
        String regexValidarPersona = "([a-zA-Z\\s]{1,20})\\s*,\\s*([a-zA-Z\\s]{1,20})\\s*,\\s*([MmFf])\\s*,\\s*(\\d{2}/\\d{2}/\\d{4})";
        String cadena, cadenaOriginal = "";
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) {
            // Se anexa al arreglo la cadena solo si es una persona con todos los atributos
            // de lo contrario la ignoro.
            //Elimina espacios vacios al inicio y al final de la línea
            //y también convierte a un solo espacio cualquier ocurrencia de 2 o más espacios
            cadena = cadena.strip().replaceAll("[ ]{2,}", " ");
            if (Pattern.matches(regexValidarPersona,cadena)) {
                vLineas.add(cadena);
            } else {
                vLineasErroneas.add(cadena);
            }
        }
        b.close();
        return vLineas;
    }

    /*
     * Método para leer un archivo de texto y descomponerlo en líneas, cada línea es limpiada de espacios
     * y es comparada contra una expresión regular para ver si cumple con las reglas de una operación de agregar
     * o remover amistad o si es una pregunta.
     *
     * @Param archivo Recibe como parámetro la ruta al archivo.
     * @Param return regresa un ArrayList conteniendo las líneas válidas
     */
    static ArrayList<String> cargarRelaciones(String archivo) throws FileNotFoundException, IOException {
        ArrayList<String> vLineas = new ArrayList<String>(); //(amigo|amigos|eliminar)
        String regexValidarAmigoPosPos = "\\d+\\s*(amigo|amigos|eliminar)\\s*\\d+";
        String regexValidarAmigoPosDatos = "\\d+\\s*(amigo|amigos|eliminar)\\s+([a-zA-Z\\s]{1,20})\\s*,\\s*([a-zA-Z\\s]{1,20})\\s*,\\s*([MmFf])\\s*,\\s*(\\d{2}/\\d{2}/\\d{4})";
        String regexValidarAmigoDatosPos = "([a-zA-Z\\s]{1,20})\\s*,\\s*([a-zA-Z\\s]{1,20})\\s*,\\s*([MmFf])\\s*,\\s*(\\d{2}/\\d{2}/\\d{4})\\s*(amigo|amigos|eliminar)\\s*\\d+";
        String regexValidarAmigoDatosDatos = "([a-zA-Z\\s]{1,20})\\s*,\\s*([a-zA-Z\\s]{1,20})\\s*,\\s*([MmFf])\\s*,\\s*(\\d{2}/\\d{2}/\\d{4})\\s*(amigo|amigos|eliminar)\\s+([a-zA-Z\\s]{1,20})\\s*,\\s*([a-zA-Z\\s]{1,20})\\s*,\\s*([MmFf])\\s*,\\s*(\\d{2}/\\d{2}/\\d{4})";
        String regexValidarAmigoPosNivel = "amigos\\s*\\d+\\s+\\d+";
        String regexValidarAmigoDatosNivel = "amigos\\s+([a-zA-Z\\s]{1,20})\\s*,\\s*([a-zA-Z\\s]{1,20})\\s*,\\s*([MmFf])\\s*,\\s*(\\d{2}/\\d{2}/\\d{4})\\s+\\d+";

        String cadena, cadenaOriginal = "";
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) {
            // Se anexa al arreglo la cadena solo si cumple con las reglas definidas para ser un dato válido
            // de lo contrario la agrego a un arraylist de líneas invalidas para posteriormente grabarlas en un
            // archivo de salida.
            //Elimina espacios vacios al inicio y al final de la línea
            //y también convierte a un solo espacio cualquier ocurrencia de 2 o más espacios
            cadena = cadena.strip().replaceAll("[ ]{2,}", " ");
            cadenaOriginal = cadena;
            cadena = cadena.toLowerCase();
            if ((contarOcurrencias("amigo",cadena) <= 1) && ((Pattern.matches(regexValidarAmigoPosPos,cadena)) || (Pattern.matches(regexValidarAmigoPosDatos,cadena)) || (Pattern.matches(regexValidarAmigoDatosPos,cadena))  || (Pattern.matches(regexValidarAmigoDatosDatos,cadena)) || (Pattern.matches(regexValidarAmigoPosNivel,cadena)) || (Pattern.matches(regexValidarAmigoDatosNivel,cadena)))) {
                vLineas.add(cadenaOriginal);
            } else {
                vLineasErroneas.add(cadenaOriginal);
            }
        }
        b.close();
        return vLineas;
    }

    public static void escribeArchivo(String archivo, ArrayList<String> vLineas)
    {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(archivo);
            pw = new PrintWriter(fichero);
            for (String l : vLineas) {
                pw.println(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    /*
     * Método para agregar a las personas obtenidas del catálogo al grafo.
     *
     * @Param vLineas es un ArrayList de las líneas de texto válidas del archivo fuente del catálogo de personas.
     */
    public static void procesaCatalogo(ArrayList<String> vLineas) {
        int cont = 1;
       String nombre, apellido = "";
        char sexo;
        LocalDate fecNacimiento;
        for (String a : vLineas) {
            String[] arrSplit = a.split(",");
            nombre = arrSplit[0];
            apellido = arrSplit[1];
            sexo = arrSplit[2].charAt(0);
            try {
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                fecNacimiento = LocalDate.parse(arrSplit[3], formato);
                gT.addNodo(new Grafo.Nodo(nombre, apellido, sexo, fecNacimiento, cont++));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /*
     * Método clave para el programa, ya que es el que analiza cada línea del archivo para
     * determinar si es es una operación (agregar o remover amistad) o una pregunta de si existe
     * amistad entre dos personas o listar los amigos a cierto nivel de una persona.
     *
     * @Param vLineas es un ArrayList de las líneas de texto válidas del archivo fuente.
     */

    public static void procesaRelaciones(ArrayList<String> vLineas) {
        String elemento1, elemento2, elemento3 = "";
        String separador ="";
        for (String a : vLineas) {
            ArrayList arrCadena = new ArrayList();
            TipoRelacion tipoRelacion = TipoRelacion.NINGUNA;
            int nivel = 0;
            Pattern regexParaSeparar = null; // = Pattern.compile("([A-z]([a-zA-Z0-9]){0,14})|(->|<-|-|=>|<=)|([.]|[?])");
            if (a.toLowerCase().contains("amigo") && !a.toLowerCase().contains("amigos")) {
                tipoRelacion = TipoRelacion.AGREGA_RELACION;
                separador = "amigo";
            } else if (a.toLowerCase().contains("amigos")) {
                if (a.toLowerCase().startsWith("amigos")) {
                    tipoRelacion = TipoRelacion.PREGUNTA_AMIGOS_DE_NIVEL;
                } else {
                    tipoRelacion = TipoRelacion.PREGUNTA_AMISTAD_DIRECTA;
                }
                separador = "amigos";
            } else if (a.toLowerCase().contains("eliminar")) {
                tipoRelacion = TipoRelacion.ELIMINA_RELACION;
                separador = "eliminar";
            }
            regexParaSeparar = Pattern.compile("(\\s*"+separador+"\\s*)|(([a-zA-Z\\s]{1,20})\\s*,\\s*([a-zA-Z\\s]{1,20})\\s*,\\s*([MmFf])\\s*,\\s*(\\d{2}/\\d{2}/\\d{4})\\s*)|(\\s*\\d+)|(d+\\s+)");

            Matcher matchPatron = regexParaSeparar.matcher(a);
            //Revisa la cadena y extrae cada componente. Los agrega a un ArrayList
            while(matchPatron.find()) {
                arrCadena.add(matchPatron.group()); //Agrega cada elemento al ArrayList
            }
            //System.out.println(" ***** Impresión de seguimiento ******");
            System.out.println("\n[*] Línea a procesar: " + a + " - Tipo: "+ tipoRelacion);
            //Obtiene cada elemento para procesarlos
            elemento1 = arrCadena.get(0).toString().trim();
            elemento2 = arrCadena.get(1).toString().trim();
            elemento3 = arrCadena.get(2).toString().trim();
            Grafo2V.Nodo nOrigen = null;
            Grafo2V.Nodo nDestino = null;
            if (tipoRelacion == TipoRelacion.PREGUNTA_AMIGOS_DE_NIVEL) { //Si es una pregunta de nivel de amigos el elemento 1 viene en la segunda posición
                elemento1 = arrCadena.get(1).toString().trim();
                nivel = Integer.parseInt(elemento3); //Nivel a revisar viene en elemento3
            } else { //En el resto de los casos elemento3 contiene la segunda persona
                if (elemento3.contains(",")) {
                    String[] arrSplit = elemento3.split(",");
                    try {
                        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate fecha = LocalDate.parse(arrSplit[3], formato);
                        nDestino = new Grafo2V.Nodo(arrSplit[0], arrSplit[1], arrSplit[2].charAt(0), fecha, -1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    nDestino = gT.buscarNodo(Integer.parseInt(elemento3));
                }
            }

            if (elemento1.contains(",")) {
                String[] arrSplit = elemento1.split(",");
                try {
                    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate fecha = LocalDate.parse(arrSplit[3], formato);
                    nOrigen = new Grafo2V.Nodo(arrSplit[0], arrSplit[1], arrSplit[2].charAt(0), fecha, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                nOrigen = gT.buscarNodo(Integer.parseInt(elemento1));
            }

            switch (tipoRelacion) {
                case AGREGA_RELACION:  //se trata de agregar una relación
                if (nOrigen != null && nDestino !=null) {
                    boolean seAgrego = gT.addArista(new Grafo2V.Arista(nOrigen, nDestino));
                }
                break;
                case PREGUNTA_AMISTAD_DIRECTA: //Determina si hay amistad directa entre dos personas
                try {
                    Grafo2V.Arista arista = gT.getArista(nOrigen,nDestino); //Marca excepción si no existe relación
                    System.out.println(nOrigen.toString() + " amigo de " + nDestino.toString() + ": Verdadero");
                } catch (Exception e) {
                    System.out.println(a + " Falso");
                }
                break;
                case PREGUNTA_AMIGOS_DE_NIVEL: //Obtiene la lista de amigos en cierto nivel
                try {
                    String listaAmigos = gT.obtenAmigosDeNivel(nOrigen, nivel);
                    System.out.println(listaAmigos);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
                case ELIMINA_RELACION: //se trata de eliminar una relación de amistad
                if (nOrigen != null && nDestino !=null) {
                    boolean seElimino = gT.removeArista(new Grafo2V.Arista(nOrigen, nDestino));
                }
                break;
            default:
                //
            }
        }
    }

    /*
     * Método principal de ejecución del programa de Amigos de mis amigos
     */
    public static void main(String[] args) {
        ArrayList<String> vectorPersonas = new ArrayList<String>(); //Contendra las líneas del archivo de texto.
        ArrayList<String> vectorRelaciones = new ArrayList<String>(); //Contendra las líneas del archivo de texto.
        System.out.println(gT.getNombre());
        System.out.println("**********Inicia Lectura del catálogo de personas**********");
        try {
             vectorPersonas = cargarCatalogo("src/catalogo.txt"); //Catálogo de personas
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("**********Inicio Líneas válidas del catálogo**********");
        int cont=1;
        for (String a : vectorPersonas) {
            System.out.println(cont++ + ".-" + a);
        }
        System.out.println("***********Fin  Líneas válidas  del catálogo***********");
        System.out.println("**********Inicia Lectura del listado de relaciones**********");
        String archivo;
        try {
            if (args.length > 0) { //si se recibió el archivo como parámetro
                archivo  = args[0];
            } else {
                JFileChooser jFC = new JFileChooser();
                jFC.setDialogTitle("Amigos - Seleccione el archivo de relaciones deseado");
                jFC.setCurrentDirectory(new File("C:/Leng/Maestría/TP/Amigos/src"));
                int res = jFC.showOpenDialog(null);
                if (res == JFileChooser.APPROVE_OPTION) {
                    archivo = jFC.getSelectedFile().getPath();
                } else {
                    archivo = "src/relaciones.txt";
                }
            }
            vectorRelaciones = cargarRelaciones(archivo); //Archivo de relaciones
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("**********Inicio Relaciones Válidas**********");
        for (String a : vectorRelaciones) {
            System.out.println(a);
        }
        System.out.println("***********Fin Relaciones Válidas***********\n");
        System.out.println("**********Inicio Líneas inválidas**********");
            for (String a : vLineasErroneas) {
            System.out.println(a);
        }
        System.out.println("***********Fin Líneas Inválidas***********\n");
        //Se almacenan las líneas inválidas en el archivo src/relaciones.txt
        escribeArchivo("src/Ignorado.txt",vLineasErroneas);
        //Se llama al método donde se procesa el catálogo y se muestran los resultados
        procesaCatalogo(vectorPersonas);
        System.out.println("\n**********Inicio evaluación de relaciones**********");
        //Se llama al método donde se procesa el archivo de relaciones y preguntas
        procesaRelaciones(vectorRelaciones);
        System.out.println("**********Termina evaluación de relaciones**********");
    }
}


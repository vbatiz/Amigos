# Los amigos de mis amigos.
Tarea 4<br>
Materia: Tecnologías de Programación de la MCC del ITC
<br>
### Tutor: Dra. María Lucía Barrón Estrada ##

#### Autor: Víctor Bátiz

#### Lista de entregables:
1. El código fuente se encuentra en la carpeta: src\Amigos (https://github.com/vbatiz/Amigos/tree/master/src/pAmigos)
2. El Diagrama UML final: DiagramaUML.png 
![alt text](https://github.com/vbatiz/Amigos/blob/master/diagramaUML.png)

#### Descripción
El presente proyecto es una implementación en Java del proyecto Los Amigos de mis Amigos utilizando Grafos. Esta desarrollado utilizando la consola como interfaz y utilizando el IDE IntelliJ IDEA Ultimate 2019.2.
<br><br>
En esta implementación se siguieron las siguientes indicaciones:
##### Descripción y Requisitos:
Para este proyecto se debe considerar un listado de personas que tienen 4 atributos (nombre, apellido, sexo y fecha de nacimiento). Las personas se almacenarán en un catálogo y se distinguirán por la posición donde se encuentran en el listado. En un archivo de texto, se almacena la información para crear y actualizar la red de amistad de las personas y resolver preguntas u obtener listados de amigos cuando estos se soliciten.
El nombre del archivo de texto debe leerse como entrada del programa. El archivo puede contener diferentes tipos de líneas como se muestra a continuación:
1. Línea definiendo relación de amistad directa entre dos personas (de acuerdo a su posición en el catálogo o con sus datos). La línea del archivo contiene tres secciones y se utiliza para crear un vínculo directo de amistad bidireccional entre las personas 1 y 2. Las líneas tienen los siguientes formatos:
PersonaPosición1 amigo PersonaPosición2
{nombre,apellido,sexo,fecha}1 amigo {nombre,apellido,sexo,fecha}2
2. Línea que elimina una relación de amistad y esta acción deberá verse reflejada en la red de amistades. Para eliminar una relación de amistad el archivo contiene líneas con el siguiente formato:
PersonaPosición1 eliminar PersonaPosición2
{nombre,apellido,sexo,fecha}1 eliminar {nombre,apellido,sexo,fecha}2
3. El archivo contiene líneas que representan una pregunta sobre si existe amistad entre dos personas. La respuesta del sistema debe ser únicamente sobre los amigos directos y será un valor boolean (verdadero o falso). El formato de la línea es el siguiente:
PersonaPosición1 amigos PersonaPosición2
{nombre,apellido,sexo,fecha}1 amigos {nombre,apellido,sexo,fecha}2
4. El archivo contiene líneas solicitando listado de amigos especificando el nivel de profundidad a revisar en la red de amistades. Este es un comando que deberá ser ejecutado dinámicamente y mostrará en pantalla el resultado de la consulta usando la información actual de la red. El formato de la línea es el siguiente:
PersonaPosición1 amigos numeroNivel
{nombre,apellido,sexo,fecha}1 amigos numeroNivel

El archivo puede contener cero o más líneas definiendo relaciones de amistad.
Las líneas pueden tener cualquier combinación de los datos de las personas PersonaPosición1 y {nombre,apellido,sexo,fecha}1
Las líneas que no tengan el formato deberán ser ignoradas almacenándolas en un archivo de texto llamado Ignorado.txt.
Las posiciones de las personas en el catálogo no están validadas.

#### Requisitos adicionales.

Tu programa debe contener comentarios que documenten la estructura usada así como también, los nombres de los autores y comentarios que documenten el código.
<br>

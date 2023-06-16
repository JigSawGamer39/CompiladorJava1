# CompiladorJava1
Proyecto de compilador con Java NetBeans 

## Detalles del compilador 
<p>
  El compilador fue creado para la materia de <strong>Lenguajes Automatas 2</strong>, tengo tiempo sin usarlo y <strong>necesita ser optimizado</strong>.
  
  Para su uso es necesario saber lo siguiente:
  1) Los archivos <strong>Automata.txt</strong> y <strong>Errores.txt</strong>
  
  >>Ambos archivos son necesarios para su funcionamiento, en el primero se coloca la estructura del código a analizar y en el segundo están guardados los mensajes de error que aparecerán en la consola al ejecutar el proyecto.
  
  2) El compilador realiza análisis sintáctico, léxico y semántico 
  3) Variables sin usar y rendundancia
  >> Como ya mencione anteriormente el proyecto necesita ser optimizados si se requiere expandir mas el análisis.
</p>
 
###   ¿Dónde inicia el análisis semántico?
<p>
Dentro del código nos encontraremos con las funciones <strong>AnalisisLexico</strong> y <strong>analizadorSint</strong> que como su nombre en español lo indica, en estas se lleva a cabo el análisis léxico y sintáctico respectivamente, pero el análisis semántico es llevado a cabo por varias funciones dentro de la función del analizador sintáctico.
</p>

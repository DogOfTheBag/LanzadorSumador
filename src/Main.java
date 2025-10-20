import java.io.*;

//TIP Para <b>ejecutar</b> el código, pulsar <shortcut actionId="Run"/> o
// Haz clic en el ícono <icon src="AllIcons.Actions.Execute"/> del margen.
public class Main {
    public static void main(String[] args) {


        /*Para primero ejecutar un programa que está en otra carpeta, deberemos ir al CMD, hacer un Change directory (CD)
        * y seleccionar la carpeta donde este el .java que queremos ejecutar. Una vez hecho eso hacemos un javac para compilarlo
        * y nos vamos aquí ya*/

        /*Al estar en otra carpeta, tendremos que pasarle la ruta de la carpeta exacta donde está el .class compilado
        * por ello debemos pasarle la ruta hasta el src (o donde este)*/
        String rutaHijo = "C:\\Users\\alber\\Documents\\IntelIJ Projects\\Sumador\\src";
        Process pb = null; //Esto lo ha hecho el intellij para manejar la excepcion, nada raro

        try {
            /*Primero creamos el nuevo proceso usando process builder, y le pasamos los comandos que queramos ejecutar
            * en ese caso queremos ejecutar con java la clase sumador, por lo que usamos java,
            * -cp (basicamente es el encargado de buscar el .class dentro del directorio que le vas a pasar), la ruta del proceso hijo y el nombre de la clase
            * por ultimo, comenzamos el proceso usando .start*/

            pb = new ProcessBuilder("java", "-cp", rutaHijo, "Sumador").start();

            /*EJEMPLO SI LE PASARAMOS ARGUMENTOS EN VEZ DE USAR EL SCANNER
            * pb = new ProcessBuilder("java", "-cp", rutaHijo, "Sumador","5","8").start();
            * Le ponemos los argumentos que le pasemos despues de poner la clase compilada
            * suponiendo que el propio programa ya maneja dentro de su codigo los argumentos*/
        } catch (IOException e) {
            throw new RuntimeException(e); //manejo automatico de excepciones
        }

        /*Puesto que yo lo he hecho con scanner pero asi automaticamente el proceso hijo siendo ejecutado asi no va a tener un espacio para teclear
        * tenemos que usar OutputStream para mandar cosas fuera
        * Usamos el getOutputStream del pb para conseguir el flujo de fuera del proceso, y después usamos un PrintWriter para pasarle
        * los numeros deseados.*/
        OutputStream os = pb.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        //con println le pasamos los dos numeros
        pw.println(7);
        pw.println(2);

        //flush lo que hace es enviar esos numeros por el stream (piensa en un vater, cuando tiras de la cadena (flush) se van las cosas por la tuberia)
        pw.flush();

        /*Después tendremos que leer lo que nos pone el proceso hijo, usando un Reader, que usa un lector de input de flujos, que cogera el flujo del hijo*/
        BufferedReader bf = new BufferedReader(new InputStreamReader(pb.getInputStream()));

        //esto ya es lo de siempre, leer lo que pone hasta que no haya nada mas
        String linea;
            try {
                while((linea = bf.readLine()) != null) {
                    System.out.println("Hijo dice:  " + linea);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        try {
            //Esto basaicamente lo que hace es que se espere a que el proceso marcado termine, y una vez termine ya se retoma este
            pb.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //esto es para ver si hemos tenido errores o no con el codigo, se podria usar getErrorStream para pillar el error en texto que nos mande
        System.out.println("Codigo de salida: " + pb.exitValue());


    }
}
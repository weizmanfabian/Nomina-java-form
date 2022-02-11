/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Recursos;


/**
 *
 * @author Fabian
 */
public class Configuracion {

    //Configuración de correo
    public static String asuntoDeIngreso = "Ingreso al Sistema";
    public static String mensajeDeIngreso = "Se usa este correo electrónico por medio del sistema DYD SOFTWRE \npor motivos de envio de nomina a los Trabajadores\n\nEste proceso se realiza el " + Utiles.obtenerFechaHoraActual();
    public static String correoDePrueba = "wfcastaneda3@misena.edu.co";
    public static int numeroDeHilos = 8; //La cantidad de correos que se enviaran al mismo tiempo (1, 2, 4, 8)

    //Configuración base de datos
    public static String nombreBaseDeDatos = "Nomina.mdb";
    public static String usuarioBaseDeDatos = "";
    public static String contraseniaBaseDeDatos = "Clave4321";

    //Configuración de campos de texto
    public static int numeroDeCaractaresNumericosEnElCodigo = 8;
    public static int numeroDeCaractaresNumericosEnElPuerto = 6;

}

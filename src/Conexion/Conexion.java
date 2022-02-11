/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import DAO.EmpleadoDAO;
import DTO.EmpleadoDTO;
import Recursos.Configuracion;
import Recursos.Utiles;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Fabian
 */
public final class Conexion {

    public static String nombre_bd;
    public static String usuarioBD;
    public static String passwordBD;
    public static Connection conexion;
    public static String driver;

    public static Statement statement;
    public static ResultSet resultSet;
    public static PreparedStatement prepStament;
    public static DatabaseMetaData databaseMetaData;

    EmpleadoDAO empleadoDAO;
    ArrayList<EmpleadoDTO> lista;

    public static void main(String[] args) {
        Conexion c = new Conexion();
    }

    public Conexion() {
        establecerConexion();
    }

//    public static boolean establecerConexion() {
//        boolean res = false;
//        nombre_bd = Configuracion.nombreBaseDeDatos;
//        usuarioBD = Configuracion.usuarioBaseDeDatos;
//        passwordBD = Configuracion.contraseniaBaseDeDatos;
//        String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
//        String url = "jdbc:ucanaccess://" + nombre_bd;
//        try {
//            if (conexion == null) {
//                Class.forName(driver);
//                System.out.println("Estableciendo conexion a base de datos...");
//                conexion = DriverManager.getConnection(url, usuarioBD, passwordBD);
//                System.out.println("Conexion establecida");
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return res;
//    }
    //base de datos encriptada
//    public static boolean establecerConexion() {
//        boolean res = false;
//        nombre_bd = Configuracion.nombreBaseDeDatos;
//        usuarioBD = Configuracion.usuarioBaseDeDatos;
//        passwordBD = Configuracion.contraseniaBaseDeDatos;
//        driver = "net.ucanaccess.jdbc.UcanaccessDriver";
//        String url = "jdbc:ucanaccess://" + nombre_bd + ";jackcessOpener=Config.CryptCodecOpener";
//        try {
//            if (conexion == null) {
//                Class.forName(driver);
//                System.out.println("Estableciendo conexion a base de datos...");
//                conexion = DriverManager
//                        .getConnection("jdbc:ucanaccess://" + nombre_bd + ";jackcessOpener=Recursos.CryptCodecOpener",
//                                usuarioBD, passwordBD);
//                if (conexion != null) {
//                    System.out.println("Conexion establecida");
//                }
//            }
//
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return res;
//    }
//    public static boolean establecerConexion() {
//        try {
//            nombre_bd = Configuracion.nombreBaseDeDatos;
//            usuarioBD = Configuracion.usuarioBaseDeDatos;
//            passwordBD = Configuracion.contraseniaBaseDeDatos;
//
//            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
//            System.out.println("Estableciendo conexión con la base de datos...");
//            conexion = DriverManager.getConnection("jdbc:ucanaccess://" + nombre_bd + ";memory=true");
//            System.out.println("Conexion establecida.");
//
//        } catch (SQLException ex) {
//            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
//            return false;
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return true;
//    }
    public static boolean establecerConexion() {
        try {
            nombre_bd = Configuracion.nombreBaseDeDatos;
            usuarioBD = Configuracion.usuarioBaseDeDatos;
            passwordBD = Configuracion.contraseniaBaseDeDatos;

            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            System.out.println("Estableciendo conexión con la base de datos...");
            conexion = DriverManager.getConnection("jdbc:ucanaccess://" + nombre_bd, usuarioBD, passwordBD);
            System.out.println("Conexion establecida.");

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    //    public static boolean establecerConexion() {
    //        try {
    //            nombre_bd = Configuracion.nombreBaseDeDatos;
    //            usuarioBD = Configuracion.usuarioBaseDeDatos;
    //            passwordBD = Configuracion.contraseniaBaseDeDatos;
    //
    //            File arquivo = new File(nombre_bd);
    //            // Verifica si el archivo existe
    //            if (!arquivo.exists()) {
    //                System.err.println("El archivo no existe!");
    //            }
    //            String database = "jdbc:ucanaccess://" + nombre_bd.trim();
    //            System.out.println("Estableciendo conexión con la base de datos...");
    //            // Realiza la conexion con la base de datos
    //            conexion = DriverManager.getConnection(database, usuarioBD, passwordBD);
    //            // Hace la lectura de los metadatos del Banco
    //            databaseMetaData = conexion.getMetaData();
    //            System.out.println("Conexion establecida.");
    //
    //        } catch (SQLException ex) {
    //            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
    //            return false;
    //        }
    //        return true;
    //    }
    //    public static boolean establecerConexion() {
    //        try {
    //            nombre_bd = Configuracion.nombreBaseDeDatos;
    //            usuarioBD = Configuracion.usuarioBaseDeDatos;
    //            passwordBD = Configuracion.contraseniaBaseDeDatos;
    //
    //            System.out.println("Conectando a Base de datos...");
    //            String dir = "jdbc:ucanaccess://" + nombre_bd;
    //            conexion = DriverManager.getConnection(dir, usuarioBD, passwordBD);
    //            System.out.println("Conexion Exitosa");
    //
    //        } catch (SQLException ex) {
    //            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
    //            return false;
    //        }
    //        return true;
    //    }

    public void cerrarConexion() {
        try {
            // Closing database connection
            if (conexion != null) {
                // and then finally close connection
                conexion.close();
                prepStament.close();
                statement.close();
                resultSet.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Conexion Cerrada exitosamente.");
    }

    public void cerrarObjetosDeConsulta() {
        System.out.println("Se cerraron los objetos de consulta...");
    }

    public static void exportarTxtParametrosDeBaseDeDatos(String nombreBd, String usuario, String contrasenia) {
        File f; //ser crea un nuevo archivo
        FileWriter w; //objeto que tiene como función escribir datos en un archivo
        PrintWriter wr; //para escribir en el archivo
        try {
            f = new File("configData.txt"); //para manipular al archivo, lo preparamos con un nombre
            if (!f.exists()) {
                f.createNewFile();
                w = new FileWriter(f, true); //permite escribir varias lineas de texto
                wr = new PrintWriter(w); //permite escribir una linea de texto

                //Escribimos en el archivo
                wr.println(nombreBd); //Nombre de la base de datos
                wr.println(usuario); //Usuario
                wr.println(contrasenia); //Contraseña

                //cerramos los objetos
                wr.close();
                w.close();

            } else {
                //si existe el archivo plano se elimina y se crea nuevamente
                Utiles.eliminarFichero(f);
                f.createNewFile();
                w = new FileWriter(f, true); //permite escribir varias lineas de texto
                wr = new PrintWriter(w); //permite escribir una linea de texto

                //Escribimos en el archivo
                wr.println(nombreBd); //Nombre de la base de datos
                wr.println(usuario); //Usuario
                wr.println(contrasenia); //Contraseña

                //cerramos los objetos
                wr.close();
                w.close();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error " + e);
        }
    }

    public static boolean leerParametrosDeBaseDeDatos() {
        String[] res = new String[3];
        // Fichero del que queremos leer
        File fichero = new File("configData.txt");
        Scanner s = null;

        try {
            // Leemos el contenido del fichero
            s = new Scanner(fichero);

            // Leemos linea a linea el fichero
            int i = 0;
            while (s.hasNextLine()) {
                String linea = s.nextLine(); 	// Guardamos la linea en un String
                res[i] = linea;
//                System.out.println(res[i]);      // Imprimimos la linea
                i++;
            }

//            for (String re : res) {
//                System.out.println(re);
//            }
            nombre_bd = res[0];
            usuarioBD = res[1];
            passwordBD = res[2];

        } catch (Exception ex) {
            System.out.println("Mensaje: " + ex.getMessage());
            return false;
        } finally {
            // Cerramos el fichero tanto si la lectura ha sido correcta o no
            try {
                if (s != null) {
                    s.close();
                }
            } catch (Exception ex2) {
                System.out.println("Mensaje 2: " + ex2.getMessage());
            }
        }
        return true;
    }

    //get and set
    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

}

package Mail;

import java.io.PrintStream;
import java.net.Socket;

public class ComprobarConexionInternet {

    public boolean test() {
        String dirWeb = "www.google.com";
        int puerto = 80;
        try {
            Socket s = new Socket(dirWeb, puerto);
            if (s.isConnected()) {
                System.out.println("Conexion establecida con la direccion: " + dirWeb + " a traves del puerto: " + puerto);
            }
        } catch (Exception e) {
            System.err.println("No se pudo establecer conexion con: " + dirWeb + " a traves del puerto: " + puerto);
            return false;
        }
        return true;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.


lectura de archivos
https://jarroba.com/lectura-escritura-ficheros-java-ejemplos/

 */
package Mail;

import Recursos.Utiles;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public final class Mail implements Serializable {

    public static String nombreEmpresa;
    public static String servidorSMTP;
    public static String puerto;
    public static String userMail;
    public static String pass;

    String destinatario;
    String asunto;
    String mensaje;
    String rutaAbsolutaDelPdf;
    boolean eliminaArchivoAdjunto;

    public Mail() {

    }

    public Mail(String destinatario, String asunto, String mensaje) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        send(this.destinatario, this.asunto, this.mensaje);
    }

    public Mail(String destinatario, String asunto, String mensaje, String rutaAbsolutaDelPdf, boolean eliminaArchivoAdjunto) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.rutaAbsolutaDelPdf = rutaAbsolutaDelPdf;
        this.eliminaArchivoAdjunto = eliminaArchivoAdjunto;
        send(this.destinatario, this.asunto, this.mensaje, this.rutaAbsolutaDelPdf, this.eliminaArchivoAdjunto);
    }

    public static boolean escribirArchivoPlano(File file, String nombreEmpresa, String servidorSMTP, String puerto, String userMail, String pass) {
        FileWriter w = null; //objeto que tiene como función escribir datos en un archivo
        PrintWriter wr = null; //para escribir en el archivo
        try {
            file.createNewFile();
            w = new FileWriter(file, true); //permite escribir varias lineas de texto
            wr = new PrintWriter(w); //permite escribir una linea de texto

            //Escribimos en el archivo
            wr.println(nombreEmpresa); //Nombre de la empresa
            wr.println(servidorSMTP); //servidor
            wr.println(puerto); //puerto
            wr.println(userMail); //Correo Electrónico
            wr.println(pass); //Contraseña
            wr.close();
            w.close();

        } catch (IOException ex) {
            System.out.println("IOException escribirArchivoPlano " + ex);
            return false;
        } finally {
            System.out.println("Se escribieron los parámetros de correo correctamente.");
        }
        return true;
    }

    public static void exportarTxtParametrosDeCorreo(String nombreEmpresa, String servidorSMTP, String puerto, String userMail, String pass) {
        System.out.println("Exportanto parámetros de Correo...");
        File f; //ser crea un nuevo archivo
        try {
            f = new File("configMail.txt"); //para manipular al archivo, lo preparamos con un nombre
            if (!f.exists()) {
                escribirArchivoPlano(f, nombreEmpresa, servidorSMTP, puerto, userMail, pass);
            } else {
                //si existe el archivo plano se elimina y se crea nuevamente
                Utiles.eliminarFichero(f);
                escribirArchivoPlano(f, nombreEmpresa, servidorSMTP, puerto, userMail, pass);
            }
        } catch (Exception e) {
            System.out.println("Exception exportarTxtParametrosDeCorreo " + e);
        } finally {
            System.out.println("Se exportaron los parámetros de correo correctamente.");
        }

    }

    public static boolean leerParametrosDeCorreo() {
        String[] res = new String[5];
        // Fichero del que queremos leer
        File fichero = new File("configMail.txt");
        Scanner s = null;
        try {
            System.out.println("Leyendo parámetos de Correo...");

            // Leemos el contenido del fichero
            s = new Scanner(fichero);

            // Leemos linea a linea el fichero
            int i = 0;
            while (s.hasNextLine()) {
                String linea = s.nextLine(); 	// Guardamos la linea en un String
                res[i] = linea;
                i++;
            }
            nombreEmpresa = res[0];
            servidorSMTP = res[1];
            puerto = res[2];
            userMail = res[3];
            pass = res[4];

            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            s.close();
        }
    }

    public boolean send(String destinatario, String asunto, String mensaje) {

        try {
            System.out.println("Enviando Correo...");

            String nuevoMensaje = "<h1 style='font-size: 20px; color:black; font-weight: bold; text-transform: uppercase;'></h1>"
                    + "<p>" + mensaje + "</p><br>\n"
                    + "<p style='text-align: center; color: black'>\n</p> \n"
                    + "<br>\n"
                    + "<p style=\"color:black; font-size: 20px\" >Att, <br/><br/> " + nombreEmpresa + "  </p> ";

            Properties props = new Properties();

            props.put("mail.smtp.host", servidorSMTP); //El servidor de correo SMTP
            props.put("mail.smtp.user", userMail); //remitente
            props.put("mail.smtp.clave", pass);    //La clave de la cuenta
            props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
            props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
            props.put("mail.smtp.port", puerto); //puerto por el que se envia

            SecurityManager security = System.getSecurityManager();

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userMail, pass);
                }
            });

            BodyPart texto = new MimeBodyPart();
            texto.setContent(nuevoMensaje, "text/html");

            MimeMultipart multiparte = new MimeMultipart();
            multiparte.addBodyPart(texto);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userMail, nombreEmpresa));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(asunto);
            message.setContent(multiparte, "text/html; charset=utf-8");

            //3rd paso)send message
            Transport.send(message);
            System.out.println("Correo Enviado con éxito");
            return true;

        } catch (MessagingException e) {
            System.out.println("MessagingException send(normal) " + e);
            return false;
        } catch (UnsupportedEncodingException ex) {
            System.out.println("UnsupportedEncodingException send(normal) " + ex);
            return false;
        }
    }

    public boolean send(String destinatario, String asunto, String mensaje, String rutaAbsolutaDelPdf, boolean eliminaArchivoAdjunto) {
        boolean res = false;
        try {
            System.out.println("Enviando Correo...");

            String nuevoMensaje = "<h1 style='font-size: 20px; color:black; font-weight: bold; text-transform: uppercase;'></h1>"
                    + "<p>" + mensaje + "</p><br>\n"
                    + "<p style='text-align: center; color: black'>\n</p> \n"
                    + "<br>\n"
                    + "<p style=\"color:black; font-size: 20px\" >Att, <br/><br/> " + nombreEmpresa + "  </p> ";

            Properties props = new Properties();

            props.put("mail.smtp.host", servidorSMTP); //El servidor de correo SMTP
            props.put("mail.smtp.user", userMail); //remitente
            props.put("mail.smtp.clave", pass);    //La clave de la cuenta
            props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
            props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
            props.put("mail.smtp.port", puerto); //puerto por el que se envia

            SecurityManager security = System.getSecurityManager();

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userMail, pass);
                }
            });

            //Archivos adjuntos
            BodyPart texto = new MimeBodyPart();
            texto.setContent(nuevoMensaje, "text/html");

            BodyPart adjunto = new MimeBodyPart();
            MimeMultipart multiparte = new MimeMultipart();
            multiparte.addBodyPart(texto);

            //Validamos, si viene el nombre del pdf entonces adjuntamos el archivo de lo contrario NO
            if (rutaAbsolutaDelPdf != null) {
                System.out.println(rutaAbsolutaDelPdf);
                //ruta del archivo *.pdf que se descargó para adjuntarlo y enviarlo por correo
                adjunto.setDataHandler(new DataHandler(new FileDataSource(rutaAbsolutaDelPdf)));
                adjunto.setFileName(Utiles.verNombrePdf(rutaAbsolutaDelPdf));
                multiparte.addBodyPart(adjunto);
            }
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userMail, nombreEmpresa));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(asunto);
            message.setContent(multiparte, "text/html; charset=utf-8");

            //3rd paso)send message
            Transport.send(message);

            //Eliminación de pdf que se descargó y se acabó de adjuntar en el correo para eso le pasamos la ruta donde esta el pdf
            if (rutaAbsolutaDelPdf != null && eliminaArchivoAdjunto == true) {
                Utiles.eliminarFicheroPorRuta(rutaAbsolutaDelPdf);
            }
            res = true;

        } catch (MessagingException e) {
            System.out.println("MessagingException send(con archivo adjunto) " + e);
            res = false;
        } catch (UnsupportedEncodingException ex) {
            System.out.println("UnsupportedEncodingException send(con archivo adjunto) " + ex);
            res = false;
        } finally {
            System.out.println("Correo Enviado con éxito");
        }
        return res;
    }

    private class GMailAuthenticator extends Authenticator {

        String user;
        String pw;

        public GMailAuthenticator(String username, String password) {
            super();
            this.user = username;
            this.pw = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, pw);
        }
    }

}

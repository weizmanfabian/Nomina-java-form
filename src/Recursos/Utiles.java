package Recursos;

/*
numeros aleatorios entre un rango
http://www.forosdelweb.com/f45/rellenar-array-con-numeros-aleatorios-sin-repetir-498606/

regex
https://www.discoduroderoer.es/ejercicios-propuestos-y-resueltos-expresiones-regulares-en-java/
 */
import Mail.ComprobarConexionInternet;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Fabian
 *
 */
public class Utiles {

    public static String obtenerDirectorioActual() {
        return System.getProperty("user.dir"); //obtenemos el directorio actual
    }

    public static void eliminarFichero(File fichero) {

        if (!fichero.exists()) {
            System.out.println("El archivo no existe.");
        } else {
            fichero.delete();
            System.out.println("El archivo fue eliminado.");
        }

    }

    public static void eliminarFicheroPorRuta(String ruta) {
        File fichero = new File(ruta);
        if (!fichero.exists()) {
            System.out.println("El archivo no existe.");
        } else {
            fichero.delete();
            System.out.println("El archivo fue eliminado.");
        }

    }

    public static String enmascararCantidad(String num) {
        String numSal = "";
        int tamanio = num.length();
        int cont = 0;
        int punto = 0;

        for (int i = tamanio - 1; i > -1; i--) {
            if (num.charAt(i) == '.' && cont != 0) {
                numSal = num + "0";
                punto++;
            }
            if (punto == 0) {
                numSal = num + ".00";
            }

            cont++;
        }
        return numSal;
    }

    public static String enmascararCedula(String num) {
        String numSal = "";
        int tamanio = num.length();
        int cont = 0;

        for (int i = tamanio - 1; i > -1; i--) {

            if (cont == 3 || cont == 6 || cont == 9 || cont == 12 || cont == 15 || cont == 18 || cont == 21) {
                numSal = num.charAt(i) + "." + numSal;
            } else {
                numSal = "" + num.charAt(i) + "" + numSal;
            }

            cont++;
        }
        return numSal;
    }

    public static String enmascararNumeroDouble(double num) {
        String newNum = "" + num;
        String numSal = "";
        int tamanio = newNum.length();
        int cont = 0;
        int punto = 0;
        int valPunto = 0;
        int coma = 0;
        //Sabremos cuantos puntos tiene el numero
        for (int i = 0; i < tamanio - 1; i++) {
            if (newNum.charAt(i) == '.') {
                valPunto++;
            }
        }
        for (int i = tamanio - 1; i > -1; i--) {
            if (punto != 0 || valPunto == 0) {
                coma++;
            } else if (newNum.charAt(i) == '.') {
                if (cont != 2) {
                    numSal = numSal + "0";
                }
                punto++;
            }
            if (coma == 3 + 1 || coma == 6 + 1 || coma == 9 + 1 || coma == 12 + 1) {
                numSal = "," + numSal;
            }
            numSal = "" + newNum.charAt(i) + "" + numSal;
            cont++;
        }
        return numSal;
    }

    public static String enmascararNumeroGrande(String num) {
        String sal = "";
        boolean p = tienePuntosElNumero(num);
        boolean pv = false;
        int contador = 0;
        int newCont = 0;
        for (int i = num.length() - 1; i > -1; i--) {
            if (num.charAt(i) == '.') {
                sal = num.charAt(i) + sal;
                pv = true;
                if (sal.length() > 3) {
                    sal = "." + sal.charAt(1) + (sal.charAt(2));
                }
                String e = sal.substring(sal.lastIndexOf("."));//aquí recupero únicamente los números decimales con el punto
                if (e.length() == 2) {
                    sal = "." + sal.charAt(1) + "0";
                }
            } else {
                if (p) {
                    if (pv) {
                        newCont++;
                    }
                    sal = num.charAt(i) + sal;
                    if (newCont == 3 || newCont == 6 || newCont == 9 || newCont == 12 || newCont == 15) {
                        sal = (contador == num.length() - 1) ? sal : "," + sal;
                    }
                } else {
                    sal = num.charAt(i) + sal;
                    if (contador == 2 || contador == 5 || contador == 8 || contador == 11 || contador == 14 || contador == 17 || contador == 20) {
                        sal = (contador == num.length() - 1) ? sal : "," + sal;
                    }
                }
            }
            contador++;
        }
        sal = (!p) ? sal + ".00" : sal;
        return sal;
    }

    public static String enmascararNumeroLong(long num) {
        String newNum = "" + num;
        String numSal = "";
        int tamanio = newNum.length();
        int cont = 0;
        int punto = 0;
        int valPunto = 0;
        int coma = 0;
        //Sabremos cuantos puntos tiene el numero
        for (int i = 0; i < tamanio - 1; i++) {
            if (newNum.charAt(i) == '.') {
                valPunto++;
            }
        }
        for (int i = tamanio - 1; i > -1; i--) {
            if (punto != 0 || valPunto == 0) {
                coma++;
            } else if (newNum.charAt(i) == '.') {
                if (cont != 2) {
                    numSal = numSal + "0";
                }
                punto++;
            }
            if (coma == 3 + 1 || coma == 6 + 1 || coma == 9 + 1 || coma == 12 + 1) {
                numSal = "," + numSal;
            }
            numSal = "" + newNum.charAt(i) + "" + numSal;
            cont++;
        }
        return numSal;
    }

    public static String formatearFechaLargaMMddYYYY(Date fecha) {
        SimpleDateFormat formatador = new SimpleDateFormat("MMMMM' 'dd' de 'yyyy' '");
        return formatador.format(fecha);
    }

    public static String formatearFechaLargaddMMyyyy(Date fecha) {
        SimpleDateFormat formatador = new SimpleDateFormat("dd' de 'MMMMM' de 'yyyy' '");
        return formatador.format(fecha);
    }

    public static String formatearFechaLarga(Date fecha) {
        SimpleDateFormat formatador = new SimpleDateFormat("EEEEEEEEE dd 'de' MMMMM 'de' yyyy");
        return formatador.format(fecha);
    }

    public static String formatearFechayyyyMMddGuion(Date fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(fecha);
    }

    public static String formatearFechayyyyMMddSlash(Date fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(fecha);
    }

    public static Date obtenerFechaActual() {
        Date f = new Date();
        return f;
    }

    public static String obtenerFechaHoraActual() {
        return obtenerFechaProceso() + " " + obtenerHoraActual();
    }

    public static String obtenerFechaProceso() {
        Date f = new Date();
        return formatearFechayyyyMMddSlash(f);
    }

    public static Date obtenerFechaQuincenaPasada() {
        Date f = new Date();
        if (f.getDate() > 15) {
            f.setDate(15);
        } else {
            Date fv = new Date();
            if (f.getMonth() == 0) {
                f.setYear(f.getYear() - 1);
                f.setMonth(11);
                f.setDate(31);
            } else {
                f.setMonth(f.getMonth() - 1);
                Calendar c = Calendar.getInstance();
                c.setTime(f);
                int d = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                f.setDate(d);
            }
        }
        return f;
    }

    public static Date obtenerFechaSiguienteQuincena() {
        Date f = new Date();
        if (f.getDate() > 15) {
            Calendar cal = Calendar.getInstance();
            int d = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            f.setDate(d);
        } else {
            f.setDate(15);
        }
        return f;
    }

    public static String obtenerHoraActual() {
        int h, m, s;
        String hh, mm, ss;
        h = fechaActual.get(Calendar.HOUR_OF_DAY);
        m = fechaActual.get(Calendar.MINUTE);
        s = fechaActual.get(Calendar.SECOND);
        hh = (h < 10) ? "0" + h : "" + h;
        mm = (m < 10) ? "0" + m : "" + m;
        ss = (s < 10) ? "0" + s : "" + s;
        return hh + ":" + mm + ":" + ss;
    }

    public static String quitarLetraEneACodigoCentroDeCosto(String codigo) {
        return codigo.substring(1, codigo.length());
    }

    public static String quitarSlachInvertidoARuta(String ruta) {
        String sal = "";
        try {
            int size = ruta.length();
            for (int i = size - 1; i > -1; i--) {
                if (ruta.charAt(i) == '\\') {
                    sal = "/" + sal;
                } else {
                    sal = ruta.charAt(i) + sal;
                }
            }
            System.out.println("Ruta sin slach invertido: " + sal);
        } catch (Exception e) {
        }
        return sal;
    }

    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
        resultado = Math.round(resultado);
        resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;

        return resultado;
    }

    public static boolean tienePuntosElNumero(String num) {
        boolean res = false;
        for (int i = 0; i < num.length(); i++) {
            if (num.charAt(i) == '.') {
                res = true;
            }
        }
        return res;
    }

    public static boolean validarIsNumeric(String str) {
        return str.matches(expresionIsNumeric);
    }

    public static boolean validarNumeroConXDecimales(String str, int numDecimales) {
        return str.matches("^-?[0-9]+([\\.,][0-9]{1," + numDecimales + "})?$");
    }

    public static boolean validarNumerosEnterosConDecimales(String numero) {
        try {
            return Pattern.matches(expresionNumerosEnterosConDecimales, numero);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean validarNumerosEnterosConXDecimales(String str, int numDecimales) {
        return str.matches("^[0-9]+(.[0-9]{1," + numDecimales + "})?$");
    }

    public static boolean validarNumerosEnterosSinDecimales(String numero) {
        try {
            return Pattern.matches(expresionNumerosEnterosSinDecimales, numero);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean validateEmail(String email) {
        try {
            // Compiles the given regular expression into a pattern.
            Pattern pattern = Pattern.compile(expresionEmail);
            // Match the given input against this pattern
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String verCodigoCentroDeCosto(String num) {
        String numSal = "";
        int size = num.length();
        for (int i = 0; i < size; i++) {
            if (num.charAt(i) == ' ') {
                break;
            } else {
                numSal = numSal + num.charAt(i);
            }
        }
        return numSal;
    }

    public static String verNombrePdf(String ruta) {
        String sal = "";
        try {
            int size = ruta.length();
            for (int i = size - 1; i > -1; i--) {
                if (ruta.charAt(i) == '\\') {
                    break;
                } else {
                    sal = ruta.charAt(i) + sal;
                }
            }
        } catch (Exception e) {
        }
        return sal;
    }

    public static boolean verificarConexionInternet() {
        boolean conexionInternet = false;
        ComprobarConexionInternet google = new ComprobarConexionInternet();
        conexionInternet = google.test();

        if (conexionInternet) {
            System.out.println("hay conexion");
            return conexionInternet;

        } else {
            System.out.println("No hay conexion a internet");
            return conexionInternet;
        }
    }

    public static final String expresionEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String expresionIsNumeric = "^-?[0-9]+([\\.,][0-9]+)?$";
    public static final String expresionNumerosEnterosConDecimales = "^[^-]*(?:-(?!\\d)[^-]*)*$";
    public static final String expresionNumerosEnterosSinDecimales = "[0-9]*";
    public static Calendar fechaActual = Calendar.getInstance();

}

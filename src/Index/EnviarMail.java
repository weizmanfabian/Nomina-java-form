/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

tipos de letras y tamaño en PdfTable
http://java-white-box.blogspot.com/2013/11/itext-itext-font-como-cambiar-el-tamano.html

Alineación de PdfContentByte
https://developers.itextpdf.com/examples/tables/indentation-cells

jCalendar
http://www.java2s.com/Code/Jar/j/Downloadjcalendar14jar.htm

fechas
https://es.stackoverflow.com/questions/3194/diferencias-entre-date-datetime-y-calendar-en-java-7
https://programacionextrema.com/2015/10/22/cambiar-el-formato-a-fechas-en-java/
https://es.stackoverflow.com/questions/41936/java-como-cambiar-el-formato-de-fecha-de-un-jdatechooser-y-compararla-con-la-f
https://undocumentedmatlab.com/blog/date-selection-components
https://www.youtube.com/watch?v=JhgWp8Ln5AE

http://datojava.blogspot.com/2015/11/jcalendarJavaSwingEjemploTutorial.html

https://es.wikihow.com/comparar-dos-fechas-en-Java
convertir Calendar a Date
http://alxvelazquez.blogspot.com/2009/12/how-to-convert-calendar-to-date-como.html

carpetas
http://supermavster.com/post/java-create-files-and-folders

jtable
https://stackoverrun.com/es/q/1914049

Seleccion de filas y no editable
https://es.stackoverflow.com/questions/43291/seleccionar-toda-una-fila-jtable-java

Ordenar columnas jtable
http://chuwiki.chuidiang.org/index.php?title=JTable:_Ordenar_y_filtrar_filas

modales de confirmaciòn
https://www.mkyong.com/swing/java-swing-how-to-make-a-confirmation-dialog/


sobre-poner combobox
https://stackoverflow.com/questions/13197257/jcombobox-hidden-behind-awt-canvas

Paneles
https://foro.elhacker.net/java/cambiar_titulo_titledborder_en_jpanel-t323986.0.html

jdateChooser
https://www.lawebdelprogramador.com/foros/Java/1492322-FOCUS-EN-JDATECHOOSER.html

seleccionar texto en jtexfield
http://carlitoxenlaweb.blogspot.com/2010/09/seleccionar-todo-de-un-jtextfield.html

cursor cambio
https://blog.davidpachecojimenez.com/cambiar-cursor-por-defecto-de-java/


validación de solo numeros en un jtextfield
http://chuwiki.chuidiang.org/index.php?title=JTextField_que_solo_admite_digitos

 */
package Index;

import Recursos.HeaderRenderer;
import Conexion.Conexion;
import DAO.AcunoveDAO;
import DAO.EmpleadoDAO;
import DAO.ParametroDAO;
import DTO.AcunoveDTO;
import DTO.EmpleadoDTO;
import DTO.ParametroDTO;
import Mail.Mail;
import Recursos.Configuracion;
import static Recursos.Configuracion.numeroDeHilos;
import Recursos.Utiles;
import static Recursos.Utiles.enmascararCantidad;
import static Recursos.Utiles.enmascararNumeroDouble;
import static Recursos.Utiles.formatearFechayyyyMMddSlash;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Fabian
 */
public class EnviarMail extends javax.swing.JFrame implements Serializable, Runnable {

    // Fonts definitions (Definición de fuentes).
    private static final Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 26, Font.BOLDITALIC);
    private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
    private static final Font categoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font subcategoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static final Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static final Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    //Font Personalizadas
    private static final Font fontCourierBold = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);
    private static final Font fontCourierNormal = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL);

    private Conexion conexion;
    private ResultSet resultSet = null;
    private String fechaInicialDeQuincena;
    String fechaPagoNomPdf;
    private String fechaDePago;
    private Date fecha;
    public static int anioGuardado;
    public static Date fechaGuardada;
    JTextFieldDateEditor editor;
    JTextFieldDateEditor editorAnioChooser;
    private String codEmp;
    public static String codIniEm;
    public static String codFinEm;
    public static int numeroEmpleado;
    public static int tipoNovedad;
    public static int tipoBusqueda;
    public static int inicio;
    private AcunoveDTO acunoveDTO;
    private ArrayList<AcunoveDTO> listaAcunove;
    private AcunoveDAO acunoveDAO;

    private EmpleadoDTO empleadoDTO;
    EmpleadoDAO empleadoDAO;
    public ArrayList<EmpleadoDTO> listaEmpleados;
    public ArrayList<EmpleadoDTO> listaEmpleadosSelect;
    public ArrayList<EmpleadoDTO> listaEmp;

    private ParametroDTO parametroDTO;
    ParametroDAO parametroDAO;
    ArrayList<ParametroDTO> listaCentrosDeCostos;

    private double totalAcunove;
    private double totalDescuentos;
    private double totalPagos;

    //Tabla de vista
    String titulo[] = {"Código", "Nombre", "CC", "Último Sueldo", "Centro de Costo", "Correo"};
    String contenidoBlanco[][] = null;
    private DefaultTableModel dtm;
    private DefaultTableCellRenderer dtcr;
    TableColumnModel columnModel;
    JTableHeader header;

    //Tabla novedades
    String tituloTablaNovedades[] = {"TIPO NOVEDAD", "CANT", "VLR UNI", "VLR TOTAL", "CANT", "VLR UNI", "VLR TOTAL"};
    String contenidoVacioNovedades[][] = null;
    private DefaultTableModel dtmNovedades;

    //Tabla pdf
    float[] anchoColumnas = {6, 2, 4, 4, 2, 4, 4};
    PdfPTable table = new PdfPTable(anchoColumnas);

    //Action LIstener del editor de fecha
    MiClase miClase = new MiClase();

    Mail mail;

    int contador;
    public static boolean mailAut = false;
    public static boolean dataAut = false;

    //Constructor
    public EnviarMail(boolean correo) {
        try {
            initComponents();
            cargarCursor(true);
            //desactiva el boton de maximizar en la ventana jframe
            this.setResizable(false);
            //desactiva el boton de la X (cerrar ventanta)
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            //desactiva el boton de maximizar en la ventana jframe
            this.setResizable(false);
            this.setTitle("Envío de Nómina por Correo Electrónico - " + Utiles.formatearFechaLarga(Utiles.obtenerFechaActual()));
            establecerFechaAMostrar();
            inicializarTablasDeVista();
            //Validar cada campo de texto
            habilitarElemetosDeVista(false);
            lblF3.setVisible(false);
            mail = new Mail();

            if (inicio == 0 && !correo) { //si apenas inicia y no se ha autenticado el coreo
                setVisible(true);
                iniciarHiloConexion();
                iniciarHiloMail();
                lblF3.setVisible(false);
                if (Utiles.fechaActual.get(Calendar.MONTH) > 5) {
                    radioButtonJunio.setSelected(true);
                } else {
                    anioChooser.setYear(Utiles.fechaActual.get(Calendar.YEAR) - 1);
                    radioButtonDiciembre.setSelected(true);
                }
                hiloConexion.join();
            }
            seleccionarFilaTeclado();
            inicio += 1;

            this.acunoveDAO = new AcunoveDAO();
            this.empleadoDAO = new EmpleadoDAO();
            this.parametroDAO = new ParametroDAO();
            this.acunoveDTO = new AcunoveDTO();
            this.empleadoDTO = new EmpleadoDTO();
            this.parametroDTO = new ParametroDTO();

            this.listaAcunove = new ArrayList<>();
            this.listaCentrosDeCostos = new ArrayList<>();
            this.listaEmpleados = new ArrayList<>();
            this.listaEmpleadosSelect = new ArrayList<>();
            this.listaEmp = new ArrayList<>();

            validarEscrituraEnTxt();
            contador = 0;
            habilitarElemetosDeVista(true);
            lblF3.setVisible(false);
            aplicarFormatoALaVista();
            cargarCursor(false);

        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException EnviarMail " + ex);
        } catch (SQLException ex) {
            System.out.println("SQLException EnviarMail " + ex);
        } catch (InterruptedException ex) {
            System.out.println("InterruptedException EnviarMail " + ex);
        }
    }

    public void abrirModal() {
        try {
            if (numeroEmpleado == 1) {
                codIniEm = (empleadoDAO.existeEmpleado(txtCodIniEm.getText())) ? txtCodIniEm.getText() : "";
            } else if (numeroEmpleado == 2) {
                codFinEm = (empleadoDAO.existeEmpleado(txtCodFinEm.getText())) ? txtCodFinEm.getText() : "";
            }
            tipoBusqueda = (radioButtonPorCodigo.isSelected()) ? 2
                    : (radioButtonCentroDeCosto.isSelected()) ? 1
                    : 0;
            tipoNovedad = (radioButtonPagoQuincena.isSelected()) ? 1
                    : (radioButtonPrima.isSelected()) ? 2
                    : 3;
            anioGuardado = anioChooser.getYear();
            fechaGuardada = calFecha.getDate();
            this.setVisible(false);
            SelectEm dialog = new SelectEm(new javax.swing.JFrame(), true);
            dialog.setVisible(true);
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException abrirModal " + ex);
        } catch (SQLException ex) {
            System.out.println("SQLException abrirModal " + ex);
        }
    }

    private void aplicarDisenioTablaEmpleados() {

        //-------------------------------------------TABLA EMPLEADOS-----------------------------------------------------------------
        // Instanciamos el TableRowSorter y lo añadimos al JTable para que me ordene por columna
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(dtm);
        tblConsulta.setRowSorter(elQueOrdena);

        dtcr = new DefaultTableCellRenderer();
        columnModel = tblConsulta.getColumnModel();

        //Ajustamos el tamaño de las columnas en pixeles
        columnModel.getColumn(0).setPreferredWidth(110);
        columnModel.getColumn(1).setPreferredWidth(300);
        columnModel.getColumn(2).setPreferredWidth(140);
        columnModel.getColumn(3).setPreferredWidth(140);
        columnModel.getColumn(4).setPreferredWidth(300);
        columnModel.getColumn(5).setPreferredWidth(300);

        // Alineamos el texto de la tabla a las columnas que queremos (matriz)
        dtcr.setHorizontalAlignment(SwingConstants.RIGHT);
        tblConsulta.getColumnModel().getColumn(2).setCellRenderer(dtcr);
        tblConsulta.getColumnModel().getColumn(3).setCellRenderer(dtcr);

        //Alinear Encabezado
        int[] alignments = new int[]{JLabel.LEFT, JLabel.LEFT, JLabel.RIGHT, JLabel.RIGHT, JLabel.LEFT, JLabel.LEFT};
        for (int i = 0; i < tblConsulta.getColumnCount(); i++) {
            tblConsulta.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(new HeaderRenderer(tblConsulta, alignments[i]));
        }

        //permitir selección de filas y prohibir selección de columnas
        tblConsulta.setRowSelectionAllowed(true);
        tblConsulta.setColumnSelectionAllowed(false);

        //mostrar simpre scrollBar
//        final Display display = new Display();
//        final Shell shell = new Shell(display);
//        final ScrolledComposite composite = new ScrolledComposite(shell, SWT.V_SCROLL);
    }

    private void aplicarDisenioTablaNovedades() {

        //---------------------------------------------TABLA NOVEDADES---------------------------------------------------------------
        dtcr = new DefaultTableCellRenderer();
        columnModel = tblNovedades.getColumnModel();

        //Ajustamos el tamaño de las columnas en pixeles
        columnModel.getColumn(0).setPreferredWidth(300);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(3).setPreferredWidth(200);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(200);
        columnModel.getColumn(6).setPreferredWidth(200);

        // Alineamos el texto de la tabla a las columnas que queremos (matriz)
        dtcr.setHorizontalAlignment(SwingConstants.RIGHT);
        tblNovedades.getColumnModel().getColumn(1).setCellRenderer(dtcr);
        tblNovedades.getColumnModel().getColumn(2).setCellRenderer(dtcr);
        tblNovedades.getColumnModel().getColumn(3).setCellRenderer(dtcr);
        tblNovedades.getColumnModel().getColumn(4).setCellRenderer(dtcr);
        tblNovedades.getColumnModel().getColumn(5).setCellRenderer(dtcr);
        tblNovedades.getColumnModel().getColumn(6).setCellRenderer(dtcr);

        //Alinear Encabezado
        int[] alignments = new int[]{JLabel.LEFT, JLabel.RIGHT, JLabel.RIGHT, JLabel.RIGHT, JLabel.RIGHT, JLabel.RIGHT, JLabel.RIGHT};
        for (int i = 0; i < tblNovedades.getColumnCount(); i++) {
            tblNovedades.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(new HeaderRenderer(tblNovedades, alignments[i]));
        }

        //permitir selección de filas y prohibir selección de columnas
        tblNovedades.setRowSelectionAllowed(true);
        tblNovedades.setColumnSelectionAllowed(false);
    }

    private void aplicarFormatoALaVista() {

        if (anioGuardado < Utiles.fechaActual.get(Calendar.YEAR) && anioGuardado > 1000) {
            anioChooser.setYear(anioGuardado);
        }
        if (fechaGuardada != null) {
            fecha = fechaGuardada;
        }
        //Seleccion de consulta
        switch (tipoNovedad) {
            case 0:
                radioButtonPagoQuincena.setSelected(true);
                lblFecha.setVisible(true);
                radioButtonDiciembre.setVisible(false);
                radioButtonJunio.setVisible(false);
                lblAnio.setVisible(false);
                anioChooser.setVisible(false);
                txtCodIniEm.requestFocus();
                break;
            case 1:
                radioButtonPagoQuincena.setSelected(true);
                System.out.println(numeroEmpleado + " numero de empleado");
                if (numeroEmpleado == 1) {
                    txtCodIniEm.requestFocus();
                } else if (numeroEmpleado == 2) {
                    txtCodFinEm.requestFocus();
                }

                calFecha.setVisible(true);
                radioButtonDiciembre.setVisible(false);
                radioButtonJunio.setVisible(false);
                lblAnio.setVisible(false);
                anioChooser.setVisible(false);
                break;
            case 2:
                if (numeroEmpleado == 1) {
                    txtCodIniEm.requestFocus();
                } else if (numeroEmpleado == 2) {
                    txtCodFinEm.requestFocus();
                }
                radioButtonPrima.setSelected(true);
                lblFecha.setVisible(false);
                calFecha.setVisible(false);
                lblAnio.setVisible(true);
                anioChooser.setVisible(true);
                anioChooser.requestFocus();
                radioButtonDiciembre.setVisible(true);
                radioButtonJunio.setVisible(true);
                radioButtonJunio.setSelected(true);
                break;
            case 3:
                if (numeroEmpleado == 1) {
                    txtCodIniEm.requestFocus();
                } else if (numeroEmpleado == 2) {
                    txtCodFinEm.requestFocus();
                }
                radioButtonInteresSobreCesantia.setSelected(true);
                lblFecha.setVisible(false);
                calFecha.setVisible(false);
                radioButtonDiciembre.setVisible(false);
                radioButtonJunio.setVisible(false);
                lblAnio.setVisible(true);
                anioChooser.setVisible(true);
                break;

        }

        comboBoxCentroDeCosto.setEnabled(tipoBusqueda == 1 ? true : false);
        radioButtonPorCodigo.setSelected(true);

        // propiedad que me permite sobre-poner/mostrar el combo    
        comboBoxCentroDeCosto.setLightWeightPopupEnabled(false); //usar componente de peso pesado
        btnVerMas.setEnabled(false);
        btnVerTodo.setEnabled(false);
        btnEnviarCorreo.setEnabled(false);

        //Titulo y borde al panel
//        panelHeader.setBorder(javax.swing.BorderFactory.createTitledBorder("Opciones Generales"));
        panelHeader.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Opciones Generales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft Sans Serif", 1, 11), new java.awt.Color(58, 58, 58)));
    }

    //Método para calcular el total a liquidar de nómina
    public void calcularTotalNomina() {
        try {
            this.totalAcunove = 0;
            this.totalPagos = 0;
            this.totalDescuentos = 0;
            for (AcunoveDTO nove : listaAcunove) {
                String codPara = nove.getCod_nov();
                for (int i = 0; i < 1; i++) {
                    if (codPara.charAt(i) == 'A') {
                        totalPagos += nove.getValor_tot();
                    } else {
                        totalDescuentos += nove.getValor_tot();
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Exception calcularTotalNomina " + e);
        } finally {
            if (!Utiles.validarNumeroConXDecimales("" + totalPagos, 2)) {
                totalPagos = Utiles.redondearDecimales(totalPagos, 2);
            }
            if (!Utiles.validarNumeroConXDecimales("" + totalDescuentos, 2)) {
                totalDescuentos = Utiles.redondearDecimales(totalDescuentos, 2);
            }
            totalAcunove = (totalPagos - totalDescuentos);
            if (!Utiles.validarNumeroConXDecimales("" + totalAcunove, 2)) {
                totalAcunove = Utiles.redondearDecimales(totalAcunove, 2);
            }
        }
    }

    private void cargarCursor(boolean cargar) {
        btnBuscar.setEnabled(!cargar);
        btnCancelar.setEnabled(!cargar);
        cargarCursorElementosDeVista(cargar);
        if (cargar) {
            //cursor para buscar
            getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
        } else {
            //Dejamos el cursor por defecto
            getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void cargarCursorElementosDeVista(boolean res) {

        if (res) {
            btnSelectEmIni.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            btnSelectEmFin.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            btnBuscar.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            btnVerMas.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            btnVerTodo.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            btnEnviarCorreo.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            btnCancelar.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            txtCodIniEm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            txtCodFinEm.setCursor(new Cursor(Cursor.WAIT_CURSOR));

            radioButtonCentroDeCosto.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            radioButtonDiciembre.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            radioButtonInteresSobreCesantia.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            radioButtonJunio.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            radioButtonPagoQuincena.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            radioButtonPorCodigo.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            radioButtonPrima.setCursor(new Cursor(Cursor.WAIT_CURSOR));

            panelBody.setCursor(new Cursor(Cursor.WAIT_CURSOR));
//            scrollPaneTablaConsulta.setCursor(new Cursor(Cursor.WAIT_CURSOR));
//            scrollPaneTablaNovedades.setCursor(new Cursor(Cursor.WAIT_CURSOR));
//            tblConsulta.setCursor(new Cursor(Cursor.WAIT_CURSOR));
//            tblNovedades.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        } else {
            btnSelectEmIni.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnSelectEmFin.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnVerMas.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnVerTodo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEnviarCorreo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            txtCodIniEm.setCursor(new Cursor(Cursor.TEXT_CURSOR));
            txtCodFinEm.setCursor(new Cursor(Cursor.TEXT_CURSOR));

            radioButtonCentroDeCosto.setCursor(new Cursor(Cursor.HAND_CURSOR));
            radioButtonDiciembre.setCursor(new Cursor(Cursor.HAND_CURSOR));
            radioButtonInteresSobreCesantia.setCursor(new Cursor(Cursor.HAND_CURSOR));
            radioButtonJunio.setCursor(new Cursor(Cursor.HAND_CURSOR));
            radioButtonPagoQuincena.setCursor(new Cursor(Cursor.HAND_CURSOR));
            radioButtonPorCodigo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            radioButtonPrima.setCursor(new Cursor(Cursor.HAND_CURSOR));

            panelBody.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//            scrollPaneTablaConsulta.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//            scrollPaneTablaNovedades.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//            tblConsulta.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//            tblNovedades.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public void consultar() {
        verTipoNovedad();
        if (organizarFechasParaConsulta()) {
            if (radioButtonCentroDeCosto.isSelected()) {
                if (comboBoxCentroDeCosto.getSelectedIndex() != 0) {
                    consultarPorCentroDeCosto();
                } else {
                    consultarTodos();
                }
            } else if (radioButtonPorCodigo.isSelected()) {
                btnVerMas.setEnabled(true);
                //Validamos si los codigos son realmente de un empleado
                boolean em1 = lblNomEm1.getText().equalsIgnoreCase("") ? false : true;
                boolean em2 = lblNomEm2.getText().equalsIgnoreCase("") ? false : true;
                if (em1 && em2) { //si  ambos son los organizamos y mostramos los resultados
                    organizarCodigos(txtCodIniEm.getText(), txtCodFinEm.getText());
                    llenarListaEmpleadosPorRango(codIniEm, codFinEm);
                    //autocompletamos si solo un empleado existe -----------------------------------
                } else if (em1 == false && em2 == true) { //si solo el em2 existe el em1 se iguala
                    txtCodIniEm.setText(txtCodFinEm.getText());
                    codIniEm = txtCodFinEm.getText();
                    codFinEm = txtCodFinEm.getText();
                    llenarLabelEmpleado(codIniEm, 1);
                    llenarListaEmpleadosPorRango(codIniEm, codFinEm);
                } else if (em2 == false && em1 == true) { //si solo em em1 existe el em2 se iguala
                    txtCodFinEm.setText(txtCodIniEm.getText());
                    codFinEm = txtCodIniEm.getText();
                    codIniEm = txtCodIniEm.getText();
                    llenarLabelEmpleado(codFinEm, 2);
                    llenarListaEmpleadosPorRango(codIniEm, codFinEm);
                } else {
                    consultarTodos();
                }
            }

            if (listaEmpleados.isEmpty()) {
                btnEnviarCorreo.setEnabled(false);
            } else {
                btnEnviarCorreo.setEnabled(true);
            }
            if (listaEmpleados.size() < 50) {
                btnVerMas.setEnabled(false);
                btnVerTodo.setEnabled(false);
            }
            mostrarResultados(true);
            //Seleccionamos la primera fila
            if (!listaEmpleados.isEmpty()) {
                tblConsulta.setRowSelectionInterval(0, 0);
                tblConsulta.requestFocus();
            }
            if (radioButtonPagoQuincena.isSelected()) {
                if (!listaEmpleados.isEmpty()) {
                    tblConsulta.requestFocus();
                } else {
                    calFecha.getDateEditor().getUiComponent().requestFocusInWindow();
                }
            } else if (radioButtonPrima.isSelected() || radioButtonInteresSobreCesantia.isSelected()) {
                if (!listaEmpleados.isEmpty()) {
                    tblConsulta.requestFocus();
                }
            }
        }
    }

    public void consultarPorCentroDeCosto() {
        try {
            listaEmpleados
                    = empleadoDAO.buscarPorCentroDeCosto(Utiles.verCodigoCentroDeCosto(comboBoxCentroDeCosto.getSelectedItem().toString()), contador,
                            tipoNovedad,
                            "" + fecha.getDate(), "" + fecha.getMonth(), "" + fecha.getYear());
            llenarTablaDeEmpleados();
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException consultarPorCentroDeCosto " + ex);
        } catch (SQLException ex) {
            System.out.println("SQLException consultarPorCentroDeCosto " + ex);
        }
    }

    public void consultarTodos() {
        try {
            listaEmpleados
                    = empleadoDAO.buscarTodos(
                            contador,
                            tipoNovedad,
                            "" + fecha.getDate(), "" + fecha.getMonth(), "" + fecha.getYear());
            llenarTablaDeEmpleados();
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException consultarTodos " + ex);
        } catch (SQLException ex) {
            System.out.println("SQLException consultarTodos " + ex);
        }
    }

    //retorna el nombre del pdf - Recibe como parámetro el empleado, el centro de costo y la lista de novedades
    public String descargarPdf(EmpleadoDTO empleado, ParametroDTO centroDeCosto, ArrayList<AcunoveDTO> listaNove) {
        String nomPdf = "";

        try {

            ///------------------------------------------------------------------------
            //Creamos un documento con su tipo de papel y margenes
            Document doc = new Document(PageSize.A4, 20, 20, 20, 20);
//
            if (radioButtonPagoQuincena.isSelected()) {//si la busquda es por pago de quincena obtenemos las fechas de quincena
                obtenerFechasDeQuincena(fecha);
            } else {
                fechaDePago = Utiles.formatearFechayyyyMMddSlash(fecha);
            }
            nomPdf//le damos un nombre al pdf
                    = (radioButtonPagoQuincena.isSelected()) ? "Nomina_" + Utiles.formatearFechayyyyMMddGuion(fecha) + "_" + empleado.getDoc_identidad() //si es de pago de quincena 
                    : (radioButtonPrima.isSelected()) ? (radioButtonJunio.isSelected()) ? "PrimaDeServiciosPrimerSemestre_" + anioChooser.getYear() + "_" + empleado.getDoc_identidad() : "PrimaDeServiciosSegundoSemestre" + anioChooser.getYear() + "_" + empleado.getDoc_identidad()//si es por prima entonces si es junio "primer semestre" de lo contrario "segundo semestre"
                    : "InteresesSobreCesantias_" + anioChooser.getYear() + "_" + empleado.getDoc_identidad();//si no interes sobre cesantias
            nomPdf = nomPdf + ".pdf"; //le concatenamos la extención al documento

            FileOutputStream fichero = new FileOutputStream(nomPdf);//Creamos el Output Stream con la ruta donde vamos a guardar el fichero
            PdfWriter writer = PdfWriter.getInstance(doc, fichero);//objeto con el cual escribimos en el pdf
            doc.open();//abrimos el documento
            PdfContentByte cb = writer.getDirectContentUnder();//Ahora el contenido
            try {

                //Definimos el tamaño y tipo de letra para el pdf
                BaseFont bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                cb.setFontAndSize(bf, 10);

                // Añadimos los metadatos del PDF
                String titulo //titulo del pdf
                        = (radioButtonPagoQuincena.isSelected()) ? "Nómina Desde " + fechaInicialDeQuincena + " Hasta " + fechaDePago//si la busqueda es de pago de quincena
                        : (radioButtonPrima.isSelected()) ? (radioButtonDiciembre.isSelected()) ? "Prima de Servicios del Segundo Semestre del Año " + anioChooser.getYear() : "Prima de Servicios del Primer Semestre del Año " + anioChooser.getYear()//si la busquda es por prima entonces si es de junio entonces "prima primer semestre" de lo contrario "prima segundo semestre"
                        : "Intereses sobre Cesantías del Año " + anioChooser.getYear();//de lo contrario interes sobre cesntia
                doc.addTitle(titulo);
                doc.addAuthor("Weizman Fabian");
                doc.addCreator("DYD SOFTWARE");
                doc.addCreationDate();

                //Empiezo a escribir
                cb.beginText();
                try {

                    //--------------------------------------------------Header-------------------------------------------------------------------------
//            1° linea
                    //Nombre de la Empresa
                    cb.setTextMatrix(20, 820); //le decimos la ubicación (left, button) del texto dentro del documento
                    cb.showText(Mail.nombreEmpresa);//escribimos el nombre de la empresa en la pocición anterior

                    //Fecha de Proceso
                    //Texto con alineación Constructor que recibe(Alineación, Texto a mostrar, posicion x, posicion y, angulo de orientación del texto
                    cb.showTextAligned(Element.ALIGN_RIGHT, "Fecha:    " + Utiles.obtenerFechaProceso(), 573, 820, 0);

//            2° linea
                    //Encabezado del pdf
                    cb.setTextMatrix(20, 810);
                    String encabezado
                            = (radioButtonPagoQuincena.isSelected()) ? "Nómina"//si la busqueda es de pago de quincena
                            : (radioButtonPrima.isSelected()) ? "Prima de Servicios"//si la busquda es por prima
                            : "Intereses sobre Cesantías, Año " + anioChooser.getYear();//de lo contrario interes sobre cesantía
                    cb.showText(encabezado);
                    cb.showTextAligned(Element.ALIGN_RIGHT, "Página             1", 573, 810, 0);

//            3° linea
                    cb.setTextMatrix(20, 800);
                    String descripcion
                            = (radioButtonPagoQuincena.isSelected()) ? "Desde:" + fechaInicialDeQuincena + "  Hasta:" + fechaDePago// si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? (radioButtonDiciembre.isSelected()) ? "Segundo Semestre del Año " + anioChooser.getYear() : "Primer Semestre del Año " + anioChooser.getYear()//si es prima entonces si es diciembre "Segundo semestre" de lo contrario "primer semestre"
                            : ""; //se deja vació porque son intereses sobre cesantias
                    cb.showText(descripcion);
                    //Nombre De la empresa de desarrollo software
                    cb.showTextAligned(Element.ALIGN_RIGHT, "D Y D  SOFTWARE  SAS", 573, 800, 0);

//            4° linea
                    cb.setTextMatrix(20, 780);
                    cb.showText("Código: " + empleado.getCod_empleado());
                    cb.setTextMatrix(120, 780);
                    cb.showText("Nombre: " + empleado.getNom_em());
                    cb.showTextAligned(Element.ALIGN_RIGHT, "C. C. : " + centroDeCosto.getNombre(), 573, 780, 0);

//            5° linea
                    cb.setTextMatrix(20, 770);
                    cb.showText("Cuenta de Pago:  " + empleado.getCuenta_de_pago());
                    cb.showTextAligned(Element.ALIGN_RIGHT, "Sueldo: $" + enmascararNumeroDouble(empleado.getUltimo_sueldo()), 573, 770, 0);

//            6° linea
                    cb.setTextMatrix(245, 738);
                    cb.showText("Pagos");
                    cb.setTextMatrix(445, 738);
                    cb.showText("Descuentos");
                    //agregamos unas lineas de párrafos en blanco para que le dé espacio a los textos anteriores
                    doc.add(new Paragraph(" "));
                    doc.add(new Paragraph(" "));
                    doc.add(new Paragraph(" "));
                    doc.add(new Paragraph(" "));
                    doc.add(new Paragraph(" "));

                    //------------------------------------------------------Body----------------------------------------------------------------------
//            La tabla
                    Paragraph p = new Paragraph(); //cramos un parrafo

                    //Llenamos la tabla pdf 
                    llenarTablaPdf(listaNove);

                    p.add(table); //le agregamos al parrafo la tabla
                    doc.add(p); //agregamos al documento el parrafo

                    //-------------------------------------------------------Footer-------------------------------------------------------------------
//            la firma de recibido
                    //creamos unos parrafos para dejar espacio en blanco
                    doc.add(new Paragraph(" "));
                    doc.add(new Paragraph(" "));
                    doc.add(new Paragraph(" "));
                    Paragraph p1 = new Paragraph("FIRMA DE RECIBIDO:_____________________________", fontCourierNormal);
                    doc.add(p1);

                    Paragraph p2 = new Paragraph("CC: " + Utiles.enmascararCedula(empleado.getDoc_identidad()), fontCourierNormal);
                    doc.add(p2);

                } catch (Exception e) {
                    System.out.println("Excepción al escribir en el pdf " + e);
                } finally {
                    //Termino de escribir
                    cb.endText();
                }
            } catch (Exception e) {
                System.out.println("Excepción al crear el pdf " + e);
            } finally {
                doc.close();
                fichero.close();
                writer.close();
            }

            //Cerramos todos los objetos
        } catch (DocumentException de) {
            System.out.println("DocumentException descargarPdf " + de);
        } catch (Exception e) {
            System.out.println("Exception descargarPdf " + e);
        } finally {
            System.out.println("El pdf se descargó correctamente");
        }

        //retornamos el nombre del pdf
        return nomPdf;
    }

    public void ejecutar1Hilo() {
        iniciarHilo2();
    }

    public void ejecutar2Hilos() {
        /*
            esto permite que:
            hilo1 :        0 < cociente    (0 < 2)
            hilo2 : cociente < size        (2 < 5)
            
            de esta manera cada hilo enviará el correo a la lista de empleados solo a los que les corresponda
            
         */
        cociente = dividendo / 2; //5 / 2 = 2; //dividimos en 2 el tamaño total de la lista
        //iniciamos los hilos
        iniciarHilo2();//envia correo
        iniciarHilo3();//envia correo
    }

    public void ejecutar4Hilos() {
        /*
        
            esto permite que:
            hilo2 :         0 < ciciente1   ( 0 < 12)
            hilo3 : cociente1 < cociente    (12 < 25)
            hilo4 :  cociente < cociente2   (25 < 37)
            hilo5 : cociente2 < size        (37 < 50)

            de esta manera cada hilo enviará el correo a la lista de empleados solo a los que les corresponda
         */
        cociente = dividendo / 2;         //50 /  2 = 25; //dividimos en 2 el tamaño total de la lista
        cociente1 = cociente / 2;         //25 /  2 = 12; //el resultado de la division (cociente) lo dividimos en 2 
        cociente2 = cociente + cociente1; //25 + 12 = 37; //el resultado de la division (cociente) le sumamos el cociente1
        //iniciamos los hilos
        iniciarHilo2();//envia correo
        iniciarHilo3();//envia correo
        iniciarHilo4();
        iniciarHilo5();
    }

    public void ejecutar8Hilos() {
        /*
            esto permite que:
            hilo2 :          0 < cociente11    ( 0 <  12)
            hilo3 : cociente11 < cociente1     (12 <  25)
            hilo4 :  cociente1 < cociente12    (25 <  37)
            hilo5 : cociente12 < cociente      (37 <  50)
            hilo6 :   cociente < cociente21    (50 <  62)
            hilo7 : cociente21 < cociente2     (62 <  75)
            hilo8 :  cociente2 < cociente22    (75 <  87)
            hilo9 : cociente22 < size          (87 < 100)
            
            de esta manera cada hilo enviará el correo a la lista de empleados solo a los que les corresponda
         */
        cociente = dividendo / 2;            //100 /  2 = 50; // 12/2=6;

        cociente1 = cociente / 2;             //50 /  2 = 25; // 6/2= 3;
        cociente11 = cociente1 / 2;           //25 /  2 = 12; // 3/2= 1;
        cociente12 = cociente1 + cociente11;  //25 + 12 = 37; // 3+1= 4;

        cociente2 = cociente + cociente1;     //50 + 25 = 75; // 6+3= 9;
        cociente21 = cociente + cociente11;   //50 + 12 = 62; // 6+1= 7;
        cociente22 = cociente2 + cociente11;  //75 + 12 = 87; // 9+1=10;

        //iniciamos los hilos
        iniciarHilo2();//envia correo
        iniciarHilo3();//envia correo
        iniciarHilo4();
        iniciarHilo5();
        iniciarHilo6();
        iniciarHilo7();
        iniciarHilo8();
        iniciarHilo9();
    }

    private void dividirTareasEIniciarHilosDeEnvíoDeCorreoMasivo() {

        //reiniciamos las variables
        dividendo = 0;
        cociente = 0;
        cociente1 = 0;
        cociente1 = 0;
        //llenamos las variables segun corresponda
        dividendo = listaEmp.size();//tamaño de la lista de empleados a los cuales hay que enviar el correo
        switch (numeroDeHilos) {
            case 8:
                //si se ejecutan 8 hilos
                ejecutar8Hilos();
                break;
            case 4:
                //si se ejecutan 4 hilos
                ejecutar4Hilos();
                break;
            case 2:
                //se ejecutan 2 hilos
                ejecutar2Hilos();
                break;
            default:
                //solo se ejecuta 1 hilo
                ejecutar1Hilo();
                break;
        }

    }

    public void enviarPdfPorCorreoAEmpleados(ArrayList<EmpleadoDTO> lista) {
        String mensajeCe = ""; //mensaje de Correos Enviados
        String mensajeNc = ""; //mensaje de los correos que NO se enviaron porque los empleados NO tienen Correo en la base de datos
        try {

            //reiniciamos variables
            listaEmp = new ArrayList<>(); //inicializamos la lista de empleados
            listaNombrePdf = new ArrayList<>(); //inicializamos la lista en donde se guardan cada uno de los nombres del pdf para adjuntar
            correosEnviados = 0; //
            noTieneCorreo = 0;
            hiloDescargarPdfIniciado = false;
            hilo2Iniciado = false;
            hilo3Iniciado = false;
            hilo4Iniciado = false;
            hilo5Iniciado = false;
            hilo6Iniciado = false;
            hilo7Iniciado = false;
            hilo8Iniciado = false;
            hilo9Iniciado = false;
            //correos que envia cada hilo
            mailSend2 = 0;
            mailSend3 = 0;
            mailSend4 = 0;
            mailSend5 = 0;
            mailSend6 = 0;
            mailSend7 = 0;
            mailSend8 = 0;
            mailSend9 = 0;

            listaEmp = lista; //cargamos la lista de empleados con la lista que llega

            //--------------------------------------------------------------------------------------------------
            //Hilos multitareas
            iniciarHiloDescargarPdf(); //descarga todos los pdf
            hiloDescargarPdf.join(); //propiedad que espera a que este hilo se acabe en su totalidad
            dividirTareasEIniciarHilosDeEnvíoDeCorreoMasivo(); //iniciamos los hilos
            switch (numeroDeHilos) {
                case 8:
                    //propiedad que espera a que los hilos acaben en su totalidad
                    hilo2.join();
                    hilo3.join();
                    hilo4.join();
                    hilo5.join();
                    hilo6.join();
                    hilo7.join();
                    hilo8.join();
                    hilo9.join();
                    //miramos desde donde se ejecutaron cada uno de los hilos
                    System.out.println("Ejecución de Hilos...");
                    System.out.println("Hilo 2: 0 < " + cociente11);
                    System.out.println("Hilo 3: " + cociente11 + " < " + cociente1);
                    System.out.println("Hilo 4: " + cociente1 + " < " + cociente12);
                    System.out.println("Hilo 5: " + cociente12 + " < " + cociente);
                    System.out.println("Hilo 6: " + cociente + " < " + cociente21);
                    System.out.println("Hilo 7: " + cociente21 + " < " + cociente2);
                    System.out.println("Hilo 8: " + cociente2 + " < " + cociente22);
                    System.out.println("Hilo 9: " + cociente22 + " < " + listaEmp.size());
                    //miramos cuantos correos envió cada hilo
                    System.out.println("Correos enviados por cada Hilo");
                    System.out.println("Hilo 2: " + mailSend2 + " correos enviados ----------------------------------------------------");
                    System.out.println("Hilo 3: " + mailSend3 + " correos enviados ----------------------------------------------------");
                    System.out.println("Hilo 4: " + mailSend4 + " correos enviados ----------------------------------------------------");
                    System.out.println("Hilo 5: " + mailSend5 + " correos enviados ----------------------------------------------------");
                    System.out.println("Hilo 6: " + mailSend6 + " correos enviados ----------------------------------------------------");
                    System.out.println("Hilo 7: " + mailSend7 + " correos enviados ----------------------------------------------------");
                    System.out.println("Hilo 8: " + mailSend8 + " correos enviados ----------------------------------------------------");
                    System.out.println("Hilo 9: " + mailSend9 + " correos enviados ----------------------------------------------------");
                    break;
                case 4:
                    //propiedad que espera a que los hilos acaben en su totalidad
                    hilo2.join();
                    hilo3.join();
                    hilo4.join();
                    hilo5.join();
                    //miramos desde donde se ejecutaron cada uno de los hilos
                    System.out.println("Ejecución de Hilos...");
                    System.out.println("Hilo 2: 0 < " + cociente1);
                    System.out.println("Hilo 3: " + cociente1 + " < " + cociente);
                    System.out.println("Hilo 4: " + cociente + " < " + cociente2);
                    System.out.println("Hilo 5: " + cociente2 + " < " + listaEmp.size());
                    //miramos cuantos correos envió cada hilo
                    System.out.println("Correos enviados por cada Hilo");
                    System.out.println("Hilo 2: " + mailSend2 + " correos enviados ----------------------------------------------------");
                    System.out.println("Hilo 3: " + mailSend3 + " correos enviados ----------------------------------------------------");
                    System.out.println("Hilo 4: " + mailSend4 + " correos enviados ----------------------------------------------------");
                    System.out.println("Hilo 5: " + mailSend5 + " correos enviados ----------------------------------------------------");
                    break;
                case 2:
                    //propiedad que espera a que los hilos acaben en su totalidad
                    hilo2.join();
                    hilo3.join();
                    //miramos desde donde se ejecutaron cada uno de los hilos
                    System.out.println("Ejecución de Hilos...");
                    System.out.println("Hilo 2: 0 < " + cociente);
                    System.out.println("Hilo 3: " + cociente + " < " + listaEmp.size());
                    //miramos cuantos correos envió cada hilo
                    System.out.println("Hilo 2: " + mailSend2 + " correos enviados ----------------------------------------------------");
                    System.out.println("Hilo 3: " + mailSend3 + " correos enviados ----------------------------------------------------");

                    break;
                default: //si no ejecuta 1 hilos
                    //propiedad que espera a que este hilo se acabe en su totalidad
                    hilo2.join();
                    //miramos desde donde se ejecutó el hilo
                    System.out.println("Ejecución del Hilo...");
                    System.out.println("Hilo 2: 0 < " + lista.size());
                    //miramos cuantos correos envió el hilo
                    System.out.println("Correos enviados por cada Hilo");
                    System.out.println("Hilo 2: " + mailSend2 + " correos enviados ----------------------------------------------------");
                    break;
            }

            if (radioButtonPagoQuincena.isSelected()) { // si es de pago de quincena
                mensajeCe = lista.size() == 1 ? "Se le envió la nómina al empleado exitosamente" : "Se les envió la nómina a " + correosEnviados + " empleados exitosamente"; //si el tamaño de la lista es igual a 1 entonces "mensaje1" de lo contrario "mensajeMuchos"
                mensajeNc = lista.size() == 1 ? "Este empleado No tiene Correo" : "NO se les envió la nómina a " + noTieneCorreo + " empleados porque NO tienen correo"; //si el tamaño de la lista es igual a 1 entonces "mensaje1" de lo contrario "mensajeMuchos"
            } else if (radioButtonPrima.isSelected()) { // si es por prima
                mensajeCe = lista.size() == 1 ? "Se le envió la Prima de Servicios al empleado exitosamente" : "Se les envió la Prima de Servicios a " + correosEnviados + " empleados exitosamente";//si el tamaño de la lista es igual a 1 entonces "mensaje1" de lo contrario "mensajeMuchos"
                mensajeNc = lista.size() == 1 ? "Este empleado No tiene Correo" : "NO se les envió la Prima de Servicios a " + noTieneCorreo + " empleados porque NO tienen correo";//si el tamaño de la lista es igual a 1 entonces "mensaje1" de lo contrario "mensajeMuchos"
            } else { //si no es por intereses sobre cesantias
                mensajeCe = lista.size() == 1 ? "Se le envió los Intereses sobre Cesantías al empleado exitosamente" : "Se les envió los Intereses sobre Cesantías a " + correosEnviados + " empleados exitosamente";//si el tamaño de la lista es igual a 1 entonces "mensaje1" de lo contrario "mensajeMuchos"
                mensajeNc = lista.size() == 1 ? "Este empleado No tiene Correo" : "NO se les envió los Intereses sobre Cesantías a " + noTieneCorreo + " empleados porque NO tienen correo";//si el tamaño de la lista es igual a 1 entonces "mensaje1" de lo contrario "mensajeMuchos"
            }

        } catch (Exception ex) {
            System.out.println("Exception enviarPdfPorCorreoAEmpleados " + ex);
        } finally {
            //Informamos cuantos correos se enviaron
            if (correosEnviados > 0) { //si se enviaron al menos un correo
                JOptionPane.showConfirmDialog(rootPane, mensajeCe, "Información", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE); //informamos
                tblConsulta.requestFocus(); //dejamos el foco en la tabla de empleados
            }
            //le decimos cuantos correos no se enviaron porque los empleados no tienen correo
            if (noTieneCorreo > 0) { //si no se envió al menos un correo
                JOptionPane.showConfirmDialog(rootPane, mensajeNc, "Ojo", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE); //informamos
                tblConsulta.requestFocus();//dejamos el foco en la tabla de empleados
            }
            tblConsulta.requestFocus();
        }
    }

    private void establecerFechaAMostrar() {
        calFecha.setDateFormatString("yyyy/MM/dd");
        Calendar c2 = new GregorianCalendar();
        fecha = fechaGuardada != null ? fechaGuardada : Utiles.obtenerFechaQuincenaPasada();
        c2.setTime(fecha);
        calFecha.setCalendar(c2);
        calFecha.setMaxSelectableDate(Utiles.obtenerFechaSiguienteQuincena());
        //Componente del editor
        editor = (JTextFieldDateEditor) calFecha.getDateEditor();
        editor.addActionListener(miClase);

        if (fecha != null) {
            if (fecha.getMonth() > 6) {
                radioButtonJunio.setSelected(true);
            } else {
                radioButtonDiciembre.setSelected(true);
            }
            if (anioGuardado != 0) {
                anioChooser.setYear(anioGuardado);
            } else {
                anioGuardado = 0;
            }
        }
    }

    public void establecerTitulosATablaPdf() {

        //Titulos 
        //Tipo NOvedad
        Paragraph tn = new Paragraph("TIPO NOVEDAD", fontCourierBold);
        PdfPCell cell = new PdfPCell(tn);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(cell);

        //Cantidad
        Paragraph c = new Paragraph("CANT", fontCourierBold);
        PdfPCell cell2 = new PdfPCell(c);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(cell2);

        //Valor unitario
        Paragraph vu = new Paragraph("VLR UNI", fontCourierBold);
        PdfPCell cell3 = new PdfPCell(vu);
        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(cell3);

        //valor total
        Paragraph vt = new Paragraph("VLR TOTAL", fontCourierBold);
        PdfPCell cell4 = new PdfPCell(vt);
        cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell4.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(cell4);

        //Cantidad
        Paragraph cant = new Paragraph("CANT", fontCourierBold);
        PdfPCell cell5 = new PdfPCell(cant);
        cell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell5.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(cell5);

        //valor unitario
        Paragraph vlrU = new Paragraph("VLR UNI", fontCourierBold);
        PdfPCell cell6 = new PdfPCell(vlrU);
        cell6.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell6.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(cell6);

        //valor total
        Paragraph vt1 = new Paragraph("VLR TOTAL", fontCourierBold);
        PdfPCell cell7 = new PdfPCell(vt1);
        cell7.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell7.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(cell7);
    }

    private void habilitarElemetosDeVista(boolean res) {
        radioButtonCentroDeCosto.setEnabled(res);
        radioButtonDiciembre.setEnabled(res);
        radioButtonInteresSobreCesantia.setEnabled(res);
        radioButtonJunio.setEnabled(res);
        radioButtonPagoQuincena.setEnabled(res);
        radioButtonPorCodigo.setEnabled(res);
        radioButtonPrima.setEnabled(res);
        txtCodIniEm.setEnabled(res);
        txtCodFinEm.setEnabled(res);
        calFecha.setEnabled(res);
        anioChooser.setEnabled(res);
        comboBoxCentroDeCosto.setEnabled(res);
        btnBuscar.setEnabled(res);
        btnCancelar.setEnabled(res);
        btnEnviarCorreo.setEnabled(res);
        btnSelectEmFin.setEnabled(res);
        btnSelectEmIni.setEnabled(res);
        btnVerMas.setEnabled(res);
        btnVerTodo.setEnabled(res);
    }

    private void inicializarTablaEmpleados() {
        //----------------------------------------TABLA EMPLEADOS-----------------------------------------------
        //Inicializamos el objeto tabla de la vista
        tblConsulta.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        dtm = new DefaultTableModel(contenidoBlanco, titulo) {
            //Cancelamos la edición de la tabla en la vista
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblConsulta.setModel(dtm);
        aplicarDisenioTablaEmpleados();
    }

    private void inicializarTablaNovedades() {
        //----------------------------------------TABLA NOVEDADES-----------------------------------------------
        tblNovedades.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        dtmNovedades = new DefaultTableModel(contenidoVacioNovedades, tituloTablaNovedades) {
            //Cancelamos la edición de la tabla en la vista
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblNovedades.setModel(dtmNovedades);
        aplicarDisenioTablaNovedades();
    }

    private void inicializarTablasDeVista() {
        inicializarTablaEmpleados();
        inicializarTablaNovedades();

        btnVerMas.setEnabled(false);
        btnVerTodo.setEnabled(false);
        btnEnviarCorreo.setEnabled(false);
        lblFechaDeNovedad.setText("");

    }

    private void llenarConboCentroDeCosto() {
        try {
            this.listaCentrosDeCostos = parametroDAO.buscarTodosLosParametrosCentroDeCosto();
            this.comboBoxCentroDeCosto.removeAllItems();
            this.comboBoxCentroDeCosto.addItem("");
            for (ParametroDTO item : listaCentrosDeCostos) {
                this.comboBoxCentroDeCosto.addItem(Utiles.quitarLetraEneACodigoCentroDeCosto(item.getCodigo()) + " - " + item.getNombre());

            }
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException llenarConboCentroDeCosto " + ex);
        } catch (SQLException ex) {
            System.out.println("SQLException llenarConboCentroDeCosto " + ex);
        }
    }

    public boolean llenarLabelEmpleado(String codEm, int numEm) {
        boolean res = false;
        try {
            EmpleadoDTO e = new EmpleadoDTO();
            e = empleadoDAO.buscarEmpleado(codEm);
            if (e.getNom_em() != null) {
                if (numEm == 1) {
                    lblNomEm1.setText(e.getNom_em());
                } else {
                    lblNomEm2.setText(e.getNom_em());
                }
                res = true;
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException llenarLabelEmpleado " + ex);
        } catch (SQLException ex) {
            System.out.println("SQLException llenarLabelEmpleado " + ex);
        }
        return res;
    }

    public void llenarListaDeEmpleadosSeleccionados(int[] filasSeleccionadas) {
        try {
            listaEmpleadosSelect = new ArrayList<>();
            for (int i = 0; i < filasSeleccionadas.length; i++) {
                EmpleadoDTO em = new EmpleadoDTO();
                em = empleadoDAO.buscarEmpleado(tblConsulta.getValueAt(filasSeleccionadas[i], 0).toString());
                listaEmpleadosSelect.add(em);
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException llenarListaDeEmpleadosSeleccionados " + ex);
        } catch (SQLException ex) {
            System.out.println("SQLException llenarListaDeEmpleadosSeleccionados " + ex);
        } finally {
            System.out.println("Lista de empleados seleccionados llenada exitosamente");
        }
    }

    public void llenarListaEmpleadosPorRango(String codIni, String codFin) {
        try {
            listaEmpleados.clear();
            if (radioButtonPorCodigo.isSelected() && empleadoDAO.existeEmpleado(txtCodIniEm.getText()) && empleadoDAO.existeEmpleado(txtCodFinEm.getText())) {
                listaEmpleados
                        = empleadoDAO.buscarPorCodigo(codIni, codFin, contador,
                                tipoNovedad,
                                "" + fecha.getDate(), "" + fecha.getMonth(), "" + fecha.getYear());
            }
            llenarTablaDeEmpleados();
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException llenarListaEmpleadosPorRango " + ex);
        } catch (SQLException ex) {
            System.out.println("SQLException llenarListaEmpleadosPorRango " + ex);
        } finally {
            System.out.println("Lista empleados por rango llenada exitosamente");
        }
    }

    public void llenarListaNovedades(String codEmplead) throws ParseException {

        try {
            listaAcunove.clear();
            listaAcunove
                    = (radioButtonPagoQuincena.isSelected()) ? acunoveDAO.listarPagosQuincenales("" + fecha.getDate(), "" + fecha.getMonth(), "" + fecha.getYear(), codEmplead)
                    : (radioButtonPrima.isSelected()) ? acunoveDAO.listarPagoPrima("" + fecha.getDate(), "" + fecha.getMonth(), "" + fecha.getYear(), codEmplead)
                    : acunoveDAO.listarPagoInteresesSobreCesantias("" + fecha.getDate(), "" + fecha.getMonth(), "" + fecha.getYear(), codEmplead);
            llenarTablaDeNovedades();
            lblFechaDeNovedad.setText(Utiles.formatearFechaLargaMMddYYYY(fecha));

        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException llenarListaNovedades " + ex);
        } catch (SQLException ex) {
            System.out.println("SQLException llenarListaNovedades " + ex);
        } finally {
            System.out.println("Lista novedades llenada exitosamente");
        }
    }

    public void llenarTablaDeEmpleados() {
        try {
            //Inicializamos la matriz de la lista con 4 columnas y el # de elementos de la lista
            String matriz[][] = new String[listaEmpleados.size()][7];

            for (int i = 0; i < listaEmpleados.size(); i++) {
                matriz[i][0] = listaEmpleados.get(i).getCod_empleado();
                matriz[i][1] = listaEmpleados.get(i).getNom_em();
                matriz[i][2] = Utiles.enmascararCedula(listaEmpleados.get(i).getDoc_identidad());
                matriz[i][3] = enmascararNumeroDouble(listaEmpleados.get(i).getUltimo_sueldo());
                ParametroDTO p = new ParametroDTO();
                p = parametroDAO.buscarParametroCentroDeCosto(listaEmpleados.get(i).getCentro_de_costo());
                matriz[i][4] = p.getNombre();
                matriz[i][5] = listaEmpleados.get(i).getCorreo();
            }

            //Inicializamos el objeto tabla de la vista
            tblConsulta.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
            dtm = new DefaultTableModel(matriz, titulo) {
                //Cancelamos la edición de la tabla en la vista
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tblConsulta.setModel(dtm);

            //Aplicamos diseño a la tabla de la vista
            aplicarDisenioTablaEmpleados();

            btnEnviarCorreo.setEnabled(!listaEmpleados.isEmpty());
        } catch (Exception ex) {
            System.out.println("Exception llenarTablaDeEmpleados " + ex);
        }
    }

    private void llenarTablaDeNovedades() {
        calcularTotalNomina();
        //Inicializamos la matriz de la lista con 4 columnas y el # de elementos de la lista
        String matriz[][] = new String[listaAcunove.size() + 2][7];

        int y = 0;

        for (int i = 0; i < listaAcunove.size(); i++) {
            String codNov = listaAcunove.get(i).getCod_nov();
            matriz[i][0] = listaAcunove.get(i).getNom_nov();

            if (codNov.charAt(0) == 'A') {
                matriz[i][1] = Utiles.enmascararCantidad(listaAcunove.get(i).getCant_nov());
                matriz[i][2] = Utiles.enmascararNumeroDouble(listaAcunove.get(i).getValor_unit());
                matriz[i][3] = Utiles.enmascararNumeroDouble(listaAcunove.get(i).getValor_tot());
                matriz[i][4] = "";
                matriz[i][5] = "";
                matriz[i][6] = "";
            } else if (codNov.charAt(0) == 'D') {
                matriz[i][1] = "";
                matriz[i][2] = "";
                matriz[i][3] = "";
                matriz[i][4] = Utiles.enmascararCantidad(listaAcunove.get(i).getCant_nov());
                matriz[i][5] = Utiles.enmascararNumeroDouble(listaAcunove.get(i).getValor_unit());
                matriz[i][6] = Utiles.enmascararNumeroDouble(listaAcunove.get(i).getValor_tot());
            }
            y++;
        }
        if (listaAcunove.size() > 0) {

            matriz[y][0] = "TOTAL EMPLEADO";
            matriz[y][1] = "";
            matriz[y][2] = "";
            matriz[y][3] = Utiles.enmascararNumeroDouble(totalPagos);
            matriz[y][4] = "";
            matriz[y][5] = "";
            matriz[y][6] = Utiles.enmascararNumeroDouble(totalDescuentos);

            y++;

            matriz[y][0] = "NETO A PAGAR";
            matriz[y][1] = "";
            matriz[y][2] = "";
            matriz[y][3] = "";
            matriz[y][4] = "";
            matriz[y][5] = "";
            matriz[y][6] = Utiles.enmascararNumeroDouble(totalAcunove);
        }

        //Inicializamos el objeto tabla de la vista
        tblNovedades.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9));
        if (listaAcunove.size() > 0) {
            dtmNovedades = new DefaultTableModel(matriz, tituloTablaNovedades) {
                //Cancelamos la edición de la tabla en la vista
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        } else {
            dtmNovedades = new DefaultTableModel(contenidoVacioNovedades, tituloTablaNovedades) {
                //Cancelamos la edición de la tabla en la vista
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        }
        tblNovedades.setModel(dtmNovedades);
        aplicarDisenioTablaNovedades();
    }

    public void llenarTablaPdf(ArrayList<AcunoveDTO> lista) {
        try {

            //Inicializamos la table y aplicamos formato
            table = new PdfPTable(anchoColumnas);
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);

            // Borde de la celda
            table.getDefaultCell().setBorder(Rectangle.BOX);

            //Titulos de la table pdf
            establecerTitulosATablaPdf();

            for (AcunoveDTO item : lista) {

                String codPara = item.getCod_nov();
                //Tipo Novedad
                Paragraph tn = new Paragraph(item.getNom_nov(), fontCourierNormal);
                PdfPCell cell = new PdfPCell(tn);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                table.addCell(cell);

                //Pagos
                if (codPara.charAt(0) == 'A') {
                    //Cantidad
                    Paragraph c = new Paragraph(enmascararCantidad("" + item.getCant_nov()), fontCourierNormal);
                    PdfPCell cell2 = new PdfPCell(c);
                    cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    table.addCell(cell2);

                    //Valor Unitario
                    Paragraph vu = new Paragraph(enmascararNumeroDouble(item.getValor_unit()), fontCourierNormal);
                    PdfPCell cell3 = new PdfPCell(vu);
                    cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    table.addCell(cell3);

                    //Valor Total
                    Paragraph vt = new Paragraph(enmascararNumeroDouble(item.getValor_tot()), fontCourierNormal);
                    PdfPCell cell4 = new PdfPCell(vt);
                    cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell4.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    table.addCell(cell4);

                    table.addCell("");
                    table.addCell("");
                    table.addCell("");

                } //Descuentos
                else {
                    table.addCell("");
                    table.addCell("");
                    table.addCell("");

                    //Cantidad
                    Paragraph cant = new Paragraph(enmascararCantidad("" + item.getCant_nov()), fontCourierNormal);
                    PdfPCell cell5 = new PdfPCell(cant);
                    cell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell5.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    table.addCell(cell5);

                    //Valor Unitario
                    Paragraph vlrU = new Paragraph(enmascararNumeroDouble(item.getValor_unit()), fontCourierNormal);
                    PdfPCell cell6 = new PdfPCell(vlrU);
                    cell6.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell6.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    table.addCell(cell6);

                    //7 TITULO
                    Paragraph vt1 = new Paragraph(enmascararNumeroDouble(item.getValor_tot()), fontCourierNormal);
                    PdfPCell cell7 = new PdfPCell(vt1);
                    cell7.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell7.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    table.addCell(cell7);
                }
            }
            //Subtotales
            //Total Empleado
            Paragraph te = new Paragraph("TOTAL EMPLEADO", fontCourierBold);
            PdfPCell cell = new PdfPCell(te);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell(cell);

            table.addCell("");
            table.addCell("");

            //Para los sub y totales calculamos todas las novedades cada vez que se llame este método
            calcularTotalNomina();

            //Total Pagos
            Paragraph tp = new Paragraph(enmascararNumeroDouble(totalPagos), fontCourierBold);
            PdfPCell cell2 = new PdfPCell(tp);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell(cell2);

            table.addCell("");
            table.addCell("");

            //Total Descuentos
            Paragraph td = new Paragraph(enmascararNumeroDouble(totalDescuentos), fontCourierBold);
            PdfPCell cell3 = new PdfPCell(td);
            cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell(cell3);

            //Neto a pagar
            Paragraph np = new Paragraph("NETO A PAGAR", fontCourierBold);
            PdfPCell cell4 = new PdfPCell(np);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell4.setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell(cell4);

            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell("");

            //$ Neto a Pagar
            Paragraph cant = new Paragraph(enmascararNumeroDouble(totalAcunove), fontCourierBold);
            PdfPCell cell5 = new PdfPCell(cant);
            cell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell5.setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell(cell5);

        } catch (Exception e) {
            System.out.println("Exception llenarTablaPdf " + e);
        } finally {
            System.out.println("La tabla del pdf se llenó correctamente");
        }
    }

    public static void main(String[] args) {
        try {

            mailAut = false;
            dataAut = false;

            String nomBd = Utiles.obtenerDirectorioActual() + "\\" + Configuracion.nombreBaseDeDatos; //le concatenamos el nombre de la base de datos
            File fichero = new File(nomBd);
            if (!fichero.exists()) { //validamos si el fichero existe si no encuentra le envía una alerta y lo saca del Programa
                JOptionPane.showMessageDialog(null, "NO se encuentra la base de datos " + Configuracion.nombreBaseDeDatos + " en el directorio actual");
                System.exit(0);
            } else if (!Utiles.verificarConexionInternet()) {
                JOptionPane.showMessageDialog(null, "NO hay acceso a internet...");
                System.exit(0);
            }

            EnviarMail enviarMail = new EnviarMail(false);

            if (!mailAut) {
                inicio = 0;
                enviarMail.setVisible(false);
                Servidor servidor = new Servidor(Mail.userMail, Mail.pass, Mail.nombreEmpresa, Mail.servidorSMTP, Mail.puerto);
                servidor.setVisible(true);

            }

//            System.exit(0);
        } catch (Exception ex) {
            System.out.println("Exception al iniciar el programa " + ex);
        }
    }

    public void mostrarAnio() {
        if (radioButtonInteresSobreCesantia.isSelected()) {
            if (Utiles.fechaActual.get(Calendar.MONTH) == 11) {
                anioChooser.setYear(Utiles.fechaActual.get(Calendar.YEAR));
            } else {
                anioChooser.setYear(Utiles.fechaActual.get(Calendar.YEAR) - 1);
            }
        } else if (radioButtonPrima.isSelected()) {
            if (radioButtonDiciembre.isSelected()) {
                anioChooser.setYear(Utiles.fechaActual.get(Calendar.YEAR) - 1);
            }
        }
    }

    public void mostrarResultados(boolean res) {
        if (res) {
            lblNumResultados.setText(listaEmpleados.size() + " resultados...");
        } else {
            lblNumResultados.setText("");
        }
    }

    private void obtenerFechasDeQuincena(Date fechaL) {
        Calendar fec = Calendar.getInstance();
        fec.setTime(fechaL);
        String y = fec.get(Calendar.YEAR) + "";
        String m = (fec.get(Calendar.MONTH) + 1) < 10 ? "0" + (fec.get(Calendar.MONTH) + 1) : "" + (fec.get(Calendar.MONTH) + 1);
        String d = fec.get(Calendar.DAY_OF_MONTH) > 15 ? "16" : "01";

        //Fecha inicial
        fechaInicialDeQuincena = y + "/" + m + "/" + d;

        //Fecha final
        fechaDePago = formatearFechayyyyMMddSlash(fechaL);
    }

    public void organizarCodigos(String codigoInicial, String codigoFinal) {
        //Organizamos los codigo ingresados
        int c1 = Integer.parseInt(codigoInicial);
        int c2 = Integer.parseInt(codigoFinal);
        if (c1 > c2) {
            txtCodIniEm.setText(codigoFinal);
            llenarLabelEmpleado(codigoFinal, 1);
            txtCodFinEm.setText(codigoInicial);
            llenarLabelEmpleado(codigoInicial, 2);
            codIniEm = codigoFinal;
            codFinEm = codigoInicial;
        } else {
            codIniEm = codigoInicial;
            codFinEm = codigoFinal;
        }
    }

    public boolean organizarFechasParaConsulta() {
        boolean res = false;
        Calendar cal = Calendar.getInstance();
        if (radioButtonPrima.isSelected()) {
            if (validarAnio(anioChooser.getYear())) {
                if (radioButtonDiciembre.isSelected()) {
                    cal.set(anioChooser.getYear(), 11, 20);
                } else {
                    cal.set(anioChooser.getYear(), 05, 20);
                }
                fecha = cal.getTime();
                res = true;
            } else {
                res = false;
                btnVerMas.setEnabled(false);
                btnVerTodo.setEnabled(false);
            }
        } else if (radioButtonInteresSobreCesantia.isSelected()) {
            if (validarAnio(anioChooser.getYear())) {
                cal.set(anioChooser.getYear(), 11, 31);
                fecha = cal.getTime();
                res = true;
            } else {
                res = false;
                btnVerMas.setEnabled(false);
                btnVerTodo.setEnabled(false);
            }
        } else if (radioButtonPagoQuincena.isSelected()) {
            if (calFecha.getDate() != null) {
                if (calFecha.getDate().before(Utiles.obtenerFechaSiguienteQuincena())) {
                    fecha = calFecha.getDate();
                    res = true;
                } else {
                    JOptionPane.showMessageDialog(rootPane, "La fecha No debe ser posterior a la de la siguiente quincena", "Información", JOptionPane.WARNING_MESSAGE);
                    res = false;
                }
            }
        }
        return res;
    }

    private void seleccionarFilaTeclado() {
        //evento al cambiar  la fila seleccionada en un jTable
        ListSelectionModel rowSM = tblConsulta.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                try {
                    //Ignore extra messages.
                    if (e.getValueIsAdjusting()) {
                        return;
                    }

                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                    if (lsm.isSelectionEmpty()) {
                        //no rows are selected
                    } else {
                        //selectedRow is selected
                        int selectedRow = lsm.getMinSelectionIndex();
                        String cod = tblConsulta.getValueAt(selectedRow, 0).toString();
                        llenarListaNovedades(cod);
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(EnviarMail.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    //Obtener dirección del usuario activo
    public String userActivo() {
        String dp = System.getProperty("user.home");
        return dp;
    }

    public boolean validarAnio(int year) {
        boolean res = true;
        if (year > Utiles.fechaActual.get(Calendar.YEAR)) {
            res = false;
            inicializarTablasDeVista();
            JOptionPane.showMessageDialog(rootPane, "NO existen Novedades en este periodo...", "Información", JOptionPane.WARNING_MESSAGE);
        } else if (year < 1000) {
            inicializarTablasDeVista();
            JOptionPane.showMessageDialog(rootPane, "NO existen Novedades en este periodo...", "Información", JOptionPane.WARNING_MESSAGE);
            res = false;
        }
        return res;
    }

    private void validarEscrituraEnTxt() {

        txtCodIniEm.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
                //Validar si es solo numérico
                char caracter = e.getKeyChar();
                // Verificar si la tecla pulsada no es un digito
                if (((caracter < '0')
                        || (caracter > '9'))
                        && (caracter != '\b' /*corresponde a BACK_SPACE*/)) {
                    e.consume();  // ignorar el evento de teclado
                }
                //Validar para que solo pueda escribir un numero determinado de caracteres
                if (txtCodIniEm.getText().length() == Configuracion.numeroDeCaractaresNumericosEnElCodigo) {
                    e.consume();
                }
            }

            public void keyPressed(KeyEvent arg0) {
            }

            public void keyReleased(KeyEvent arg0) {
            }
        });

        txtCodFinEm.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
                //Validar si es solo numérico
                char caracter = e.getKeyChar();
                // Verificar si la tecla pulsada no es un digito
                if (((caracter < '0')
                        || (caracter > '9'))
                        && (caracter != '\b' /*corresponde a BACK_SPACE*/)) {
                    e.consume();  // ignorar el evento de teclado
                }
                //Validar para que solo pueda escribir un numero determinado de caracteres
                if (txtCodFinEm.getText().length() == Configuracion.numeroDeCaractaresNumericosEnElCodigo) {
                    e.consume();
                }
            }

            public void keyPressed(KeyEvent arg0) {
            }

            public void keyReleased(KeyEvent arg0) {
            }
        });
    }

    public void verTipoNovedad() {
        tipoNovedad
                = (radioButtonPagoQuincena.isSelected()) ? 1
                : (radioButtonPrima.isSelected()) ? 2
                : 3;
    }

//-------------------------------------------------------------------------------Inicio Configuración de Hilos...---------------------------------------------------------------------------------------------------------
    ArrayList<String> listaNombrePdf = new ArrayList<>(); //al descargar guarda todos los nombres de los pdf
    int dividendo, cociente, cociente1, cociente11, cociente12, cociente2, cociente21, cociente22; //variables que dividen la asignación de envio de coreo a cada uno de los hilos
    int mailSend2, mailSend3, mailSend4, mailSend5, mailSend6, mailSend7, mailSend8, mailSend9; //numero de correo enviados de cada hilo

    //variables que inician y detiene cada uno de los hilo
    boolean hiloDescargarPdfIniciado = false;
    boolean hilo2Iniciado = false;
    boolean hilo3Iniciado = false;
    boolean hilo4Iniciado = false;
    boolean hilo5Iniciado = false;
    boolean hilo6Iniciado = false;
    boolean hilo7Iniciado = false;
    boolean hilo8Iniciado = false;
    boolean hilo9Iniciado = false;
    boolean hiloConexionIniciado = false;
    boolean hiloMailIniciado = false;
    //hilos
    Thread hiloDescargarPdf;
    Thread hilo2;
    Thread hilo3;
    Thread hilo4;
    Thread hilo5;
    Thread hilo6;
    Thread hilo7;
    Thread hilo8;
    Thread hilo9;
    Thread hiloConexion;
    Thread hiloMail;
    //# de correos enviados
    public static int correosEnviados;
    //# de correos no enviados porque no tiene correo la persona
    public static int noTieneCorreo;

    //iniciación de hilos
    private void iniciarHiloDescargarPdf() {
        hiloDescargarPdf = new Thread(this);
        hiloDescargarPdf.start();
        hiloDescargarPdfIniciado = true;
    }

    private void iniciarHilo2() {
        hilo2 = new Thread(this);
        hilo2.start();
        hilo2Iniciado = true;
    }

    private void iniciarHilo3() {
        hilo3 = new Thread(this);
        hilo3.start();
        hilo3Iniciado = true;
    }

    private void iniciarHilo4() {
        hilo4 = new Thread(this);
        hilo4.start();
        hilo4Iniciado = true;
    }

    private void iniciarHilo5() {
        hilo5 = new Thread(this);
        hilo5.start();
        hilo5Iniciado = true;
    }

    private void iniciarHilo6() {
        hilo6 = new Thread(this);
        hilo6.start();
        hilo6Iniciado = true;
    }

    private void iniciarHilo7() {
        hilo7 = new Thread(this);
        hilo7.start();
        hilo7Iniciado = true;
    }

    private void iniciarHilo8() {
        hilo8 = new Thread(this);
        hilo8.start();
        hilo8Iniciado = true;
    }

    private void iniciarHilo9() {
        hilo9 = new Thread(this);
        hilo9.start();
        hilo9Iniciado = true;
    }

    private void iniciarHiloConexion() {
        hiloConexion = new Thread(this);
        hiloConexion.start();
        hiloConexionIniciado = true;
    }

    private void iniciarHiloMail() {
        hiloMail = new Thread(this);
        hiloMail.start();
        hiloMailIniciado = true;
    }

    //ejecución de hilos
    @Override
    public void run() {
        Thread ct = Thread.currentThread(); //si hay un hilo iniciado
        //Establecer conexion a base de datos ---------------------------------------------------------
        try {
            if (ct == hiloConexion && hiloConexionIniciado) {
                if (Conexion.establecerConexion()) {
                    hiloConexionIniciado = false;
                }
            }
        } catch (Exception e) {
            System.out.println("Excepción en la ejecución del hilo que establece la conexion " + e);
        }
        //VAlida el correo ---------------------------------------------------------------------------
        try {
            if (ct == hiloMail && hiloMailIniciado) {
                //Validamos el correo
                //leemos los parametros en los txt
                if (Mail.leerParametrosDeCorreo()) {
                    //Capturamos todos los parametros del txt y los guardamos en unas variables globales en la clase Mail
                    //verificamos si los parametros existentes son correctos mediante el envío de un correo electróico al correo capturado
                    //Si hay conexion verifica que los parametros dados sean válidos
                    if (mail.send(Mail.userMail, Configuracion.asuntoDeIngreso, Configuracion.mensajeDeIngreso)) {
                        mailAut = true;
                        hiloMailIniciado = false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Excepción en la ejecución del hilo de correo " + e);
        } finally {
            hiloMailIniciado = false;
            System.out.println("Se validó el correo...");
        }
        //Descarga todos los pdf... hilo  descargar pdf------------------------------------------------
        if (ct == hiloDescargarPdf && hiloDescargarPdfIniciado) {
            try {

                for (EmpleadoDTO e : listaEmp) {
                    String nombrePdf = "";
                    if (e.getCorreo() == null) { //si no tiene correo agregeme nulo a esta posición de los nombres del pdf
                        listaNombrePdf.add(null);
                    } else { //de lo contrario
                        //inicializamos objetos
                        listaAcunove = new ArrayList<>();
                        parametroDTO = new ParametroDTO();

                        //Llenamos los objetos con sus valores correspondientes
                        parametroDTO = parametroDAO.buscarParametroCentroDeCosto(e.getCentro_de_costo());
                        llenarListaNovedades(e.getCod_empleado());
                        nombrePdf = descargarPdf(e, parametroDTO, listaAcunove);//descargamos el pdf obteniendo su nombre
                        listaNombrePdf.add(nombrePdf);//agregamos el nombre a la lista
                    }
                }
                hiloDescargarPdfIniciado = false;
            } catch (Exception e) {
                System.out.println("Exepcion en la ejecución del hilo descarga de los pdf " + e);
            } finally {
                System.out.println("Se descargaron todos los pdf ----------------------------------------------------");
            }
        }

        //Envia el correo a todos los empleados de la lista hilo 2 ------------------------------------
        if (ct == hilo2 && hilo2Iniciado) {
            try {
                System.out.println("Hilo 2..................................");
                int size = (numeroDeHilos == 8) ? cociente11 //ejecute 8 hilos
                        : (numeroDeHilos == 4) ? cociente1 //ejecute 4 hilos
                                : (numeroDeHilos == 4) ? cociente //ejecute 2 hilos
                                        : listaEmp.size(); //si no recorra toda la lista ejecutando solo 1 hilo
                for (int i = 0; i < size; i++) {
                    System.out.println("Estamos en el hilo 2---------------------------------------------------------");
                    if (listaEmp.get(i).getCorreo() == null || listaNombrePdf.get(i) == null) {  //si no tiene correo ó la posición i es null
                        noTieneCorreo++; //aumentamos en 1 la variable no tiene correo
                        System.out.println("No tiene correo");
                        continue;
                    }
                    String asunto
                            = (radioButtonPagoQuincena.isSelected()) ? "Nomina: " + Utiles.formatearFechayyyyMMddSlash(fecha) + " de " + listaEmp.get(i).getNom_em() //si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? "Prima de Servicios"//si es prima de servicios
                            : "Intereses sobre Cesantías"; //si no es interes sobre cesantia
                    String mensaje
                            = (radioButtonPagoQuincena.isSelected()) ? "Nómina desde: " + fechaInicialDeQuincena + " hasta: " + fechaDePago//si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? (radioButtonJunio.isSelected()) ? "Prima de Servicios Primer Semestre año " + anioChooser.getYear() : "Prima de Servicios Segundo Semestre año " + anioChooser.getYear()//si es prima de servicios
                            : "Intereses sobre Cesantías año " + anioChooser.getYear();//si no es interes sobre
                    mail.send(Configuracion.correoDePrueba, asunto, mensaje, listaNombrePdf.get(i), true); //enviamos el correo al objeto instanciado //para enviar en producción hay que reemplazar Configuracion.correoDePrueba por listaEmp.get(i).getCorreo() y para hacer pruebas lo contrario
                    correosEnviados++; //aumentamos en 1 la variable correos enviados
                    mailSend2++; //aumenta el 1 el munero de correos enviados de este hilo
                    if (i == size - 1) { //si i es igual a  size - 1 entoces el hilo2 = false
                        hilo2Iniciado = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exepcion en la ejecución de los hilo 2 " + e);
            } finally {
                System.out.println("El hilo 2 terminó..................................");
            }

        }

        //Envia el correo a todos los empleados de la lista hilo 3 ------------------------------------
        if (ct == hilo3 && hilo3Iniciado) {
            try {
                System.out.println("hilo 3..................................");
                int ini = (numeroDeHilos == 8) ? cociente11 //ejecute 8 hilos
                        : (numeroDeHilos == 4) ? cociente1 //ejecute 4 hilos
                                : cociente; //si no ejecute 2 hilos
                int size = (numeroDeHilos == 8) ? cociente1 //ejecute 8 hilos
                        : (numeroDeHilos == 4) ? cociente //ejecute 4 hilos
                                : listaEmp.size(); //si no ejecute 2 hilos
                for (int i = ini; i < size; i++) {
                    System.out.println("Estamos en el hilo 3---------------------------------------------------------");
                    if (listaEmp.get(i).getCorreo() == null || listaNombrePdf.get(i) == null) {  //si no tiene correo ó la posición i es null
                        noTieneCorreo++; //aumentamos en 1 la variable no tiene correo
                        System.out.println("No tiene correo");
                        continue;
                    }
                    String asunto
                            = (radioButtonPagoQuincena.isSelected()) ? "Nomina: " + Utiles.formatearFechayyyyMMddSlash(fecha) + " de " + listaEmp.get(i).getNom_em() //si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? "Prima de Servicios"//si es prima de servicios
                            : "Intereses sobre Cesantías"; //si no es interes sobre cesantia
                    String mensaje
                            = (radioButtonPagoQuincena.isSelected()) ? "Nómina desde: " + fechaInicialDeQuincena + " hasta: " + fechaDePago//si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? (radioButtonJunio.isSelected()) ? "Prima de Servicios Primer Semestre año " + anioChooser.getYear() : "Prima de Servicios Segundo Semestre año " + anioChooser.getYear()//si es prima de servicios
                            : "Intereses sobre Cesantías año " + anioChooser.getYear();//si no es interes sobre
                    mail.send(Configuracion.correoDePrueba, asunto, mensaje, listaNombrePdf.get(i), true); //enviamos el correo al objeto instanciado
                    correosEnviados++; //aumentamos en 1 la variable correos enviados
                    mailSend3++; //aumenta el 1 el munero de correos enviados de este hilo
                    if (i == size - 1) { //si i es igual a  size - 1 entoces el hilo3 = false
                        hilo3Iniciado = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exepcion en la ejecución de los hilo 3 " + e);
            } finally {
                System.out.println("El hilo 3 terminó..................................");
            }
        }

        //Envia el correo a todos los empleados de la lista hilo 4 ------------------------------------
        if (ct == hilo4 && hilo4Iniciado) {
            try {
                System.out.println("hilo 4..................................");
                int ini = (numeroDeHilos == 8) ? cociente1 //ejecute 8 hilos
                        : cociente; //ejecute 4 hilos

                int size = (numeroDeHilos == 8) ? cociente12 //ejecute 8 hilos
                        : cociente2; //ejecute 4 hilos
                for (int i = ini; i < size; i++) {
                    System.out.println("Estamos en el hilo 4---------------------------------------------------------");
                    if (listaEmp.get(i).getCorreo() == null || listaNombrePdf.get(i) == null) {  //si no tiene correo ó la posición i es null
                        noTieneCorreo++; //aumentamos en 1 la variable no tiene correo
                        System.out.println("No tiene correo");
                        continue;
                    }
                    String asunto
                            = (radioButtonPagoQuincena.isSelected()) ? "Nomina: " + Utiles.formatearFechayyyyMMddSlash(fecha) + " de " + listaEmp.get(i).getNom_em() //si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? "Prima de Servicios"//si es prima de servicios
                            : "Intereses sobre Cesantías"; //si no es interes sobre cesantia
                    String mensaje
                            = (radioButtonPagoQuincena.isSelected()) ? "Nómina desde: " + fechaInicialDeQuincena + " hasta: " + fechaDePago//si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? (radioButtonJunio.isSelected()) ? "Prima de Servicios Primer Semestre año " + anioChooser.getYear() : "Prima de Servicios Segundo Semestre año " + anioChooser.getYear()//si es prima de servicios
                            : "Intereses sobre Cesantías año " + anioChooser.getYear();//si no es interes sobre
                    mail.send(Configuracion.correoDePrueba, asunto, mensaje, listaNombrePdf.get(i), true); //enviamos el correo al objeto instanciado
                    correosEnviados++; //aumentamos en 1 la variable correos enviados
                    mailSend4++; //aumenta el 1 el munero de correos enviados de este hilo
                    if (i == size - 1) { //si i es igual a size - 1 entoces el hilo4 = false
                        hilo4Iniciado = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exepcion en la ejecución de los hilo 4 " + e);
            } finally {
                System.out.println("El hilo 4 terminó..................................");
            }
        }

        //Envia el correo a todos los empleados de la lista hilo 5 ------------------------------------
        if (ct == hilo5 && hilo5Iniciado) {
            try {
                System.out.println("hilo 5..................................");
                int ini = (numeroDeHilos == 8) ? cociente12 //ejecute 8 hilos
                        : cociente2; //ejecute 4 hilos
                int size = (numeroDeHilos == 8) ? cociente //ejecute 8 hilos
                        : listaEmp.size(); //ejecute 4 hilos
                for (int i = ini; i < size; i++) {
                    System.out.println("Estamos en el hilo 5---------------------------------------------------------");
                    if (listaEmp.get(i).getCorreo() == null || listaNombrePdf.get(i) == null) {  //si no tiene correo ó la posición i es null
                        noTieneCorreo++; //aumentamos en 1 la variable no tiene correo
                        System.out.println("No tiene correo");
                        continue;
                    }
                    String asunto
                            = (radioButtonPagoQuincena.isSelected()) ? "Nomina: " + Utiles.formatearFechayyyyMMddSlash(fecha) + " de " + listaEmp.get(i).getNom_em() //si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? "Prima de Servicios"//si es prima de servicios
                            : "Intereses sobre Cesantías"; //si no es interes sobre cesantia
                    String mensaje
                            = (radioButtonPagoQuincena.isSelected()) ? "Nómina desde: " + fechaInicialDeQuincena + " hasta: " + fechaDePago//si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? (radioButtonJunio.isSelected()) ? "Prima de Servicios Primer Semestre año " + anioChooser.getYear() : "Prima de Servicios Segundo Semestre año " + anioChooser.getYear()//si es prima de servicios
                            : "Intereses sobre Cesantías año " + anioChooser.getYear();//si no es interes sobre
                    mail.send(Configuracion.correoDePrueba, asunto, mensaje, listaNombrePdf.get(i), true); //enviamos el correo al objeto instanciado
                    correosEnviados++; //aumentamos en 1 la variable correos enviados
                    mailSend5++; //aumenta el 1 el munero de correos enviados de este hilo
                    if (i == size - 1) { //si i es igual a size - 1 entoces el hilo5 = false
                        hilo5Iniciado = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception en la ejecución de los hilo 5 " + e);
            } finally {
                System.out.println("El hilo 5 terminó..................................");
            }
        }

        //Envia el correo a todos los empleados de la lista hilo 6 ------------------------------------
        if (ct == hilo6 && hilo6Iniciado) {
            try {
                System.out.println("hilo 6..................................");
                for (int i = cociente; i < cociente21; i++) {
                    System.out.println("Estamos en el hilo 6---------------------------------------------------------");
                    if (listaEmp.get(i).getCorreo() == null || listaNombrePdf.get(i) == null) {  //si no tiene correo ó la posición i es null
                        noTieneCorreo++; //aumentamos en 1 la variable no tiene correo
                        System.out.println("No tiene correo");
                        continue;
                    }
                    String asunto
                            = (radioButtonPagoQuincena.isSelected()) ? "Nomina: " + Utiles.formatearFechayyyyMMddSlash(fecha) + " de " + listaEmp.get(i).getNom_em() //si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? "Prima de Servicios"//si es prima de servicios
                            : "Intereses sobre Cesantías"; //si no es interes sobre cesantia
                    String mensaje
                            = (radioButtonPagoQuincena.isSelected()) ? "Nómina desde: " + fechaInicialDeQuincena + " hasta: " + fechaDePago//si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? (radioButtonJunio.isSelected()) ? "Prima de Servicios Primer Semestre año " + anioChooser.getYear() : "Prima de Servicios Segundo Semestre año " + anioChooser.getYear()//si es prima de servicios
                            : "Intereses sobre Cesantías año " + anioChooser.getYear();//si no es interes sobre
                    mail.send(Configuracion.correoDePrueba, asunto, mensaje, listaNombrePdf.get(i), true); //enviamos el correo al objeto instanciado
                    correosEnviados++; //aumentamos en 1 la variable correos enviados
                    mailSend6++; //aumenta el 1 el munero de correos enviados de este hilo
                    if (i == cociente21 - 1) { //si i es igual a cociente21 - 1 entoces el hilo6 = false
                        hilo6Iniciado = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception en la ejecución de los hilo 6 " + e);
            } finally {
                System.out.println("El hilo 6 terminó..................................");
            }
        }

        //Envia el correo a todos los empleados de la lista hilo 7 ------------------------------------
        if (ct == hilo7 && hilo7Iniciado) {
            try {
                System.out.println("hilo 7..................................");
                for (int i = cociente21; i < cociente2; i++) {
                    System.out.println("Estamos en el hilo 7---------------------------------------------------------");
                    if (listaEmp.get(i).getCorreo() == null || listaNombrePdf.get(i) == null) {  //si no tiene correo ó la posición i es null
                        noTieneCorreo++; //aumentamos en 1 la variable no tiene correo
                        System.out.println("No tiene correo");
                        continue;
                    }
                    String asunto
                            = (radioButtonPagoQuincena.isSelected()) ? "Nomina: " + Utiles.formatearFechayyyyMMddSlash(fecha) + " de " + listaEmp.get(i).getNom_em() //si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? "Prima de Servicios"//si es prima de servicios
                            : "Intereses sobre Cesantías"; //si no es interes sobre cesantia
                    String mensaje
                            = (radioButtonPagoQuincena.isSelected()) ? "Nómina desde: " + fechaInicialDeQuincena + " hasta: " + fechaDePago//si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? (radioButtonJunio.isSelected()) ? "Prima de Servicios Primer Semestre año " + anioChooser.getYear() : "Prima de Servicios Segundo Semestre año " + anioChooser.getYear()//si es prima de servicios
                            : "Intereses sobre Cesantías año " + anioChooser.getYear();//si no es interes sobre
                    mail.send(Configuracion.correoDePrueba, asunto, mensaje, listaNombrePdf.get(i), true); //enviamos el correo al objeto instanciado
                    correosEnviados++; //aumentamos en 1 la variable correos enviados
                    mailSend7++; //aumenta el 1 el munero de correos enviados de este hilo
                    if (i == cociente2 - 1) { //si i es igual a cociente2 - 1 entoces el hilo7 = false
                        hilo7Iniciado = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception en la ejecución de los hilo 7 " + e);
            } finally {
                System.out.println("El hilo 7 terminó..................................");
            }
        }
        //Envia el correo a todos los empleados de la lista hilo 8 ------------------------------------
        if (ct == hilo8 && hilo8Iniciado) {
            try {
                System.out.println("hilo 8..................................");
                for (int i = cociente2; i < cociente22; i++) {
                    System.out.println("Estamos en el hilo 8---------------------------------------------------------");
                    if (listaEmp.get(i).getCorreo() == null || listaNombrePdf.get(i) == null) {  //si no tiene correo ó la posición i es null
                        noTieneCorreo++; //aumentamos en 1 la variable no tiene correo
                        System.out.println("No tiene correo");
                        continue;
                    }
                    String asunto
                            = (radioButtonPagoQuincena.isSelected()) ? "Nomina: " + Utiles.formatearFechayyyyMMddSlash(fecha) + " de " + listaEmp.get(i).getNom_em() //si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? "Prima de Servicios"//si es prima de servicios
                            : "Intereses sobre Cesantías"; //si no es interes sobre cesantia
                    String mensaje
                            = (radioButtonPagoQuincena.isSelected()) ? "Nómina desde: " + fechaInicialDeQuincena + " hasta: " + fechaDePago//si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? (radioButtonJunio.isSelected()) ? "Prima de Servicios Primer Semestre año " + anioChooser.getYear() : "Prima de Servicios Segundo Semestre año " + anioChooser.getYear()//si es prima de servicios
                            : "Intereses sobre Cesantías año " + anioChooser.getYear();//si no es interes sobre
                    mail.send(Configuracion.correoDePrueba, asunto, mensaje, listaNombrePdf.get(i), true); //enviamos el correo al objeto instanciado
                    correosEnviados++; //aumentamos en 1 la variable correos enviados
                    mailSend8++; //aumenta el 1 el munero de correos enviados de este hilo
                    if (i == cociente22 - 1) { //si i es igual a cociente22 - 1 entoces el hilo8 = false
                        hilo8Iniciado = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception en la ejecución de los hilo 8 " + e);
            } finally {
                System.out.println("El hilo 8 terminó..................................");
            }
        }
        //Envia el correo a todos los empleados de la lista hilo 9 ------------------------------------
        if (ct == hilo9 && hilo9Iniciado) {
            try {
                System.out.println("hilo 9..................................");
                for (int i = cociente22; i < listaEmp.size(); i++) {
                    System.out.println("Estamos en el hilo 9---------------------------------------------------------");
                    if (listaEmp.get(i).getCorreo() == null || listaNombrePdf.get(i) == null) {  //si no tiene correo ó la posición i es null
                        noTieneCorreo++; //aumentamos en 1 la variable no tiene correo
                        System.out.println("No tiene correo");
                        continue;
                    }
                    String asunto
                            = (radioButtonPagoQuincena.isSelected()) ? "Nomina: " + Utiles.formatearFechayyyyMMddSlash(fecha) + " de " + listaEmp.get(i).getNom_em() //si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? "Prima de Servicios"//si es prima de servicios
                            : "Intereses sobre Cesantías"; //si no es interes sobre cesantia
                    String mensaje
                            = (radioButtonPagoQuincena.isSelected()) ? "Nómina desde: " + fechaInicialDeQuincena + " hasta: " + fechaDePago//si es pago de quincena
                            : (radioButtonPrima.isSelected()) ? (radioButtonJunio.isSelected()) ? "Prima de Servicios Primer Semestre año " + anioChooser.getYear() : "Prima de Servicios Segundo Semestre año " + anioChooser.getYear()//si es prima de servicios
                            : "Intereses sobre Cesantías año " + anioChooser.getYear();//si no es interes sobre
                    mail.send(Configuracion.correoDePrueba, asunto, mensaje, listaNombrePdf.get(i), true); //enviamos el correo al objeto instanciado
                    correosEnviados++; //aumentamos en 1 la variable correos enviados
                    mailSend9++; //aumenta el 1 el munero de correos enviados de este hilo
                    if (i == listaEmp.size() - 1) { //si i es igual a listaEmp.size() - 1 entoces el hilo9 = false
                        hilo9Iniciado = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception en la ejecución de los hilo 9 " + e);
            } finally {
                System.out.println("El hilo 9 terminó..................................");
            }
        }
    }

//-------------------------------------------------------------------------------Fin Configuración de Hilos...---------------------------------------------------------------------------------------------------------
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogSelectEm = new javax.swing.JDialog();
        buttonGroupBusqueda = new javax.swing.ButtonGroup();
        buttonGroupFiltro = new javax.swing.ButtonGroup();
        buttonGroupMesPrima = new javax.swing.ButtonGroup();
        panelBody = new javax.swing.JPanel();
        scrollPaneTablaConsulta = new javax.swing.JScrollPane();
        tblConsulta = new javax.swing.JTable();
        scrollPaneTablaNovedades = new javax.swing.JScrollPane();
        tblNovedades = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblFechaDeNovedad = new javax.swing.JLabel();
        panelFooter = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnEnviarCorreo = new javax.swing.JButton();
        btnVerTodo = new javax.swing.JButton();
        btnVerMas = new javax.swing.JButton();
        lblNumResultados = new javax.swing.JLabel();
        lblF3 = new javax.swing.JLabel();
        panelHeader = new javax.swing.JPanel();
        radioButtonPagoQuincena = new javax.swing.JRadioButton();
        radioButtonPrima = new javax.swing.JRadioButton();
        radioButtonInteresSobreCesantia = new javax.swing.JRadioButton();
        radioButtonCentroDeCosto = new javax.swing.JRadioButton();
        comboBoxCentroDeCosto = new javax.swing.JComboBox<>();
        txtCodIniEm = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        radioButtonPorCodigo = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        txtCodFinEm = new javax.swing.JTextField();
        btnSelectEmFin = new javax.swing.JButton();
        btnSelectEmIni = new javax.swing.JButton();
        lblNomEm1 = new javax.swing.JLabel();
        lblNomEm2 = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        calFecha = new com.toedter.calendar.JDateChooser();
        anioChooser = new com.toedter.calendar.JYearChooser();
        lblAnio = new javax.swing.JLabel();
        radioButtonJunio = new javax.swing.JRadioButton();
        radioButtonDiciembre = new javax.swing.JRadioButton();
        btnBuscar = new javax.swing.JButton();

        javax.swing.GroupLayout dialogSelectEmLayout = new javax.swing.GroupLayout(dialogSelectEm.getContentPane());
        dialogSelectEm.getContentPane().setLayout(dialogSelectEmLayout);
        dialogSelectEmLayout.setHorizontalGroup(
            dialogSelectEmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        dialogSelectEmLayout.setVerticalGroup(
            dialogSelectEmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panelBody.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        scrollPaneTablaConsulta.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneTablaConsulta.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneTablaConsulta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tblConsulta.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        tblConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "COD EMP", "NOM EMP", "CC", "Ultimo Sueldo", "Centro de Costo", "Correo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblConsulta.setRequestFocusEnabled(false);
        tblConsulta.setRowHeight(20);
        tblConsulta.getTableHeader().setReorderingAllowed(false);
        tblConsulta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblConsultaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblConsultaFocusLost(evt);
            }
        });
        tblConsulta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblConsultaMouseClicked(evt);
            }
        });
        tblConsulta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblConsultaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tblConsultaKeyTyped(evt);
            }
        });
        scrollPaneTablaConsulta.setViewportView(tblConsulta);
        tblConsulta.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblConsulta.getColumnModel().getColumnCount() > 0) {
            tblConsulta.getColumnModel().getColumn(0).setResizable(false);
            tblConsulta.getColumnModel().getColumn(0).setPreferredWidth(5);
            tblConsulta.getColumnModel().getColumn(1).setResizable(false);
            tblConsulta.getColumnModel().getColumn(1).setPreferredWidth(40);
            tblConsulta.getColumnModel().getColumn(2).setResizable(false);
            tblConsulta.getColumnModel().getColumn(2).setPreferredWidth(5);
            tblConsulta.getColumnModel().getColumn(3).setResizable(false);
            tblConsulta.getColumnModel().getColumn(3).setPreferredWidth(5);
            tblConsulta.getColumnModel().getColumn(4).setResizable(false);
            tblConsulta.getColumnModel().getColumn(4).setPreferredWidth(40);
            tblConsulta.getColumnModel().getColumn(5).setResizable(false);
            tblConsulta.getColumnModel().getColumn(5).setPreferredWidth(40);
        }

        scrollPaneTablaNovedades.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneTablaNovedades.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneTablaNovedades.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tblNovedades.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        tblNovedades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "TIPO NOVEDAD", "CANT", "VLR UNI", "VLR TOTAL", "CANT", "VLR UNI", "VLR TOTAL"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNovedades.setRequestFocusEnabled(false);
        tblNovedades.setRowHeight(20);
        tblNovedades.getTableHeader().setReorderingAllowed(false);
        tblNovedades.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNovedadesMouseClicked(evt);
            }
        });
        tblNovedades.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tblNovedadesKeyTyped(evt);
            }
        });
        scrollPaneTablaNovedades.setViewportView(tblNovedades);
        tblNovedades.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblNovedades.getColumnModel().getColumnCount() > 0) {
            tblNovedades.getColumnModel().getColumn(0).setResizable(false);
            tblNovedades.getColumnModel().getColumn(0).setPreferredWidth(5);
            tblNovedades.getColumnModel().getColumn(1).setResizable(false);
            tblNovedades.getColumnModel().getColumn(1).setPreferredWidth(40);
            tblNovedades.getColumnModel().getColumn(2).setResizable(false);
            tblNovedades.getColumnModel().getColumn(2).setPreferredWidth(5);
            tblNovedades.getColumnModel().getColumn(3).setResizable(false);
            tblNovedades.getColumnModel().getColumn(3).setPreferredWidth(5);
            tblNovedades.getColumnModel().getColumn(4).setResizable(false);
            tblNovedades.getColumnModel().getColumn(4).setPreferredWidth(40);
            tblNovedades.getColumnModel().getColumn(5).setResizable(false);
            tblNovedades.getColumnModel().getColumn(5).setPreferredWidth(40);
        }

        jLabel5.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 13)); // NOI18N
        jLabel5.setText("NOVEDADES");

        jLabel3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 13)); // NOI18N
        jLabel3.setText("Pagos");

        jLabel4.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 13)); // NOI18N
        jLabel4.setText("Descuentos");

        lblFechaDeNovedad.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 11)); // NOI18N

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblFechaDeNovedad, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(137, 137, 137))
            .addComponent(scrollPaneTablaConsulta)
            .addComponent(scrollPaneTablaNovedades)
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(scrollPaneTablaConsulta, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblFechaDeNovedad, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPaneTablaNovedades, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnCancelar.setText("Cancelar");
        btnCancelar.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnCancelar.setPreferredSize(new java.awt.Dimension(70, 40));
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnEnviarCorreo.setText("E-mail");
        btnEnviarCorreo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnEnviarCorreo.setPreferredSize(new java.awt.Dimension(70, 40));
        btnEnviarCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarCorreoActionPerformed(evt);
            }
        });

        btnVerTodo.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        btnVerTodo.setText("Ver Todo");
        btnVerTodo.setPreferredSize(new java.awt.Dimension(70, 30));
        btnVerTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerTodoActionPerformed(evt);
            }
        });
        btnVerTodo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                btnVerTodoKeyTyped(evt);
            }
        });

        btnVerMas.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        btnVerMas.setText("Ver Mas");
        btnVerMas.setPreferredSize(new java.awt.Dimension(70, 30));
        btnVerMas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerMasActionPerformed(evt);
            }
        });
        btnVerMas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                btnVerMasKeyTyped(evt);
            }
        });

        lblNumResultados.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N

        lblF3.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 14)); // NOI18N
        lblF3.setText("F3 -> Seleccionar Empleado");

        javax.swing.GroupLayout panelFooterLayout = new javax.swing.GroupLayout(panelFooter);
        panelFooter.setLayout(panelFooterLayout);
        panelFooterLayout.setHorizontalGroup(
            panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFooterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFooterLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblNumResultados, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49))
                    .addGroup(panelFooterLayout.createSequentialGroup()
                        .addComponent(lblF3, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(btnVerMas, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnVerTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEnviarCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelFooterLayout.setVerticalGroup(
            panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFooterLayout.createSequentialGroup()
                .addGroup(panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnEnviarCorreo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnVerTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnVerMas, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelFooterLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(lblNumResultados, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblF3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonGroupFiltro.add(radioButtonPagoQuincena);
        radioButtonPagoQuincena.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        radioButtonPagoQuincena.setText("Pago Quincena");
        radioButtonPagoQuincena.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        radioButtonPagoQuincena.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonPagoQuincenaActionPerformed(evt);
            }
        });

        buttonGroupFiltro.add(radioButtonPrima);
        radioButtonPrima.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        radioButtonPrima.setText("Prima");
        radioButtonPrima.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        radioButtonPrima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonPrimaActionPerformed(evt);
            }
        });

        buttonGroupFiltro.add(radioButtonInteresSobreCesantia);
        radioButtonInteresSobreCesantia.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        radioButtonInteresSobreCesantia.setText("Interés sobre Cesantía");
        radioButtonInteresSobreCesantia.setActionCommand("Interes sobre Cesantía");
        radioButtonInteresSobreCesantia.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        radioButtonInteresSobreCesantia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonInteresSobreCesantiaActionPerformed(evt);
            }
        });

        buttonGroupBusqueda.add(radioButtonCentroDeCosto);
        radioButtonCentroDeCosto.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        radioButtonCentroDeCosto.setText("Por Centro de Costo");
        radioButtonCentroDeCosto.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        radioButtonCentroDeCosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonCentroDeCostoActionPerformed(evt);
            }
        });

        comboBoxCentroDeCosto.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N
        comboBoxCentroDeCosto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxCentroDeCostoItemStateChanged(evt);
            }
        });
        comboBoxCentroDeCosto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                comboBoxCentroDeCostoKeyTyped(evt);
            }
        });

        txtCodIniEm.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N
        txtCodIniEm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtCodIniEm.setPreferredSize(new java.awt.Dimension(104, 27));
        txtCodIniEm.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCodIniEmFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodIniEmFocusLost(evt);
            }
        });
        txtCodIniEm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodIniEmKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodIniEmKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        jLabel1.setText("Cod. Inicial Empleado");

        buttonGroupBusqueda.add(radioButtonPorCodigo);
        radioButtonPorCodigo.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        radioButtonPorCodigo.setText("Por Código");
        radioButtonPorCodigo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        radioButtonPorCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonPorCodigoActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        jLabel8.setText("Cod. Final Empleado");

        txtCodFinEm.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N
        txtCodFinEm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtCodFinEm.setPreferredSize(new java.awt.Dimension(104, 27));
        txtCodFinEm.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCodFinEmFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodFinEmFocusLost(evt);
            }
        });
        txtCodFinEm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodFinEmActionPerformed(evt);
            }
        });
        txtCodFinEm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodFinEmKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodFinEmKeyTyped(evt);
            }
        });

        btnSelectEmFin.setText("...");
        btnSelectEmFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectEmFinActionPerformed(evt);
            }
        });

        btnSelectEmIni.setText("...");
        btnSelectEmIni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectEmIniActionPerformed(evt);
            }
        });

        lblNomEm1.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 9)); // NOI18N

        lblNomEm2.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 9)); // NOI18N

        lblFecha.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        lblFecha.setText("Fecha");

        calFecha.setToolTipText("");
        calFecha.setDateFormatString("yyyy/MM/dd");
        calFecha.setDoubleBuffered(false);
        calFecha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                calFechaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                calFechaFocusLost(evt);
            }
        });
        calFecha.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calFechaPropertyChange(evt);
            }
        });
        calFecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                calFechaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                calFechaKeyTyped(evt);
            }
        });

        anioChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                anioChooserFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                anioChooserFocusLost(evt);
            }
        });
        anioChooser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                anioChooserMouseClicked(evt);
            }
        });
        anioChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                anioChooserPropertyChange(evt);
            }
        });

        lblAnio.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        lblAnio.setText("Año");

        buttonGroupMesPrima.add(radioButtonJunio);
        radioButtonJunio.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        radioButtonJunio.setText("Junio");
        radioButtonJunio.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        radioButtonJunio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonJunioActionPerformed(evt);
            }
        });
        radioButtonJunio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                radioButtonJunioKeyTyped(evt);
            }
        });

        buttonGroupMesPrima.add(radioButtonDiciembre);
        radioButtonDiciembre.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        radioButtonDiciembre.setText("Diciembre");
        radioButtonDiciembre.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        radioButtonDiciembre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonDiciembreActionPerformed(evt);
            }
        });
        radioButtonDiciembre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                radioButtonDiciembreKeyTyped(evt);
            }
        });

        btnBuscar.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.setPreferredSize(new java.awt.Dimension(70, 30));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        btnBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                btnBuscarKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioButtonInteresSobreCesantia)
                    .addComponent(radioButtonPagoQuincena, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radioButtonPrima, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioButtonCentroDeCosto)
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addComponent(radioButtonPorCodigo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelHeaderLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelHeaderLayout.createSequentialGroup()
                                        .addComponent(comboBoxCentroDeCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(panelHeaderLayout.createSequentialGroup()
                                        .addComponent(txtCodIniEm, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSelectEmIni)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblNomEm1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblNomEm2, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(panelHeaderLayout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodFinEm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSelectEmFin)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelHeaderLayout.createSequentialGroup()
                            .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelHeaderLayout.createSequentialGroup()
                                    .addComponent(radioButtonJunio)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(radioButtonDiciembre))
                                .addGroup(panelHeaderLayout.createSequentialGroup()
                                    .addGap(13, 13, 13)
                                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(1, 1, 1))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createSequentialGroup()
                            .addComponent(anioChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap()))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createSequentialGroup()
                        .addComponent(calFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(radioButtonPagoQuincena, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioButtonPrima, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioButtonInteresSobreCesantia, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radioButtonCentroDeCosto)
                            .addComponent(comboBoxCentroDeCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelHeaderLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtCodIniEm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)
                                    .addComponent(btnSelectEmIni))
                                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtCodFinEm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnSelectEmFin)))
                            .addGroup(panelHeaderLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblNomEm1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)
                                .addComponent(lblNomEm2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelHeaderLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(radioButtonPorCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(calFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(anioChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radioButtonJunio)
                            .addComponent(radioButtonDiciembre))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        calFecha.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(panelHeader, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelBody, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(panelFooter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelFooter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//        sadfds
    }//GEN-LAST:event_formWindowClosing

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        if (inicio > 0) {
            txtCodIniEm.setText(codIniEm);
            llenarLabelEmpleado(codIniEm, 1);
            txtCodFinEm.setText(codFinEm);
            llenarLabelEmpleado(codFinEm, 2);
            if (fechaGuardada != null) {
                calFecha.setDate(fechaGuardada);
            }

        }
    }//GEN-LAST:event_formWindowActivated

    private void btnVerMasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnVerMasKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVerMasKeyTyped

    private void btnVerMasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerMasActionPerformed
        cargarCursor(true);
        contador += 50;
        consultar();
        if (listaEmpleados.size() < contador) {
            btnVerMas.setEnabled(false);
            btnVerTodo.setEnabled(false);
        }
        //Seleccionamos la primera fila
        if (!listaEmpleados.isEmpty()) {
            tblConsulta.setRowSelectionInterval(0, 0);
            tblConsulta.requestFocus();
        }
        cargarCursor(false);
    }//GEN-LAST:event_btnVerMasActionPerformed

    private void btnVerTodoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnVerTodoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVerTodoKeyTyped

    private void btnVerTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerTodoActionPerformed
        try {
            cargarCursor(true);
            btnVerMas.setEnabled(false);
            btnVerTodo.setEnabled(false);
            verTipoNovedad();
            if (radioButtonCentroDeCosto.isSelected() && comboBoxCentroDeCosto.getSelectedIndex() != 0) {
                listaEmpleados
                        = empleadoDAO.buscarPorCentroDeCosto(Utiles.verCodigoCentroDeCosto(comboBoxCentroDeCosto.getSelectedItem().toString()),
                                tipoNovedad,
                                "" + fecha.getDate(), "" + fecha.getMonth(), "" + fecha.getYear());
            } else if (radioButtonPorCodigo.isSelected() && lblNomEm1.getText().length() > 0 && lblNomEm2.getText().length() > 0) {
                codIniEm = txtCodIniEm.getText();
                codFinEm = txtCodFinEm.getText();
                listaEmpleados
                        = empleadoDAO.buscarPorCodigo(codIniEm, codFinEm,
                                tipoNovedad,
                                "" + fecha.getDate(), "" + fecha.getMonth(), "" + fecha.getYear());
            } else {
                listaEmpleados
                        = empleadoDAO.buscarTodos(
                                tipoNovedad,
                                "" + fecha.getDate(), "" + fecha.getMonth(), "" + fecha.getYear());
            }

        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException btnVerTodoActionPerformed " + ex);
        } catch (SQLException ex) {
            System.out.println("SQLException btnVerTodoActionPerformed " + ex);
        } finally {
            llenarTablaDeEmpleados();
            mostrarResultados(true);
            //Seleccionamos la primera fila
            tblConsulta.setRowSelectionInterval(0, 0);
            tblConsulta.requestFocus();
            cargarCursor(false);
        }

    }//GEN-LAST:event_btnVerTodoActionPerformed

    private void btnEnviarCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarCorreoActionPerformed
        try {
            btnEnviarCorreo.setEnabled(false);
            fechaGuardada = calFecha.getDate();
            anioGuardado = anioChooser.getYear();
            //Comprobar si hay conexion a internet

            if (Utiles.verificarConexionInternet()) { //si hay conexion a internet
                String mensaje = "";
                int[] filasSeleccionadas = tblConsulta.getSelectedRows(); // guardamos en un vector las filas seleccionadas
                if (radioButtonPagoQuincena.isSelected()) { // si es pago de quincena
                    mensaje
                            = filasSeleccionadas.length == 1 ? listaEmpleados.size() == 1 ? "¿Esta seguro de enviar la nómina a este empleado?" : "¿Esta seguro de enviar la nómina al empleado seleccionado?" //si las filasselect es igual a uno(1) entonces si listaEmpleados es igual a uno(1) entonces "mensaje1" 
                                    : "¿Esta seguro de enviar la nómina a los empleados seleccionados?";//de lo contrario "mensajeMuchos"
                } else if (radioButtonPrima.isSelected()) { // si es por prima
                    mensaje
                            = filasSeleccionadas.length == 1 ? listaEmpleados.size() == 1 ? "¿Esta seguro de enviar la Prima de Servicios a este empleado?" : "¿Esta seguro de enviar la Prima de Servicios al empleado seleccionado?"//si las filasselect es igual a uno(1) entonces si listaEmpleados es igual a uno(1) entonces "mensaje1" 
                                    : "¿Esta seguro de enviar la Prima de Servicios a los empleados seleccionados?";//de lo contrario "mensajeMuchos"
                } else {
                    mensaje
                            = filasSeleccionadas.length == 1 ? listaEmpleados.size() == 1 ? "¿Esta seguro de enviar los Intereses sobre Cesantías a este empleado?" : "¿Esta seguro de enviar los Intereses sobre Cesantías al empleado seleccionado?"//si las filasselect es igual a uno(1) entonces si listaEmpleados es igual a uno(1) entonces "mensaje1" 
                                    : "¿Esta seguro de enviar los Intereses sobre Cesantías a los empleados seleccionados?";//de lo contrario "mensajeMuchos"
                }

                //botones a mostrar en el mensaje de confirmación
                String seleccionAceptar = (filasSeleccionadas.length == 1) ? "Selección" : "Los " + filasSeleccionadas.length + " Empleados Seleccionados";
                Object[] opcionMuchos = {seleccionAceptar, "Todos los " + listaEmpleados.size() + " Empleados", "Cancelar"};
                Object[] opcionUno = {"Enviar", "Cancelar"};

                int eleccion; //respuesta 

                if (listaEmpleados.isEmpty()) {
                    consultar();
                }
                eleccion = JOptionPane.showOptionDialog(
                        rootPane, //componente que centra la modal en el centro del JFrame
                        mensaje, //mensaje
                        "¿A quién desea enviar el correo?", //titulo
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, //icono
                        (listaEmpleados.size() == 1) ? opcionUno : opcionMuchos, //validamos los botones de confirmación
                        null); //seleccion por defecto

                cargarCursor(true);
                if (listaEmpleados.size() == 1) { //si la lista solo tiene 1 empleado
                    if (eleccion == JOptionPane.YES_OPTION) { //si dice que enviar
                        cargarCursor(true);
                        enviarPdfPorCorreoAEmpleados(listaEmpleados); //enviar correo al empleado
                    } else { //de lo contrario ubiquelo en la tabla de empleados
                        tblConsulta.requestFocus();
                    }
                } else { //si no es porque es a la selección
                    switch (eleccion) {
                        case JOptionPane.YES_OPTION:
                            //si se envia a la selección
                            cargarCursor(true);
                            llenarListaDeEmpleadosSeleccionados(filasSeleccionadas); //lleneme la lista de empleados select 
                            enviarPdfPorCorreoAEmpleados(listaEmpleadosSelect); //envio la lista al metodo enviar
                            break;
                        case JOptionPane.NO_OPTION:
                            //si no es elección es a todos
                            cargarCursor(true);
                            enviarPdfPorCorreoAEmpleados(listaEmpleados); //envio la lista de empleados al metodo enviar
                            break;
                        default:
                            //de lo contrario ubiquelo en la tabla de empleados
                            tblConsulta.requestFocus();
                            break;
                    }
                }

            } else { //si no hay acceso a internet
                JOptionPane.showMessageDialog(rootPane, "NO hay acceso a internet", "Información", JOptionPane.WARNING_MESSAGE);
                tblConsulta.requestFocus();
            }
        } catch (Exception e) {
            btnEnviarCorreo.setEnabled(true);
            System.out.println("Exception btnEnviarCorreoActionPerformed " + e);
        } finally {
            cargarCursor(false);
            btnEnviarCorreo.setEnabled(true);
            tblConsulta.requestFocus();
        }
    }//GEN-LAST:event_btnEnviarCorreoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int res = JOptionPane.showConfirmDialog(rootPane, "Desea Salir del Programa", "¿Está seguro...?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            System.exit(0);
            cargarCursor(true);
            //propiedad que elimina la ventana
            this.dispose();
            this.setVisible(false);
            cargarCursor(false);
        } else if (res == JOptionPane.NO_OPTION) {
            System.out.println("Continuamos...");
        }

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void radioButtonDiciembreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonDiciembreActionPerformed
        //        dfd
        //Inicializamos el objeto tabla de la vista
        inicializarTablasDeVista();
        mostrarResultados(false);
        lblFechaDeNovedad.setText("");
    }//GEN-LAST:event_radioButtonDiciembreActionPerformed

    private void radioButtonJunioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonJunioActionPerformed
        //        fsdf
        //Inicializamos el objeto tabla de la vista
        inicializarTablasDeVista();
        mostrarResultados(false);
        lblFechaDeNovedad.setText("");
    }//GEN-LAST:event_radioButtonJunioActionPerformed

    private void anioChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_anioChooserPropertyChange
        // TODO add your handling code here:
        inicializarTablasDeVista();
    }//GEN-LAST:event_anioChooserPropertyChange

    private void anioChooserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_anioChooserMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_anioChooserMouseClicked

    private void anioChooserFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_anioChooserFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_anioChooserFocusLost

    private void anioChooserFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_anioChooserFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_anioChooserFocusGained

    private void btnBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnBuscarKeyTyped
        //capturamos la tecla presionada
        char tecla = evt.getKeyChar();
        if (tecla == KeyEvent.VK_ENTER) {
            btnBuscar.doClick();
        }
    }//GEN-LAST:event_btnBuscarKeyTyped


    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        if (lblNomEm1.getText().equals("")) {
            txtCodIniEm.setText("");
        }
        if (lblNomEm2.getText().equals("")) {
            txtCodFinEm.setText("");
        }
        txtCodIniEm.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        txtCodFinEm.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        cargarCursor(true);
        btnVerMas.setEnabled(true);
        btnVerTodo.setEnabled(true);
        inicializarTablaNovedades();
        contador = 50;
        consultar();
        if (listaEmpleados.isEmpty()) {
            btnVerMas.setEnabled(false);
            btnVerTodo.setEnabled(false);
        }
        lblFechaDeNovedad.setText(Utiles.formatearFechaLargaMMddYYYY(fecha));
        cargarCursor(false);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void calFechaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_calFechaKeyTyped
        //sadf
    }//GEN-LAST:event_calFechaKeyTyped

    private void calFechaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_calFechaKeyPressed
        //capturamos la tecla presionada
    }//GEN-LAST:event_calFechaKeyPressed

    private void calFechaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_calFechaPropertyChange
//        sadfs
        inicializarTablasDeVista();
    }//GEN-LAST:event_calFechaPropertyChange

    private void calFechaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_calFechaFocusLost
        //        sdfds
    }//GEN-LAST:event_calFechaFocusLost

    private void calFechaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_calFechaFocusGained
        //dfsf
    }//GEN-LAST:event_calFechaFocusGained

    private void btnSelectEmFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectEmFinActionPerformed
        numeroEmpleado = 2;
        abrirModal();
    }//GEN-LAST:event_btnSelectEmFinActionPerformed

    private void btnSelectEmIniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectEmIniActionPerformed
        numeroEmpleado = 1;
        abrirModal();
    }//GEN-LAST:event_btnSelectEmIniActionPerformed

    private void radioButtonPorCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonPorCodigoActionPerformed
        tipoBusqueda = 2;
        btnVerMas.setEnabled(false);
        comboBoxCentroDeCosto.setEnabled(false);
        comboBoxCentroDeCosto.setSelectedIndex(0);
        txtCodIniEm.setEnabled(true);
        btnSelectEmIni.setEnabled(true);
        txtCodFinEm.setEnabled(true);
        btnSelectEmFin.setEnabled(true);
        txtCodIniEm.requestFocus();
        codIniEm = "";
        codFinEm = "";

        btnVerMas.setEnabled(false);
        btnVerTodo.setEnabled(false);
        btnEnviarCorreo.setEnabled(false);

        //Inicializamos el objeto tabla de la vista
        inicializarTablasDeVista();

        mostrarResultados(false);
        lblFechaDeNovedad.setText("");
    }//GEN-LAST:event_radioButtonPorCodigoActionPerformed

    private void radioButtonCentroDeCostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonCentroDeCostoActionPerformed
        llenarConboCentroDeCosto();
        tipoBusqueda = 1;
        numeroEmpleado = 0;
        btnVerMas.setEnabled(true);
        txtCodIniEm.setEnabled(false);
        txtCodIniEm.setText("");
        lblNomEm1.setText("");
        btnSelectEmIni.setEnabled(false);
        txtCodFinEm.setEnabled(false);
        txtCodFinEm.setText("");
        lblNomEm2.setText("");
        btnSelectEmFin.setEnabled(false);
        comboBoxCentroDeCosto.setEnabled(true);
        comboBoxCentroDeCosto.setVisible(true);
        // propiedad que me permite sobre-poner el combo
        comboBoxCentroDeCosto.setLightWeightPopupEnabled(false); //usar componente de peso pesado
        comboBoxCentroDeCosto.requestFocus();
        codIniEm = "";
        codFinEm = "";

        btnVerMas.setEnabled(false);
        btnVerTodo.setEnabled(false);
        btnEnviarCorreo.setEnabled(false);

        //Inicializamos el objeto tabla de la vista
        inicializarTablasDeVista();

        mostrarResultados(false);
        lblFechaDeNovedad.setText("");
    }//GEN-LAST:event_radioButtonCentroDeCostoActionPerformed

    private void comboBoxCentroDeCostoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxCentroDeCostoKeyTyped
        //capturamos la tecla presionada
        char tecla = evt.getKeyChar();
        if (tecla == KeyEvent.VK_ENTER) {
            btnBuscar.doClick();
            if (listaEmpleados.size() > 0) {
                tblConsulta.requestFocus();
            } else {
                if (radioButtonPagoQuincena.isSelected()) {
                    calFecha.getDateEditor().getUiComponent().requestFocusInWindow();
                } else if (radioButtonPrima.isSelected() || radioButtonInteresSobreCesantia.isSelected()) {
                    anioChooser.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_comboBoxCentroDeCostoKeyTyped

    private void comboBoxCentroDeCostoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxCentroDeCostoItemStateChanged
        // TODO add your handling code here:
        inicializarTablasDeVista();
    }//GEN-LAST:event_comboBoxCentroDeCostoItemStateChanged

    private void txtCodIniEmKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodIniEmKeyTyped
        txtCodIniEm.addKeyListener(new KeyAdapter() {
            public void keyReleased(final KeyEvent e) {
                String cadena = txtCodIniEm.getText();
                repaint();
                if (txtCodIniEm.getText().length() > 5) {
                    if (!llenarLabelEmpleado(cadena, 1) && txtCodIniEm.getText().length() > 0) {
                        txtCodIniEm.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                        btnBuscar.setEnabled(false);
                        lblNomEm1.setText("");
                    } else {
                        if (txtCodIniEm.getText().length() == 0) {
                            lblNomEm1.setText("");
                        }
                        btnBuscar.setEnabled(true);
                        txtCodIniEm.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                        codIniEm = txtCodIniEm.getText();
                    }
                } else if (txtCodIniEm.getText().length() > 0) {
                    btnBuscar.setEnabled(false);
                } else {
                    btnBuscar.setEnabled(true);
                }
                if (txtCodIniEm.getText().length() == 0) {
                    txtCodIniEm.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                }
            }
        });

    }//GEN-LAST:event_txtCodIniEmKeyTyped

    private void txtCodIniEmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodIniEmKeyPressed
        numeroEmpleado = 1;
        //capturamos la tecla presionada
        char tecla = evt.getKeyChar();
        if (evt.getKeyCode() == KeyEvent.VK_F3) {
            abrirModal();
        } else if (tecla == KeyEvent.VK_ENTER) {
            if (txtCodIniEm.getText().equalsIgnoreCase("")) {
                lblNomEm1.setText("");
            }
            if (!llenarLabelEmpleado(txtCodIniEm.getText(), 1) && txtCodIniEm.getText().length() > 0) {
                txtCodIniEm.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                lblNomEm1.setText("");
            } else {
                txtCodIniEm.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                codIniEm = txtCodIniEm.getText();
                txtCodFinEm.requestFocus();
            }
        }
    }//GEN-LAST:event_txtCodIniEmKeyPressed

    private void txtCodIniEmFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodIniEmFocusLost
        // TODO add your handling code here:
        if (!llenarLabelEmpleado(txtCodIniEm.getText(), 1)) {
            lblNomEm1.setText("");
            txtCodIniEm.setText("");
            txtCodIniEm.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        } else {
            codIniEm = txtCodIniEm.getText();
        }
        lblF3.setVisible(false);
    }//GEN-LAST:event_txtCodIniEmFocusLost

    private void txtCodIniEmFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodIniEmFocusGained
        // TODO add your handling code here:
        txtCodIniEm.selectAll();
        lblF3.setVisible(true);
    }//GEN-LAST:event_txtCodIniEmFocusGained

    private void txtCodFinEmKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodFinEmKeyTyped
        txtCodFinEm.addKeyListener(new KeyAdapter() {
            public void keyReleased(final KeyEvent e) {
                String cadena = txtCodFinEm.getText();
                repaint();
                if (txtCodFinEm.getText().length() > 5) {
                    if (!llenarLabelEmpleado(cadena, 2) && txtCodFinEm.getText().length() > 0) {
                        txtCodFinEm.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                        btnBuscar.setEnabled(false);
                        lblNomEm2.setText("");
                    } else {
                        if (txtCodFinEm.getText().length() == 0) {
                            lblNomEm2.setText("");
                        }
                        btnBuscar.setEnabled(true);
                        txtCodFinEm.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                    }
                } else if (txtCodFinEm.getText().length() > 0) {
                    btnBuscar.setEnabled(false);
                } else {
                    btnBuscar.setEnabled(true);
                }
                if (txtCodFinEm.getText().length() == 0) {
                    txtCodFinEm.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                }
            }
        });
    }//GEN-LAST:event_txtCodFinEmKeyTyped

    private void txtCodFinEmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodFinEmKeyPressed
        numeroEmpleado = 2;
        //capturamos la tecla presionada
        char tecla = evt.getKeyChar();
        if (evt.getKeyCode() == KeyEvent.VK_F3) {
            abrirModal();
        } else if (tecla == KeyEvent.VK_ENTER) {
            if (txtCodFinEm.getText().equalsIgnoreCase("")) {
                lblNomEm2.setText("");
            }
            if (!llenarLabelEmpleado(txtCodFinEm.getText(), 2) && txtCodFinEm.getText().length() > 0) {
                txtCodFinEm.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                lblNomEm2.setText("");
            } else {
                txtCodFinEm.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                codFinEm = txtCodFinEm.getText();
                btnBuscar.doClick();
            }

        }
    }//GEN-LAST:event_txtCodFinEmKeyPressed

    private void txtCodFinEmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodFinEmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodFinEmActionPerformed

    private void txtCodFinEmFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodFinEmFocusLost
        // TODO add your handling code here:
        if (!llenarLabelEmpleado(txtCodFinEm.getText(), 2)) {
            lblNomEm2.setText("");
            txtCodFinEm.setText("");
            txtCodFinEm.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        } else {
            codFinEm = txtCodFinEm.getText();
        }
        lblF3.setVisible(false);
    }//GEN-LAST:event_txtCodFinEmFocusLost

    private void txtCodFinEmFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodFinEmFocusGained
        // TODO add your handling code here:
        txtCodFinEm.selectAll();
        lblF3.setVisible(true);
    }//GEN-LAST:event_txtCodFinEmFocusGained

    private void radioButtonInteresSobreCesantiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonInteresSobreCesantiaActionPerformed
        tipoNovedad = 3;
        lblFecha.setVisible(false);
        calFecha.setVisible(false);
        radioButtonDiciembre.setVisible(false);
        radioButtonJunio.setVisible(false);
        lblAnio.setVisible(true);
        anioChooser.setVisible(true);
        //Inicializamos el objeto tabla de la vista
        inicializarTablasDeVista();
        mostrarResultados(false);
        if (tipoBusqueda == 1) {
            comboBoxCentroDeCosto.requestFocus();
        } else {
            txtCodIniEm.requestFocus();
        }
        anioGuardado = anioChooser.getYear();
        lblFechaDeNovedad.setText("");
    }//GEN-LAST:event_radioButtonInteresSobreCesantiaActionPerformed

    private void radioButtonPrimaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonPrimaActionPerformed
        tipoNovedad = 2;
        lblFecha.setVisible(false);
        calFecha.setVisible(false);
        lblAnio.setVisible(true);
        anioChooser.setVisible(true);
        radioButtonDiciembre.setVisible(true);
        radioButtonJunio.setVisible(true);
        //Inicializamos el objeto tabla de la vista
        inicializarTablasDeVista();
        mostrarResultados(false);
        if (tipoBusqueda == 1) {
            comboBoxCentroDeCosto.requestFocus();
        } else {
            txtCodIniEm.requestFocus();
        }
        anioGuardado = anioChooser.getYear();
        lblFechaDeNovedad.setText("");
    }//GEN-LAST:event_radioButtonPrimaActionPerformed

    private void radioButtonPagoQuincenaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonPagoQuincenaActionPerformed
        tipoNovedad = 1;
        lblFecha.setVisible(true);
        calFecha.setVisible(true);
        radioButtonDiciembre.setVisible(false);
        radioButtonJunio.setVisible(false);
        lblAnio.setVisible(false);
        anioChooser.setVisible(false);
        //Inicializamos el objeto tabla de la vista
        inicializarTablasDeVista();
        mostrarResultados(false);
        if (tipoBusqueda == 1) {
            comboBoxCentroDeCosto.requestFocus();
        } else {
            txtCodIniEm.requestFocus();
        }
        lblFechaDeNovedad.setText("");
    }//GEN-LAST:event_radioButtonPagoQuincenaActionPerformed

    private void tblNovedadesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblNovedadesKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_tblNovedadesKeyTyped

    private void tblNovedadesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNovedadesMouseClicked
        tblNovedades.requestFocus();
    }//GEN-LAST:event_tblNovedadesMouseClicked

    private void tblConsultaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblConsultaKeyTyped

    }//GEN-LAST:event_tblConsultaKeyTyped

    private void tblConsultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblConsultaMouseClicked
        tblConsulta.requestFocus();
    }//GEN-LAST:event_tblConsultaMouseClicked

    private void radioButtonJunioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_radioButtonJunioKeyTyped
        // TODO add your handling code here:
        //capturamos la tecla presionada
        char tecla = evt.getKeyChar();
        if (tecla == KeyEvent.VK_ENTER) {
            btnBuscar.doClick();
        }
    }//GEN-LAST:event_radioButtonJunioKeyTyped

    private void radioButtonDiciembreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_radioButtonDiciembreKeyTyped
        // TODO add your handling code here:
        //capturamos la tecla presionada
        char tecla = evt.getKeyChar();
        if (tecla == KeyEvent.VK_ENTER) {
            btnBuscar.doClick();
        }
    }//GEN-LAST:event_radioButtonDiciembreKeyTyped

    private void tblConsultaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblConsultaKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_tblConsultaKeyPressed

    private void tblConsultaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblConsultaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblConsultaFocusGained

    private void tblConsultaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblConsultaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblConsultaFocusLost

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JYearChooser anioChooser;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEnviarCorreo;
    private javax.swing.JButton btnSelectEmFin;
    private javax.swing.JButton btnSelectEmIni;
    private javax.swing.JButton btnVerMas;
    private javax.swing.JButton btnVerTodo;
    private javax.swing.ButtonGroup buttonGroupBusqueda;
    private javax.swing.ButtonGroup buttonGroupFiltro;
    private javax.swing.ButtonGroup buttonGroupMesPrima;
    private com.toedter.calendar.JDateChooser calFecha;
    private javax.swing.JComboBox<String> comboBoxCentroDeCosto;
    private javax.swing.JDialog dialogSelectEm;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JLabel lblF3;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFechaDeNovedad;
    private javax.swing.JLabel lblNomEm1;
    private javax.swing.JLabel lblNomEm2;
    private javax.swing.JLabel lblNumResultados;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelFooter;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JRadioButton radioButtonCentroDeCosto;
    private javax.swing.JRadioButton radioButtonDiciembre;
    private javax.swing.JRadioButton radioButtonInteresSobreCesantia;
    private javax.swing.JRadioButton radioButtonJunio;
    private javax.swing.JRadioButton radioButtonPagoQuincena;
    private javax.swing.JRadioButton radioButtonPorCodigo;
    private javax.swing.JRadioButton radioButtonPrima;
    private javax.swing.JScrollPane scrollPaneTablaConsulta;
    private javax.swing.JScrollPane scrollPaneTablaNovedades;
    private javax.swing.JTable tblConsulta;
    private javax.swing.JTable tblNovedades;
    public static javax.swing.JTextField txtCodFinEm;
    public static javax.swing.JTextField txtCodIniEm;
    // End of variables declaration//GEN-END:variables

    public Conexion getConexion() {
        return conexion;
    }

    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCodEmp() {
        return codEmp;
    }

    public void setCodEmp(String codEmp) {
        this.codEmp = codEmp;
    }

    public AcunoveDTO getAcunoveDTO() {
        return acunoveDTO;
    }

    public void setAcunoveDTO(AcunoveDTO acunoveDTO) {
        this.acunoveDTO = acunoveDTO;
    }

    public AcunoveDAO getAcunoveDAO() {
        return acunoveDAO;
    }

    public void setAcunoveDAO(AcunoveDAO acunoveDAO) {
        this.acunoveDAO = acunoveDAO;
    }

    public double getTotalAcunove() {
        return totalAcunove;
    }

    public void setTotalAcunove(double totalAcunove) {
        this.totalAcunove = totalAcunove;
    }

    public double getTotalDescuentos() {
        return totalDescuentos;
    }

    public void setTotalDescuentos(double totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }

    public double getTotalPagos() {
        return totalPagos;
    }

    public void setTotalPagos(double totalPagos) {
        this.totalPagos = totalPagos;
    }

    public ArrayList<AcunoveDTO> getListaAcunove() {
        return listaAcunove;
    }

    public void setListaAcunove(ArrayList<AcunoveDTO> listaAcunove) {
        this.listaAcunove = listaAcunove;
    }

    public EmpleadoDTO getEmpleadoDTO() {
        return empleadoDTO;
    }

    public void setEmpleadoDTO(EmpleadoDTO empleadoDTO) {
        this.empleadoDTO = empleadoDTO;
    }

    public String getFechaInicialDeQuincena() {
        return fechaInicialDeQuincena;
    }

    public void setFechaInicialDeQuincena(String fechaInicialDeQuincena) {
        this.fechaInicialDeQuincena = fechaInicialDeQuincena;
    }

    public String getFechaDePago() {
        return fechaDePago;
    }

    public void setFechaDePago(String fechaDePago) {
        this.fechaDePago = fechaDePago;
    }

    public ParametroDTO getParametroDTO() {
        return parametroDTO;
    }

    public void setParametroDTO(ParametroDTO parametroDTO) {
        this.parametroDTO = parametroDTO;
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }

    public void setDtm(DefaultTableModel dtm) {
        this.dtm = dtm;
    }

    public DefaultTableCellRenderer getDtcr() {
        return dtcr;
    }

    public void setDtcr(DefaultTableCellRenderer dtcr) {
        this.dtcr = dtcr;
    }

    public ArrayList<EmpleadoDTO> getListaEmpleados() {
        return listaEmpleados;
    }

    public void setListaEmpleados(ArrayList<EmpleadoDTO> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public String getCodIniEm() {
        return codIniEm;
    }

    public void setCodIniEm(String codIniEm) {
        this.codIniEm = codIniEm;
    }

    public String getCodFinEm() {
        return codFinEm;
    }

    public void setCodFinEm(String codFinEm) {
        this.codFinEm = codFinEm;
    }

    public ArrayList<EmpleadoDTO> getListaEmpleadosSelect() {
        return listaEmpleadosSelect;
    }

    public void setListaEmpleadosSelect(ArrayList<EmpleadoDTO> listaEmpleadosSelect) {
        this.listaEmpleadosSelect = listaEmpleadosSelect;

    }

    // Esta es la clase interna. Está definida DENTRO de MiVentana con el evento --------------------------------------------
    public class MiClase implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (calFecha.getDate() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(calFecha.getDate());
                System.out.println(cal.get(Calendar.YEAR));
                if (validarAnio(cal.get(Calendar.YEAR))) {
                    btnBuscar.doClick();
                } else {
                    editor.requestFocus();
                    btnVerMas.setEnabled(false);
                    btnVerTodo.setEnabled(false);
                }
            } else {
                inicializarTablasDeVista();
                editor.requestFocus();
                JOptionPane.showMessageDialog(rootPane, "La fecha no es válida", "Información", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

}

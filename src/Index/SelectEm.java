package Index;

import Conexion.Conexion;
import DAO.EmpleadoDAO;
import DAO.ParametroDAO;
import DTO.EmpleadoDTO;
import DTO.ParametroDTO;
import Recursos.Configuracion;
import Recursos.Utiles;
import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Fabian
 *
 * radio button
 * https://gl-epn-programacion-ii.blogspot.com/2010/10/uso-de-radiobuttons-en-java.html
 *
 * Conbo box con base de datos
 *
 * https://www.youtube.com/watch?v=4rXdm_AZYDU
 *
 * Agregar selección de la tabla
 * http://www.forosdelweb.com/f45/seleccionar-filas-jtable-895674/
 *
 * evento https://www.youtube.com/watch?v=hvnBKuFiNAg
 *
 * evento al cambiar la fila seleccionada en un jTable
 * https://docs.oracle.com/javase/tutorial/uiswing/components/table.html
 *
 *
 * filtrar columnas https://www.youtube.com/watch?v=UwmsO2mXylI
 *
 * http://www.forosdelweb.com/f45/filtrar-resultandos-jtable-sin-tomar-cuenta-las-mayusculas-minusculas-960603/
 *
 *
 * cerrar ventanas http://www.forosdelweb.com/f45/cerrar-jdialog-316924/
 *
 */
public class SelectEm extends javax.swing.JDialog {

    //Tabla de vista
    private DefaultTableModel dtm;
    private DefaultTableCellRenderer dtcr;
    TableColumnModel columnModel;
    String titulos[] = {"Codigo", "Nombre", "Centro de Costo", "Correo"};
    String contenidoBlanco[][] = null;
    String matriz[][];
    TableRowSorter<DefaultTableModel> elQueOrdena;

    EnviarMail index = new EnviarMail(true) {
    };
    EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    EmpleadoDTO empleadoDTOInicial = new EmpleadoDTO();
    EmpleadoDTO empleadoDTOFinal = new EmpleadoDTO();
    ArrayList<EmpleadoDTO> listaEmpleados;
    Conexion conexion;
    ParametroDTO parametroDTO = new ParametroDTO();
    ParametroDAO parametroDAO = new ParametroDAO();
    ArrayList<ParametroDTO> listaCentrosDeCosto;
    String codEmpleadoGuardado = "";
    int contador;

    //Selección de la tabla
    int filaSeleccionada;

    public SelectEm(java.awt.Frame parent, boolean modal) throws ClassNotFoundException, SQLException {

        super(parent, modal);
        initComponents();
        codEmpleadoGuardado = (EnviarMail.numeroEmpleado == 1) ? EnviarMail.codIniEm : EnviarMail.codFinEm;

        //inabilitar el boton maximizar
        this.setResizable(false);

        btnFiltrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerMas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerTodo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSeleccionarEmpleado.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //Inhabilitar la x de la ventana para cerrar exclusivamente desde el boton
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.conexion = new Conexion();
        listaEmpleados = new ArrayList<>();
        listaCentrosDeCosto = new ArrayList<>();

        //tabla de vista
        tblEmpleados.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        dtm = new DefaultTableModel(contenidoBlanco, titulos) {
            //Cancelamos la edición de la tabla en la vista
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblEmpleados.setModel(dtm);

        // Instanciamos el TableRowSorter y lo añadimos al JTable para que me ordene por columna
        elQueOrdena = new TableRowSorter<DefaultTableModel>(dtm);
        tblEmpleados.setRowSorter(elQueOrdena);

        columnModel = tblEmpleados.getColumnModel();
        aplicarDisenioTablaVista();

        radioButonNombre.setSelected(true);
        comboBoxCentroDeCosto.setEnabled(false);
        txtNomEmpleado.setEnabled(true);
        txtNomEmpleado.requestFocus();
        mostrarCentrosDeCostoEnComboBox();
        contador = 0;
        btnVerMas.setEnabled(false);
        btnVerTodo.setEnabled(false);
        btnSeleccionarEmpleado.setEnabled(false);
        txtFiltroCod.setEnabled(false);
        txtFiltroNombre.setEnabled(false);
        panelHeader.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Parametros de Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft Sans Serif", 1, 11), new java.awt.Color(58, 58, 58)));
        panelBody.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtro Dinámico", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft Sans Serif", 1, 11), new java.awt.Color(58, 58, 58)));

        //evento al cambiar  la fila seleccionada en un jTable
        ListSelectionModel rowSM = tblEmpleados.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                //Ignore extra messages.
                if (e.getValueIsAdjusting()) {
                    return;
                }

                ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.isSelectionEmpty()) {
                    //no rows are selected
                    btnSeleccionarEmpleado.setEnabled(false);
                } else {
                    //selectedRow is selected
                    int selectedRow = lsm.getMinSelectionIndex();
                    btnSeleccionarEmpleado.setEnabled(true);
                }
            }
        });

        //Validar cada campo de texto
        txtFiltroCod.addKeyListener(new KeyListener() {

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
                if (txtFiltroCod.getText().length() == Configuracion.numeroDeCaractaresNumericosEnElCodigo) {
                    e.consume();
                }
            }

            public void keyPressed(KeyEvent arg0) {
            }

            public void keyReleased(KeyEvent arg0) {
            }
        });
        setTitle("Seleccione un empleado");
    }

    private void aplicarDisenioTablaVista() {
        columnModel = tblEmpleados.getColumnModel();

        //Ajustamos el ancho de las columnas en pixeles
        columnModel.getColumn(0).setPreferredWidth(110);
        columnModel.getColumn(1).setPreferredWidth(300);
        columnModel.getColumn(2).setPreferredWidth(300);
        columnModel.getColumn(3).setPreferredWidth(300);

        //permitir selección de filas y prohibir selección de columnas
        tblEmpleados.setRowSelectionAllowed(true);
        tblEmpleados.setColumnSelectionAllowed(false);
    }

    public EmpleadoDTO buscarEmpleadoEnLaLista(String cod) {
        EmpleadoDTO em = new EmpleadoDTO();
        try {
            for (EmpleadoDTO item : listaEmpleados) {
                String codComp = item.getCod_empleado();
                if (cod.equals(codComp)) {
                    em = item;
                    break;
                }
            }
        } catch (Exception e) {
        }
        return em;
    }

    public void limpiarTablaDeConsulta() {
        listaEmpleados.clear();
        //tabla de vista
        tblEmpleados.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        dtm = new DefaultTableModel(contenidoBlanco, titulos) {
            //Cancelamos la edición de la tabla en la vista
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblEmpleados.setModel(dtm);

        // Instanciamos el TableRowSorter y lo añadimos al JTable para que me ordene por columna
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(dtm);
        tblEmpleados.setRowSorter(elQueOrdena);

        columnModel = tblEmpleados.getColumnModel();
        aplicarDisenioTablaVista();
    }

    private void mostrarCentrosDeCostoEnComboBox() throws ClassNotFoundException, SQLException {
        listaCentrosDeCosto = parametroDAO.buscarTodosLosParametrosCentroDeCosto();
        this.comboBoxCentroDeCosto.removeAllItems();
        this.comboBoxCentroDeCosto.addItem("");
        for (ParametroDTO item : listaCentrosDeCosto) {
            this.comboBoxCentroDeCosto.addItem(Utiles.quitarLetraEneACodigoCentroDeCosto(item.getCodigo()) + " - " + item.getNombre());
        }
    }

    public void mostrarListaEmpleados() throws ClassNotFoundException, SQLException {
        //Inicializamos la matriz de la lista con 4 columnas y el # de elementos de la lista
        matriz = new String[listaEmpleados.size()][4];

        //recorremos la lista y llenamos la matriz
        for (int i = 0; i < listaEmpleados.size(); i++) {
            ParametroDTO p = new ParametroDTO();
            matriz[i][0] = listaEmpleados.get(i).getCod_empleado();
            matriz[i][1] = listaEmpleados.get(i).getNom_em();
            p = parametroDAO.buscarParametroCentroDeCosto(listaEmpleados.get(i).getCentro_de_costo());
            matriz[i][2] = p.getNombre();
            matriz[i][3] = listaEmpleados.get(i).getCorreo();

        }

        //tabla de vista
        tblEmpleados.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        dtm = new DefaultTableModel(matriz, titulos) {
            //Cancelamos la edición de la tabla en la vista
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblEmpleados.setModel(dtm);

        // Instanciamos el TableRowSorter y lo añadimos al JTable
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(dtm);
        tblEmpleados.setRowSorter(elQueOrdena);
        aplicarDisenioTablaVista();
    }

    private void selectEm() {
        filaSeleccionada = tblEmpleados.getSelectedRow();
        String codEm = tblEmpleados.getValueAt(filaSeleccionada, 0).toString();
        empleadoDTOInicial = buscarEmpleadoEnLaLista(codEm);
        String codIni = empleadoDTOInicial.getCod_empleado();

        if (EnviarMail.numeroEmpleado == 1) {
            EnviarMail.codIniEm = codIni;
            EnviarMail.txtCodIniEm.requestFocus();
        } else {
            EnviarMail.codFinEm = codIni;
            EnviarMail.txtCodFinEm.requestFocus();
        }

        this.dispose();
        this.setVisible(false);
        index.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupCentroDeCosto = new javax.swing.ButtonGroup();
        panelSelectEm = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        radioButonNombre = new javax.swing.JRadioButton();
        radioButonCentroDeCosto = new javax.swing.JRadioButton();
        txtNomEmpleado = new javax.swing.JTextField();
        comboBoxCentroDeCosto = new javax.swing.JComboBox<>();
        btnFiltrar = new javax.swing.JButton();
        panelBody = new javax.swing.JPanel();
        scrollPaneTblEmpleados = new javax.swing.JScrollPane();
        tblEmpleados = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroCod = new javax.swing.JTextField();
        txtFiltroNombre = new javax.swing.JTextField();
        panelFooter = new javax.swing.JPanel();
        btnVerMas = new javax.swing.JButton();
        btnVerTodo = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSeleccionarEmpleado = new javax.swing.JButton();
        lblNumeroDeResultados = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        groupCentroDeCosto.add(radioButonNombre);
        radioButonNombre.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        radioButonNombre.setText("Nombre");
        radioButonNombre.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        radioButonNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButonNombreActionPerformed(evt);
            }
        });

        groupCentroDeCosto.add(radioButonCentroDeCosto);
        radioButonCentroDeCosto.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        radioButonCentroDeCosto.setText("Centro de Costo");
        radioButonCentroDeCosto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        radioButonCentroDeCosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButonCentroDeCostoActionPerformed(evt);
            }
        });

        txtNomEmpleado.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N
        txtNomEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomEmpleadoActionPerformed(evt);
            }
        });
        txtNomEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNomEmpleadoKeyTyped(evt);
            }
        });

        comboBoxCentroDeCosto.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N
        comboBoxCentroDeCosto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
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

        btnFiltrar.setText("Filtrar");
        btnFiltrar.setPreferredSize(new java.awt.Dimension(70, 30));
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });
        btnFiltrar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                btnFiltrarKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioButonNombre)
                .addGap(45, 45, 45)
                .addComponent(txtNomEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioButonCentroDeCosto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBoxCentroDeCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panelHeaderLayout.createSequentialGroup()
                        .addComponent(txtNomEmpleado)
                        .addGap(4, 4, 4))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(radioButonCentroDeCosto)
                            .addComponent(comboBoxCentroDeCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(radioButonNombre)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        scrollPaneTblEmpleados.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneTblEmpleados.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblEmpleados.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        tblEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Centro de Costo", "Correo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmpleados.setRowHeight(20);
        tblEmpleados.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblEmpleados.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblEmpleadosFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblEmpleadosFocusLost(evt);
            }
        });
        tblEmpleados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmpleadosMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblEmpleadosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tblEmpleadosMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblEmpleadosMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblEmpleadosMouseReleased(evt);
            }
        });
        tblEmpleados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblEmpleadosKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblEmpleadosKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tblEmpleadosKeyTyped(evt);
            }
        });
        scrollPaneTblEmpleados.setViewportView(tblEmpleados);

        jLabel1.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        jLabel1.setText("Codigo");

        jLabel2.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9)); // NOI18N
        jLabel2.setText("Nombre");

        txtFiltroCod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtFiltroCodFocusGained(evt);
            }
        });
        txtFiltroCod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtFiltroCodMouseClicked(evt);
            }
        });
        txtFiltroCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFiltroCodKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFiltroCodKeyTyped(evt);
            }
        });

        txtFiltroNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtFiltroNombreFocusGained(evt);
            }
        });
        txtFiltroNombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtFiltroNombreMouseClicked(evt);
            }
        });
        txtFiltroNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroNombreActionPerformed(evt);
            }
        });
        txtFiltroNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFiltroNombreKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFiltroNombreKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneTblEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFiltroCod, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFiltroNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFiltroNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFiltroCod, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPaneTblEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnVerMas.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
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

        btnVerTodo.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
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

        btnCancelar.setText("Cancelar");
        btnCancelar.setPreferredSize(new java.awt.Dimension(70, 40));
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnSeleccionarEmpleado.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnSeleccionarEmpleado.setText("Seleccionar");
        btnSeleccionarEmpleado.setPreferredSize(new java.awt.Dimension(70, 30));
        btnSeleccionarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarEmpleadoActionPerformed(evt);
            }
        });
        btnSeleccionarEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                btnSeleccionarEmpleadoKeyTyped(evt);
            }
        });

        lblNumeroDeResultados.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N

        javax.swing.GroupLayout panelFooterLayout = new javax.swing.GroupLayout(panelFooter);
        panelFooter.setLayout(panelFooterLayout);
        panelFooterLayout.setHorizontalGroup(
            panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFooterLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(lblNumeroDeResultados, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnVerMas, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnVerTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSeleccionarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelFooterLayout.setVerticalGroup(
            panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFooterLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblNumeroDeResultados, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFooterLayout.createSequentialGroup()
                .addGroup(panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnVerMas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnVerTodo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSeleccionarEmpleado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panelSelectEmLayout = new javax.swing.GroupLayout(panelSelectEm);
        panelSelectEm.setLayout(panelSelectEmLayout);
        panelSelectEmLayout.setHorizontalGroup(
            panelSelectEmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSelectEmLayout.createSequentialGroup()
                .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panelFooter, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelSelectEmLayout.createSequentialGroup()
                .addComponent(panelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelSelectEmLayout.setVerticalGroup(
            panelSelectEmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSelectEmLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelFooter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelSelectEm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelSelectEm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        //cursor para buscar
        panelSelectEm.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        this.dispose();
        if (EnviarMail.numeroEmpleado == 1) {
            EnviarMail.txtCodIniEm.requestFocus();
            EnviarMail.codIniEm = codEmpleadoGuardado;
        } else {
            EnviarMail.txtCodFinEm.requestFocus();
            EnviarMail.codFinEm = codEmpleadoGuardado;
        }
        //Dejamos el cursor por defecto
        panelSelectEm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        index.setVisible(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed

        try {
            //cursor para buscar
            panelSelectEm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            contador = 50;
            btnVerMas.setEnabled(true);
            btnVerTodo.setEnabled(true);
            listaEmpleados.clear();
            if (radioButonCentroDeCosto.isSelected()) {
                if (comboBoxCentroDeCosto.getSelectedIndex() != 0) {
                    listaEmpleados = empleadoDAO.buscarPorCentroDeCosto(Utiles.verCodigoCentroDeCosto(comboBoxCentroDeCosto.getSelectedItem().toString()), contador);
                } else {
                    listaEmpleados = empleadoDAO.buscarTodos(contador);
                }
            } else {
                if (txtNomEmpleado.getText().equals("")) {
                    listaEmpleados = empleadoDAO.buscarTodos(contador);
                } else {
                    listaEmpleados = empleadoDAO.buscarPorNombre(txtNomEmpleado.getText(), contador);
                }
            }
            mostrarListaEmpleados();
            if (listaEmpleados.size() < 50) {
                btnVerMas.setEnabled(false);
                btnVerTodo.setEnabled(false);
            }
            tblEmpleados.requestFocus();
            txtFiltroCod.setText("");
            txtFiltroNombre.setText("");
            if (listaEmpleados.size() > 0) {
                txtFiltroCod.setEnabled(true);
                txtFiltroNombre.setEnabled(true);
            }
            lblNumeroDeResultados.setText(listaEmpleados.size() + " resultados...");
            //Seleccionamos la primera fila
            if (listaEmpleados.size() > 0) {
                tblEmpleados.requestFocus();
                tblEmpleados.setRowSelectionInterval(0, 0);
            }
            //Dejamos el cursor por defecto
            panelSelectEm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SelectEm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SelectEm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void radioButonNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButonNombreActionPerformed
        if (radioButonNombre.isSelected()) {
            limpiarTablaDeConsulta();
            comboBoxCentroDeCosto.setEnabled(false);
            txtNomEmpleado.setEnabled(true);
            comboBoxCentroDeCosto.setSelectedIndex(0);
            txtNomEmpleado.requestFocus();
            lblNumeroDeResultados.setText("");
            btnVerMas.setEnabled(false);
            btnVerTodo.setEnabled(false);
            btnSeleccionarEmpleado.setEnabled(false);
            txtFiltroCod.setEnabled(false);
            txtFiltroNombre.setEnabled(false);
            txtFiltroCod.setText("");
            txtFiltroNombre.setText("");
        }
    }//GEN-LAST:event_radioButonNombreActionPerformed

    private void radioButonCentroDeCostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButonCentroDeCostoActionPerformed
        if (radioButonCentroDeCosto.isSelected()) {
            limpiarTablaDeConsulta();
            txtNomEmpleado.setEnabled(false);
            comboBoxCentroDeCosto.setEnabled(true);
            txtNomEmpleado.setText("");
            comboBoxCentroDeCosto.requestFocus();
            lblNumeroDeResultados.setText("");
            btnVerMas.setEnabled(false);
            btnVerTodo.setEnabled(false);
            btnSeleccionarEmpleado.setEnabled(false);
            txtFiltroCod.setEnabled(false);
            txtFiltroNombre.setEnabled(false);
            txtFiltroCod.setText("");
            txtFiltroNombre.setText("");
        }
    }//GEN-LAST:event_radioButonCentroDeCostoActionPerformed

    private void tblEmpleadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpleadosMouseClicked
        btnSeleccionarEmpleado.setEnabled(true);
        if (evt.getClickCount() == 2) {
            selectEm();
        }
    }//GEN-LAST:event_tblEmpleadosMouseClicked

    private void tblEmpleadosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpleadosMouseEntered

    }//GEN-LAST:event_tblEmpleadosMouseEntered

    private void tblEmpleadosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpleadosMousePressed

    }//GEN-LAST:event_tblEmpleadosMousePressed

    private void tblEmpleadosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpleadosMouseReleased

    }//GEN-LAST:event_tblEmpleadosMouseReleased

    private void tblEmpleadosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpleadosMouseExited

    }//GEN-LAST:event_tblEmpleadosMouseExited

    private void tblEmpleadosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblEmpleadosKeyPressed
        //capturamos la tecla presionada
        char tecla = evt.getKeyChar();
        if (tecla == KeyEvent.VK_ENTER) {
            selectEm();
        }
    }//GEN-LAST:event_tblEmpleadosKeyPressed

    private void tblEmpleadosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblEmpleadosKeyTyped

    }//GEN-LAST:event_tblEmpleadosKeyTyped

    private void txtNomEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomEmpleadoKeyTyped
        //capturamos la tecla presionada
        char tecla = evt.getKeyChar();
        if (tecla == KeyEvent.VK_ENTER) {
            btnFiltrar.doClick();
            if (listaEmpleados.isEmpty()) {
                txtNomEmpleado.requestFocus();
            } else {
                tblEmpleados.requestFocus();
            }
        }
    }//GEN-LAST:event_txtNomEmpleadoKeyTyped

    private void btnFiltrarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnFiltrarKeyTyped
        //capturamos la tecla presionada
        char tecla = evt.getKeyChar();
        if (tecla == KeyEvent.VK_ENTER) {
            btnFiltrar.doClick();
            tblEmpleados.requestFocus();
        }
    }//GEN-LAST:event_btnFiltrarKeyTyped

    private void txtNomEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomEmpleadoActionPerformed

    private void comboBoxCentroDeCostoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxCentroDeCostoKeyTyped
        //capturamos la tecla presionada
        char tecla = evt.getKeyChar();
        if (tecla == KeyEvent.VK_ENTER) {
            btnFiltrar.doClick();
            if (listaEmpleados.isEmpty()) {
                comboBoxCentroDeCosto.requestFocus();
            } else {
                tblEmpleados.requestFocus();
            }
        }
    }//GEN-LAST:event_comboBoxCentroDeCostoKeyTyped

    private void btnVerMasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerMasActionPerformed
        try {
            //cursor para buscar
            panelSelectEm.setCursor(new Cursor(Cursor.WAIT_CURSOR));

            contador = contador + 50;
            listaEmpleados.clear();
            if (radioButonCentroDeCosto.isSelected()) {
                if (comboBoxCentroDeCosto.getSelectedIndex() == 0) {
                    listaEmpleados = empleadoDAO.buscarTodos(contador);
                } else {
                    listaEmpleados = empleadoDAO.buscarPorCentroDeCosto(Utiles.verCodigoCentroDeCosto(comboBoxCentroDeCosto.getSelectedItem().toString()), contador);
                }
            } else if (radioButonNombre.isSelected()) {
                listaEmpleados = empleadoDAO.buscarPorNombre(txtNomEmpleado.getText(), contador);
            } else {
                listaEmpleados = empleadoDAO.buscarTodos(contador);
            }
            mostrarListaEmpleados();
            if (listaEmpleados.size() < contador) {
                btnVerMas.setEnabled(false);
                btnVerTodo.setEnabled(false);
            }
            txtFiltroCod.setText("");
            txtFiltroNombre.setText("");
            tblEmpleados.requestFocus();
            lblNumeroDeResultados.setText(listaEmpleados.size() + " resultados...");
            //Dejamos el cursor por defecto
            panelSelectEm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SelectEm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SelectEm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnVerMasActionPerformed

    private void btnVerMasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnVerMasKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVerMasKeyTyped

    private void btnVerTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerTodoActionPerformed
        try {
            //cursor para buscar
            panelSelectEm.setCursor(new Cursor(Cursor.WAIT_CURSOR));

            btnVerMas.setEnabled(false);
            btnVerTodo.setEnabled(false);
            if (radioButonCentroDeCosto.isSelected() && comboBoxCentroDeCosto.getSelectedIndex() != 0) {
                listaEmpleados = empleadoDAO.buscarPorCentroDeCosto(Utiles.verCodigoCentroDeCosto(comboBoxCentroDeCosto.getSelectedItem().toString()));
            } else if (txtNomEmpleado.getText().equals("")) {
                listaEmpleados = empleadoDAO.buscarTodos();
            } else if (radioButonNombre.isSelected()) {
                listaEmpleados = empleadoDAO.buscarPorNombre(txtNomEmpleado.getText());
            } else {
                listaEmpleados = empleadoDAO.buscarTodos();
            }
            mostrarListaEmpleados();
            txtFiltroCod.setText("");
            txtFiltroNombre.setText("");
            tblEmpleados.requestFocus();
            lblNumeroDeResultados.setText(listaEmpleados.size() + " resultados...");
            //Dejamos el cursor por defecto
            panelSelectEm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SelectEm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SelectEm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnVerTodoActionPerformed

    private void btnVerTodoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnVerTodoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVerTodoKeyTyped

    private void tblEmpleadosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblEmpleadosKeyReleased

    }//GEN-LAST:event_tblEmpleadosKeyReleased

    private void tblEmpleadosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblEmpleadosFocusGained
    }//GEN-LAST:event_tblEmpleadosFocusGained

    private void tblEmpleadosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblEmpleadosFocusLost

    }//GEN-LAST:event_tblEmpleadosFocusLost

    private void btnSeleccionarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarEmpleadoActionPerformed
        //cursor para buscar
        panelSelectEm.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        selectEm();

        //Dejamos el cursor por defecto
        panelSelectEm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btnSeleccionarEmpleadoActionPerformed

    private void btnSeleccionarEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSeleccionarEmpleadoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSeleccionarEmpleadoKeyTyped

    private void txtFiltroCodKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroCodKeyTyped
        // TODO add your handling code here:
        txtFiltroCod.addKeyListener(new KeyAdapter() {
            public void keyReleased(final KeyEvent e) {
                txtFiltroNombre.setText("");
//                String cadena = (txtFiltroNombre.getText()).toUpperCase(); //Convierte en mayuscula
//                String cadena = (txtFiltroNombre.getText()).toLowerCase(); //Convierte en miniscula
                String cadena = txtFiltroCod.getText();
                txtFiltroCod.setText(cadena);
                repaint();
                elQueOrdena.setRowFilter(RowFilter.regexFilter(txtFiltroCod.getText(), 0));

            }
        });

        elQueOrdena = new TableRowSorter(this.tblEmpleados.getModel());
        this.tblEmpleados.setRowSorter(elQueOrdena);
    }//GEN-LAST:event_txtFiltroCodKeyTyped

    private void txtFiltroNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroNombreKeyTyped
        // TODO add your handling code here:
        txtFiltroNombre.addKeyListener(new KeyAdapter() {
            public void keyReleased(final KeyEvent e) {
                txtFiltroCod.setText("");
//                String cadena = (txtFiltroNombre.getText()).toUpperCase(); //Lo convierte en mayuscula
                String cadena = txtFiltroNombre.getText(); //Lo convierte en mayuscula
                txtFiltroNombre.setText(cadena);
                repaint();
                //comodin "(?i)" para que filtre ignorando las mayusculas
                elQueOrdena.setRowFilter(RowFilter.regexFilter("(?i)" + txtFiltroNombre.getText(), 1));
            }
        });

        elQueOrdena = new TableRowSorter(this.tblEmpleados.getModel());
        this.tblEmpleados.setRowSorter(elQueOrdena);
    }//GEN-LAST:event_txtFiltroNombreKeyTyped

    private void txtFiltroCodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFiltroCodMouseClicked
        // TODO add your handling code here:
        txtFiltroNombre.setText("");
        elQueOrdena = new TableRowSorter(dtm);
        this.tblEmpleados.setRowSorter(elQueOrdena);
        this.tblEmpleados.setModel(dtm);
    }//GEN-LAST:event_txtFiltroCodMouseClicked

    private void txtFiltroNombreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFiltroNombreMouseClicked
        // TODO add your handling code here:
        txtFiltroCod.setText("");
        elQueOrdena = new TableRowSorter(dtm);
        this.tblEmpleados.setRowSorter(elQueOrdena);
        this.tblEmpleados.setModel(dtm);
    }//GEN-LAST:event_txtFiltroNombreMouseClicked

    private void txtFiltroCodKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroCodKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtFiltroCodKeyPressed

    private void txtFiltroNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroNombreKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtFiltroNombreKeyPressed

    private void txtFiltroCodFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroCodFocusGained
        // TODO add your handling code here:
        txtFiltroNombre.setText("");
        elQueOrdena = new TableRowSorter(dtm);
        this.tblEmpleados.setRowSorter(elQueOrdena);
        this.tblEmpleados.setModel(dtm);
    }//GEN-LAST:event_txtFiltroCodFocusGained

    private void txtFiltroNombreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroNombreFocusGained
        // TODO add your handling code here:
        txtFiltroCod.setText("");
        elQueOrdena = new TableRowSorter(dtm);
        this.tblEmpleados.setRowSorter(elQueOrdena);
        this.tblEmpleados.setModel(dtm);
    }//GEN-LAST:event_txtFiltroNombreFocusGained

    private void txtFiltroNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroNombreActionPerformed

    private void comboBoxCentroDeCostoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxCentroDeCostoItemStateChanged
        // TODO add your handling code here:
        limpiarTablaDeConsulta();
    }//GEN-LAST:event_comboBoxCentroDeCostoItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnSeleccionarEmpleado;
    private javax.swing.JButton btnVerMas;
    private javax.swing.JButton btnVerTodo;
    private javax.swing.JComboBox<String> comboBoxCentroDeCosto;
    private javax.swing.ButtonGroup groupCentroDeCosto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblNumeroDeResultados;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelFooter;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelSelectEm;
    private javax.swing.JRadioButton radioButonCentroDeCosto;
    private javax.swing.JRadioButton radioButonNombre;
    private javax.swing.JScrollPane scrollPaneTblEmpleados;
    private javax.swing.JTable tblEmpleados;
    private javax.swing.JTextField txtFiltroCod;
    private javax.swing.JTextField txtFiltroNombre;
    private javax.swing.JTextField txtNomEmpleado;
    // End of variables declaration//GEN-END:variables

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Recursos;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Fabian
 * Clase que me permite controlar el alimeamiento de cada uno de los encabezados de mi jtable
 */
public class HeaderRenderer implements TableCellRenderer {

    DefaultTableCellRenderer renderer;
    int horAlignment;

    public HeaderRenderer(JTable table, int horizontalAlignment) {
        horAlignment = horizontalAlignment;
        renderer = (DefaultTableCellRenderer) table.getTableHeader()
                .getDefaultRenderer();
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {
        Component c = renderer.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, col);
        JLabel label = (JLabel) c;
        label.setHorizontalAlignment(horAlignment);
        return label;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

consultas sql
https://support.office.com/es-es/article/predicados-all-distinct-distinctrow-o-top-24f2a47d-a803-4c7c-8e81-756fe298ce57
https://docs.microsoft.com/es-es/sql/t-sql/language-elements/not-equal-to-transact-sql-traditional?view=sql-server-2017
 */
package DAO;

import Conexion.*;
import DTO.AcunoveDTO;
import Recursos.Utiles;
import static Recursos.Utiles.redondearDecimales;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fabian
 */
public class AcunoveDAO {

    Conexion conexion;

    public AcunoveDAO() {
    }

    private ArrayList<AcunoveDTO> llenarLista() {
        ArrayList<AcunoveDTO> lista = new ArrayList<>();
        try {
            // processing returned data and printing into console
            while (Conexion.resultSet.next()) {
                AcunoveDTO acunoveDTO = new AcunoveDTO();
                acunoveDTO.setCod_nov(Conexion.resultSet.getString(1));
                acunoveDTO.setNom_nov(Conexion.resultSet.getString(2));
                acunoveDTO.setCant_nov(Conexion.resultSet.getString(3));
                double valU = Conexion.resultSet.getDouble(4);
                if (Utiles.validarNumeroConXDecimales("" + valU, 2)) {
                    acunoveDTO.setValor_unit(valU);
                } else {
                    acunoveDTO.setValor_unit(redondearDecimales(valU, 2));
                }
                double valT = Conexion.resultSet.getDouble(5);
                if (Utiles.validarNumeroConXDecimales("" + valT, 2)) {
                    acunoveDTO.setValor_tot(valT);
                } else {
                    acunoveDTO.setValor_tot(redondearDecimales(valT, 2));
                }
                acunoveDTO.setFec_mov(Conexion.resultSet.getDate(6));
                acunoveDTO.setCc_em(Conexion.resultSet.getString(7));
                acunoveDTO.setNom_em(Conexion.resultSet.getString(8));

                lista.add(acunoveDTO);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AcunoveDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public ArrayList<AcunoveDTO> listarPagosQuincenales(String dd, String mm, String yyyy, String codEmpleado) throws ClassNotFoundException, SQLException, ParseException {

        ArrayList<AcunoveDTO> lista = new ArrayList<>();
        try {
            conexion = new Conexion();
            // Ejecutamos la consulta y la guardamos en la variable
            String sql = "SELECT acunove.TIN_NOV, acunove.NOM_NOV, acunove.CAN_NOV, acunove.VUN_NOV, acunove.VTO_NOV, acunove.FEC_NOV, acunove.COD_NOV AS ['cod_em'], EMPLEADOS.NOM_MAE\n"
                    + "FROM acunove INNER JOIN EMPLEADOS ON acunove.COD_NOV = EMPLEADOS.COD_MAE\n"
                    + "WHERE (((acunove.TIN_NOV)<>'A014') AND ((acunove.TIN_NOV)<>'A012')  AND ((acunove.FEC_NOV)=?) AND ((EMPLEADOS.COD_MAE)=?))\n"
                    + "ORDER BY acunove.TIN_NOV;";

            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);

            Date fv = new Date(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd));

            Conexion.prepStament.setDate(1, fv);
            Conexion.prepStament.setString(2, codEmpleado);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
            System.out.println("La lista tiene " + lista.size() + " elementos.............................");
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error en la consulta sql");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;
    }

    public ArrayList<AcunoveDTO> listarPagoPrima(String dd, String mm, String yyyy, String codEmpleado) throws ClassNotFoundException, SQLException, ParseException {

        ArrayList<AcunoveDTO> lista = new ArrayList<>();
        try {
            conexion = new Conexion();
            // Ejecutamos la consulta y la guardamos en la variable
            String sql = "SELECT acunove.TIN_NOV, acunove.NOM_NOV, acunove.CAN_NOV, acunove.VUN_NOV, acunove.VTO_NOV, acunove.FEC_NOV, acunove.COD_NOV AS ['cod_em'], EMPLEADOS.NOM_MAE\n"
                    + "FROM acunove INNER JOIN EMPLEADOS ON acunove.COD_NOV = EMPLEADOS.COD_MAE\n"
                    + "WHERE (((acunove.TIN_NOV)='A012') AND ((acunove.FEC_NOV)=?) AND ((EMPLEADOS.COD_MAE)=?))"
                    + "ORDER BY acunove.TIN_NOV;";

            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);

            Date fv = new Date(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd));

            Conexion.prepStament.setDate(1, fv);
            Conexion.prepStament.setString(2, codEmpleado);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
            System.out.println("La lista tiene " + lista.size() + " elementos.............................");
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error en la consulta sql");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;
    }

    public ArrayList<AcunoveDTO> listarPagoInteresesSobreCesantias(String dd, String mm, String yyyy, String codEmpleado) throws ClassNotFoundException, SQLException, ParseException {

        ArrayList<AcunoveDTO> lista = new ArrayList<>();
        try {
            conexion = new Conexion();
            // Ejecutamos la consulta y la guardamos en la variable
            String sql = "SELECT acunove.TIN_NOV, acunove.NOM_NOV, acunove.CAN_NOV, acunove.VUN_NOV, acunove.VTO_NOV, acunove.FEC_NOV, acunove.COD_NOV AS ['cod_em'], EMPLEADOS.NOM_MAE\n"
                    + "FROM acunove INNER JOIN EMPLEADOS ON acunove.COD_NOV = EMPLEADOS.COD_MAE\n"
                    + "WHERE (((acunove.TIN_NOV)='A014') AND ((acunove.FEC_NOV)=?) AND ((EMPLEADOS.COD_MAE)=?))"
                    + "ORDER BY acunove.TIN_NOV;";

            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);

            Date fv = new Date(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd));

            Conexion.prepStament.setDate(1, fv);
            Conexion.prepStament.setString(2, codEmpleado);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
            System.out.println("La lista tiene " + lista.size() + " elementos.............................");
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error en la consulta sql");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;
    }

}

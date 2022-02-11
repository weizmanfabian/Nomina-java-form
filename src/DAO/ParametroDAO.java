/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Conexion.Conexion;
import DTO.ParametroDTO;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Fabian
 */
public class ParametroDAO {

    Conexion conexion;

    public ParametroDAO() throws ClassNotFoundException, SQLException {
    }

    public ParametroDTO buscarParametroCentroDeCosto(String cod) throws ClassNotFoundException, SQLException {
        conexion = new Conexion();

        ParametroDTO para = new ParametroDTO();
        boolean res = false;
        try {
            String sql = "SELECT parametros.LLA_PAR, parametros.NOM_PAR\n"
                    + "FROM parametros\n"
                    + "WHERE (((parametros.LLA_PAR)=?));";
            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Conexion.prepStament.setString(1, "N" + cod);
            Conexion.resultSet = Conexion.prepStament.executeQuery();

            while (Conexion.resultSet.next()) {
                para = new ParametroDTO();
                para.setCodigo(Conexion.resultSet.getString(1));
                para.setNombre(Conexion.resultSet.getString(2));

            }
            System.out.println("Busco el parametro por el codigo");
            conexion.cerrarObjetosDeConsulta();
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar el parametro o no lo encontro");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return para;
    }

    public ArrayList<ParametroDTO> buscarTodosLosParametrosCentroDeCosto() throws ClassNotFoundException, SQLException {
        ArrayList<ParametroDTO> lista = new ArrayList<>();

        ParametroDTO para = new ParametroDTO();

        String concepBusq = "N%";

        try {
            conexion = new Conexion();
            String sql = "SELECT parametros.LLA_PAR, parametros.NOM_PAR\n"
                    + "FROM parametros\n"
                    + "WHERE (((parametros.LLA_PAR) Like ?))\n"
                    + "ORDER BY parametros.LLA_PAR;";
            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Conexion.prepStament.setString(1, concepBusq);
            Conexion.resultSet = Conexion.prepStament.executeQuery();

            while (Conexion.resultSet.next()) {
                para = new ParametroDTO();
                para.setCodigo(Conexion.resultSet.getString(1));
                para.setNombre(Conexion.resultSet.getString(2));

                lista.add(para);
            }
            conexion.cerrarObjetosDeConsulta();
            System.out.println("Busco todos los parámetros");
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar el parametro o no lo encontro");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;
    }
}

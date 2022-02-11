/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

consultas
https://www.1keydata.com/es/sql/sql-between.php
 */
package DAO;

import Conexion.*;
import DTO.EmpleadoDTO;
import Recursos.Utiles;
import static Recursos.Utiles.redondearDecimales;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fabian
 */
public class EmpleadoDAO {

    Conexion conexion;

    public EmpleadoDAO() throws ClassNotFoundException, SQLException {
        conexion = new Conexion();
    }

    public EmpleadoDTO buscarEmpleado(String cod) throws ClassNotFoundException, SQLException {
        EmpleadoDTO em = new EmpleadoDTO();
        try {
            String sql = "SELECT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                    + "FROM EMPLEADOS\n"
                    + "WHERE (((EMPLEADOS.COD_MAE)=?));";
            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Conexion.prepStament.setString(1, cod);
            Conexion.resultSet = Conexion.prepStament.executeQuery();

            while (Conexion.resultSet.next()) {
                em = new EmpleadoDTO();
                em.setCod_empleado(Conexion.resultSet.getString(1));
                em.setDoc_identidad(Conexion.resultSet.getString(2));
                double us = Double.parseDouble(Conexion.resultSet.getString(3));
                em.setUltimo_sueldo(redondearDecimales(us, 2));
                em.setCentro_de_costo(Conexion.resultSet.getString(4));
                em.setCuenta_de_pago(Conexion.resultSet.getString(5));
                em.setNom_em(Conexion.resultSet.getString(6));
                em.setCorreo(Conexion.resultSet.getString(7));

            }
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar el empleado o no lo encontro");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        } finally {
            conexion.cerrarObjetosDeConsulta();
        }
        return em;
    }

    private ArrayList<EmpleadoDTO> llenarLista() {
        ArrayList<EmpleadoDTO> lista = new ArrayList<>();
        try {
            while (Conexion.resultSet.next()) {
                EmpleadoDTO em = new EmpleadoDTO();
                em = new EmpleadoDTO();
                em.setCod_empleado(Conexion.resultSet.getString(1));
                em.setDoc_identidad(Conexion.resultSet.getString(2));
                double us = Double.parseDouble(Conexion.resultSet.getString(3));
                if (Utiles.validarNumeroConXDecimales("" + us, 2)) {
                    em.setUltimo_sueldo(us);
                } else {
                    em.setUltimo_sueldo(redondearDecimales(us, 2));
                }
                em.setCentro_de_costo(Conexion.resultSet.getString(4));
                em.setCuenta_de_pago(Conexion.resultSet.getString(5));
                em.setNom_em(Conexion.resultSet.getString(6));
                em.setCorreo(Conexion.resultSet.getString(7));

                lista.add(em);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    //busca los empleados que tengan novedades para la fecha  segun el numero de resultados y por el centro de costo
    public ArrayList<EmpleadoDTO> buscarPorCentroDeCosto(String centroDeCosto, int numResultados, int tipoNovedad, String dd, String mm, String yyyy) throws ClassNotFoundException, SQLException {
        ArrayList<EmpleadoDTO> lista = new ArrayList<>();
        try {
            String sql = "";
            switch (tipoNovedad) {
                case 1:
                    //pago quincenal
                    sql = "SELECT TOP ? DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)<>'A014' And (acunove.TIN_NOV)<>'A012') AND ((acunove.FEC_NOV)=?)) AND (((EMPLEADOS.CEN_MAE)=?))\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
                case 2:
                    //pago prima
                    sql = "SELECT TOP ? DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)=\"A012\") AND ((acunove.FEC_NOV)=?)) AND (((EMPLEADOS.CEN_MAE)=?))\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
                default:
                    //interese sobre cesantias
                    sql = "SELECT  TOP ? DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)=\"A014\") AND ((acunove.FEC_NOV)=?)) AND (((EMPLEADOS.CEN_MAE)=?))\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
            }
            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Conexion.prepStament.setInt(1, numResultados);
            Date fv = new Date(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd));
            Conexion.prepStament.setDate(2, fv);
            Conexion.prepStament.setString(3, centroDeCosto);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar el empleado o no lo encontro");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;
    }

    //busca todos los empleados por centro de costo que tengan novedades para la fecha
    public ArrayList<EmpleadoDTO> buscarPorCentroDeCosto(String centroDeCosto, int tipoNovedad, String dd, String mm, String yyyy) throws ClassNotFoundException, SQLException {

        ArrayList<EmpleadoDTO> lista = new ArrayList<>();
        try {
            String sql = "";
            switch (tipoNovedad) {
                case 1:
                    //pago quincenal
                    sql = "SELECT DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)<>'A014' And (acunove.TIN_NOV)<>'A012') AND ((acunove.FEC_NOV)=?)) AND (((EMPLEADOS.CEN_MAE)=?))\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
                case 2:
                    //pago prima
                    sql = "SELECT DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)=\"A012\") AND ((acunove.FEC_NOV)=?)) AND (((EMPLEADOS.CEN_MAE)=?))\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
                default:
                    //intereses sobre cesantias
                    sql = "SELECT DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)=\"A014\") AND ((acunove.FEC_NOV)=?)) AND (((EMPLEADOS.CEN_MAE)=?))\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
            }
            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Date fv = new Date(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd));
            Conexion.prepStament.setDate(1, fv);
            Conexion.prepStament.setString(2, centroDeCosto);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar el empleado o no lo encontro");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;
    }

    //busca los empleados segun el numero de resultados y por el centro de costo
    public ArrayList<EmpleadoDTO> buscarPorCentroDeCosto(String centroDeCost, int numResultados) throws ClassNotFoundException, SQLException {
        ArrayList<EmpleadoDTO> lista = new ArrayList<>();
        try {
            String sql = "SELECT TOP ? EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                    + "FROM EMPLEADOS\n"
                    + "WHERE (((EMPLEADOS.CEN_MAE)=?));";

            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Conexion.prepStament.setInt(1, numResultados);
            Conexion.prepStament.setString(2, centroDeCost);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar el empleado o no lo encontro");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;

    }

    //Busca todos los empleados por centro de costo
    public ArrayList<EmpleadoDTO> buscarPorCentroDeCosto(String centroDeCost) throws ClassNotFoundException, SQLException {
        ArrayList<EmpleadoDTO> lista = new ArrayList<>();
        try {
            String sql = "SELECT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                    + "FROM EMPLEADOS\n"
                    + "WHERE (((EMPLEADOS.CEN_MAE)=?));";

            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Conexion.prepStament.setString(1, centroDeCost);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar el empleado o no lo encontro");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;

    }

    //busca los empleados que comiencen por la inicial del texto (LIKE) según el número de resultados
    public ArrayList<EmpleadoDTO> buscarPorNombre(String nom, int numResult) throws ClassNotFoundException, SQLException {
        ArrayList<EmpleadoDTO> lista = new ArrayList<>();
        nom = nom + '%';
        try {
            String sql = "SELECT TOP ? EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                    + "FROM EMPLEADOS\n"
                    + "WHERE EMPLEADOS.NOM_MAE Like ?";
            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Conexion.prepStament.setInt(1, numResult);
            Conexion.prepStament.setString(2, nom);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar el empleado o no lo encontro");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;
    }

    //Busca todos los empleados que comiencen por la inicial del texto (LIKE)
    public ArrayList<EmpleadoDTO> buscarPorNombre(String nom) throws ClassNotFoundException, SQLException {
        ArrayList<EmpleadoDTO> lista = new ArrayList<>();
        nom = nom + '%';
        try {
            String sql = "SELECT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                    + "FROM EMPLEADOS\n"
                    + "WHERE EMPLEADOS.NOM_MAE Like ?";
            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Conexion.prepStament.setString(1, nom);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar el empleado o no lo encontro");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;
    }

    //busca los empleados entre un rango según el código, con un número de resultados(cantidad) y que tengan novedades para esa fecha
    public ArrayList<EmpleadoDTO> buscarPorCodigo(String codInicial, String codFinal, int numResultados, int tipoNovedad, String dd, String mm, String yyyy) throws ClassNotFoundException, SQLException {
        ArrayList<EmpleadoDTO> lista = new ArrayList<>();
        try {
            String sql = null;
            switch (tipoNovedad) {
                case 1:
                    //pago quincenal
                    sql = "SELECT TOP ? DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)<>'A014' And (acunove.TIN_NOV)<>'A012') AND ((acunove.FEC_NOV)=?)) AND EMPLEADOS.COD_MAE BETWEEN ? AND ?\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
                case 2:
                    //pago prima
                    sql = "SELECT TOP ? DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)=\"A012\") AND ((acunove.FEC_NOV)=?)) AND EMPLEADOS.COD_MAE BETWEEN ? AND ?\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
                default: //intereses sobre cesantias
                    sql = "SELECT TOP ? DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)=\"A014\") AND ((acunove.FEC_NOV)=?)) AND EMPLEADOS.COD_MAE BETWEEN ? AND ?\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
            }
            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Conexion.prepStament.setInt(1, numResultados);
            Date fv = new Date(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd));
            Conexion.prepStament.setDate(2, fv);
            Conexion.prepStament.setString(3, codInicial);
            Conexion.prepStament.setString(4, codFinal);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar todos los empleados");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;
    }

    //busca todos los empleados entre un rango según el código (BETWEEN) y que tengan novedades para esa fecha
    public ArrayList<EmpleadoDTO> buscarPorCodigo(String codInicial, String codFinal, int tipoNovedad, String dd, String mm, String yyyy) throws ClassNotFoundException, SQLException {
        ArrayList<EmpleadoDTO> lista = new ArrayList<>();
        try {
            conexion = new Conexion();
            String sql = "";
            switch (tipoNovedad) {
                case 1:
                    //pago quincenal
                    sql = "SELECT DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)<>'A014' And (acunove.TIN_NOV)<>'A012') AND ((acunove.FEC_NOV)=?)) AND EMPLEADOS.COD_MAE BETWEEN ? AND ?\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
                case 2:
                    //pago prima
                    sql = "SELECT DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)=\"A012\") AND ((acunove.FEC_NOV)=?)) AND EMPLEADOS.COD_MAE BETWEEN ? AND ?\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
                default:
                    //intereses sobre cesantias
                    sql = "SELECT DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)=\"A014\") AND ((acunove.FEC_NOV)=?)) AND EMPLEADOS.COD_MAE BETWEEN ? AND ?\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
            }
            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Date fv = new Date(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd));
            Conexion.prepStament.setDate(1, fv);
            Conexion.prepStament.setString(2, codInicial);
            Conexion.prepStament.setString(3, codFinal);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar todos los empleados");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;
    }

    //busca todos los empleados que tengan novedades para la fecha
    public ArrayList<EmpleadoDTO> buscarTodos(int tipoNovedad, String dd, String mm, String yyyy) throws ClassNotFoundException, SQLException {
        ArrayList<EmpleadoDTO> lista = new ArrayList<>();
        try {
            String sql = "";
            switch (tipoNovedad) {
                case 1:
                    //pago quincenal
                    sql = "SELECT DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)<>'A014' And (acunove.TIN_NOV)<>'A012') AND ((acunove.FEC_NOV)=?))\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
                case 2:
                    //pago prima
                    sql = "SELECT DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)=\"A012\") AND ((acunove.FEC_NOV)=?))\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
                default:
                    //intereses sobre cesantias
                    sql = "SELECT DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)=\"A014\") AND ((acunove.FEC_NOV)=?))\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
            }
            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Date fv = new Date(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd));
            Conexion.prepStament.setDate(1, fv);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar todos los empleados");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;
    }

    //busca los empleados que tengan novedades para la fecha según el numero de resultados
    public ArrayList<EmpleadoDTO> buscarTodos(int numResult, int tipoNovedad, String dd, String mm, String yyyy) throws ClassNotFoundException, SQLException {
        ArrayList<EmpleadoDTO> lista = new ArrayList<>();
        try {
            String sql = "";
            switch (tipoNovedad) {
                case 1:
                    //pago quincenal
                    sql = "SELECT TOP ? DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)<>'A014' And (acunove.TIN_NOV)<>'A012') AND ((acunove.FEC_NOV)=?))\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
                case 2:
                    //pago prima
                    sql = "SELECT TOP ? DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)=\"A012\") AND ((acunove.FEC_NOV)=?))\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
                default:
                    //intereses sobre cesantias
                    sql = "SELECT TOP ? DISTINCT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo\n"
                            + "FROM EMPLEADOS INNER JOIN acunove ON EMPLEADOS.COD_MAE = acunove.COD_NOV\n"
                            + "WHERE (((acunove.TIN_NOV)=\"A014\") AND ((acunove.FEC_NOV)=?))\n"
                            + "ORDER BY EMPLEADOS.COD_MAE;";
                    break;
            }
            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Conexion.prepStament.setInt(1, numResult);
            Date fv = new Date(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd));
            Conexion.prepStament.setDate(2, fv);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar todos los empleados");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;
    }

    //busca los empleados según el numero de resultados
    public ArrayList<EmpleadoDTO> buscarTodos(int numResult) throws ClassNotFoundException, SQLException {
        ArrayList<EmpleadoDTO> lista = new ArrayList<>();
        try {
            String sql = "SELECT TOP ? EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo FROM EMPLEADOS;";

            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Conexion.prepStament.setInt(1, numResult);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar todos los empleados");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;
    }

    //busca todos los empleados
    public ArrayList<EmpleadoDTO> buscarTodos() throws ClassNotFoundException, SQLException {
        ArrayList<EmpleadoDTO> lista = new ArrayList<>();
        try {
            String sql = "SELECT EMPLEADOS.COD_MAE, EMPLEADOS.IDE_MAE, EMPLEADOS.ULS_MAE, EMPLEADOS.CEN_MAE, EMPLEADOS.CUE_MAE, EMPLEADOS.NOM_MAE, EMPLEADOS.Correo FROM EMPLEADOS;";

            Conexion.prepStament = conexion.getConexion().prepareStatement(sql);
            Conexion.resultSet = Conexion.prepStament.executeQuery();
            lista = llenarLista();
            conexion.cerrarObjetosDeConsulta();
        } catch (SQLException sqlex) {
            System.out.println("Si llego aquí es porque hubo un error al consultar todos los empleados");
            System.out.println(sqlex);
            sqlex.printStackTrace();
        }
        return lista;
    }

    //conprueba si el código corresponde a un empleado
    public boolean existeEmpleado(String cod) {
        EmpleadoDTO em = new EmpleadoDTO();
        try {
            em = buscarEmpleado(cod);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (em.getNom_em() != null) {
            return true;
        } else {
            return false;
        }

    }

}

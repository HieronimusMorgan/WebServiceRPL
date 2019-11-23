/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.service;

import Tools.DatabaseConnection;
import java.util.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author asus
 */
@WebService(serviceName = "WebServiceRPL")
public class WebServiceRPL {

    private DatabaseConnection conn;

    /**
     * Web service operation
     *
     * @param userDB
     * @param passDB
     * @return
     */
    @WebMethod(operationName = "operation")
    public Boolean operation(@WebParam(name = "userDB") String userDB, @WebParam(name = "passDB") String passDB) {
        try {
            conn = new DatabaseConnection();
            String query = "SELECT usernameUser, passwordUser FROM `user` WHERE usernameUser like '" + userDB + "' AND passwordUser like '" + passDB + "'";
            java.sql.Statement statement = conn.getConnection().createStatement();
            java.sql.ResultSet result = statement.executeQuery(query);
            result.next();
            user a = new user();
            if (result.isFirst()) {
                a.setUsername(result.getString("usernameUser"));
                a.setPassword(result.getString("passwordUser"));
                return true;
            }
            String query1 = "SELECT usernameAdmin, passwordAdmin FROM `admin` WHERE usernameAdmin like '" + userDB + "' AND passwordAdmin like '" + passDB + "'";
            java.sql.Statement statement1 = conn.getConnection().createStatement();
            java.sql.ResultSet result1 = statement1.executeQuery(query1);
            result1.next();
            if (result1.isFirst()) {
                a.setUsername(result1.getString("usernameAdmin"));
                a.setPassword(result1.getString("passwordAdmin"));
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(WebServiceRPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Web service operation
     *
     * @return
     */
    @WebMethod(operationName = "tampilPostingan")
    public ArrayList<Postingan> tampilPostingan() {
        //TODO write your implementation code here:
        conn = new DatabaseConnection();
        ArrayList<Postingan> data = new ArrayList<>();
        try {
            String query = "SELECT * FROM postingan ORDER BY waktuPostingan DESC";
            try (java.sql.Statement statement = conn.getConnection().createStatement()) {
                java.sql.ResultSet result = statement.executeQuery(query);

                while (result.next()) {
                    Postingan p = new Postingan();
                    p.setIdPostingan(result.getString("idPostingan"));
                    p.setIsi(result.getString("isiPostingan"));
                    p.setIdUser(result.getString("idUser"));
                    p.setIdAdmin(result.getString("idAdmin"));
                    java.sql.Timestamp s = result.getTimestamp("waktuPostingan");
                    p.setWaktu(s);
                    p.setNamaPengirim(caripengirim(p.getIdUser(), p.getIdAdmin()));
                    System.out.println(caripengirim(p.getIdUser(), p.getIdAdmin()));
                    data.add(p);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Gagal");
        }
        return data;
    }

    public String caripengirim(String user, String admin) {
        conn = new DatabaseConnection();
        String query = "SELECT nameUser FROM `user` WHERE '" + user + "' = usernameUser";
        String query1 = "SELECT nameAdmin FROM `admin` WHERE '" + admin + "' = usernameAdmin";
        java.sql.Statement statement;
        try {
            statement = conn.getConnection().createStatement();
            java.sql.ResultSet result = statement.executeQuery(query);
            result.next();
            if (result.isFirst()) {
                System.out.println(result.getString(1));
                return result.getString(1);
            }
            java.sql.ResultSet result1 = statement.executeQuery(query1);
            result1.next();
            if (result1.isFirst()) {
                System.out.println(result.getString(1));
                return result1.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WebServiceRPL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    private String idSearch(String nama) {
        conn = new DatabaseConnection();
        String query = "SELECT idUser FROM user a WHERE '" + nama + "' = a.usernameUser";
        String query1 = "SELECT idAdmin FROM admin a WHERE '" + nama + "' = a.usernameAdmin";
        java.sql.Statement statement;
        try {
            statement = conn.getConnection().createStatement();
            java.sql.ResultSet result = statement.executeQuery(query);
            result.next();
            if (result.isFirst()) {
                System.out.println(result.getString(1)+" user");
                return result.getString(1);
            } else {
                java.sql.ResultSet result1 = statement.executeQuery(query1);
                result1.next();
                if (result1.isFirst()) {
                    System.out.println(result.getString(1)+" admin");
                    return result1.getString(1);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(WebServiceRPL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    /**
     * Web service operation
     *
     * @param username
     * @param password
     * @param postingan
     */
    @WebMethod(operationName = "tambahPostingan")
    @Oneway
    public void tambahPostingan(@WebParam(name = "username") String username,
            @WebParam(name = "password") String password,
            @WebParam(name = "postingan") String postingan) {
        conn = new DatabaseConnection();
        String id;
        Timestamp date = new Timestamp(System.currentTimeMillis());
        System.out.println(caripengirim(username, username));
        if (caripengirim(username, username).equalsIgnoreCase(username)) {
            try {
                id = idSearch(username);
                String idposting = date.getTime() + "" + id;
                String query = "INSERT INTO POSTINGAN (idPostingan, isiPostingan, idUser, waktuPostingan) "
                        + "VALUES ('" + idposting + "', '" + postingan + "', '" + id
                        + "', '" + date + "')";
                java.sql.Statement statement = conn.getConnection().createStatement();
                statement.executeUpdate(query);
            } catch (SQLException ex) {
                System.out.println("Gagal 1");
            }
        } else {
            try {
                id = idSearch(username);
                System.out.println(id);
                String idposting = date.getTime() + "" + id;
                String query = "INSERT INTO POSTINGAN (idPostingan, isiPostingan, idAdmin, waktuPostingan) "
                        + "VALUES ('" + idposting + "', '" + postingan + "', '" + id
                        + "', '" + date + "')";
                java.sql.Statement statement = conn.getConnection().createStatement();
                statement.executeUpdate(query);
            } catch (SQLException ex) {
                System.out.println("Gagal 2");
            }
        }
    }
}

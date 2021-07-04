package br.com.main.backend;

import br.com.main.entity.Gamer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static br.com.main.Saturn.*;

public class SQLManager {

    public void insertValues(String tabela, String colunas, String valores) {
        try {
            getSql().openConnection();
            Connection connection = getSql().getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO "+tabela+" ("+colunas+") VALUES ("+valores+");");
            ps.executeUpdate();
            ps.close();
            getSql().closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getData(Gamer gamer, String column, String label, String parameter, String values) {

        try {
            getSql().openConnection();
            String permission = "";
            Connection connection = getSql().getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT "+column+" FROM "+label+" WHERE "+parameter+" = "+values+" ");
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                permission = rs.getString(column);
            }

            getSql().closeConnection();
            return permission;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateData(String label, String column, String valueOfColumn, String parameter, String valueOfParameter) {
        try {
            getSql().openConnection();
            Connection connection = getSql().getConnection();

            PreparedStatement ps = connection.prepareStatement("UPDATE "+label+" SET "+column+" = '" + valueOfColumn + "' WHERE "+parameter+" = '" + valueOfParameter + "'");
            ps.execute();
            ps.close();

            getSql().closeConnection();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}

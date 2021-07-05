package br.com.main.commands.manager;

import br.com.main.groups.Permission;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

import static br.com.main.Saturn.*;

public class VipManager {
    public java.sql.Date getFinalDate(Player player) {

        try {
            getSql().openConnection();
            java.util.Date data = new java.util.Date();
            java.sql.Date date = new java.sql.Date(data.getTime());

            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT END_VIP FROM player_vip WHERE NAME = ?");
            preparedStatement.setString(1, player.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                date = resultSet.getDate("END_VIP");
            }
            resultSet.close();
            getSql().closeConnection();
            return date;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setVIP(Player player, Date date_vip, Date end_vip) {
        try {
            getSql().openConnection();

            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player_vip (NAME,UUID,DATE_VIP,END_VIP) VALUES (?,?,?,?)");
            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2,player.getUniqueId().toString());
            preparedStatement.setDate(3,date_vip);
            preparedStatement.setDate(4,end_vip);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            getSql().closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeVIP(Player player) {
        try {
            getSql().openConnection();

            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM player_vip WHERE UUID = ?");
            preparedStatement.setString(1,player.getUniqueId().toString());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            getSql().closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean hasVIP(Player player) {
        try {
            getSql().openConnection();

            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT DATE_VIP FROM player_vip WHERE NAME = ?");
            preparedStatement.setString(1, player.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet != null && resultSet.next()) {
                return true;
            }
            resultSet.close();
            getSql().closeConnection();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

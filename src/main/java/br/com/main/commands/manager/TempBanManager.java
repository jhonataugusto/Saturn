package br.com.main.commands.manager;

import org.bukkit.entity.Player;

import java.sql.*;

import static br.com.main.Saturn.*;

public class TempBanManager {
    public java.sql.Date getFinalDate(Player player) {

        try {
            getSql().openConnection();
            java.util.Date data = new java.util.Date();
            java.sql.Date date = new java.sql.Date(data.getTime());

            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT END_BAN FROM player_bans WHERE NAME = ?");
            preparedStatement.setString(1, player.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                date = resultSet.getDate("END_BAN");
            }
            resultSet.close();
            getSql().closeConnection();
            return date;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setBan(Player player,String reason,Player banner, Date date_ban, Date end_ban) {
        try {
            getSql().openConnection();

            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player_bans (NAME,UUID,REASON,BANNER,DATE_BAN,END_BAN) VALUES (?,?,?,?,?,?)");
            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.setString(3,reason);
            preparedStatement.setString(4,banner.getName());
            preparedStatement.setDate(5, date_ban);
            preparedStatement.setDate(6, end_ban);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            getSql().closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBan(Player player) {
        try {
            getSql().openConnection();

            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM player_bans WHERE UUID = ?");
            preparedStatement.setString(1, player.getUniqueId().toString());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            getSql().closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean hasBanned(Player player) {
        try {
            getSql().openConnection();

            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT DATE_BAN FROM player_bans WHERE NAME = ?");
            preparedStatement.setString(1, player.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet != null && resultSet.next()) {
                return true;
            }
            assert resultSet != null;
            resultSet.close();
            getSql().closeConnection();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getBanner(Player player) {

        try {
            getSql().openConnection();
            String banner = "";

            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT BANNER FROM player_bans WHERE NAME = ?");
            preparedStatement.setString(1, player.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                banner = resultSet.getString("BANNER");
            }
            resultSet.close();
            getSql().closeConnection();
            return banner;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getReason(Player player) {

        try {
            getSql().openConnection();
            String reason = "";

            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT REASON FROM player_bans WHERE NAME = ?");
            preparedStatement.setString(1, player.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                reason = resultSet.getString("REASON");
            }
            resultSet.close();
            getSql().closeConnection();
            return reason;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

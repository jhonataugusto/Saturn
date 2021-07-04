package br.com.main.commands.managers;

import org.bukkit.entity.Player;

import java.sql.*;

import static br.com.main.Saturn.*;

public class BanManager {

    public static String getBan(String name) {

        try {
            getSql().openConnection();
            String bannedUUID = "";
            Connection connection = getSql().getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT UUID FROM player_bans WHERE NAME = ? ");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                bannedUUID = rs.getString("UUID");
            }
            getSql().closeConnection();
            return bannedUUID;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addBan(Player player, String reason, java.sql.Date ban, java.sql.Date end_ban) {
        try {
            getSql().openConnection();
            Connection connection = getSql().getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO player_bans (NAME,UUID,REASON,DATE_BAN,END_BAN) VALUES (?,?,?,?,?);");
            ps.setString(1, player.getName());
            ps.setString(2, player.getUniqueId().toString());
            ps.setString(3, reason);
            ps.setDate(4, ban);
            ps.setDate(5, end_ban);
            ps.executeUpdate();
            ps.close();
            getSql().closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeBan(String namePlayer) {
        try {
            getSql().openConnection();
            Connection connection = getSql().getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM player_bans WHERE NAME = ?");
            ps.setString(1, namePlayer);

            ps.executeUpdate();
            ps.close();
            getSql().closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static boolean isBanned(String namePlayer) {

        try {
            getSql().openConnection();
            Connection connection = getSql().getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT UUID FROM player_bans WHERE NAME = ? ");
            ps.setString(1, namePlayer);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps.close();
                getSql().closeConnection();
                return true;
            } else {
                ps.close();
                getSql().closeConnection();
                return false;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static java.sql.Date getEndBan(String name) {


        try {

            getSql().openConnection();
            Connection connection = getSql().getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT END_BAN FROM player_bans WHERE NAME = ? ");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                java.sql.Date endBanDate = rs.getDate("END_BAN");
                return endBanDate;
            }
            getSql().closeConnection();
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getReason(String name) {


        try {

            getSql().openConnection();
            String reason = "";
            Connection connection = getSql().getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT REASON FROM player_bans WHERE NAME = ? ");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                reason = rs.getString("REASON");
                return reason;
            }
            getSql().closeConnection();
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

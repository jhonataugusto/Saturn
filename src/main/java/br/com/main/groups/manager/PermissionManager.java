package br.com.main.groups.manager;

import br.com.main.groups.Permission;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static br.com.main.Saturn.*;

public class PermissionManager {

    public String getPermission(Player player) {

        try {
            getSql().openConnection();
            String permission = "";
            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT PERM FROM player_data WHERE UUID = ?");
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                permission = resultSet.getString("PERM");
            }
            resultSet.close();
            getSql().closeConnection();
            return permission;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setPermission(Player player, Permission permission) {
        try {
            getSql().openConnection();

            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE player_data SET PERM = ? WHERE UUID = ?");
            preparedStatement.setString(1, permission.toString());
            preparedStatement.setString(2,player.getUniqueId().toString());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            getSql().closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePermission(Player player) {
        try {
            getSql().openConnection();

            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE player_data SET PERM = ? WHERE UUID = ?");
            preparedStatement.setString(1, Permission.MEMBER.toString());
            preparedStatement.setString(2,player.getUniqueId().toString());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            getSql().closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasPermission(Player player, Permission permission) {
        try {
            getSql().openConnection();

            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT PERM FROM player_data WHERE NAME = ?");
            preparedStatement.setString(1, player.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet != null && resultSet.next()) {
                if (getPermission(player).equals(permission.toString())) {
                    return true;
                }
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


    public void checkPermissionChat(Player player, AsyncPlayerChatEvent event) {

        if (getPermission(player).equals("MEMBER")) {
            event.setFormat("§7§lMEMBRO§r§7 "+player.getName()+" §8››§r " + event.getMessage());
        } else if (getPermission(player).equals("VIP")) {
            event.setFormat("§d§lSATURN§r§d "+player.getName()+" §8››§r  " + event.getMessage());
        } else if (getPermission(player).equals("YOUTUBER")) {
            event.setFormat("§b§lYT§r§b "+player.getName()+" §8››§r " + event.getMessage());
        } else if (getPermission(player).equals("YOUTUBER_PLUS")) {
            event.setFormat("§3§lYT+§r§3 "+player.getName()+" §8››§r " + event.getMessage());
        } else if (getPermission(player).equals("HELPER")) {
            event.setFormat("§2§lHELPER§r§2 "+player.getName()+" §8››§r " + event.getMessage());
        } else if (getPermission(player).equals("TRIAL")) {
            event.setFormat("§d§lTRIAL§r§d "+player.getName()+" §8››§r " + event.getMessage());
        } else if (getPermission(player).equals("MOD")){
            event.setFormat("§5§lMOD§r§5 "+player.getName()+" §8››§r " + event.getMessage());
        } else if (getPermission(player).equals("ADMIN")) {
            event.setFormat("§c§lADMIN§r§c "+player.getName()+" §8››§r " + event.getMessage());
        }
    }

    public void checkPermissionTag(Player player, PlayerJoinEvent event) {

        switch (getPermission(player)) {
            case "MEMBER":
                NametagEdit.getApi().setNametag(player, "§7§lMEMBRO§r§7 ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
            case "VIP":
                NametagEdit.getApi().setNametag(player, "§d§lSATURN§r§d ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
            case "YOUTUBER":
                NametagEdit.getApi().setNametag(player, "§b§lYT§r§b ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
            case "YOUTUBER_PLUS":
                NametagEdit.getApi().setNametag(player, "§3§lYT+§r§3 ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
            case "HELPER":
                NametagEdit.getApi().setNametag(player, "§2§lHELPER§r§2 ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
            case "TRIAL":
                NametagEdit.getApi().setNametag(player, "§d§lTRIAL§r§d ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
            case "MOD":
                NametagEdit.getApi().setNametag(player, "§5§lMOD§r§5 ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
            case "ADMIN":
                NametagEdit.getApi().setNametag(player, "§c§lADMIN§r§c ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
        }
    }

    public void checkPermissionTag(Player player) {

        switch (getPermission(player)) {
            case "MEMBER":
                NametagEdit.getApi().setNametag(player, "§7§lMEMBRO§r§7 ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
            case "VIP":
                NametagEdit.getApi().setNametag(player, "§d§lSATURN§r§d ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
            case "YOUTUBER":
                NametagEdit.getApi().setNametag(player, "§b§lYT§r§b ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
            case "YOUTUBER_PLUS":
                NametagEdit.getApi().setNametag(player, "§3§lYT+§r§3 ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
            case "HELPER":
                NametagEdit.getApi().setNametag(player, "§2§lHELPER§r§2 ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
            case "TRIAL":
                NametagEdit.getApi().setNametag(player, "§d§lTRIAL§r§d ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
            case "MOD":
                NametagEdit.getApi().setNametag(player, "§5§lMOD§r§5 ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
            case "ADMIN":
                NametagEdit.getApi().setNametag(player, "§c§lADMIN§r§c ", "");
                NametagEdit.getApi().reloadNametag(player);
                break;
        }
    }




}

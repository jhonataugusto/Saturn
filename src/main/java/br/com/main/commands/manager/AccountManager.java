package br.com.main.commands.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.main.groups.Permission;
import org.bukkit.entity.Player;

import static br.com.main.Saturn.*;

public class AccountManager {

    public void createAccount(Player player) {

        try {
            getSql().openConnection();
            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player_data (NAME,UUID,PERM,ELO) VALUES (?,?,?,?)");
            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.setString(3, Permission.MEMBER.toString()); // sistema de tags
            preparedStatement.setInt(4, 1000);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            getSql().closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeAccount(Player player) {

        try {
            getSql().openConnection();
            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM player_data WHERE UUID = ?");
            preparedStatement.setString(1, player.getUniqueId().toString());

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasAccount(Player player) {

        try {
            getSql().openConnection();
            Connection connection = getSql().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT UUID FROM player_data WHERE NAME = ?");
            preparedStatement.setString(1, player.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return true;
            }
            resultSet.close();
            getSql().closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

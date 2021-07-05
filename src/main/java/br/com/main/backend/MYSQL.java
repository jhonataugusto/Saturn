package br.com.main.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MYSQL {

    private static MYSQL plugin;
    private Connection connection;

    private final String user;
    private final String pass;
    private final String host;
    private final int port;
    private final String db;
    private int query;

    public MYSQL(String user, String pass, String host, int port, String db) {
        this.user = user;
        this.pass = pass;
        this.host = host;
        this.port = port;
        this.db = db;
        this.query = 0;
        loadDB();
    }

    public void openConnection() {
        try {
            query++;
            if ((connection != null) && (!connection.isClosed()))
                return;

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false", user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            query--;
            e.getStackTrace();
            System.out.println("Ocorreu um erro ao abrir a conexão!");
        }

    }

    public void closeConnection() {
        query--;
        if (query <= 0) {
            try {
                if (connection != null && !connection.isClosed())
                    connection.close();
            } catch (Exception e) {
                System.out.println("Houve um erro ao fechar a conexão!");
            }
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    private void loadDB() {
        openConnection();
        criarTabelas();
        closeConnection();
    }

    private void criarTabela(String tabela, String colunas) {
        try {
            if ((connection != null) && (!connection.isClosed())) {
                Statement stm = connection.createStatement();
                stm.executeUpdate("CREATE TABLE IF NOT EXISTS " + tabela + " (" + colunas + ");");
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao salvar o MYSQL!");
        }
    }

    private void criarTabelas() {
        criarTabela("player_data", "NAME varchar(20) not null, UUID varchar(100) not null primary key, PERM varchar(30), ELO smallint not null");
        criarTabela("player_bans","NAME varchar(20) not null, UUID varchar(100) not null primary key,REASON varchar(50) not null, DATE_BAN date, END_BAN date");
        criarTabela("player_vip","NAME varchar(20) not null,UUID varchar(100) not null primary key, DATE_VIP date, END_VIP date");

    }

    public static MYSQL getPlugin() {
        return plugin;
    }
}
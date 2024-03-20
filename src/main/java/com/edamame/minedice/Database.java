package com.edamame.minedice;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.xml.crypto.Data;
import java.sql.*;

public class Database {
    private final String DBname = "database.db";
    private Connection connection;
    private Statement statement;

    public Database(){
        try{
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.DBname);

            Bukkit.getLogger().info("ーーーーMineDiceーーーー");
            Bukkit.getLogger().info("データベースに接続しました");
            Bukkit.getLogger().info("ーーーーーーーーーーーーー");
        }catch (Exception e){
            Bukkit.getLogger().warning("ーーーーMineDiceーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーー");
        }
        try{
            this.statement.executeUpdate("create table moneydata(uuid text,name text, money integer)");
            Bukkit.getLogger().info("ーーーーMineDiceーーーー");
            Bukkit.getLogger().info("moneydataテーブルを作成しました");
            Bukkit.getLogger().info("ーーーーーーーーーーーーーーー");
            //this.connection.commit();
        }
        catch(SQLException e){
            Bukkit.getLogger().warning("ーーーーMinediceーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーーーー");
        }
    }

    public void AddMoney(Player player, int add_money){
        try{
           String uuid = player.getUniqueId().toString();

           this.statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery("select money from moneydata where uuid = '" + uuid + "'");

            int money = resultSet.getInt("money");
            money = money + add_money;

            this.statement.executeUpdate("update moneydata set money = " + money + " where uuid = '" + uuid + "'");

            resultSet.close();
            statement.close();
        }
        catch (SQLException e){
            Bukkit.getLogger().warning("ーーーーMineDiceーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーー");
        }
    }

    public int CheckMoney(Player player){
        try{
            String uuid = player.getUniqueId().toString();

            this.statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select money from moneydata where uuid = '" + uuid + "'");

            int money = resultSet.getInt("money");

            resultSet.close();
            statement.close();
            return money;
        }
        catch (SQLException e){
            Bukkit.getLogger().warning("ーーーーMineDiceーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーー");
        }

        return -1;
    }
}

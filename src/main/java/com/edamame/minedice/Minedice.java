package com.edamame.minedice;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minedice extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("minediceが起動しました");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("minediceは無効になりました");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("dice")) {
            if (sender instanceof Player) { //このコマンドがプレイヤーから実行されているか確認
                Player player_sender = (Player) sender;         //CommandSender型のsenderをPlayer型に変換
                String name = player_sender.getDisplayName();   //Player型になったので、getDisplayName()が使える。

                if (args.length == 1) {
                    try {
                        int maxnumber = Integer.parseInt(args[0]);
                        if (maxnumber >= 1) {
                            int number = (int) Math.ceil(Math.random() * maxnumber);     //Math.random →0~1の小数を乱数　Math.random×面の数を切り上げでさいころ
                            Bukkit.getServer().broadcastMessage(name + " は " + maxnumber + " 面さいころを振り、" + number + " を出しました。");

                            return true;
                        } else {
                            Bukkit.getServer().broadcastMessage("面の数は1以上の整数にしてください");
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        Bukkit.getServer().broadcastMessage("面の数は1以上の整数にしてください");
                        return false;
                    }
                } else if (args.length == 2) {
                    try {
                        int sum = 0;
                        int maxnumber = Integer.parseInt(args[0]);
                        int count = Integer.parseInt(args[1]);
                        if (1 <= count && count <= 10) {
                            for (int i = 1; i <= count; i++) {
                                int number = (int) Math.ceil(Math.random() * maxnumber);     //Math.random →0~1の小数を乱数　Math.random×面の数を切り上げでさいころ
                                Bukkit.getServer().broadcastMessage(i + " 回目 " + name + " は " + maxnumber + " 面さいころを振り、" + number + " を出しました。");
                                sum += number;
                            }
                            Bukkit.getServer().broadcastMessage(name + " は " + maxnumber + " 面さいころを " + count + " 回振り、出目の合計は" + sum + " でした。");
                            return true;
                        } else {
                            Bukkit.getServer().broadcastMessage("振る個数は1以上10以下の整数にしてください");
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        Bukkit.getServer().broadcastMessage("面の数は1以上の整数にしてください");
                        Bukkit.getServer().broadcastMessage("振る個数は1以上10以下の整数にしてください");
                        return false;
                    }
                }
                return false;
            } else {
                Bukkit.getLogger().info("このコマンドはコンソールからは使用できません。");
            }
        }
        return false;
    }
}
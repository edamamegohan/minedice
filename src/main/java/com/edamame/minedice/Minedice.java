package com.edamame.minedice;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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

    private BukkitTask timerTask = null;
    String parent;
    String child;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mdice")) {

            if (sender instanceof Player) { //このコマンドがプレイヤーから実行されているか確認
                Player player_sender = (Player) sender;         //CommandSender型のsenderをPlayer型に変換
                String name = player_sender.getDisplayName();   //Player型になったので、getDisplayName()が使える。

                if (args.length == 1) {
                    try {
                        int maxnumber = Integer.parseInt(args[0]);
                        if(maxnumber > 100000000){
                            Bukkit.getServer().broadcastMessage("面の数は1~100000000にしてください");
                            return false;
                        }
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
                        if(maxnumber > 100000000){
                            Bukkit.getServer().broadcastMessage("面の数は1~100000000にしてください");
                            return false;
                        }
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
            } else {
                Bukkit.getLogger().info("このコマンドはコンソールからは使用できません");
                return false;
            }
        }

        if(command.getName().equalsIgnoreCase("mcr")){
            if(!(sender instanceof Player)){
                Bukkit.getLogger().info("このコマンドはコンソールからは使用できません");
                return false;
            }

            Player player_sender = (Player) sender;
            String name = player_sender.getDisplayName();

            if(args[0].equalsIgnoreCase("alone")){
                chinchiro(name);
                return true;
            }

            if(args[0].equalsIgnoreCase("open")){
                parent = name;
                CountDownTimer(name);
                return true;
            }

            if(args[0].equalsIgnoreCase("join")){
                if(timerTask != null){
                    timerTask.cancel();
                }
                Bukkit.getServer().broadcastMessage("joined");
                child = name;

                int pointParent = chinchiro(parent);
                int pointChild = chinchiro(child);

                if(pointChild < pointParent){
                    Bukkit.getServer().broadcastMessage("親の勝ち！");
                } else if (pointChild > pointParent){
                    Bukkit.getServer().broadcastMessage("子の勝ち！");
                } else {
                    Bukkit.getServer().broadcastMessage("引き分け");
                }
                return true;
            }
        }
        return false;
    }

    public int chinchiro(String name){
        int[] dices = new int[3];
        for(int i = 0; i < 3; i++) dices[i] = (int) Math.ceil(Math.random() * 6);  //dicesにさいころ3つを格納
        Bukkit.getServer().broadcastMessage(name + " は、" + dices[0] + " , " + dices[1] + " , " + dices[2] + " を出しました");
        if(Math.random() < 0.005){
            //ションベンの処理。if文の中で確率を変更
            Bukkit.getServer().broadcastMessage("おっと！ションベン...！");
            return -1;
        }
        if(dices[0] == dices[1] && dices[1] == dices[2]) {   //ゾロ目の処理
            if(dices[0] == 1){
                Bukkit.getServer().broadcastMessage("ピンゾロ！！！");
                return 11;
            } else {
                Bukkit.getServer().broadcastMessage("ゾロ目！！");
                return 10;
            }
        }
        else if (dices[0] != dices[1] && dices[1] != dices[2] && dices[2] != dices[0]) {  //すべて違うときの処理
            int squared = dices[0]*dices[0] + dices[1]*dices[1] + dices[2]*dices[2];
            if(dices[0] + dices[1] + dices[2] == 14){
                //0014(大石)の処理
                Bukkit.getServer().broadcastMessage("大石！！");
                return 9;
            } else if (squared == 14) {
                //ヒフミの処理
                Bukkit.getServer().broadcastMessage("ヒフミ...");
                return -2;
            } else if (squared == 77) {
                //シゴロの処理
                Bukkit.getServer().broadcastMessage("シゴロ！！");
                return 8;
            } else {
                //役なしの処理
                Bukkit.getServer().broadcastMessage("役なし...");
                return 0;
            }
        }
        else{   //目が2:1で出たとき
            if(dices[0] == dices[1]){
                Bukkit.getServer().broadcastMessage(dices[2] + " の目！");
                return dices[2];
            } else if (dices[1] == dices[2]) {
                Bukkit.getServer().broadcastMessage(dices[0] + " の目！");
                return dices[0];
            }else {
                Bukkit.getServer().broadcastMessage(dices[1] + " の目！");
                return dices[1];
            }
        }
    }

    public void CountDownTimer(String name){

        timerTask = new BukkitRunnable(){
            int time = 80;
            @Override
            public void run() {
                time -= 20;
                if(time <= 0){
                    Bukkit.getServer().broadcastMessage(name+"のチンチロリンの募集は終了しました。");
                    parent = null;
                    this.cancel();
                }
                Bukkit.getServer().broadcastMessage(name+" がチンチロリンを募集しています！/mcr joinで参加しよう！");
                Bukkit.getServer().broadcastMessage("残り募集時間 "+time+" 秒");
            }
        }.runTaskTimer(this, 0, 20*20L);
    }
}
package br.com.main.commands;

import br.com.main.apis.Msg;
import br.com.main.commands.manager.VipManager;
import br.com.main.groups.Permission;
import br.com.main.groups.manager.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.Date;

public class Vip implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            Msg.consoleNoPermMessage();
        } else {
            VipManager vipManager = new VipManager();
            PermissionManager permissionManager = new PermissionManager();
            Player player = (Player) sender;
            if(command.getName().equalsIgnoreCase("setvip")){
                if(permissionManager.hasPermission(player, Permission.ADMIN) || permissionManager.hasPermission(player,Permission.MOD)){
                    if(args.length < 2){
                        Msg.sendMessage(player,"§cUtilize: /setvip <Jogador> <Tempo:DIAS>");
                    } else if (args.length == 2){
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        String timeInDays = args[1];
                        int integerTimeInDays = Integer.parseInt(timeInDays);

                        Date date_vip = new Date();

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date_vip);
                        calendar.add(Calendar.DAY_OF_MONTH,integerTimeInDays);
                        Date end_vip = calendar.getTime();

                        java.sql.Date date_vipInSQL = new java.sql.Date(date_vip.getTime());
                        java.sql.Date end_vipInSQL = new java.sql.Date(end_vip.getTime());

                        vipManager.setVIP(target,date_vipInSQL,end_vipInSQL);
                        permissionManager.setPermission(target,Permission.VIP);
                        permissionManager.checkPermissionTag(target);
                        target.sendMessage("§e Você foi promovido para VIP por §b"+integerTimeInDays+ "§e dias. ");
                    }
                } else{
                    Msg.noPermMessage(player);
                }
            }
        }

        return false;
    }
}

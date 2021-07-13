package br.com.main.commands;

import br.com.main.apis.Msg;
import br.com.main.commands.manager.TempBanManager;
import br.com.main.groups.Permission;
import br.com.main.groups.manager.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class TempBan implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            Msg.consoleNoPermMessage();
        } else {
            PermissionManager permissionManager = new PermissionManager();
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("tempban")) {
                if (!permissionManager.getPermission(player).equals("ADMIN") || permissionManager.getPermission(player).equals("MOD")) {
                    Msg.noPermMessage(player);
                } else {
                    if (args.length == 0) {
                        Msg.sendMessage(player, "§cUtilize: /tempban <Jogador> <Tempo:DIAS> <razao>");
                    } else if (args.length == 1) {
                        Msg.sendMessage(player,"§cVocê precisa adicionar o tempo de ban.");
                        Msg.sendMessage(player, "§cUtilize: /tempban <Jogador> <Tempo:DIAS> <razao>");
                    } else if(args.length == 2){
                        Msg.sendMessage(player,"§cVocê precisa especificar uma razão.");
                        Msg.sendMessage(player, "§cUtilize: /tempban <Jogador> <Tempo:DIAS> <razao>");
                    } else {
                        TempBanManager tempBanManager = new TempBanManager();
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        String timeInString = args[1];
                        int time = Integer.parseInt(timeInString);

                        String myString = "";
                        for (int i = 2; i < args.length; i++) {
                            String reason = args[i] + " ";
                            myString = myString + reason;
                        }

                        Date date_vip = new Date();

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date_vip);
                        calendar.add(Calendar.DAY_OF_MONTH,time);
                        Date end_vip = calendar.getTime();

                        java.sql.Date date_banInSQL = new java.sql.Date(date_vip.getTime());
                        java.sql.Date end_banInSQL = new java.sql.Date(end_vip.getTime());

                        LocalDate localdatefinal = new java.sql.Date(end_vip.getTime()).toLocalDate();
                        LocalDate dateActual = new java.sql.Date(System.currentTimeMillis()).toLocalDate();

                        long diferencaEmDias = ChronoUnit.DAYS.between(dateActual,localdatefinal);

                        tempBanManager.setBan(target, myString, player, date_banInSQL, end_banInSQL);

                        target.kickPlayer("§m---------------------------------------------------------- \n" +
                                "§6SATURN NETWORK \n" +
                                "\n" +
                                "§cVOCÊ FOI BANIDO TEMPORARIAMENTE \n" +
                                "\n" +
                                "§7Data prevista para o encerramento do BAN : \n" +
                                "§r( " + end_banInSQL + " ) = "+diferencaEmDias+" dias\n" +
                                "\n" +
                                "§7Banido por: §f" + player.getName() + "\n" +
                                "\n" +
                                "§7Motivo do ban: §f" + myString + "\n" +
                                "§m----------------------------------------------------------");
                    }
                }
            }
        }
        return false;
    }
}

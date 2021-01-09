package me.preciouso.lobbynickcheck.Command;

import me.preciouso.lobbynickcheck.MojangWrapper.UserCheck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class CommandNickCheck extends CommandBase {

    public ArrayList<String> getOnlinePlayerNames() {
        Collection<NetworkPlayerInfo> playerInfoCollection =  Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();
        ArrayList<String> onlinePlayers = new ArrayList<>();
        for (NetworkPlayerInfo playerInfo: playerInfoCollection) {
            onlinePlayers.add(playerInfo.getGameProfile().getName());
        }

        return onlinePlayers;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        UserCheck uc = new UserCheck();
        ArrayList<String> toCheck;
        if (args.length > 0) {
            toCheck = new ArrayList<>(Arrays.asList(args));
        } else {
            toCheck = getOnlinePlayerNames();
        }

        try {
            uc.checkUsername(toCheck);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCommandName() {
        return "nickcheck";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("nc");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "§7§m------------§7[§b§l NickCheck §7]§7§m------------" + "\n" +
                "§b● /nc §7- Verify usernames of all players in lobby" + "\n" +
                "§b● /nc <username(s)> §7- Verify specific usernames" + "\n" +
                "§7§m------------------------------------------";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            // TODO player names
            ArrayList<String> onlinePlayers = getOnlinePlayerNames();
            return getListOfStringsMatchingLastWord(args, onlinePlayers);
        }
        return null;
    }
}

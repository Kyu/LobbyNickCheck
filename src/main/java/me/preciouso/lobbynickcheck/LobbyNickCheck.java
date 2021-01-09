package me.preciouso.lobbynickcheck;

import me.preciouso.lobbynickcheck.Command.CommandNickCheck;
import me.preciouso.lobbynickcheck.MojangWrapper.UserCheck;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.concurrent.ExecutionException;

@Mod(modid = LobbyNickCheck.MODID, version = LobbyNickCheck.VERSION)
public class LobbyNickCheck {
    public static final String MODID = "lobbynickcheck";
    public static final String VERSION = "1.0.0";

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new CommandNickCheck());
    }
}

package com.yourname.npcbattle;

import com.yourname.npcbattle.commands.BattleCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NpcBattleCommandMod implements ModInitializer {
    public static final String MOD_ID = "npcbattle";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Inicializando NPC Battle Command Mod");
        
        // Registra o comando /npcbattle
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            BattleCommand.register(dispatcher);
        });
        
        LOGGER.info("Comando /npcbattle registrado com sucesso!");
    }
}

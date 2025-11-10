package com.yourname.npcbattle.commands;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

public class BattleCommand {
    
    // Lista de NPCs disponíveis (pode ser expandida)
    private static final List<String> AVAILABLE_NPCS = Arrays.asList(
        "standard",
        "sacchi",
        "ai_test"
    );
    
    // Sugestões para autocompletar
    private static final SuggestionProvider<ServerCommandSource> NPC_SUGGESTIONS = 
        (context, builder) -> CommandSource.suggestMatching(AVAILABLE_NPCS, builder);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("npcbattle")
                .then(CommandManager.argument("npc_name", StringArgumentType.string())
                    .suggests(NPC_SUGGESTIONS)
                    .executes(BattleCommand::executeBattle)
                )
                .executes(context -> {
                    // Se executado sem argumentos, mostra ajuda
                    showHelp(context.getSource());
                    return 1;
                })
        );
    }

    private static int executeBattle(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        
        if (player == null) {
            source.sendError(Text.literal("Este comando só pode ser usado por jogadores!"));
            return 0;
        }

        String npcName = StringArgumentType.getString(context, "npc_name");
        
        // Valida se o NPC existe
        if (!AVAILABLE_NPCS.contains(npcName.toLowerCase())) {
            player.sendMessage(
                Text.literal("❌ Treinador '")
                    .append(Text.literal(npcName).formatted(Formatting.RED))
                    .append(Text.literal("' não encontrado!"))
                    .formatted(Formatting.RED),
                false
            );
            showHelp(source);
            return 0;
        }

        // Verifica se o jogador tem Pokémon
        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (party == null || party.toGappyList().isEmpty()) {
            player.sendMessage(
                Text.literal("❌ Você precisa ter pelo menos um Pokémon para batalhar!")
                    .formatted(Formatting.RED),
                false
            );
            return 0;
        }

        // Cria e spawna o NPC diretamente
        try {
            NPCEntity npc = spawnTrainerNPC(player, npcName);
            
            if (npc == null) {
                player.sendMessage(
                    Text.literal("❌ Erro ao criar o treinador NPC!")
                        .formatted(Formatting.RED),
                    false
                );
                return 0;
            }
            
            // Inicia a batalha imediatamente
            npc.interact(player, player.getActiveHand());
            
            player.sendMessage(
                Text.literal("⚔ Batalha iniciada com ")
                    .append(Text.literal(npc.getName().getString()).formatted(Formatting.GREEN, Formatting.BOLD))
                    .append(Text.literal("!"))
                    .formatted(Formatting.GOLD),
                false
            );
            
            return 1;
            
        } catch (Exception e) {
            player.sendMessage(
                Text.literal("❌ Erro ao iniciar batalha: " + e.getMessage())
                    .formatted(Formatting.RED),
                false
            );
            e.printStackTrace();
            return 0;
        }
    }

    private static NPCEntity spawnTrainerNPC(ServerPlayerEntity player, String npcName) {
        try {
            // Cria a entidade NPC
            NPCEntity npc = new NPCEntity(player.getWorld());
            
            // Define a posição do NPC (3 blocos na frente do jogador)
            Vec3d playerPos = player.getPos();
            Vec3d playerLook = player.getRotationVector();
            Vec3d spawnPos = playerPos.add(playerLook.multiply(3.0));
            
            npc.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
            
            // Faz o NPC olhar para o jogador
            npc.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, player.getPos());
            
            // Carrega o preset do NPC baseado no nome
            String presetId = "cobblemon:" + npcName.toLowerCase();
            
            // Tenta carregar o preset do Cobblemon
            // NOTA: Você precisará ajustar isso baseado na API do Cobblemon
            // Esta é uma implementação básica que pode precisar de ajustes
            
            // Define o nome customizado do NPC
            String displayName = switch (npcName.toLowerCase()) {
                case "standard" -> "Treinador Padrão";
                case "sacchi" -> "Professora Sacchi";
                case "ai_test" -> "Treinador de Teste";
                default -> "Treinador";
            };
            npc.setCustomName(Text.literal(displayName));
            
            // Spawna o NPC no mundo
            player.getWorld().spawnEntity(npc);
            
            // Aguarda um tick para o NPC ser processado
            // e depois remove ele (batalha temporária)
            scheduleNPCRemoval(npc, 100); // Remove após 5 segundos (100 ticks)
            
            return npc;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static void scheduleNPCRemoval(NPCEntity npc, int ticks) {
        // Remove o NPC após X ticks para não poluir o mundo
        new Thread(() -> {
            try {
                Thread.sleep(ticks * 50); // 50ms por tick
                if (npc != null && !npc.isRemoved()) {
                    npc.remove(net.minecraft.entity.Entity.RemovalReason.DISCARDED);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void showHelp(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("========== NPC Battle Command ==========")
            .formatted(Formatting.GOLD, Formatting.BOLD), false);
        source.sendFeedback(() -> Text.literal("Uso: /npcbattle <nome_do_treinador>")
            .formatted(Formatting.YELLOW), false);
        source.sendFeedback(() -> Text.literal(""), false);
        source.sendFeedback(() -> Text.literal("Treinadores disponíveis:")
            .formatted(Formatting.AQUA, Formatting.BOLD), false);
        
        for (String npc : AVAILABLE_NPCS) {
            source.sendFeedback(() -> Text.literal("  • " + npc)
                .formatted(Formatting.GREEN), false);
        }
        
        source.sendFeedback(() -> Text.literal(""), false);
        source.sendFeedback(() -> Text.literal("Exemplo: /npcbattle standard")
            .formatted(Formatting.GRAY, Formatting.ITALIC), false);
        source.sendFeedback(() -> Text.literal("=======================================")
            .formatted(Formatting.GOLD, Formatting.BOLD), false);
    }
}

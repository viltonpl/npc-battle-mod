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

        // Tenta spawnar e iniciar batalha com o NPC
        try {
            boolean success = spawnAndBattleNPC(player, npcName);
            
            if (success) {
                player.sendMessage(
                    Text.literal("⚔ Iniciando batalha com treinador ")
                        .append(Text.literal(npcName).formatted(Formatting.GREEN, Formatting.BOLD))
                        .append(Text.literal("!"))
                        .formatted(Formatting.GOLD),
                    false
                );
                return 1;
            } else {
                player.sendMessage(
                    Text.literal("❌ Não foi possível iniciar a batalha. Verifique se o preset '")
                        .append(Text.literal(npcName).formatted(Formatting.YELLOW))
                        .append(Text.literal("' existe no Cobblemon."))
                        .formatted(Formatting.RED),
                    false
                );
                return 0;
            }
            
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

    private static boolean spawnAndBattleNPC(ServerPlayerEntity player, String npcName) {
        try {
            // Cria a entidade NPC
            NPCEntity npc = new NPCEntity(player.getWorld());
            
            // Define a posição do NPC (3 blocos na frente do jogador)
            Vec3d playerPos = player.getPos();
            Vec3d playerLook = player.getRotationVector();
            Vec3d spawnPos = playerPos.add(playerLook.multiply(3.0));
            
            npc.refreshPositionAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, 
                player.getYaw() + 180.0f, 0.0f);
            
            // Define o nome customizado do NPC
            String displayName = switch (npcName.toLowerCase()) {
                case "standard" -> "Treinador Padrão";
                case "sacchi" -> "Professora Sacchi";
                case "ai_test" -> "Treinador de Teste";
                default -> "Treinador";
            };
            npc.setCustomName(Text.literal(displayName));
            npc.setCustomNameVisible(true);
            
            // Spawna o NPC no mundo
            boolean spawned = player.getWorld().spawnEntity(npc);
            
            if (!spawned) {
                return false;
            }
            
            // Agenda a interação com o NPC após 1 tick (50ms)
            // Isso permite que o NPC seja processado antes da interação
            player.getServer().execute(() -> {
                if (npc != null && !npc.isRemoved()) {
                    // Força a interação com o jogador (isso deve iniciar a batalha)
                    npc.interactAt(player, player.getPos(), player.getActiveHand());
                    
                    // Agenda remoção do NPC após a batalha (200 ticks = 10 segundos)
                    scheduleNPCRemoval(player.getServer(), npc, 200);
                }
            });
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static void scheduleNPCRemoval(net.minecraft.server.MinecraftServer server, NPCEntity npc, int ticks) {
        // Agenda a remoção do NPC após X ticks
        server.execute(() -> {
            try {
                Thread.sleep(ticks * 50L); // 50ms por tick
                server.execute(() -> {
                    if (npc != null && !npc.isRemoved()) {
                        npc.remove(net.minecraft.entity.Entity.RemovalReason.DISCARDED);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
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

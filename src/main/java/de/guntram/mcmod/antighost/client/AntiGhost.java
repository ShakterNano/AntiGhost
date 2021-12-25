package de.guntram.mcmod.antighost.client;

import de.guntram.mcmod.antighost.client.config.AntiGhostConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommandSource;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class AntiGhost {

    private AntiGhost() {}

    private static KeyBinding showGui;

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(AntiGhost::registerListener);
    }

    private static void registerListener(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new AntiGhost());
        ClientRegistry.registerKeyBinding(showGui = new KeyBinding("key." + AntiGhostMain.MOD_ID + ".reveal", 'G', "key.categories." + AntiGhostMain.MOD_ID));
    }

    @SubscribeEvent
    public void keyPressed(final InputEvent.KeyInputEvent inputEvent) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (showGui.consumeClick()) {
            execute(player);
        }
    }
    
    @SubscribeEvent
    public void chatEvent(final ClientChatEvent chatEvent) {
        if (chatEvent.getOriginalMessage().equals("/ghost")) {
            execute(Minecraft.getInstance().player);
            chatEvent.setCanceled(true);
        }
    }

    private void execute(ICommandSource sender) {
        ClientPlayNetHandler connection = Minecraft.getInstance().getConnection();

        if (connection != null) {
            if (sender instanceof ClientPlayerEntity) {
                ClientPlayerEntity player = (ClientPlayerEntity) sender;
                player.displayClientMessage(new TranslationTextComponent("msg." + AntiGhostMain.MOD_ID + ".request"), false);

                int requestRadius = AntiGhostConfig.getRequestRadius();
                BlockPos pos = player.blockPosition();
                for (int dx = -requestRadius; dx <= requestRadius; dx++) {
                    for (int dy = -requestRadius; dy <= requestRadius; dy++) {
                        for (int dz = -requestRadius; dz <= requestRadius; dz++) {
                            CPlayerDiggingPacket packet = new CPlayerDiggingPacket(
                                    CPlayerDiggingPacket.Action.ABORT_DESTROY_BLOCK,
                                    new BlockPos(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz),
                                    Direction.UP // with ABORT_DESTROY_BLOCK, this value is unused
                            );

                            connection.send(packet);
                        }
                    }
                }
            }
        }
    }
}

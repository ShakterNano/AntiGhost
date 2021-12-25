package de.guntram.mcmod.antighost.client;

import de.guntram.mcmod.antighost.client.config.AntiGhostConfig;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

@Mod(AntiGhostMain.MOD_ID)
public final class AntiGhostMain {

    public static final String MOD_ID = "antighost";
    public static final String MOD_NAME = "AntiGhost";

    public AntiGhostMain() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        if (FMLEnvironment.dist.isClient()) {
            AntiGhost.init();
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, AntiGhostConfig.initAndGetSpec());
        } else {
            System.err.println(MOD_NAME + " detected a dedicated server. Not doing anything.");
        }
    }
}

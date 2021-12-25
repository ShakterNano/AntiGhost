package de.guntram.mcmod.antighost.client.config;

import de.guntram.mcmod.antighost.client.AntiGhostMain;
import net.minecraftforge.common.ForgeConfigSpec;

public final class AntiGhostConfig {

    private AntiGhostConfig() {}

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    private static ForgeConfigSpec spec;

    private static ForgeConfigSpec.ConfigValue<Integer> requestRadius;

    private static void initConfig() {
        builder.push(AntiGhostMain.MOD_NAME + " config");

        requestRadius = builder.comment("The radius of the cube to resend blocks in. Maximum value is 16").define("Request radius", 4);

        builder.pop();
        spec = builder.build();
    }

    public static int getRequestRadius() {
        int radius = requestRadius.get();

        if (radius > 16) {
            radius = 16;
        }

        return radius;
    }

    public static ForgeConfigSpec initAndGetSpec() {
        initConfig();
        return spec;
    }
}

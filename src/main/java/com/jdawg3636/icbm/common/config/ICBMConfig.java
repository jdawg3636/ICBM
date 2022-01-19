package com.jdawg3636.icbm.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ICBMConfig {

    /**
     * Client specific configuration - only loaded clientside
     * CURRENTLY UNUSED - NEED TO UNCOMMENT REGISTRATION IN MOD CLASS CONSTRUCTOR BEFORE USE!
     */
    public static class Client {

        public ForgeConfigSpec spec;

        //private final ForgeConfigSpec.BooleanValue testValue;

        public Client() {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            /*this.testValue = builder
                    .comment("This is a test of the Forge config system. This setting does nothing.")
                    .define("testValue", false);*/
            this.spec = builder.build();
        }

        //public boolean getTestValue() {
        //    return testValue.get();
        //}

    }

    /**
     * General configuration that doesn't need to be synchronized but needs to be available before server startup
     */
    public static class Common {

        public ForgeConfigSpec spec;

        private final ForgeConfigSpec.IntValue maxBlastManagerThreadCountPerLevel;

        private final ForgeConfigSpec.IntValue maxNumTicksAliveForLinearMissiles;

        private final ForgeConfigSpec.BooleanValue doLogMissilePathsHandheld;

        private final ForgeConfigSpec.BooleanValue enableEasterEggForRedcoats;

        private final ForgeConfigSpec.DoubleValue antimatterFuzzinessPercentage;

        private final ForgeConfigSpec.IntValue blastDepthBreaching;
        private final ForgeConfigSpec.DoubleValue blastRadiusEndothermic;
        private final ForgeConfigSpec.DoubleValue blastRadiusExothermic;
        private final ForgeConfigSpec.DoubleValue blastRadiusHypersonic;
        private final ForgeConfigSpec.DoubleValue blastRadiusNuclear;
        private final ForgeConfigSpec.DoubleValue blastRadiusSonic;

        public Common() {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            this.maxBlastManagerThreadCountPerLevel = builder
                    .comment("Defines the maximum number of blast manager threads that can be simultaneously active per ",
                            "level/dimension. SETTING THIS VALUE TOO HIGH MAY CAUSE ENOUGH LAG TO COMPLETELY LOCK UP YOUR",
                            "COMPUTER. YOU HAVE BEEN WARNED!")
                    .defineInRange("maxBlastManagerThreadCountPerLevel", 4, 1, Integer.MAX_VALUE);
            this.maxNumTicksAliveForLinearMissiles = builder
                    .comment("Defines the amount of time, in game ticks (20 ticks = 1 second), that a missile launched from",
                            "a handheld launcher or a cruise launcher (rather than from a launcher platform) can be in flight",
                            "before automatically detonating.")
                    .defineInRange("maxNumTicksAliveForLinearMissiles", 100, 0, Integer.MAX_VALUE);
            this.doLogMissilePathsHandheld = builder
                    .comment("Enables logging to console of missile launches from handheld Rocket Launchers.")
                    .define("doLogMissilePathsHandheld", true);
            this.enableEasterEggForRedcoats = builder
                    .comment("Enables an easter egg where all explosives placed by \"SlushierZeus69\" or \"dig_dug__\"",
                            "have the \"unstable = true\" variant of the BlockState, causing them to automatically ignite",
                            "when broken rather than drop as an item.")
                    .define("enableEasterEggForRedcoats", true);
            this.antimatterFuzzinessPercentage = builder
                    .comment("Defines the percentage, as a decimal (0.0 = 0%, 1.0 = 100%), that a block within the \"fuzzy\"",
                            "section within the radius of an antimatter blast will be destroyed in the blast.")
                    .defineInRange("antimatterFuzzynessPercentage", 0.4D, 0D, 1D);
            this.blastDepthBreaching = builder
                    .comment("The breaching blast creates a string of explosions in the direction towards which it is",
                            "ignited. This value defines the maximum depth, in blocks, that said string can reach. The",
                            "actual depth for a given blast may be lower, depending on the blast resistance of the blocks",
                            "in its way.")
                    .defineInRange("blastDepthBreaching", 7, 1, Integer.MAX_VALUE);
            this.blastRadiusEndothermic = builder
                    .comment("Defines the maximum radius of an Endothermic Blast.")
                    .defineInRange("blastRadiusEndothermic", 12D, 0D, (double)Float.MAX_VALUE);
            this.blastRadiusExothermic = builder
                    .comment("Defines the maximum radius of an Exothermic Blast.")
                    .defineInRange("blastRadiusExothermic", 12D, 0D, (double)Float.MAX_VALUE);
            this.blastRadiusHypersonic = builder
                    .comment("Defines the maximum radius of an Hypersonic Blast.")
                    .defineInRange("blastRadiusHypersonic", 13D, 0D, (double)Float.MAX_VALUE);
            this.blastRadiusNuclear = builder
                    .comment("Defines the maximum radius of an Nuclear Blast.")
                    .defineInRange("blastRadiusNuclear", 30D, 0D, (double)Float.MAX_VALUE);
            this.blastRadiusSonic = builder
                    .comment("Defines the maximum radius of an Sonic Blast.")
                    .defineInRange("blastRadiusSonic", 9D, 0D, (double)Float.MAX_VALUE);
            this.spec = builder.build();
        }

        public int getMaxBlastManagerThreadCountPerLevel() {
            return maxBlastManagerThreadCountPerLevel.get();
        }

        public int getMaxNumTicksAliveForLinearMissiles() {
            return maxNumTicksAliveForLinearMissiles.get();
        }

        public boolean getDoLogMissilePathsHandheld() {
            return doLogMissilePathsHandheld.get();
        }

        public boolean getEnableEasterEggForRedcoats() {
            return enableEasterEggForRedcoats.get();
        }

        public double getAntimatterFuzzinessPercentage() {
            return antimatterFuzzinessPercentage.get();
        }

        public int getBlastDepthBreaching() {
            return blastDepthBreaching.get();
        }

        public double getBlastRadiusEndothermic() {
            return blastRadiusEndothermic.get();
        }

        public double getBlastRadiusExothermic() {
            return blastRadiusExothermic.get();
        }

        public double getBlastRadiusHypersonic() {
            return blastRadiusHypersonic.get();
        }

        public double getBlastRadiusNuclear() {
            return blastRadiusNuclear.get();
        }

        public double getBlastRadiusSonic() {
            return blastRadiusSonic.get();
        }

    }

    /**
     * Synchronized, stored in world file. Ewww.
     * CURRENTLY UNUSED - NEED TO UNCOMMENT REGISTRATION IN MOD CLASS CONSTRUCTOR BEFORE USE!
     */
    public static class Server {

        public ForgeConfigSpec spec;

        //private final ForgeConfigSpec.BooleanValue testValue;

        public Server() {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            /*this.testValue = builder
                    .comment("This is a test of the Forge config system. This setting does nothing.")
                    .define("testValue", false);*/
            this.spec = builder.build();
        }

        //public boolean getTestValue() {
        //    return testValue.get();
        //}

    }

}

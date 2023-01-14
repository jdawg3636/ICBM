package com.jdawg3636.icbm.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ICBMConfig {

    /**
     * Client specific configuration - only loaded clientside
     * CURRENTLY UNUSED - NEED TO UNCOMMENT REGISTRATION IN MOD CLASS CONSTRUCTOR BEFORE USE!
     */
    public static class Client {

        public final ForgeConfigSpec spec;

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

        public final ForgeConfigSpec spec;

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

        private final ForgeConfigSpec.IntValue oreCopperSize;
        private final ForgeConfigSpec.IntValue oreCopperRange;
        private final ForgeConfigSpec.IntValue oreCopperCount;
        private final ForgeConfigSpec.IntValue oreSulfurSize;
        private final ForgeConfigSpec.IntValue oreSulfurRange;
        private final ForgeConfigSpec.IntValue oreSulfurCount;
        private final ForgeConfigSpec.IntValue oreTinSize;
        private final ForgeConfigSpec.IntValue oreTinRange;
        private final ForgeConfigSpec.IntValue oreTinCount;
        private final ForgeConfigSpec.IntValue oreUraniumSize;
        private final ForgeConfigSpec.IntValue oreUraniumRange;
        private final ForgeConfigSpec.IntValue oreUraniumCount;

        private final ForgeConfigSpec.IntValue particleAcceleratorEnergyUsagePerTick;
        private final ForgeConfigSpec.DoubleValue particleAcceleratorSpeedIncreasePerTick;
        private final ForgeConfigSpec.DoubleValue particleAcceleratorSpeedPenaltyForCollision;
        private final ForgeConfigSpec.DoubleValue particleAcceleratorSpeedRequiredToGenerateAntimatter;

        public Common() {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            this.maxBlastManagerThreadCountPerLevel = builder
                    .comment("",
                            "Defines the maximum number of blast manager threads that can be simultaneously active per ",
                            "level/dimension. SETTING THIS VALUE TOO HIGH MAY CAUSE ENOUGH LAG TO COMPLETELY LOCK UP YOUR",
                            "COMPUTER. YOU HAVE BEEN WARNED!")
                    .defineInRange("maxBlastManagerThreadCountPerLevel", 4, 1, Integer.MAX_VALUE);
            this.maxNumTicksAliveForLinearMissiles = builder
                    .comment("",
                            "Defines the amount of time, in game ticks (20 ticks = 1 second), that a missile launched from",
                            "a handheld launcher or a cruise launcher (rather than from a launcher platform) can be in flight",
                            "before automatically detonating.")
                    .defineInRange("maxNumTicksAliveForLinearMissiles", 200, 0, Integer.MAX_VALUE);
            this.doLogMissilePathsHandheld = builder
                    .comment("",
                            "Enables logging to console of missile launches from handheld Rocket Launchers.")
                    .define("doLogMissilePathsHandheld", true);
            this.enableEasterEggForRedcoats = builder
                    .comment("",
                            "Enables an easter egg where all explosives placed by \"SlushierZeus69\" or \"dig_dug__\"",
                            "have the \"unstable = true\" variant of the BlockState, causing them to automatically ignite",
                            "when broken rather than drop as an item.")
                    .define("enableEasterEggForRedcoats", true);
            this.antimatterFuzzinessPercentage = builder
                    .comment("",
                            "Defines the percentage, as a decimal (0.0 = 0%, 1.0 = 100%), that a block within the \"fuzzy\"",
                            "section within the radius of an antimatter blast will be destroyed in the blast.")
                    .defineInRange("antimatterFuzzynessPercentage", 0.4D, 0D, 1D);
            this.blastDepthBreaching = builder
                    .comment("",
                            "The breaching blast creates a string of explosions in the direction towards which it is",
                            "ignited. This value defines the maximum depth, in blocks, that said string can reach. The",
                            "actual depth for a given blast may be lower, depending on the blast resistance of the blocks",
                            "in its way.")
                    .defineInRange("blastDepthBreaching", 7, 1, Integer.MAX_VALUE);
            this.blastRadiusEndothermic = builder
                    .comment("",
                            "Defines the maximum radius of an Endothermic Blast.")
                    .defineInRange("blastRadiusEndothermic", 12D, 0D, Float.MAX_VALUE);
            this.blastRadiusExothermic = builder
                    .comment("",
                            "Defines the maximum radius of an Exothermic Blast.")
                    .defineInRange("blastRadiusExothermic", 12D, 0D, Float.MAX_VALUE);
            this.blastRadiusHypersonic = builder
                    .comment("",
                            "Defines the maximum radius of an Hypersonic Blast.")
                    .defineInRange("blastRadiusHypersonic", 13D, 0D, Float.MAX_VALUE);
            this.blastRadiusNuclear = builder
                    .comment("",
                            "Defines the maximum radius of an Nuclear Blast.")
                    .defineInRange("blastRadiusNuclear", 30D, 0D, Float.MAX_VALUE);
            this.blastRadiusSonic = builder
                    .comment("",
                            "Defines the maximum radius of an Sonic Blast.")
                    .defineInRange("blastRadiusSonic", 9D, 0D, Float.MAX_VALUE);
            this.oreCopperSize = builder
                    .comment("",
                            "Defines the 'size' parameter of Copper Ore vein generation.")
                    .defineInRange("oreCopperSize", 9, 0, Integer.MAX_VALUE);
            this.oreCopperRange = builder
                    .comment("",
                            "Defines the 'range' parameter of Copper Ore vein generation.")
                    .defineInRange("oreCopperRange", 64, 0, Integer.MAX_VALUE);
            this.oreCopperCount = builder
                    .comment("",
                            "Defines the 'count' parameter of Copper Ore vein generation.")
                    .defineInRange("oreCopperCount", 20, 0, Integer.MAX_VALUE);
            this.oreSulfurSize = builder
                    .comment("",
                            "Defines the 'size' parameter of Sulfur Ore vein generation.")
                    .defineInRange("oreSulfurSize", 16, 0, Integer.MAX_VALUE);
            this.oreSulfurRange = builder
                    .comment("",
                            "Defines the 'range' parameter of Sulfur Ore vein generation.")
                    .defineInRange("oreSulfurRange", 11, 0, Integer.MAX_VALUE);
            this.oreSulfurCount = builder
                    .comment("",
                            "Defines the 'count' parameter of Sulfur Ore vein generation.")
                    .defineInRange("oreSulfurCount", 20, 0, Integer.MAX_VALUE);
            this.oreTinSize = builder
                    .comment("",
                            "Defines the 'size' parameter of Tin Ore vein generation.")
                    .defineInRange("oreTinSize", 6, 0, Integer.MAX_VALUE);
            this.oreTinRange = builder
                    .comment("",
                            "Defines the 'range' parameter of Tin Ore vein generation.")
                    .defineInRange("oreTinRange", 48, 0, Integer.MAX_VALUE);
            this.oreTinCount = builder
                    .comment("",
                            "Defines the 'count' parameter of Tin Ore vein generation.")
                    .defineInRange("oreTinCount", 20, 0, Integer.MAX_VALUE);
            this.oreUraniumSize = builder
                    .comment("",
                            "Defines the 'size' parameter of Uranium Ore vein generation.")
                    .defineInRange("oreUraniumSize", 8, 0, Integer.MAX_VALUE);
            this.oreUraniumRange = builder
                    .comment("",
                            "Defines the 'range' parameter of Uranium Ore vein generation.")
                    .defineInRange("oreUraniumRange", 16, 0, Integer.MAX_VALUE);
            this.oreUraniumCount = builder
                    .comment("",
                            "Defines the 'count' parameter of Uranium Ore vein generation.")
                    .defineInRange("oreUraniumCount", 1, 0, Integer.MAX_VALUE);
            this.particleAcceleratorEnergyUsagePerTick = builder
                    .comment("",
                            "Defines the amount of Forge Energy (FE) consumed per tick by a particle accelerator while it is active.")
                    .defineInRange("particleAcceleratorEnergyUsagePerTick", 1000, 0, Integer.MAX_VALUE);
            this.particleAcceleratorSpeedIncreasePerTick = builder
                    .comment("",
                            "Defines the speed increase in blocks per tick that is applied each tick to the particle in a particle accelerator.")
                    .defineInRange("particleAcceleratorSpeedIncreasePerTick", 0.004D, 0, Float.MAX_VALUE);
            this.particleAcceleratorSpeedPenaltyForCollision = builder
                    .comment("",
                            "Defines the speed penalty in percent that is incurred to a particle when it collides with the walls of a particle accelerator.")
                    .defineInRange("particleAcceleratorSpeedPenaltyForCollision", 0.05D, 0, 1D);
            this.particleAcceleratorSpeedRequiredToGenerateAntimatter = builder
                    .comment("",
                            "Defines the speed in blocks per tick that is required for a particle to be converted into antimatter in a particle accelerator.")
                    .defineInRange("particleAcceleratorSpeedRequiredToGenerateAntimatter", 1D, 0, Float.MAX_VALUE);
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

        public int getOreCopperSize() {
            return oreCopperSize.get();
        }

        public int getOreCopperRange() {
            return oreCopperRange.get();
        }

        public int getOreCopperCount() {
            return oreCopperCount.get();
        }

        public int getOreSulfurSize() {
            return oreSulfurSize.get();
        }

        public int getOreSulfurRange() {
            return oreSulfurRange.get();
        }

        public int getOreSulfurCount() {
            return oreSulfurCount.get();
        }

        public int getOreTinSize() {
            return oreTinSize.get();
        }

        public int getOreTinRange() {
            return oreTinRange.get();
        }

        public int getOreTinCount() {
            return oreTinCount.get();
        }

        public int getOreUraniumSize() {
            return oreUraniumSize.get();
        }

        public int getOreUraniumRange() {
            return oreUraniumRange.get();
        }

        public int getOreUraniumCount() {
            return oreUraniumCount.get();
        }

        public int getParticleAcceleratorEnergyUsagePerTick() {
            return particleAcceleratorEnergyUsagePerTick.get();
        }

        public double getParticleAcceleratorSpeedIncreasePerTick() {
            return particleAcceleratorSpeedIncreasePerTick.get();
        }

        public double getParticleAcceleratorSpeedPenaltyForCollision() {
            return particleAcceleratorSpeedPenaltyForCollision.get();
        }

        public double getParticleAcceleratorSpeedRequiredToGenerateAntimatter() {
            return particleAcceleratorSpeedRequiredToGenerateAntimatter.get();
        }

    }

    /**
     * Synchronized, stored in world file. Ewww.
     * CURRENTLY UNUSED - NEED TO UNCOMMENT REGISTRATION IN MOD CLASS CONSTRUCTOR BEFORE USE!
     */
    public static class Server {

        public final ForgeConfigSpec spec;

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

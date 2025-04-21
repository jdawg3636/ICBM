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
        private final ForgeConfigSpec.IntValue numWorkerThreadsPerNuclearBlast;

        private final ForgeConfigSpec.IntValue maxNumTicksAliveForLinearMissiles;

        private final ForgeConfigSpec.BooleanValue doLogMissilePathsHandheld;

        private final ForgeConfigSpec.BooleanValue enableEasterEggForRedcoats;

        private final ForgeConfigSpec.DoubleValue blastResistanceObsidian;

        private final ForgeConfigSpec.BooleanValue antimatterCanDestroyBedrock;
        private final ForgeConfigSpec.DoubleValue antimatterFuzzinessPercentage;
        private final ForgeConfigSpec.BooleanValue redmatterCanDestroyBedrock;

        private final ForgeConfigSpec.BooleanValue engineeredPathogenPerpetualForPlayers;
        private final ForgeConfigSpec.DoubleValue engineeredPathogenSpreadRadius;

        private final ForgeConfigSpec.IntValue blastDepthBreaching;
        private final ForgeConfigSpec.DoubleValue blastRadiusAntimatter;
        private final ForgeConfigSpec.DoubleValue blastRadiusContagion;
        private final ForgeConfigSpec.DoubleValue blastRadiusEMP;
        private final ForgeConfigSpec.DoubleValue blastRadiusEnder;
        private final ForgeConfigSpec.DoubleValue blastRadiusEndothermic;
        private final ForgeConfigSpec.DoubleValue blastRadiusExothermic;
        private final ForgeConfigSpec.DoubleValue blastRadiusHypersonic;
        private final ForgeConfigSpec.DoubleValue blastRadiusNuclear;
        private final ForgeConfigSpec.DoubleValue blastRadiusSonic;

        private final ForgeConfigSpec.IntValue oreCopperSize;
        private final ForgeConfigSpec.IntValue oreCopperRange;
        private final ForgeConfigSpec.IntValue oreCopperCount;
        private final ForgeConfigSpec.IntValue oreFluoriteSize;
        private final ForgeConfigSpec.IntValue oreFluoriteRange;
        private final ForgeConfigSpec.IntValue oreFluoriteCount;
        private final ForgeConfigSpec.IntValue oreSulfurSize;
        private final ForgeConfigSpec.IntValue oreSulfurRange;
        private final ForgeConfigSpec.IntValue oreSulfurCount;
        private final ForgeConfigSpec.IntValue oreTinSize;
        private final ForgeConfigSpec.IntValue oreTinRange;
        private final ForgeConfigSpec.IntValue oreTinCount;
        private final ForgeConfigSpec.IntValue oreUraniumSize;
        private final ForgeConfigSpec.IntValue oreUraniumRange;
        private final ForgeConfigSpec.IntValue oreUraniumCount;

        private final ForgeConfigSpec.BooleanValue generateOilLakes;

        private final ForgeConfigSpec.IntValue particleAcceleratorEnergyUsagePerTick;
        private final ForgeConfigSpec.DoubleValue particleAcceleratorSpeedIncreasePerTick;
        private final ForgeConfigSpec.DoubleValue particleAcceleratorSpeedPenaltyForCollision;
        private final ForgeConfigSpec.DoubleValue particleAcceleratorSpeedRequiredToGenerateAntimatter;

        private final ForgeConfigSpec.DoubleValue empTowerRangeMinimum;
        private final ForgeConfigSpec.DoubleValue empTowerRangeMaximum;
        private final ForgeConfigSpec.IntValue empTowerEnergyUsePerBlast;

        private final ForgeConfigSpec.IntValue coalGeneratorEnergyGenerationPerTick;

        private final ForgeConfigSpec.BooleanValue camouflageFlammable;

        public Common() {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            this.maxBlastManagerThreadCountPerLevel = builder
                    .comment("",
                            "Defines the maximum number of blast manager threads that can be simultaneously active per ",
                            "level/dimension. SETTING THIS VALUE TOO HIGH MAY CAUSE ENOUGH LAG TO COMPLETELY LOCK UP YOUR",
                            "COMPUTER. YOU HAVE BEEN WARNED!")
                    .defineInRange("maxBlastManagerThreadCountPerLevel", 4, 1, Integer.MAX_VALUE);
            this.numWorkerThreadsPerNuclearBlast = builder
                    .comment("",
                            "Defines the exact number of worker threads to be used for each nuclear blast. (The blast ",
                            "will have a single manager thread, which will then spawn these worker threads). SETTING THIS",
                            "VALUE TOO HIGH MAY CAUSE ENOUGH LAG TO COMPLETELY LOCK UP YOUR COMPUTER. YOU HAVE BEEN WARNED!")
                    .defineInRange("numWorkerThreadsPerNuclearBlast", 4, 1, Integer.MAX_VALUE);
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
            this.blastResistanceObsidian = builder
                    .comment("",
                            "Vanilla obsidian normally has a blast resistance of 1200 (virtually unbreakable). To balance",
                            "it, we reduce this value down to match the base tier of concrete (25.0 by default). For context,",
                            "end stone is 9.0, normal stone is 6.0, and dirt is 0.5. IF THIS VALUE IS SET TO A NEGATIVE",
                            "NUMBER, THEN THE MOD WILL LEAVE THE EXPLOSION RESISTANCE UNCHANGED (DEFER TO VANILLA OR OTHER",
                            "MODS)."
                            )
                    .defineInRange("blastResistanceObsidian", 25.0, -1.0, Float.MAX_VALUE);
            this.antimatterCanDestroyBedrock = builder
                    .comment("",
                            "Enables/disables the ability of Antimatter blasts to destroy normally indestructible blocks, such as bedrock.")
                    .define("antimatterCanDestroyBedrock", false);
            this.antimatterFuzzinessPercentage = builder
                    .comment("",
                            "Defines the percentage, as a decimal (0.0 = 0%, 1.0 = 100%), that a block within the \"fuzzy\"",
                            "section within the radius of an antimatter blast will be destroyed in the blast.")
                    .defineInRange("antimatterFuzzynessPercentage", 0.4D, 0D, 1D);
            this.redmatterCanDestroyBedrock = builder
                    .comment("",
                            "Enables/disables the ability of Red Matter blasts to destroy normally indestructible blocks, such as bedrock.")
                    .define("redmatterCanDestroyBedrock", false);
            this.engineeredPathogenPerpetualForPlayers = builder
                    .comment("",
                            "If enabled, players will never recover from The Engineered Pathogen unless they die or",
                            "consume an antidote.")
                    .define("engineeredPathogenPerpetualForPlayers", true);
            this.engineeredPathogenSpreadRadius = builder
                    .comment("",
                            "Entities which are within this radius of another entity which is infected with the Engineered Pathogen",
                            "will become infected with the engineered pathogen. Set to zero to disable pathogen spreading.")
                    .defineInRange("engineeredPathogenSpreadRadius", 10D, 0D, 100D);
            this.blastDepthBreaching = builder
                    .comment("",
                            "The breaching blast creates a string of explosions in the direction towards which it is",
                            "ignited. This value defines the maximum depth, in blocks, that said string can reach. The",
                            "actual depth for a given blast may be lower, depending on the blast resistance of the blocks",
                            "in its way.")
                    .defineInRange("blastDepthBreaching", 7, 1, Integer.MAX_VALUE);
            this.blastRadiusAntimatter = builder
                    .comment("",
                            "Defines the maximum radius of an Antimatter Blast.")
                    .defineInRange("blastRadiusAntimatter", 50D, 0D, Float.MAX_VALUE);
            this.blastRadiusContagion = builder
                    .comment("",
                            "Defines the maximum radius of a Contagion Blast.")
                    .defineInRange("blastRadiusContagion", 10D, 0D, Float.MAX_VALUE);
            this.blastRadiusEMP = builder
                    .comment("",
                            "Defines the maximum radius of an EMP Blast.")
                    .defineInRange("blastRadiusEMP", 20D, 0D, Float.MAX_VALUE);
            this.blastRadiusEnder = builder
                    .comment("",
                            "Defines the maximum radius of an Ender Blast.")
                    .defineInRange("blastRadiusEnder", 20D, 0D, Float.MAX_VALUE);
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
                            "Defines the maximum radius of a Hypersonic Blast.")
                    .defineInRange("blastRadiusHypersonic", 13D, 0D, Float.MAX_VALUE);
            this.blastRadiusNuclear = builder
                    .comment("",
                            "Defines the maximum radius of a Nuclear Blast.")
                    .defineInRange("blastRadiusNuclear", 30D, 0D, Float.MAX_VALUE);
            this.blastRadiusSonic = builder
                    .comment("",
                            "Defines the maximum radius of a Sonic Blast.")
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
            this.oreFluoriteSize = builder
                    .comment("",
                            "Defines the 'size' parameter of Fluorite Ore vein generation.")
                    .defineInRange("oreFluoriteSize", 12, 0, Integer.MAX_VALUE);
            this.oreFluoriteRange = builder
                    .comment("",
                            "Defines the 'range' parameter of Fluorite Ore vein generation.")
                    .defineInRange("oreFluoriteRange", 32, 0, Integer.MAX_VALUE);
            this.oreFluoriteCount = builder
                    .comment("",
                            "Defines the 'count' parameter of Fluorite Ore vein generation.")
                    .defineInRange("oreFluoriteCount", 6, 0, Integer.MAX_VALUE);
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
            this.generateOilLakes = builder
                    .comment("",
                            "Enables/disables the generation of Oil Lakes.")
                    .define("generateOilLakes", true);
            this.particleAcceleratorEnergyUsagePerTick = builder
                    .comment("",
                            "Defines the amount of Forge Energy (FE) consumed per tick by a particle accelerator while it is active.")
                    .defineInRange("particleAcceleratorEnergyUsagePerTick", 2_500_000, 0, Integer.MAX_VALUE);
            this.particleAcceleratorSpeedIncreasePerTick = builder
                    .comment("",
                            "Defines the speed increase in blocks per tick that is applied each tick to the particle in a particle accelerator.")
                    .defineInRange("particleAcceleratorSpeedIncreasePerTick", 0.003D, 0, Float.MAX_VALUE);
            this.particleAcceleratorSpeedPenaltyForCollision = builder
                    .comment("",
                            "Defines the speed penalty in percent that is incurred to a particle when it collides with the walls of a particle accelerator.")
                    .defineInRange("particleAcceleratorSpeedPenaltyForCollision", 0.05D, 0, 1D);
            this.particleAcceleratorSpeedRequiredToGenerateAntimatter = builder
                    .comment("",
                            "Defines the speed in blocks per tick that is required for a particle to be converted into antimatter in a particle accelerator.")
                    .defineInRange("particleAcceleratorSpeedRequiredToGenerateAntimatter", 0.885D, 0, Float.MAX_VALUE);
            this.empTowerRangeMinimum = builder
                    .comment("",
                            "Defines the minimum effective radius that a user may specify for an EMP Tower")
                    .defineInRange("empTowerRangeMinimum", 10.0D, 0, Double.MAX_VALUE);
            this.empTowerRangeMaximum = builder
                    .comment("",
                            "Defines the maximum effective radius that a user may specify for an EMP Tower")
                    .defineInRange("empTowerRangeMaximum", 150.0D, 0, Double.MAX_VALUE);
            this.empTowerEnergyUsePerBlast = builder
                    .comment("",
                            "Defines the amount of Forge Energy (FE) consumed per blast by an EMP Tower.")
                    .defineInRange("empTowerEnergyUsePerBlast", 1_000_000, 0, Integer.MAX_VALUE);
            this.coalGeneratorEnergyGenerationPerTick = builder
                    .comment("",
                            "Defines the amount of Forge Energy (FE) generated per tick by a coal generator while it is active.")
                    .defineInRange("coalGeneratorEnergyGenerationPerTick", 50, 0, Integer.MAX_VALUE);
            this.camouflageFlammable = builder
                    .comment("",
                            "Enables/disables camouflage being flammable. If enabled, fire behavior is identical to vanilla vines.")
                    .define("camouflageFlammable", true);
            this.spec = builder.build();
        }

        public int getMaxBlastManagerThreadCountPerLevel() {
            return maxBlastManagerThreadCountPerLevel.get();
        }

        public int getNumWorkerThreadsPerNuclearBlast() {
            return numWorkerThreadsPerNuclearBlast.get();
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

        public float getBlastResistanceObsidian() {
            return blastResistanceObsidian.get().floatValue();
        }

        public boolean getAntimatterCanDestroyBedrock() {
            return antimatterCanDestroyBedrock.get();
        }

        public double getAntimatterFuzzinessPercentage() {
            return antimatterFuzzinessPercentage.get();
        }

        public boolean getRedmatterCanDestroyBedrock() {
            return redmatterCanDestroyBedrock.get();
        }

        public boolean getEngineeredPathogenPerpetualForPlayers() {
            return  engineeredPathogenPerpetualForPlayers.get();
        }

        public double getEngineeredPathogenSpreadRadius() {
            return engineeredPathogenSpreadRadius.get();
        }

        public int getBlastDepthBreaching() {
            return blastDepthBreaching.get();
        }

        public double getBlastRadiusAntimatter() {
            return blastRadiusAntimatter.get();
        }

        public double getBlastRadiusContagion() {
            return blastRadiusContagion.get();
        }

        public double getBlastRadiusEMP() {
            return blastRadiusEMP.get();
        }

        public double getBlastRadiusEnder() {
            return blastRadiusEnder.get();
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

        public int getOreFluoriteSize() {
            return oreFluoriteSize.get();
        }

        public int getOreFluoriteRange() {
            return oreFluoriteRange.get();
        }

        public int getOreFluoriteCount() {
            return oreFluoriteCount.get();
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

        public boolean getGenerateOilLakes() {
            return generateOilLakes.get();
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

        public double getEMPTowerRangeMinimum() {
            return empTowerRangeMinimum.get();
        }

        public double getEMPTowerRangeMaximum() {
            return empTowerRangeMaximum.get();
        }

        public int empTowerEnergyUsePerBlast() {
            return empTowerEnergyUsePerBlast.get();
        }

        public int getCoalGeneratorEnergyGenerationPerTick() {
            return coalGeneratorEnergyGenerationPerTick.get();
        }

        public boolean getCamouflageFlammable() {
            return camouflageFlammable.get();
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

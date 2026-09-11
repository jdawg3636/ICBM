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

        private final ForgeConfigSpec.IntValue launcherSupportFrameInaccuracyT0;
        private final ForgeConfigSpec.IntValue launcherSupportFrameInaccuracyT1;
        private final ForgeConfigSpec.IntValue launcherSupportFrameInaccuracyT2;
        private final ForgeConfigSpec.IntValue launcherSupportFrameInaccuracyT3;

        private final ForgeConfigSpec.BooleanValue doLogMissilePathsHandheld;

        private final ForgeConfigSpec.BooleanValue enableEasterEggForRedcoats;

        private final ForgeConfigSpec.DoubleValue blastResistanceObsidian;
        private final ForgeConfigSpec.DoubleValue blastResistanceConcreteBasic;
        private final ForgeConfigSpec.DoubleValue blastResistanceConcreteCompact;
        private final ForgeConfigSpec.DoubleValue blastResistanceConcreteReinforced;

        private final ForgeConfigSpec.IntValue antigravitationalBlastLingerDuration;
        private final ForgeConfigSpec.BooleanValue antimatterCanDestroyBedrock;
        private final ForgeConfigSpec.DoubleValue antimatterFuzzinessPercentage;
        private final ForgeConfigSpec.IntValue chemicalBlastLingerDuration;
        private final ForgeConfigSpec.IntValue contagionBlastLingerDuration;
        private final ForgeConfigSpec.IntValue debilitationBlastLingerDuration;
        private final ForgeConfigSpec.IntValue enderBlastLingerDuration;
        private final ForgeConfigSpec.IntValue nuclearBlastRadiationLingerDuration;
        private final ForgeConfigSpec.BooleanValue redmatterCanDestroyBedrock;
        private final ForgeConfigSpec.DoubleValue redmatterFuzzinessPercentage;
        private final ForgeConfigSpec.IntValue redmatterMaxBlocksDestroyedPerTick;
        private final ForgeConfigSpec.IntValue redmatterNumTicksLingerAfterCompletion;
        private final ForgeConfigSpec.DoubleValue rejuvenationFuzzinessPercentage;

        private final ForgeConfigSpec.BooleanValue engineeredPathogenPerpetualForPlayers;
        private final ForgeConfigSpec.DoubleValue engineeredPathogenSpreadRadius;

        private final ForgeConfigSpec.IntValue blastDepthBreaching;
        private final ForgeConfigSpec.DoubleValue blastRadiusAntigravitational;
        private final ForgeConfigSpec.DoubleValue blastRadiusAntimatter;
        private final ForgeConfigSpec.DoubleValue blastRadiusAnvil;
        private final ForgeConfigSpec.DoubleValue blastRadiusAttractive;
        private final ForgeConfigSpec.DoubleValue blastRadiusCondensed;
        private final ForgeConfigSpec.DoubleValue blastRadiusContagion;
        private final ForgeConfigSpec.DoubleValue blastRadiusEMP;
        private final ForgeConfigSpec.DoubleValue blastRadiusEnder;
        private final ForgeConfigSpec.DoubleValue blastRadiusEndothermic;
        private final ForgeConfigSpec.DoubleValue blastRadiusExothermic;
        private final ForgeConfigSpec.DoubleValue blastRadiusHypersonic;
        private final ForgeConfigSpec.DoubleValue blastRadiusIncendiary;
        private final ForgeConfigSpec.DoubleValue blastRadiusNightmare;
        private final ForgeConfigSpec.DoubleValue blastRadiusNuclear;
        private final ForgeConfigSpec.DoubleValue blastRadiusRedmatter;
        private final ForgeConfigSpec.DoubleValue blastRadiusRejuvenation;
        private final ForgeConfigSpec.DoubleValue blastRadiusRepulsive;
        private final ForgeConfigSpec.DoubleValue blastRadiusShrapnelImpact;
        private final ForgeConfigSpec.DoubleValue blastRadiusSMine;
        private final ForgeConfigSpec.DoubleValue blastRadiusSonic;
        private final ForgeConfigSpec.DoubleValue blastRadiusThermobaric;

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

        private final ForgeConfigSpec.DoubleValue proximityDetectorRadiusMinimum;
        private final ForgeConfigSpec.DoubleValue proximityDetectorRadiusMaximum;

        private final ForgeConfigSpec.IntValue metallurgicLeachingChamberEnergyUsagePerTick;
        private final ForgeConfigSpec.IntValue centrifugalDepositionUnitEnergyUsagePerTick;
        private final ForgeConfigSpec.IntValue oilRefineryEnergyUsagePerTick;
        private final ForgeConfigSpec.IntValue thermochemicalMixerEnergyUsagePerTick;

        private final ForgeConfigSpec.DoubleValue empTowerRangeMinimum;
        private final ForgeConfigSpec.DoubleValue empTowerRangeMaximum;
        private final ForgeConfigSpec.IntValue empTowerEnergyUsePerBlast;

        private final ForgeConfigSpec.DoubleValue radarStationRangeMinimum;
        private final ForgeConfigSpec.DoubleValue radarStationRangeMaximum;

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
            this.launcherSupportFrameInaccuracyT0 = builder
                    .comment("",
                            "Defines the potential inaccuracy, in blocks, of a missile launched without using a Support Frame.")
                    .defineInRange("launcherSupportFrameInaccuracyT0", 30, 0, Integer.MAX_VALUE);
            this.launcherSupportFrameInaccuracyT1 = builder
                    .comment("",
                            "Defines the potential inaccuracy, in blocks, of a missile launched using a T1 Support Frame.")
                    .defineInRange("launcherSupportFrameInaccuracyT1", 15, 0, Integer.MAX_VALUE);
            this.launcherSupportFrameInaccuracyT2 = builder
                    .comment("",
                            "Defines the potential inaccuracy, in blocks, of a missile launched using a T2 Support Frame.")
                    .defineInRange("launcherSupportFrameInaccuracyT2", 7, 0, Integer.MAX_VALUE);
            this.launcherSupportFrameInaccuracyT3 = builder
                    .comment("",
                            "Defines the potential inaccuracy, in blocks, of a missile launched using a T3 Support Frame.")
                    .defineInRange("launcherSupportFrameInaccuracyT3", 0, 0, Integer.MAX_VALUE);
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
            this.blastResistanceConcreteBasic = builder
                    .comment("",
                            "Blast/explosion resistance of Basic Concrete. For context, end stone is 9.0, normal stone is 6.0, and dirt is 0.5.")
                    .defineInRange("blastResistanceConcreteBasic", 25.0, 0.0, Float.MAX_VALUE);
            this.blastResistanceConcreteCompact = builder
                    .comment("",
                            "Blast/explosion resistance of Compact Concrete. For context, end stone is 9.0, normal stone is 6.0, and dirt is 0.5.")
                    .defineInRange("blastResistanceConcreteCompact", 50.0, 0.0, Float.MAX_VALUE);
            this.blastResistanceConcreteReinforced = builder
                    .comment("",
                            "Blast/explosion resistance of Reinforced Concrete. For context, end stone is 9.0, normal stone is 6.0, and dirt is 0.5.")
                    .defineInRange("blastResistanceConcreteReinforced", 75.0, 0.0, Float.MAX_VALUE);
            this.antigravitationalBlastLingerDuration = builder
                    .comment("",
                            "Duration in ticks of the antigravitational blast effect after detonation. 20 ticks = 1 second.")
                    .defineInRange("antigravitationalBlastLingerDuration", 500, 0, Integer.MAX_VALUE);
            this.antimatterCanDestroyBedrock = builder
                    .comment("",
                            "Enables/disables the ability of Antimatter blasts to destroy normally indestructible blocks, such as bedrock.")
                    .define("antimatterCanDestroyBedrock", false);
            this.antimatterFuzzinessPercentage = builder
                    .comment("",
                            "Defines the percentage chance, as a decimal (0.0 = 0%, 1.0 = 100%), that a block within the \"fuzzy\"",
                            "section within the radius of an antimatter blast will be destroyed in the blast.")
                    .defineInRange("antimatterFuzzynessPercentage", 0.4D, 0D, 1D);
            this.chemicalBlastLingerDuration = builder
                    .comment("",
                            "Duration in ticks that the chemical blast lingers after detonation. 20 ticks = 1 second.")
                    .defineInRange("chemicalBlastLingerDuration", 100, 0, Integer.MAX_VALUE);
            this.contagionBlastLingerDuration = builder
                    .comment("",
                            "Duration in ticks that the contagion blast lingers after detonation. 20 ticks = 1 second.")
                    .defineInRange("contagionBlastLingerDuration", 400, 0, Integer.MAX_VALUE);
            this.debilitationBlastLingerDuration = builder
                    .comment("",
                            "Duration in ticks that the debilitation blast lingers after detonation. 20 ticks = 1 second.")
                    .defineInRange("debilitationBlastLingerDuration", 400, 0, Integer.MAX_VALUE);
            this.enderBlastLingerDuration = builder
                    .comment("",
                            "Duration in ticks that the ender blast lingers after detonation. 20 ticks = 1 second.")
                    .defineInRange("enderBlastLingerDuration", 200, 0, Integer.MAX_VALUE);
            this.nuclearBlastRadiationLingerDuration = builder
                    .comment("",
                            "Duration in ticks that the radiation lingers after a nuclear blast. 20 ticks = 1 second.")
                    .defineInRange("nuclearBlastRadiationLingerDuration", 6000, 0, Integer.MAX_VALUE);
            this.redmatterCanDestroyBedrock = builder
                    .comment("",
                            "Enables/disables the ability of Red Matter blasts to destroy normally indestructible blocks, such as bedrock.")
                    .define("redmatterCanDestroyBedrock", false);
            this.redmatterFuzzinessPercentage = builder
                    .comment("",
                            "Defines the percentage chance, as a decimal (0.0 = 0%, 1.0 = 100%), that a block within the \"fuzzy\"",
                            "section within the radius of a redmatter blast will be destroyed in the blast.")
                    .defineInRange("redmatterFuzzinessPercentage", 0.4D, 0D, 1D);
            this.redmatterMaxBlocksDestroyedPerTick = builder
                    .comment("",
                            "Defines the maximum number of blocks that a redmatter blast (black hole) can destroy per tick. 20 ticks = 1 second.")
                    .defineInRange("redmatterMaxBlocksDestroyedPerTick", 50, 0, Integer.MAX_VALUE);
            this.redmatterNumTicksLingerAfterCompletion = builder
                    .comment("",
                            "Defines the number of ticks (20 ticks = 1 second) that a black hole will survive after it is no longer",
                            "able to find at least redmatterMaxBlocksDestroyedPerTick non-fluid blocks within its radius.",
                            "Note: increasing this can have a significant performance penalty. Since the redmatterMaxBlocksDestroyedPerTick",
                            "threshold is not being met, the black hole will re-search the entire blast area once every second looking",
                            "for new blocks to destroy. This is expensive and WILL impact server TPS.")
                    .defineInRange("redmatterNumTicksLingerAfterCompletion", 400, 0, Integer.MAX_VALUE);
            this.rejuvenationFuzzinessPercentage = builder
                    .comment("",
                            "Defines the percentage chance, as a decimal (0.0 = 0%, 1.0 = 100%), that a block within the \"fuzzy\"",
                            "section within the radius of an rejuvenation blast will be rejuvenated in the blast.")
                    .defineInRange("rejuvenationFuzzinessPercentage", 0.4D, 0D, 1D);
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
            this.blastRadiusAntigravitational = builder
                    .comment("",
                            "Defines the maximum radius of an Antimatter Blast.")
                    .defineInRange("blastRadiusAntigravitational", 20D, 0D, Float.MAX_VALUE);
            this.blastRadiusAntimatter = builder
                    .comment("",
                            "Defines the maximum radius of an Antimatter Blast.")
                    .defineInRange("blastRadiusAntimatter", 50D, 0D, Float.MAX_VALUE);
            this.blastRadiusAnvil = builder
                    .comment("",
                            "Defines the approximate radius of an Anvil Blast. The actual radius will usually be slightly higher.")
                    .defineInRange("blastRadiusAnvil", 15D, 0D, Float.MAX_VALUE);
            this.blastRadiusAttractive = builder
                    .comment("",
                            "Defines the maximum radius of an Attractive Blast.")
                    .defineInRange("blastRadiusAttractive", 10D, 0D, Float.MAX_VALUE);
            this.blastRadiusCondensed = builder
                    .comment("",
                            "Defines the maximum radius of a Condensed/Conventional Blast.")
                    .defineInRange("blastRadiusCondensed", 7D, 0D, Float.MAX_VALUE);
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
            this.blastRadiusIncendiary = builder
                    .comment("",
                            "Defines the maximum radius of an Incendiary Blast.")
                    .defineInRange("blastRadiusIncendiary", 14D, 0D, Float.MAX_VALUE);
            this.blastRadiusNightmare = builder
                    .comment("",
                            "Defines the maximum radius of a Nightmare Blast.")
                    .defineInRange("blastRadiusNightmare", 50D, 0D, Float.MAX_VALUE);
            this.blastRadiusNuclear = builder
                    .comment("",
                            "Defines the maximum radius of a Nuclear Blast.")
                    .defineInRange("blastRadiusNuclear", 30D, 0D, Float.MAX_VALUE);
            this.blastRadiusRedmatter = builder
                    .comment("",
                            "Defines the maximum radius of a Red Matter Blast.")
                    .defineInRange("blastRadiusRedmatter", 50D, 8, Float.MAX_VALUE);
            this.blastRadiusRejuvenation = builder
                    .comment("",
                            "Defines the maximum radius of a Rejuvenation Blast.")
                    .defineInRange("blastRadiusRejuvenation", 30D, 0D, Float.MAX_VALUE);
            this.blastRadiusRepulsive = builder
                    .comment("",
                            "Defines the maximum radius of a Repulsive Blast.")
                    .defineInRange("blastRadiusRepulsive", 10D, 0D, Float.MAX_VALUE);
            this.blastRadiusShrapnelImpact = builder
                    .comment("",
                            "Defines the maximum radius of a the blast resulting from a shrapnel impact. Note that this",
                            "is the radius of the explosion produced by each individual piece of shrapnel, not",
                            "the radius over which the Shrapnel Blast distributes the shrapnel to begin with.")
                    .defineInRange("blastRadiusShrapnelImpact", 2D, 0D, Float.MAX_VALUE);
            this.blastRadiusSMine = builder
                    .comment("",
                            "Defines the maximum radius of an S-Mine.")
                    .defineInRange("blastRadiusSMine", 7D, 0D, Float.MAX_VALUE);
            this.blastRadiusSonic = builder
                    .comment("",
                            "Defines the maximum radius of a Sonic Blast.")
                    .defineInRange("blastRadiusSonic", 9D, 0D, Float.MAX_VALUE);
            this.blastRadiusThermobaric = builder
                    .comment("",
                            "Defines the maximum radius of a Thermobaric Blast.")
                    .defineInRange("blastRadiusThermobaric", 16D, 0D, Float.MAX_VALUE);
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
                    .defineInRange("particleAcceleratorSpeedIncreasePerTick", 0.0005D, 0, Float.MAX_VALUE);
            this.particleAcceleratorSpeedPenaltyForCollision = builder
                    .comment("",
                            "Defines the speed penalty in percent that is incurred to a particle when it collides with the walls of a particle accelerator.")
                    .defineInRange("particleAcceleratorSpeedPenaltyForCollision", 0.01D, 0, 1D);
            this.particleAcceleratorSpeedRequiredToGenerateAntimatter = builder
                    .comment("",
                            "Defines the speed in blocks per tick that is required for a particle to be converted into antimatter in a particle accelerator.")
                    .defineInRange("particleAcceleratorSpeedRequiredToGenerateAntimatter", 1.5D, 0, Float.MAX_VALUE);
            this.proximityDetectorRadiusMinimum = builder
                    .comment("",
                            "Defines the minimum detection radius that a user may specify for a Proximity Detector")
                    .defineInRange("proximityDetectorRadiusMinimum", 0.0D, 0, Double.MAX_VALUE);
            this.proximityDetectorRadiusMaximum = builder
                    .comment("",
                            "Defines the maximum detection radius that a user may specify for a Proximity Detector")
                    .defineInRange("proximityDetectorRadiusMaximum", 30.0D, 0, Double.MAX_VALUE);
            this.metallurgicLeachingChamberEnergyUsagePerTick = builder
                    .comment("",
                            "Defines the amount of Forge Energy (FE) consumed per tick by a metallurgic leaching chamber while it is active.")
                    .defineInRange("metallurgicLeachingChamberEnergyUsagePerTick", 30, 0, Integer.MAX_VALUE);
            this.centrifugalDepositionUnitEnergyUsagePerTick = builder
                    .comment("",
                            "Defines the amount of Forge Energy (FE) consumed per tick by a centrifugal deposition unit while it is active.")
                    .defineInRange("centrifugalDepositionUnitEnergyUsagePerTick", 30, 0, Integer.MAX_VALUE);
            this.oilRefineryEnergyUsagePerTick = builder
                    .comment("",
                            "Defines the amount of Forge Energy (FE) consumed per tick by an oil refinery while it is active.")
                    .defineInRange("oilRefineryEnergyUsagePerTick", 30, 0, Integer.MAX_VALUE);
            this.thermochemicalMixerEnergyUsagePerTick = builder
                    .comment("",
                            "Defines the amount of Forge Energy (FE) consumed per tick by a thermochemical mixer while it is active.")
                    .defineInRange("thermochemicalMixerEnergyUsagePerTick", 30, 0, Integer.MAX_VALUE);
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
            this.radarStationRangeMinimum = builder
                    .comment("",
                            "Defines the minimum effective radius that a user may specify for an Radar Station")
                    .defineInRange("radarStationRangeMinimum", 10.0D, 0, Double.MAX_VALUE);
            this.radarStationRangeMaximum = builder
                    .comment("",
                            "Defines the maximum effective radius that a user may specify for an Radar Station")
                    .defineInRange("radarStationRangeMaximum", 150.0D, 0, Double.MAX_VALUE);
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

        public int getMissileInaccuracyGivenSupportFrameTier(int tier) {
            switch(tier) {
                case 3:
                    return launcherSupportFrameInaccuracyT3.get();
                case 2:
                    return launcherSupportFrameInaccuracyT2.get();
                case 1:
                    return launcherSupportFrameInaccuracyT1.get();
                default:
                    return launcherSupportFrameInaccuracyT0.get();
            }
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

        public float getBlastResistanceConcreteBasic() {
            return blastResistanceConcreteBasic.get().floatValue();
        }

        public float getBlastResistanceConcreteCompact() {
            return blastResistanceConcreteCompact.get().floatValue();
        }

        public float getBlastResistanceConcreteReinforced() {
            return blastResistanceConcreteReinforced.get().floatValue();
        }

        public int getAntigravitationalBlastLingerDuration() {
            return antigravitationalBlastLingerDuration.get();
        }

        public boolean getAntimatterCanDestroyBedrock() {
            return antimatterCanDestroyBedrock.get();
        }

        public double getAntimatterFuzzinessPercentage() {
            return antimatterFuzzinessPercentage.get();
        }

        public int getChemicalBlastLingerDuration() {
            return chemicalBlastLingerDuration.get();
        }

        public int getContagionBlastLingerDuration() {
            return contagionBlastLingerDuration.get();
        }

        public int getDebilitationBlastLingerDuration() {
            return debilitationBlastLingerDuration.get();
        }

        public int getEnderBlastLingerDuration() {
            return enderBlastLingerDuration.get();
        }

        public int getNuclearBlastRadiationLingerDuration() {
            return nuclearBlastRadiationLingerDuration.get();
        }

        public boolean getRedmatterCanDestroyBedrock() {
            return redmatterCanDestroyBedrock.get();
        }

        public double getRedmatterFuzzinessPercentage() {
            return redmatterFuzzinessPercentage.get();
        }

        public int getRedmatterMaxBlocksDestroyedPerTick() {
            return redmatterMaxBlocksDestroyedPerTick.get();
        }

        public int getRedmatterNumTicksLingerAfterCompletion() {
            return redmatterNumTicksLingerAfterCompletion.get();
        }

        public double getRejuvenationFuzzinessPercentage() {
            return rejuvenationFuzzinessPercentage.get();
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

        public double getBlastRadiusAntigravitational() {
            return blastRadiusAntigravitational.get();
        }

        public double getBlastRadiusAntimatter() {
            return blastRadiusAntimatter.get();
        }

        public double getBlastRadiusAnvil() {
            return blastRadiusAnvil.get();
        }

        public double getBlastRadiusAttractive() {
            return blastRadiusAttractive.get();
        }

        public double getBlastRadiusCondensed() {
            return blastRadiusCondensed.get();
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

        public double getBlastRadiusIncendiary() {
            return blastRadiusIncendiary.get();
        }

        public double getBlastRadiusNightmare() {
            return blastRadiusNightmare.get();
        }

        public double getBlastRadiusNuclear() {
            return blastRadiusNuclear.get();
        }

        public double getBlastRadiusRedmatter() {
            return blastRadiusRedmatter.get();
        }

        public double getBlastRadiusRejuvenation() {
            return blastRadiusRejuvenation.get();
        }

        public double getBlastRadiusRepulsive() {
            return blastRadiusRepulsive.get();
        }

        public double getBlastRadiusShrapnelImpact() {
            return blastRadiusShrapnelImpact.get();
        }

        public double getBlastRadiusSMine() {
            return blastRadiusSMine.get();
        }

        public double getBlastRadiusSonic() {
            return blastRadiusSonic.get();
        }

        public double getBlastRadiusThermobaric() {
            return blastRadiusThermobaric.get();
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

        public double getProximityDetectorRadiusMinimum() {
            return proximityDetectorRadiusMinimum.get();
        }
        public double getProximityDetectorRadiusMaximum() {
            return proximityDetectorRadiusMaximum.get();
        }

        public int getMetallurgicLeachingChamberEnergyUsagePerTick() {
            return metallurgicLeachingChamberEnergyUsagePerTick.get();
        }

        public int getCentrifugalDepositionUnitEnergyUsagePerTick() {
            return centrifugalDepositionUnitEnergyUsagePerTick.get();
        }

        public int getOilRefineryEnergyUsagePerTick() {
            return oilRefineryEnergyUsagePerTick.get();
        }

        public int getThermochemicalMixerEnergyUsagePerTick() {
            return thermochemicalMixerEnergyUsagePerTick.get();
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

        public double getRadarStationRangeMinimum() {
            return radarStationRangeMinimum.get();
        }

        public double getRadarStationRangeMaximum() {
            return radarStationRangeMaximum.get();
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

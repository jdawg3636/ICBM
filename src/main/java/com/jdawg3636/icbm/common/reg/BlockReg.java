package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.common.blocks.*;
import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.blocks.launcher_platform.BlockLauncherPlatform;
import com.jdawg3636.icbm.common.blocks.launcher_platform.ContainerLauncherPlatform;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class BlockReg {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ICBMReference.MODID);

    // Basic Blocks
    public static final RegistryObject<Block> CONCRETE                      = BLOCKS.register("concrete",                       () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(10F, 28)));
    public static final RegistryObject<Block> CONCRETE_COMPACT              = BLOCKS.register("concrete_compact",               () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(10F, 280)));
    public static final RegistryObject<Block> CONCRETE_REINFORCED           = BLOCKS.register("concrete_reinforced",            () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(10F, 2800)));
    public static final RegistryObject<Block> RADIOACTIVE_MATERIAL          = BLOCKS.register("radioactive_material",           () -> new GrassBlock(AbstractBlock.Properties.create(Material.ORGANIC).tickRandomly().hardnessAndResistance(0.6F).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> REINFORCED_GLASS              = BLOCKS.register("reinforced_glass",               BlockReinforcedGlass::new);

    // Ores
    public static final RegistryObject<Block> ORE_COPPER                    = BLOCKS.register("ore_copper",                     () -> new OreBlock(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> ORE_SULFUR                    = BLOCKS.register("ore_sulfur",                     () -> new OreBlock(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> ORE_TIN                       = BLOCKS.register("ore_tin",                        () -> new OreBlock(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> ORE_URANIUM                   = BLOCKS.register("ore_uranium",                    () -> new OreBlock(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(3.0F, 3.0F)) {
        @OnlyIn(Dist.CLIENT)
        public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
            super.animateTick(stateIn, worldIn, pos, rand);
            // TODO Radiation Particles
        }
    });

    // Explosives
    public static final RegistryObject<Block> EXPLOSIVES_CONDENSED          = BLOCKS.register("explosives_condensed",           () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_SHRAPNEL           = BLOCKS.register("explosives_shrapnel",            () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_INCENDIARY         = BLOCKS.register("explosives_incendiary",          BlockExplosivesIncendiary::new);
    public static final RegistryObject<Block> EXPLOSIVES_DEBILITATION       = BLOCKS.register("explosives_debilitation",        () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_CHEMICAL           = BLOCKS.register("explosives_chemical",            () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_ANVIL              = BLOCKS.register("explosives_anvil",               () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_REPULSIVE          = BLOCKS.register("explosives_repulsive",           () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_ATTRACTIVE         = BLOCKS.register("explosives_attractive",          () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_NIGHTMARE          = BLOCKS.register("explosives_nightmare",           () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_FRAGMENTATION      = BLOCKS.register("explosives_fragmentation",       () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_CONTAGIOUS         = BLOCKS.register("explosives_contagious",          () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_SONIC              = BLOCKS.register("explosives_sonic",               () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_BREACHING          = BLOCKS.register("explosives_breaching",           () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_REJUVENATION       = BLOCKS.register("explosives_rejuvenation",        () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_THERMOBARIC        = BLOCKS.register("explosives_thermobaric",         () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_NUCLEAR            = BLOCKS.register("explosives_nuclear",             () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_EMP                = BLOCKS.register("explosives_emp",                 () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_EXOTHERMIC         = BLOCKS.register("explosives_exothermic",          () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_ENDOTHERMIC        = BLOCKS.register("explosives_endothermic",         () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_ANTIGRAVITATIONAL  = BLOCKS.register("explosives_antigravitational",   () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_ENDER              = BLOCKS.register("explosives_ender",               () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_HYPERSONIC         = BLOCKS.register("explosives_hypersonic",          () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_ANTIMATTER         = BLOCKS.register("explosives_antimatter",          () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> EXPLOSIVES_REDMATTER          = BLOCKS.register("explosives_redmatter",           () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));

    // Unconventional Explosives
    public static final RegistryObject<Block> S_MINE                        = BLOCKS.register("s_mine",                         () -> new TNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT)));

    // Spikes
    public static final RegistryObject<Block> SPIKES                        = BLOCKS.register("spikes",                         BlockSpikes::new);
    public static final RegistryObject<Block> SPIKES_POISON                 = BLOCKS.register("spikes_poison",                  () -> new BlockSpikes() {
        @Override
        public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
            super.onEntityCollision(state, world, pos, entity);
            if (entity instanceof LivingEntity)
                ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.POISON, 7 * 20, 0));
        }
    });
    public static final RegistryObject<Block> SPIKES_FIRE                   = BLOCKS.register("spikes_fire",                    () -> new BlockSpikes() {
        @Override
        public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
            super.onEntityCollision(state, world, pos, entity);
            if (entity instanceof LivingEntity && !state.get(BlockSpikes.WATERLOGGED))
                entity.setFire(7);
        }
    });

    // Missile Launch Apparatus
    public static final RegistryObject<Block> LAUNCHER_PLATFORM_T1          = BLOCKS.register("launcher_platform_t1",           () -> new BlockLauncherPlatform(TileReg.LAUNCHER_PLATFORM_T1) {
        @Override
        public ContainerType<ContainerLauncherPlatform> getContainerType() {
            return ContainerReg.LAUNCHER_PLATFORM_T1.get();
        }
    });
    public static final RegistryObject<Block> LAUNCHER_PLATFORM_T2          = BLOCKS.register("launcher_platform_t2",           () -> new BlockLauncherPlatform(TileReg.LAUNCHER_PLATFORM_T2) {
        @Override
        public ContainerType<ContainerLauncherPlatform> getContainerType() {
            return ContainerReg.LAUNCHER_PLATFORM_T2.get();
        }
    });
    public static final RegistryObject<Block> LAUNCHER_PLATFORM_T3          = BLOCKS.register("launcher_platform_t3",           () -> new BlockLauncherPlatform(TileReg.LAUNCHER_PLATFORM_T3) {
        @Override
        public ContainerType<ContainerLauncherPlatform> getContainerType() {
            return ContainerReg.LAUNCHER_PLATFORM_T3.get();
        }
    });
    public static final RegistryObject<Block> LAUNCHER_CONTROL_PANEL_T1     = BLOCKS.register("launcher_control_panel_t1",      () -> new Block(Block.Properties.create(Material.IRON)));
    public static final RegistryObject<Block> LAUNCHER_CONTROL_PANEL_T2     = BLOCKS.register("launcher_control_panel_t2",      () -> new Block(Block.Properties.create(Material.IRON)));
    public static final RegistryObject<Block> LAUNCHER_CONTROL_PANEL_T3     = BLOCKS.register("launcher_control_panel_t3",      () -> new Block(Block.Properties.create(Material.IRON)));
    public static final RegistryObject<Block> LAUNCHER_SUPPORT_FRAME_T1     = BLOCKS.register("launcher_support_frame_t1",      BlockLauncherSupportFrame::new);
    public static final RegistryObject<Block> LAUNCHER_SUPPORT_FRAME_T2     = BLOCKS.register("launcher_support_frame_t2",      BlockLauncherSupportFrame::new);
    public static final RegistryObject<Block> LAUNCHER_SUPPORT_FRAME_T3     = BLOCKS.register("launcher_support_frame_t3",      BlockLauncherSupportFrame::new);

    // Other Machinery
    public static final RegistryObject<Block> CRUISE_LAUNCHER               = BLOCKS.register("cruise_launcher",                () -> new Block(Block.Properties.create(Material.IRON)));
    public static final RegistryObject<Block> EMP_TOWER                     = BLOCKS.register("emp_tower",                      BlockEMPTower::new);
    public static final RegistryObject<Block> RADAR_STATION                 = BLOCKS.register("radar_station",                  BlockRadarStation::new);

    // Extras
    public static final RegistryObject<Block> GLASS_BUTTON                  = BLOCKS.register("glass_button",                   BlockGlassButton::new);
    public static final RegistryObject<Block> GLASS_PRESSURE_PLATE          = BLOCKS.register("glass_pressure_plate",           BlockGlassPressurePlate::new);

}

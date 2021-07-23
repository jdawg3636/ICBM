package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.common.block.*;
import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.cruise_launcher.BlockCruiseLauncher;
import com.jdawg3636.icbm.common.block.emp_tower.BlockEMPTower;
import com.jdawg3636.icbm.common.block.launcher_control_panel.BlockLauncherControlPanel;
import com.jdawg3636.icbm.common.block.launcher_platform.BlockLauncherPlatform;
import com.jdawg3636.icbm.common.block.launcher_platform.ContainerLauncherPlatform;
import com.jdawg3636.icbm.common.event.BlastEvent;
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
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class BlockReg {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ICBMReference.MODID);

    // Basic Blocks
    public static final RegistryObject<Block> CONCRETE                      = BLOCKS.register("concrete",                       () -> new Block(Block.Properties.of(Material.STONE).strength(3.8F, 0028F)));
    public static final RegistryObject<Block> CONCRETE_COMPACT              = BLOCKS.register("concrete_compact",               () -> new Block(Block.Properties.of(Material.STONE).strength(3.8F, 0280F)));
    public static final RegistryObject<Block> CONCRETE_REINFORCED           = BLOCKS.register("concrete_reinforced",            () -> new Block(Block.Properties.of(Material.STONE).strength(3.8F, 2800F)));
    public static final RegistryObject<Block> RADIOACTIVE_MATERIAL          = BLOCKS.register("radioactive_material",           () -> new GrassBlock(AbstractBlock.Properties.of(Material.GRASS).randomTicks().harvestTool(ToolType.SHOVEL).strength(0.6F, 6F).sound(SoundType.GRASS)));
    public static final RegistryObject<Block> REINFORCED_GLASS              = BLOCKS.register("reinforced_glass",               BlockReinforcedGlass::new);

    // Ores
    public static final RegistryObject<Block> ORE_COPPER                    = BLOCKS.register("ore_copper",                     () -> new OreBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistryObject<Block> ORE_SULFUR                    = BLOCKS.register("ore_sulfur",                     () -> new OreBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistryObject<Block> ORE_TIN                       = BLOCKS.register("ore_tin",                        () -> new OreBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistryObject<Block> ORE_URANIUM                   = BLOCKS.register("ore_uranium",                    () -> new OreBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F).randomTicks()) {
        @OnlyIn(Dist.CLIENT)
        public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
            super.animateTick(stateIn, worldIn, pos, rand);
            // TODO Radiation Particles
        }
    });

    // Explosives
    public static final RegistryObject<Block> EXPLOSIVES_CONDENSED          = BLOCKS.register("explosives_condensed",           () -> new BlockExplosives(EntityReg.EXPLOSIVES_CONDENSED,           BlastEvent.Condensed::new,          ItemReg.EXPLOSIVES_CONDENSED));
    public static final RegistryObject<Block> EXPLOSIVES_SHRAPNEL           = BLOCKS.register("explosives_shrapnel",            () -> new BlockExplosives(EntityReg.EXPLOSIVES_SHRAPNEL,            BlastEvent.Shrapnel::new,           ItemReg.EXPLOSIVES_SHRAPNEL));
    public static final RegistryObject<Block> EXPLOSIVES_INCENDIARY         = BLOCKS.register("explosives_incendiary",          () -> new BlockExplosives(EntityReg.EXPLOSIVES_INCENDIARY,          BlastEvent.Incendiary::new,         ItemReg.EXPLOSIVES_INCENDIARY));
    public static final RegistryObject<Block> EXPLOSIVES_DEBILITATION       = BLOCKS.register("explosives_debilitation",        () -> new BlockExplosives(EntityReg.EXPLOSIVES_DEBILITATION,        BlastEvent.Debilitation::new,       ItemReg.EXPLOSIVES_DEBILITATION));
    public static final RegistryObject<Block> EXPLOSIVES_CHEMICAL           = BLOCKS.register("explosives_chemical",            () -> new BlockExplosives(EntityReg.EXPLOSIVES_CHEMICAL,            BlastEvent.Chemical::new,           ItemReg.EXPLOSIVES_CHEMICAL));
    public static final RegistryObject<Block> EXPLOSIVES_ANVIL              = BLOCKS.register("explosives_anvil",               () -> new BlockExplosives(EntityReg.EXPLOSIVES_ANVIL,               BlastEvent.Anvil::new,              ItemReg.EXPLOSIVES_ANVIL));
    public static final RegistryObject<Block> EXPLOSIVES_REPULSIVE          = BLOCKS.register("explosives_repulsive",           () -> new BlockExplosives(EntityReg.EXPLOSIVES_REPULSIVE,           BlastEvent.Repulsive::new,          ItemReg.EXPLOSIVES_REPULSIVE));
    public static final RegistryObject<Block> EXPLOSIVES_ATTRACTIVE         = BLOCKS.register("explosives_attractive",          () -> new BlockExplosives(EntityReg.EXPLOSIVES_ATTRACTIVE,          BlastEvent.Attractive::new,         ItemReg.EXPLOSIVES_ATTRACTIVE));
    public static final RegistryObject<Block> EXPLOSIVES_NIGHTMARE          = BLOCKS.register("explosives_nightmare",           () -> new BlockExplosives(EntityReg.EXPLOSIVES_NIGHTMARE,           BlastEvent.Nightmare::new,          ItemReg.EXPLOSIVES_NIGHTMARE));
    public static final RegistryObject<Block> EXPLOSIVES_FRAGMENTATION      = BLOCKS.register("explosives_fragmentation",       () -> new BlockExplosives(EntityReg.EXPLOSIVES_FRAGMENTATION,       BlastEvent.Fragmentation::new,      ItemReg.EXPLOSIVES_FRAGMENTATION));
    public static final RegistryObject<Block> EXPLOSIVES_CONTAGIOUS         = BLOCKS.register("explosives_contagious",          () -> new BlockExplosives(EntityReg.EXPLOSIVES_CONTAGIOUS,          BlastEvent.Contagious::new,         ItemReg.EXPLOSIVES_CONTAGIOUS));
    public static final RegistryObject<Block> EXPLOSIVES_SONIC              = BLOCKS.register("explosives_sonic",               () -> new BlockExplosives(EntityReg.EXPLOSIVES_SONIC,               BlastEvent.Sonic::new,              ItemReg.EXPLOSIVES_SONIC));
    public static final RegistryObject<Block> EXPLOSIVES_BREACHING          = BLOCKS.register("explosives_breaching",           () -> new BlockExplosives(EntityReg.EXPLOSIVES_BREACHING,           BlastEvent.Breaching::new,          ItemReg.EXPLOSIVES_BREACHING));
    public static final RegistryObject<Block> EXPLOSIVES_REJUVENATION       = BLOCKS.register("explosives_rejuvenation",        () -> new BlockExplosives(EntityReg.EXPLOSIVES_REJUVENATION,        BlastEvent.Rejuvenation::new,       ItemReg.EXPLOSIVES_REJUVENATION));
    public static final RegistryObject<Block> EXPLOSIVES_THERMOBARIC        = BLOCKS.register("explosives_thermobaric",         () -> new BlockExplosives(EntityReg.EXPLOSIVES_THERMOBARIC,         BlastEvent.Thermobaric::new,        ItemReg.EXPLOSIVES_THERMOBARIC));
    public static final RegistryObject<Block> EXPLOSIVES_NUCLEAR            = BLOCKS.register("explosives_nuclear",             () -> new BlockExplosives(EntityReg.EXPLOSIVES_NUCLEAR,             BlastEvent.Nuclear::new,            ItemReg.EXPLOSIVES_NUCLEAR));
    public static final RegistryObject<Block> EXPLOSIVES_EMP                = BLOCKS.register("explosives_emp",                 () -> new BlockExplosives(EntityReg.EXPLOSIVES_EMP,                 BlastEvent.Emp::new,                ItemReg.EXPLOSIVES_EMP));
    public static final RegistryObject<Block> EXPLOSIVES_EXOTHERMIC         = BLOCKS.register("explosives_exothermic",          () -> new BlockExplosives(EntityReg.EXPLOSIVES_EXOTHERMIC,          BlastEvent.Exothermic::new,         ItemReg.EXPLOSIVES_EXOTHERMIC));
    public static final RegistryObject<Block> EXPLOSIVES_ENDOTHERMIC        = BLOCKS.register("explosives_endothermic",         () -> new BlockExplosives(EntityReg.EXPLOSIVES_ENDOTHERMIC,         BlastEvent.Endothermic::new,        ItemReg.EXPLOSIVES_ENDOTHERMIC));
    public static final RegistryObject<Block> EXPLOSIVES_ANTIGRAVITATIONAL  = BLOCKS.register("explosives_antigravitational",   () -> new BlockExplosives(EntityReg.EXPLOSIVES_ANTIGRAVITATIONAL,   BlastEvent.Antigravitational::new,  ItemReg.EXPLOSIVES_ANTIGRAVITATIONAL));
    public static final RegistryObject<Block> EXPLOSIVES_ENDER              = BLOCKS.register("explosives_ender",               () -> new BlockExplosives(EntityReg.EXPLOSIVES_ENDER,               BlastEvent.Ender::new,              ItemReg.EXPLOSIVES_ENDER));
    public static final RegistryObject<Block> EXPLOSIVES_HYPERSONIC         = BLOCKS.register("explosives_hypersonic",          () -> new BlockExplosives(EntityReg.EXPLOSIVES_HYPERSONIC,          BlastEvent.Hypersonic::new,         ItemReg.EXPLOSIVES_HYPERSONIC));
    public static final RegistryObject<Block> EXPLOSIVES_ANTIMATTER         = BLOCKS.register("explosives_antimatter",          () -> new BlockExplosives(EntityReg.EXPLOSIVES_ANTIMATTER,          BlastEvent.Antimatter::new,         ItemReg.EXPLOSIVES_ANTIMATTER));
    public static final RegistryObject<Block> EXPLOSIVES_REDMATTER          = BLOCKS.register("explosives_redmatter",           () -> new BlockExplosives(EntityReg.EXPLOSIVES_REDMATTER,           BlastEvent.Redmatter::new,          ItemReg.EXPLOSIVES_REDMATTER));

    // Unconventional Explosives
    public static final RegistryObject<Block> S_MINE                        = BLOCKS.register("s_mine",                         () -> new BlockSMine(EntityReg.S_MINE, BlastEvent.SMine::new, ItemReg.S_MINE));

    // Spikes
    public static final RegistryObject<Block> SPIKES                        = BLOCKS.register("spikes",                         BlockSpikes::new);
    public static final RegistryObject<Block> SPIKES_POISON                 = BLOCKS.register("spikes_poison",                  () -> new BlockSpikes() {
        @Override
        public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
            super.entityInside(state, world, pos, entity);
            if (entity instanceof LivingEntity)
                ((LivingEntity) entity).addEffect(new EffectInstance(Effects.POISON, 7 * 20, 0));
        }
    });
    public static final RegistryObject<Block> SPIKES_FIRE                   = BLOCKS.register("spikes_fire",                    () -> new BlockSpikes() {
        @Override
        public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
            super.entityInside(state, world, pos, entity);
            if (entity instanceof LivingEntity && !state.getValue(BlockSpikes.WATERLOGGED))
                entity.setSecondsOnFire(7);
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
    public static final RegistryObject<Block> LAUNCHER_CONTROL_PANEL_T1     = BLOCKS.register("launcher_control_panel_t1",      () -> new BlockLauncherControlPanel(TileReg.LAUNCHER_CONTROL_PANEL_T1));
    public static final RegistryObject<Block> LAUNCHER_CONTROL_PANEL_T2     = BLOCKS.register("launcher_control_panel_t2",      () -> new BlockLauncherControlPanel(TileReg.LAUNCHER_CONTROL_PANEL_T2));
    public static final RegistryObject<Block> LAUNCHER_CONTROL_PANEL_T3     = BLOCKS.register("launcher_control_panel_t3",      () -> new BlockLauncherControlPanel(TileReg.LAUNCHER_CONTROL_PANEL_T3));
    public static final RegistryObject<Block> LAUNCHER_SUPPORT_FRAME_T1     = BLOCKS.register("launcher_support_frame_t1",      BlockLauncherSupportFrame::new);
    public static final RegistryObject<Block> LAUNCHER_SUPPORT_FRAME_T2     = BLOCKS.register("launcher_support_frame_t2",      BlockLauncherSupportFrame::new);
    public static final RegistryObject<Block> LAUNCHER_SUPPORT_FRAME_T3     = BLOCKS.register("launcher_support_frame_t3",      BlockLauncherSupportFrame::new);

    // Other Machinery
    public static final RegistryObject<Block> CRUISE_LAUNCHER               = BLOCKS.register("cruise_launcher",                () -> new BlockCruiseLauncher(TileReg.CRUISE_LAUNCHER));
    public static final RegistryObject<Block> EMP_TOWER                     = BLOCKS.register("emp_tower",                      () -> new BlockEMPTower(TileReg.EMP_TOWER));
    public static final RegistryObject<Block> RADAR_STATION                 = BLOCKS.register("radar_station",                  BlockRadarStation::new);

    // Extras
    public static final RegistryObject<Block> GLASS_BUTTON                  = BLOCKS.register("glass_button",                   BlockGlassButton::new);
    public static final RegistryObject<Block> GLASS_PRESSURE_PLATE          = BLOCKS.register("glass_pressure_plate",           BlockGlassPressurePlate::new);

}

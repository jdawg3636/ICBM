package icbm.classic.content.blast;

import icbm.classic.api.events.BlastBlockModifyEvent;
import icbm.classic.config.blast.ConfigBlast;
import icbm.classic.content.potion.CustomPotionEffect;
import icbm.classic.content.potion.PoisonFrostBite;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.Iterator;
import java.util.List;

public class BlastEndothermic extends BlastBeam {

    public BlastEndothermic() {
        this.red = 0f;
        this.green = 0.3f;
        this.blue = 0.7f;
    }

    @Override
    protected void mutateBlocks(List<BlockPos> edits) {

        final double radius = this.getBlastRadius();
        final double radiusDecay = Math.max(1, radius * 0.3); //TODO config

        for (BlockPos targetPosition : edits) {

            final double delta_x = location.xi() - targetPosition.getX();
            final double delta_y = location.yi() - targetPosition.getY();
            final double delta_z = location.zi() - targetPosition.getZ();

            final double distance = Math.sqrt(delta_x * delta_x + delta_y * delta_y + delta_z * delta_z);
            final double distanceScale = 1 - (distance / radius);

            BlockState blockState = world.getBlockState(targetPosition);

            //Closer to center the better the chance of spawning blocks
            if (distance <= radiusDecay || Math.random() < distanceScale) {

                //Turn fluids and liquid like blocks to air
                if (blockState.getMaterial() == Material.WATER)
                    MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(this.world(), targetPosition, Blocks.ICE.getDefaultState(), 3));
                else if (blockState.getBlock() == Blocks.FIRE)
                    MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition));
                else if (blockState.getBlock() == Blocks.LAVA)
                    MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition, Blocks.OBSIDIAN.getDefaultState()));
                else if (blockState.getFluidState() == Fluids.FLOWING_LAVA.getDefaultState()) {

                    int level = Math.min(8, Math.max(1, blockState.get(FlowingFluidBlock.LEVEL) / 2));

                    MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(
                            world,
                            targetPosition,
                            Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, level),
                            3
                    ));

                }
                else if (blockState.getBlock() == Blocks.LAVA) {
                    MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition, Blocks.STONE.getDefaultState(), 3));
                }
                else if (blockState.getBlock() == Blocks.NETHERRACK) {
                    MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition, Blocks.DIRT.getDefaultState(), 3));
                }
                else if (blockState.getBlock() == Blocks.SOUL_SAND) {
                    if (world.rand.nextBoolean())
                        MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition, Blocks.SAND.getDefaultState(), 3));
                    else
                        MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition, Blocks.GRAVEL.getDefaultState(), 3));
                }

                //Ground replacement
                else if (blockState.getMaterial() == Material.EARTH || blockState.getMaterial() == Material.ORGANIC) {
                    if (world.rand.nextBoolean())
                        MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition, Blocks.ICE.getDefaultState(), 3));
                    else
                        MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition, Blocks.SNOW.getDefaultState(), 3));
                }

                //Randomly place fire TODO move to outside mutate so we always place snow while charging up
                if (Math.random() < distanceScale)
                    tryPlaceSnow(world, targetPosition.up(), false);

            }
        }
    }

    private static void tryPlaceSnow(World world, BlockPos pos, boolean random) {

        if (!random || world.rand.nextBoolean()) {

            //Place fire
            final BlockState blockState = world.getBlockState(pos);
            final BlockState blockStateUnder = world.getBlockState(pos.down());

            if (blockState.getBlock().isReplaceable(world, pos)
                    && Blocks.SNOW.canPlaceBlockAt(world, pos)
                    && blockStateUnder.isSolidSide(world, pos.down(), Direction.UP)
            ) {
                MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, pos,
                        Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, 1 + world.rand.nextInt(7)), 3
                ));
            }

        }

    }

    @Override
    protected void onBlastCompleted() {

        super.onBlastCompleted();

        //Freeze all nearby entities.
        final List<LivingEntity> livingEntities = world().getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(location.x() - getBlastRadius(), location.y() - getBlastRadius(), location.z() - getBlastRadius(), location.x() + getBlastRadius(), location.y() + getBlastRadius(), location.z() + getBlastRadius()));

        if (livingEntities != null && !livingEntities.isEmpty()) {

            final Iterator<LivingEntity> it = livingEntities.iterator();

            while (it.hasNext()) {

                LivingEntity entity = it.next();

                if (entity != null && entity.isAlive()) {
                    entity.addPotionEffect(new CustomPotionEffect(PoisonFrostBite.INSTANCE, 60 * 20, 1, null));
                    entity.addPotionEffect(new EffectInstance(Effects.POISON, 10 * 20, 2));
                    entity.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 120 * 20, 2));
                    entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 120 * 20, 4));
                }

            }

        }

        //Change to time
        if (ConfigBlast.ALLOW_DAY_NIGHT && world().getGameRules().getBoolean("doDaylightCycle"))
            this.world().setTime(1200);

    }

}

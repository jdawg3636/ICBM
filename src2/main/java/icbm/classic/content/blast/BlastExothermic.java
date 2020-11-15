package icbm.classic.content.blast;

import icbm.classic.api.events.BlastBlockModifyEvent;
import icbm.classic.config.blast.ConfigBlast;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class BlastExothermic extends BlastBeam {

    public BlastExothermic() {
        this.red = 0.7f;
        this.green = 0.3f;
        this.blue = 0;
    }

    @Override
    protected void mutateBlocks(List<BlockPos> edits) {

        final double radius = this.getBlastRadius();
        final double radiusDecay = Math.max(1, radius * 0.3); //TODO config
        final double radiusEnsured = Math.max(1, radius * 0.1); //TODO config

        for (BlockPos targetPosition : edits) {

            final double delta_x = location.xi() - targetPosition.getX();
            final double delta_y = location.yi() - targetPosition.getY();
            final double delta_z = location.zi() - targetPosition.getZ();

            final double distance = Math.sqrt(delta_x * delta_x + delta_y * delta_y + delta_z * delta_z);
            final double distanceScale = 1 - (distance / radius);

            BlockState blockState = world.getBlockState(targetPosition);
            Block block = blockState.getBlock();

            //Turn fluids and liquid like blocks to air
            if (blockState.getMaterial() == Material.WATER || block == Blocks.ICE)
                MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(this.world(), targetPosition));

            //Closer to center the better the chance of spawning blocks
            if (distance <= radiusDecay || Math.random() < distanceScale) {

                //Destroy plants
                if (blockState.getMaterial() == Material.LEAVES
                        || blockState.getMaterial() == Material.PLANTS
                        || blockState.getMaterial() == Material.TALL_PLANTS
                        || blockState.getMaterial() == Material.OCEAN_PLANT
                        || blockState.getMaterial() == Material.BAMBOO
                        || blockState.getMaterial() == Material.BAMBOO_SAPLING
                ) {
                    if (!blockState.getMaterial().isReplaceable() || Blocks.FIRE.canPlaceBlockAt(world(), targetPosition))
                        MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(this.world(), targetPosition));
                    else
                        MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(this.world(), targetPosition, Blocks.FIRE.getDefaultState()));
                }

                //Turn random stone into lava
                else if (blockState.getMaterial() == Material.ROCK) {
                    //Small chance to turn to lava
                    if (this.world().rand.nextFloat() > 0.9) //TODO add config
                        MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(this.world(), targetPosition, Fluids.FLOWING_LAVA.getDefaultState().getBlockState(), 3));
                    //Coin flip to turn to magma
                    else if (this.world().rand.nextBoolean()) //TODO add config
                        MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(this.world(), targetPosition, Blocks.LAVA.getDefaultState(), 3));
                    //Coin flip to turn to netherrack
                    else if (this.world().rand.nextBoolean() || distance <= radiusEnsured) /* TODO add config */ {
                        MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition,
                                () -> placeNetherrack(world, targetPosition)
                        ));
                    }
                }

                //Sand replacement
                else if (blockState.getMaterial() == Material.SAND) {
                    if (this.world().rand.nextBoolean()) //TODO add config
                        MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(this.world(), targetPosition.down(), Blocks.SOUL_SAND.getDefaultState(), 3));
                    else
                        MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition,
                                () -> placeNetherrack(world, targetPosition)
                        ));
                }

                //Ground replacement
                else if (blockState.getMaterial() == Material.EARTH || blockState.getMaterial() == Material.ORGANIC) {
                    MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition,
                            () -> placeNetherrack(world, targetPosition)
                    ));
                }

                //Randomly place fire TODO move to outside mutate so we always place fire while charging up
                if (Math.random() < distanceScale)
                    tryPlaceFire(world, targetPosition.up(), false);

            }
        }
    }

    private static void placeNetherrack(World world, BlockPos pos) {

        if (!world.setBlockState(pos, Blocks.NETHERRACK.getDefaultState(), 3))
            System.out.println("Failed to place netherrack at " + pos);

        //Place fire randomly above netherrack
        tryPlaceFire(world, pos.up(), true);

    }

    private static void tryPlaceFire(World world, BlockPos pos, boolean random) {

        if (!random || world.rand.nextBoolean()) {

            //Place fire
            final BlockState blockState = world.getBlockState(pos);

            if (blockState.getMaterial().isReplaceable() && Blocks.FIRE.canPlaceBlockAt(world, pos))
                MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, pos, Blocks.FIRE.getDefaultState(), 3));

        }

    }

    @Override
    public void onBlastCompleted() {

        super.onBlastCompleted();

        //Change time of day
        if (ConfigBlast.ALLOW_DAY_NIGHT && world().getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE))
            this.world().getWorldInfo().setWorldTime(18000);

    }

}

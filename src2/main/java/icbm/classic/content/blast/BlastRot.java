package icbm.classic.content.blast;

import icbm.classic.ICBMClassic;
import icbm.classic.api.events.BlastBlockModifyEvent;
import icbm.classic.api.explosion.IBlastTickable;
import icbm.classic.content.blast.threaded.BlastThreaded;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Consumer;

/**
 * Creates radiation spawning
 *
 * @author Calclavia
 */
public class BlastRot extends BlastThreaded implements IBlastTickable {

    // Moved here from main mod class, originally labeled with TODO implement
    public static Block blockRadioactive = Blocks.MYCELIUM;

    @Override
    public boolean doRun(int loops, Consumer<BlockPos> edits) {
        BlastHelpers.forEachPosInRadius(this.getBlastRadius(), (x, y, z) ->
        edits.accept(new BlockPos(xi() + x, yi() + y, zi() + z)));
        //TODO implement pathfinder so virus doesn't go through unbreakable air-tight walls
        return false;
    }

    @Override
    public void destroyBlock(BlockPos targetPosition) {

        //get block
        final BlockState blockState = world.getBlockState(targetPosition);
        final Block block = blockState.getBlock();

        if (block == Blocks.GRASS || block == Blocks.SAND)
            if (this.world().rand.nextFloat() > 0.96)
                MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition, blockRadioactive.getDefaultState(), 3));

        if (block == Blocks.STONE)
            if (this.world().rand.nextFloat() > 0.99)
                MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition, blockRadioactive.getDefaultState(), 3));

        else if (blockState.getMaterial() == Material.LEAVES || blockState.getMaterial() == Material.PLANTS)
            MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition));
        else if (block == Blocks.FARMLAND)
            MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition, blockRadioactive.getDefaultState(), 3));
        else if (blockState.getMaterial() == Material.WATER){

            if (Registry.FLUID.func_241873_b(new ResourceLocation("toxicwaste")).isPresent()) {

                Block blockToxic = Registry.FLUID.func_241873_b(new ResourceLocation("toxicwaste")).orElse(null).getDefaultState().getBlockState().getBlock();

                if (blockToxic != null)
                    MinecraftForge.EVENT_BUS.post(new BlastBlockModifyEvent(world, targetPosition, blockToxic.getDefaultState(), 3));

            }

        }

    }

}

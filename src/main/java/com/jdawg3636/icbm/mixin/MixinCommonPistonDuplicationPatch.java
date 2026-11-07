package com.jdawg3636.icbm.mixin;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.BlockExplosives;
import com.jdawg3636.icbm.common.config.ICBMConfig.TNTDuplicationPatchMode;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonBlockStructureHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;

@Mixin(PistonBlock.class)
public abstract class MixinCommonPistonDuplicationPatch {

    /**
     * Vanilla caches moved states, then performs updates while their source blocks still exist.
     * Those updates can prime TNT, leaving both an entity and a cached block to move.
     * Clear protected sources after caching, without neighbor or shape updates; vanilla's
     * remaining movement and final update passes still use the original states and positions.
     */
    @Inject(method = "moveBlocks", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/block/PistonBlockStructureHelper;getToDestroy()Ljava/util/List;"),
            locals = LocalCapture.CAPTURE_FAILHARD, require = 1)
    private void icbm$removeProtectedSources(World level, BlockPos pos, Direction direction, boolean extending, CallbackInfoReturnable<Boolean> cir, BlockPos headPos, PistonBlockStructureHelper helper, Map<BlockPos, BlockState> sources, List<BlockPos> toPush, List<BlockState> movedStates) {
        TNTDuplicationPatchMode mode = ICBMReference.COMMON_CONFIG.getTntDuplicationPatchMode();
        if (mode == TNTDuplicationPatchMode.UNPATCHED) {
            return;
        }
        for (BlockPos sourcePos : toPush) {
            BlockState state = sources.get(sourcePos);
            ResourceLocation id = state.getBlock().getRegistryName();
            if (
                mode == TNTDuplicationPatchMode.FULLY_PATCHED
                || (mode == TNTDuplicationPatchMode.NON_VANILLA_ONLY && id != null && !"minecraft".equals(id.getNamespace()))
                || (mode == TNTDuplicationPatchMode.BALLISTIC_REDUX_ONLY && state.getBlock() instanceof BlockExplosives)
            ) {
                // Notify clients, suppress shape updates, and mark the removal as piston movement.
                level.setBlock(sourcePos, Blocks.AIR.defaultBlockState(), 2 | 16 | 64);
            }
        }
    }
}

package com.jdawg3636.icbm.common.blocks;

import com.jdawg3636.icbm.common.blocks.multiblock.AbstractBlockMultiTile;
import com.jdawg3636.icbm.common.container.ContainerLauncherPlatform;
import com.jdawg3636.icbm.common.tile.TileLauncherPlatform;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockLauncherPlatform extends AbstractBlockMultiTile {

    private static final Vector3i[] MULTIBLOCK_POSITIONS = {
            new Vector3i(1,0,0),
            new Vector3i(1,1,0),
            new Vector3i(1,2,0),
            new Vector3i(-1,0,0),
            new Vector3i(-1,1,0),
            new Vector3i(-1,2,0)
    };

    /**
     * Constructor - Sets Default State for Multiblock Positioning Properties
     */
    public BlockLauncherPlatform() {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public Vector3i[] getMultiblockPositions() {
        return MULTIBLOCK_POSITIONS;
    }

    @Override
    public void onMultiblockActivated(TileEntity tileEntity, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if(tileEntity instanceof TileLauncherPlatform) {
            INamedContainerProvider containerProvider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent("gui.launcherBase");
                }

                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return new ContainerLauncherPlatform(i, world, getMultiblockCenter(world, pos, state), playerInventory, playerEntity);
                }
            };
            NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getPos());
        }
    }

}

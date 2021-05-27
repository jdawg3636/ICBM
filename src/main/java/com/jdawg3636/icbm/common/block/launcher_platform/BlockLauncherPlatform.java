package com.jdawg3636.icbm.common.block.launcher_platform;

import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMultiTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class BlockLauncherPlatform extends AbstractBlockMultiTile {

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
    public BlockLauncherPlatform(AbstractBlock.Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntity) {
        super(properties, tileEntity);
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
                    return new ContainerLauncherPlatform(getContainerType(), state.getBlock(), i, world, getMultiblockCenter(world, pos, state), playerInventory, playerEntity);
                }
            };
            NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getBlockPos());
        }
    }

    public abstract ContainerType<ContainerLauncherPlatform> getContainerType();

}

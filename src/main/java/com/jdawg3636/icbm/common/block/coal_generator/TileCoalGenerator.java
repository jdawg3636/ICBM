package com.jdawg3636.icbm.common.block.coal_generator;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.reg.BlockReg;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileCoalGenerator  extends TileEntity implements ITickableTileEntity {

    public TileCoalGenerator(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public void tick() {
//        if(!this.level.isClientSide()) {
//            int radius = 3;
//            for(Direction direction : Direction.values()) {
//                for(int i = -radius; i <= radius; ++i) {
//                    for(int j = -radius; j <= radius; ++j) {
//                        level.setBlock(getBlockPos().offset(rotateOffsetAboutDirection(i, j, direction, radius)), BlockReg.ELECTROMAGNETIC_GLASS.get().defaultBlockState(), 2);
//                    }
//                }
//            }
//        }
    }

    public BlockPos rotateOffsetAboutDirection(int i, int j, Direction direction, int magnitude) {
        if(direction.getAxis() == Direction.Axis.X) return new BlockPos(magnitude * direction.getAxisDirection().getStep(), i, j);
        if(direction.getAxis() == Direction.Axis.Y) return new BlockPos(i, magnitude * direction.getAxisDirection().getStep(), j);
        if(direction.getAxis() == Direction.Axis.Z) return new BlockPos(i, j, magnitude * direction.getAxisDirection().getStep());
        return null;
    }

    @Override
    public double getViewDistance() {
        return ICBMReference.distProxy().getTileEntityUpdateDistance();
    }

}

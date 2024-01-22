package com.jdawg3636.icbm.common.block.radar_station;

import com.jdawg3636.icbm.common.block.emp_tower.TileEMPTower;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * I am directly extending {@link TileEMPTower} since it already has logic for spinning in a circle a storing a "radius"
 * field synced over the network and saved as NBT data. Doing this is, of course, a bad idea, since a radar station is
 * not in fact an EMP tower, and the EMP tower could later implement functionality that is NOT shared by the radar station
 * and would need to be explicitly overridden here to not inherit. I'm doing it anyway though, so get off my ass <3
 */
public class TileRadarStation extends TileEMPTower implements ITickableTileEntity {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.radar_station");

    public TileRadarStation(TileEntityType<?> tileEntityType) {
        super(tileEntityType, DEFAULT_NAME);
    }

    @Override
    public void tick() {
        if(level != null && level.isClientSide()) addAnimationPercent(5D);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expandTowards(0, 1, 0).inflate(1, 0, 1);
    }

}

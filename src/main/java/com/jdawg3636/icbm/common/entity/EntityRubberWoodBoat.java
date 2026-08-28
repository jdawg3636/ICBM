package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.reg.EntityReg;
import com.jdawg3636.icbm.common.reg.ItemReg;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityRubberWoodBoat extends BoatEntity {

    public EntityRubberWoodBoat(EntityType<? extends EntityRubberWoodBoat> type, World world) {
        super(type, world);
    }

    public EntityRubberWoodBoat(World world, double x, double y, double z) {
        this(EntityReg.RUBBER_WOOD_BOAT.get(), world);
        setPos(x, y, z);
    }

    @Override
    public Item getDropItem() {
        return ItemReg.RUBBER_WOOD_BOAT.get();
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}

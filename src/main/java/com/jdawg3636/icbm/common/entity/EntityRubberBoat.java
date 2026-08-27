package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.reg.ItemReg;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityRubberBoat extends BoatEntity {
    public EntityRubberBoat(EntityType<? extends EntityRubberBoat> type, World world) { super(type, world); }
    public EntityRubberBoat(World world, double x, double y, double z) {
        this(com.jdawg3636.icbm.common.reg.EntityReg.RUBBER_WOOD_BOAT.get(), world);
        setPos(x, y, z);
    }
    @Override public Item getDropItem() { return ItemReg.RUBBER_WOOD_BOAT.get(); }
    @Override public IPacket<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
}

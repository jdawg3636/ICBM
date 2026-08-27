package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.common.entity.EntityRubberBoat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemRubberBoat extends Item {
    public ItemRubberBoat(Properties properties) { super(properties); }
    @Override public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        RayTraceResult hit = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.ANY);
        if (hit.getType() != RayTraceResult.Type.BLOCK) return ActionResult.pass(stack);
        EntityRubberBoat boat = new EntityRubberBoat(world, hit.getLocation().x, hit.getLocation().y, hit.getLocation().z);
        boat.yRot = player.yRot;
        if (!world.noCollision(boat, boat.getBoundingBox().inflate(-0.1D))) return ActionResult.fail(stack);
        if (!world.isClientSide) {
            world.addFreshEntity(boat);
            if (!player.abilities.instabuild) stack.shrink(1);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return ActionResult.sidedSuccess(stack, world.isClientSide());
    }
}

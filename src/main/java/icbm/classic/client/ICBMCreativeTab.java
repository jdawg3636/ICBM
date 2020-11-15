package icbm.classic.client;

//TODO//import icbm.classic.api.ICBMClassicHelpers;
//TODO//import icbm.classic.api.caps.IExplosive;
//TODO//import icbm.classic.api.refs.ICBMExplosives;
//TODO//import icbm.classic.api.reg.IExplosiveData;
import icbm.classic.content.reg.BlockReg;
import icbm.classic.content.reg.ItemReg;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Prefab creative tab to either create a fast creative tab or reduce code
 * need to make a more complex tab
 * Created by robert on 11/25/2014.
 */
public class ICBMCreativeTab extends ItemGroup {

    private final List<Item> definedTabItemsInOrder = new ArrayList();

    public ICBMCreativeTab(String name) {
        super(name);
    }

    //call during FMLInitializationEvent as registries need to be frozen for this
    public void init() {
        definedTabItemsInOrder.clear();
        //define items in order
        //TODO//orderItem(BlockReg.LAUNCHER_BASE.get());
        //TODO//orderItem(BlockReg.LAUNCHER_SCREEN.get());
        //TODO//orderItem(BlockReg.LAUNCHER_FRAME.get());
        //TODO//orderItem(BlockReg.EMP_TOWER.get());
        //TODO//orderItem(BlockReg.RADAR_STATION.get());

        orderItem(BlockReg.CONCRETE.get());
        orderItem(BlockReg.REINFORCED_GLASS.get());
        orderItem(BlockReg.SPIKES.get());

        orderItem(ItemReg.itemRocketLauncher);
        orderItem(ItemReg.itemRadarGun);
        orderItem(ItemReg.itemRemoteDetonator);
        orderItem(ItemReg.itemLaserDetonator);
        orderItem(ItemReg.itemTracker);
        orderItem(ItemReg.itemSignalDisrupter);
        orderItem(ItemReg.itemDefuser);
        orderItem(ItemReg.itemBattery);

        //TODO//orderItem(BlockReg.EXPLOSIVES.get());
        orderItem(ItemReg.itemMissile);
        orderItem(ItemReg.itemGrenade);
        orderItem(ItemReg.itemBombCart);

        //Collect any non-defined items
        for (Item item : Registry.ITEM) // registries are frozen during FMLInitializationEvent, can safely iterate
            if (item != null)
                for (ItemGroup tab : item.getCreativeTabs())
                    if (tab == this && !definedTabItemsInOrder.contains(item))
                        orderItem(item);

    }

    private void orderItem(Block item) {
        orderItem(Item.getItemFromBlock(item));
    }

    private void orderItem(Item item) {
        definedTabItemsInOrder.add(item);
    }

    /* TODO
    @Override
    @OnlyIn(Dist.CLIENT)
    public void displayAllRelevantItems(final NonNullList<ItemStack> list) {
        //Insert items in order
        definedTabItemsInOrder.forEach(item -> collectSubItems(item, list));
    }

    protected void collectSubItems(final Item item, final NonNullList<ItemStack> masterList) {

        //Collect stacks
        final NonNullList<ItemStack> collectedItemStacks = NonNullList.create();
        item.getSubItems(this, collectedItemStacks);

        //Sort explosive types, if not explosive it will leave it alone
        collectedItemStacks.sort(this::compareExplosives);

        //Merge into list with null check
        masterList.addAll(collectedItemStacks);

    }

    private int compareExplosives(ItemStack itemA, ItemStack itemB) {

        final IExplosive explosiveA = ICBMClassicHelpers.getExplosive(itemA);
        final IExplosive explosiveB = ICBMClassicHelpers.getExplosive(itemB);

        if (explosiveA != null && explosiveB != null)
            return compareExplosives(explosiveA, explosiveB);

        return 0;

    }

    private int compareExplosives(IExplosive explosiveA, IExplosive explosiveB) {

        final IExplosiveData dataA = Optional.ofNullable(explosiveA.getExplosiveData()).orElse(ICBMExplosives.CONDENSED);
        final IExplosiveData dataB = Optional.ofNullable(explosiveB.getExplosiveData()).orElse(ICBMExplosives.CONDENSED);
        final int tierA = dataA.getTier().ordinal();
        final int tierB = dataB.getTier().ordinal();

        //If tiers are the same move to sorting by explosive registry index
        if (tierA == tierB)
            return dataA.getRegistryID() - dataB.getRegistryID();

        return tierA - tierB;

    }
    */

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemReg.itemMissile);
    }

}

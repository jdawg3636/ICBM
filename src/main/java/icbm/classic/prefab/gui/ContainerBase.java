package icbm.classic.prefab.gui;

import icbm.classic.prefab.inventory.IInventoryProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;

public class ContainerBase<H extends Object> extends Container {

    protected int slotCount = 0;

    protected IInventory inventory;
    protected PlayerEntity player;
    protected H host;

    public ContainerBase(IInventory inventory) {
        super(null, 0); // This is done is net.minecraft.inventory.container.PlayerContainer, assuming it's okay
        this.inventory = inventory;
        this.slotCount = inventory.getSizeInventory();
    }

    @Deprecated
    public ContainerBase(PlayerEntity player, IInventory inventory) {

        this(inventory);

        this.player = player;
        if (inventory instanceof IPlayerUsing)
            ((IPlayerUsing) inventory).getPlayersUsing().add(player);

    }

    public ContainerBase(PlayerEntity player, H node) {

        super(null, 0); // This is done is net.minecraft.inventory.container.PlayerContainer, assuming it's okay

        if (node instanceof IInventory)
            inventory = (IInventory) node;
        else if (node instanceof IInventoryProvider)
            inventory = ((IInventoryProvider) node).getInventory();

        this.player = player;
        if (node instanceof IPlayerUsing)
            ((IPlayerUsing) node).addPlayerToUseList(player);

    }

    @Override
    public void onContainerClosed(PlayerEntity playerEntity) {
        if (host instanceof IPlayerUsing && playerEntity.openContainer != this)
            ((IPlayerUsing) host).removePlayerToUseList(playerEntity);
        super.onContainerClosed(playerEntity);
    }

    public void addPlayerInventory(PlayerEntity player) {
        addPlayerInventory(player, 8, 84);
    }

    public void addPlayerInventory(PlayerEntity player, int x, int y) {

        if (this.inventory instanceof IPlayerUsing)
            ((IPlayerUsing) this.inventory).getPlayersUsing().add(player);

        //Inventory
        for (int row = 0; row < 3; ++row)
            for (int slot = 0; slot < 9; ++slot)
                this.addSlot(new Slot(player.inventory, slot + row * 9 + 9, slot * 18 + x, row * 18 + y));

        //Hot bar
        for (int slot = 0; slot < 9; ++slot)
            this.addSlot(new Slot(player.inventory, slot, slot * 18 + x, 58 + y));

    }

    @Override
    public boolean canInteractWith(PlayerEntity entityplayer) {
        return this.inventory.isUsableByPlayer(entityplayer);
    }

}
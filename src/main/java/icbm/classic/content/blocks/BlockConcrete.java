package icbm.classic.content.blocks;

import com.google.common.collect.Lists;
import icbm.classic.ICBMClassic;
import icbm.classic.ICBMConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static icbm.classic.content.blocks.BlockConcrete.EnumType.COMPACT;
import static icbm.classic.content.blocks.BlockConcrete.EnumType.NORMAL;
import static icbm.classic.content.blocks.BlockConcrete.EnumType.REINFORCED;

public class BlockConcrete extends Block {

    public static final PropertyType TYPE_PROP = new PropertyType();

    public BlockConcrete() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(10F));
        this.setRegistryName(ICBMConstants.PREFIX + "concrete");
        this.setCreativeTab(ICBMClassic.CREATIVE_TAB);
    }

    @Override
    public int damageDropped(BlockState state) {
        return getMetaFromState(state);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE_PROP);
    }

    @Override
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand) {
        return getDefaultState().with(TYPE_PROP, EnumType.get(meta));
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.get(TYPE_PROP).ordinal();
    }

    @Deprecated
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(TYPE_PROP, EnumType.get(meta));
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {

        BlockState blockState = world.getBlockState(pos);

        switch (blockState.get(TYPE_PROP)) {
            case COMPACT:
                return 280;
            case REINFORCED:
                return 2800; //obsidian is 2000
            default:
            case NORMAL:
                return 28;
        }

    }

    @Override
    public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> items) {

        if (tab == getCreativeTab())
            for (int i = 0; i < 3; i++)
                items.add(new ItemStack(this, 1, i));

    }

    public static class PropertyType extends EnumProperty {

        public PropertyType() {
            super("type", EnumType.class, Lists.newArrayList(EnumType.values()));
        }

    }

    public enum EnumType implements IStringSerializable {

        NORMAL,
        COMPACT,
        REINFORCED;

        /**
         * Should be getName(), shows the correct name in MCPBot and says the mapping has existed since September 2014.
         */
        @Override
        public String func_176610_l() {
            return name().toLowerCase();
        }

        public static EnumType get(int meta) {
            return meta >= 0 && meta < values().length ? values()[meta] : NORMAL;
        }

    }

}
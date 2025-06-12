package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.reg.EntityReg;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;

public class EntitySonicBlast extends Entity {

    public ArrayList<BlockPos> targetBlocks;
    public int blocksAffectedPerTick;

    public EntitySonicBlast(EntityType<?> entityType, World level) {
        this(entityType, level, new ArrayList<>(), 15);
    }

    public EntitySonicBlast(EntityType<?> entityType, World level, ArrayList<BlockPos> targetBlocks, int blocksAffectedPerTick) {
        super(entityType, level);
        this.targetBlocks = targetBlocks;
        this.blocksAffectedPerTick = blocksAffectedPerTick;
    }

    @Override
    public void tick() {

        // Server-Side Only
        if(level.isClientSide()) return;

        // Limit Blocks Affected per Tick
        for(int i = 0; i < blocksAffectedPerTick; ++i) {

            // Kill Blast Entity if Out of Targets
            if(targetBlocks.size() == 0) {
                kill();
                return;
            }

            // Identify Current Target
            BlockPos blockPos = targetBlocks.get(0);

            // Chance to Randomly Skip Current Target
            if(!(level.random.nextFloat() < 0.6)) {
                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                targetBlocks.remove(0);
                continue;
            }

            // Validate Current Target
            try {
                if(!level.getBlockState(blockPos).canDropFromExplosion(level, blockPos, null) || level.getBlockState(blockPos).hasTileEntity() || level.getBlockState(blockPos).getBlock() instanceof FlowingFluidBlock) {
                    level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                    targetBlocks.remove(0);
                    continue;
                }
            } catch (Exception e) {
                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                targetBlocks.remove(0);
                continue;
            }

            // Affect Current Target
            EntityFancyFallingBlock fancyFallingBlockEntity = EntityReg.FANCY_FALLING_BLOCK.get().create(level);
            if(fancyFallingBlockEntity != null) {
                fancyFallingBlockEntity.setDeltaMovement(2 * level.random.nextDouble() - 1D, 2 * level.random.nextDouble() + 4D, 2 * level.random.nextDouble() - 1D);
                fancyFallingBlockEntity.addEntityToLevel(blockPos, level.getBlockState(blockPos), -0.04f);
            }
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);

            // Remove Current Target from Queue
            targetBlocks.remove(0);

        }

    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        nbt.putInt("target_block_count", targetBlocks.size());
        CompoundNBT serializedTargetBlocks = new CompoundNBT();
        for(int i = 0; i < targetBlocks.size(); ++i) {
            CompoundNBT serializedTargetBlock = new CompoundNBT();
            serializedTargetBlock.putInt("x", targetBlocks.get(i).getX());
            serializedTargetBlock.putInt("y", targetBlocks.get(i).getY());
            serializedTargetBlock.putInt("z", targetBlocks.get(i).getZ());
            serializedTargetBlocks.put("" + i, serializedTargetBlock);
        }
        nbt.put("target_blocks", serializedTargetBlocks);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        final int targetBlockCount = nbt.getInt("target_block_count");
        targetBlocks = new ArrayList<>(targetBlockCount);
        final CompoundNBT serializedTargetBlocks = nbt.getCompound("target_blocks");
        for(int i = 0; i < targetBlockCount; ++i) {
            final CompoundNBT serializedTargetBlock = serializedTargetBlocks.getCompound("" + i);
            final BlockPos targetBlock = new BlockPos(serializedTargetBlock.getInt("x"), serializedTargetBlock.getInt("y"), serializedTargetBlock.getInt("z"));
            targetBlocks.add(targetBlock);
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}

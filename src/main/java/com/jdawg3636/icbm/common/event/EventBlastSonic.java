package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;

public class EventBlastSonic extends AbstractBlastEvent {

    public EventBlastSonic(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection, SoundEventReg.EXPLOSION_SONIC);
    }

    @Override
    public boolean executeBlast() {

        ICBMBlastEventUtil.doBlastSoundAndParticles(this);

        ArrayList<BlockPos> results = new ArrayList<>();

        // Parameters (different for sonic/hypersonic)
        double radius = 6; // think this was 15 in 1.7.10?
        double energy = 30;

        // Identify Target Blocks
        int steps = (int) Math.ceil(Math.PI / Math.atan(1.0D / radius));
        for (int phi_n = 0; phi_n < 2 * steps; phi_n++)
        {
            for (int theta_n = 0; theta_n < steps; theta_n++)
            {
                double phi = Math.PI * 2 / steps * phi_n;
                double theta = Math.PI / steps * theta_n;

                Vector3d delta = new Vector3d(Math.sin(theta) * Math.cos(phi), Math.cos(theta), Math.sin(theta) * Math.sin(phi));
                double power = energy - (energy * getBlastWorld().random.nextDouble() / 2);

                Vector3d t = new Vector3d(getBlastPosition().getX(), getBlastPosition().getY(), getBlastPosition().getZ());

                for (double d = 0.3F; power > 0f; power -= d * 0.75F * 10)
                {
                    if (t.distanceTo(t) > radius)
                    {
                        break;
                    }

                    BlockState block = getBlastWorld().getBlockState(getBlastPosition());

                    if (block.getBlock().getExplosionResistance() >= 0)
                    {
                        power -= block.getBlock().getExplosionResistance();

                        if (power > 0f)
                        {
                            results.add(new BlockPos(t));
                        }
                    }
                    t = t.add(delta);
                }
            }
        }

        // Affect Blocks
        for(BlockPos blockPos : results) {
            FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(getBlastWorld(), blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, getBlastWorld().getBlockState(blockPos));
            fallingBlockEntity.time = 1;
            fallingBlockEntity.push(0D, 4D, 0D);
            getBlastWorld().setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
            if(!getBlastWorld().getBlockState(blockPos).hasTileEntity())
                getBlastWorld().addFreshEntity(fallingBlockEntity);
        }
        return true;
    }

}

package com.jdawg3636.icbm;

import com.jdawg3636.icbm.common.config.ICBMConfig;
import com.jdawg3636.icbm.common.listener.ClientProxy;
import com.jdawg3636.icbm.common.listener.CommonProxy;
import com.jdawg3636.icbm.common.reg.ItemReg;
import com.jdawg3636.icbm.common.reg.ParticleTypeReg;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.DistExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public final class ICBMReference {

    public static final String MODID = "icbm";

    public static final String CONFIG_FILE_PREFIX = "intercontinentalballisticredux";
    public static final ICBMConfig.Client CLIENT_CONFIG = new ICBMConfig.Client();
    public static final ICBMConfig.Common COMMON_CONFIG = new ICBMConfig.Common();
    public static final ICBMConfig.Server SERVER_CONFIG = new ICBMConfig.Server();

    public static enum ICBMTextColors {
        LIGHT_GRAY(0x8f8f8f),
        DARK_GRAY(0x363636);
        public final int code;
        ICBMTextColors(int code) {
            this.code = code;
        }
    }

    public static final ItemGroup CREATIVE_TAB = new ItemGroup(ICBMReference.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemReg.EXPLOSIVES_ANTIMATTER.get());
        }
    };

    private static final Logger logger = LogManager.getLogger(ICBMReference.MODID);

    private static final CommonProxy distProxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static Logger logger() {
        return logger;
    }

    public static CommonProxy distProxy() {
        return distProxy;
    }

    public static double getTileEntityUpdateDistance() {
        double l1Distance = (distProxy().getRenderDistance() + 1) * 16;
        return Math.sqrt(l1Distance * l1Distance + l1Distance * l1Distance);
    }

    @CapabilityInject(IEnergyStorage.class)
    public static Capability<IEnergyStorage> FORGE_ENERGY_CAPABILITY;

    public static void broadcastToChat(IWorldReader level, String message) {
        if (level instanceof ServerWorld) {
            ((ServerWorld) level).players().forEach(
                (ServerPlayerEntity serverPlayer) -> serverPlayer.sendMessage(new StringTextComponent(message), Util.NIL_UUID)
            );
        }
    }

    public static void broadcastToChat(IWorldReader level, String message, Object... messageArgs) {
        String completeMessage = message;
        try {
            completeMessage = String.format(message, messageArgs);
        } catch (Exception ignored) {}
        broadcastToChat(level, completeMessage);
    }

    // Variant of the vanilla ServerWorld method where we send true instead of false for the override parameter. This increases the packet render distance to 512 blocks.
    public static <T extends IParticleData> int sendParticlesOverrideLimiter(ServerWorld level, T p_195598_1_, double p_195598_2_, double p_195598_4_, double p_195598_6_, int p_195598_8_, double p_195598_9_, double p_195598_11_, double p_195598_13_, double p_195598_15_) {
        int i = 0;

        for(int j = 0; j < level.players().size(); ++j) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)level.players().get(j);
            if (level.sendParticles(serverplayerentity, p_195598_1_, true, p_195598_2_, p_195598_4_, p_195598_6_, p_195598_8_, p_195598_9_, p_195598_11_, p_195598_13_, p_195598_15_)) {
                ++i;
            }
        }

        return i;
    }

    public static void spawnRadiationParticle(World level, BlockPos blockPos, Random random) {
        double x = (double)blockPos.getX() + random.nextDouble();
        double y = (double)blockPos.getY() + random.nextDouble();
        double z = (double)blockPos.getZ() + random.nextDouble();
        double speedX = (random.nextDouble() - 0.5) / 8;
        double speedY = (random.nextDouble() - 0.5) / 8;
        double speedZ = (random.nextDouble() - 0.5) / 8;
        level.addParticle((BasicParticleType) ParticleTypeReg.RADIOACTIVE_EFFECT.get(), x, y, z, speedX, speedY, speedZ);
    }

    public static boolean entityIsAPlayerInCreativeOrSpectatorMode(Entity entity) {
        if(!(entity instanceof ServerPlayerEntity)) return false;
        return !((ServerPlayerEntity) entity).gameMode.getGameModeForPlayer().isSurvival();
    }

    public static boolean checkItemStackContainsFluidTag(ItemStack stack, ResourceLocation fluidTag) {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                .map(handler -> handler.getFluidInTank(0))
                .map(FluidStack::getFluid)
                .map(Fluid::getTags)
                .map(set -> set.contains(fluidTag))
                .orElse(false);
    }

    public static int getMinYLevelForLevel(World level) {
        return 0;
    }

    public static int getMaxYLevelForLevel(World level) {
        return 255;
    }

}

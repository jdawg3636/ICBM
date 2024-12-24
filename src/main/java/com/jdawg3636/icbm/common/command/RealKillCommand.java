package com.jdawg3636.icbm.common.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

/**
 * Modification of {@link net.minecraft.command.impl.KillCommand} to be able to kill LivingEntities which are either
 * immune or highly resistant to void damage.
 */
public class RealKillCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
            Commands.literal("realkill")
            .requires(source -> source.hasPermission(2))
            .executes(context -> kill(context.getSource(), ImmutableList.of(context.getSource().getEntityOrException())))
            .then(Commands.argument("targets", EntityArgument.entities()).executes((context) -> kill(context.getSource(), EntityArgument.getEntities(context, "targets"))))
        );
    }

    private static int kill(CommandSource source, Collection<? extends Entity> targets) {

        for(Entity entity : targets) {
            // This if-statement is the only change from the vanilla command.
            if(entity instanceof LivingEntity) {
                LivingEntity livingEntity = ((LivingEntity)entity);
                livingEntity.setHealth(0);
                SoundEvent soundevent = livingEntity.getDeathSound();
                if (soundevent != null) {
                    livingEntity.playSound(soundevent, livingEntity.getSoundVolume(), livingEntity.getVoicePitch());
                }
                livingEntity.die(DamageSource.OUT_OF_WORLD);
            }
            else {
                entity.kill();
            }
        }

        if (targets.size() == 1) {
            source.sendSuccess(new TranslationTextComponent("commands.kill.success.single", targets.iterator().next().getDisplayName()), true);
        } else {
            source.sendSuccess(new TranslationTextComponent("commands.kill.success.multiple", targets.size()), true);
        }

        return targets.size();

    }

}

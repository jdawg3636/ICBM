package com.jdawg3636.icbm.client.render.entity;

import com.jdawg3636.icbm.common.entity.EntityMissile;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class EntityMissileRenderer extends EntityRenderer<EntityMissile> {

    public ItemStack missileItem;
    public float rotationX;
    public float rotationY;
    public float rotationZ;

    public EntityMissileRenderer(EntityRendererManager renderManagerIn, ItemStack missileItem) {
        super(renderManagerIn);
        this.missileItem = missileItem;
    }

    @Override
    public void render(@Nonnull EntityMissile entity, float entityYaw, float partialTick, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer buffer, int light) {

        //rotationX = entity.getEntityData().get(entity.ROTATION_X);
        //rotationY = entity.getEntityData().get(entity.ROTATION_Y);
        //rotationZ = entity.getEntityData().get(entity.ROTATION_Z);

        matrix.pushPose();
        //matrix.mulPose(anglesToQuaternion(entity.getRotationVector().x, entity.getRotationVector().y, rotationZ));
        matrix.translate(0, 0.5, 0);

        // Based on net.minecraft.client.renderer.entity.FireworkRocketRenderer
        Minecraft.getInstance().getItemRenderer().renderStatic(missileItem, ItemCameraTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY, matrix, buffer);

        matrix.popPose();

    }

    // Wikipedia, the solution to all of life's problems.
    // https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles#Euler_angles_to_quaternion_conversion
    // roll (X), pitch (In-Game Y, Blender Z), yaw (In-Game Z, Blender Y)
    Quaternion anglesToQuaternion(double rollDegrees, double pitchDegrees, double yawDegrees) {

        double roll  = Math.toRadians(rollDegrees);
        double pitch = Math.toRadians(pitchDegrees);
        double yaw   = Math.toRadians(yawDegrees);

        float cy = (float)Math.cos(yaw * 0.5);
        float sy = (float)Math.sin(yaw * 0.5);
        float cp = (float)Math.cos(pitch * 0.5);
        float sp = (float)Math.sin(pitch * 0.5);
        float cr = (float)Math.cos(roll * 0.5);
        float sr = (float)Math.sin(roll * 0.5);

        float x = sr * cp * cy - cr * sp * sy;
        float y = cr * sp * cy + sr * cp * sy;
        float z = cr * cp * sy - sr * sp * cy;
        float w = cr * cp * cy + sr * sp * sy;

        return new Quaternion(x, y, z, w);

    }

    /**
     * Ideally get rid of this (or at least make it less aggressive).
     * Implemented preemptively as a missile's hitbox will frequently not line up with its render.
     */
    @Override
    public boolean shouldRender(EntityMissile livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
        return true;
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityMissile entity) {
        return AtlasTexture.LOCATION_BLOCKS;
    }

}

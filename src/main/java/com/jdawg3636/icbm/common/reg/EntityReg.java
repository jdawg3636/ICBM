package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.common.entity.EntityExplosivesIncendiary;
import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.entity.EntityMissile;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = ICBMReference.MODID)
public final class EntityReg {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ICBMReference.MODID);

    // Explosives Registration
    public static final RegistryObject<EntityType<EntityExplosivesIncendiary>> EXPLOSIVES_INCENDIARY = ENTITIES.register(
            "explosives_incendiary",
            () -> EntityType.Builder.<EntityExplosivesIncendiary>create(EntityExplosivesIncendiary::new, EntityClassification.MISC)
                    .immuneToFire().size(0.98F, 0.98F).trackingRange(10).func_233608_b_(10)
                    .build("explosives_incendiary")
    );

    // Missile Registration
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_MODULE = registerMissile(ItemReg.MISSILE_MODULE);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CONVENTIONAL = registerMissile(ItemReg.MISSILE_CONVENTIONAL);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_SHRAPNEL = registerMissile(ItemReg.MISSILE_SHRAPNEL);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_INCENDIARY = registerMissile(ItemReg.MISSILE_INCENDIARY);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_DEBILITATION = registerMissile(ItemReg.MISSILE_DEBILITATION);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CHEMICAL = registerMissile(ItemReg.MISSILE_CHEMICAL);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANVIL = registerMissile(ItemReg.MISSILE_ANVIL);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_REPULSIVE = registerMissile(ItemReg.MISSILE_REPULSIVE);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ATTRACTIVE = registerMissile(ItemReg.MISSILE_ATTRACTIVE);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_NIGHTMARE = registerMissile(ItemReg.MISSILE_NIGHTMARE);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_FRAGMENTATION = registerMissile(ItemReg.MISSILE_FRAGMENTATION);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CONTAGIOUS = registerMissile(ItemReg.MISSILE_CONTAGIOUS);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_SONIC = registerMissile(ItemReg.MISSILE_SONIC);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_BREACHING = registerMissile(ItemReg.MISSILE_BREACHING);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_REJUVENATION = registerMissile(ItemReg.MISSILE_REJUVENATION);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_THERMOBARIC = registerMissile(ItemReg.MISSILE_THERMOBARIC);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_NUCLEAR = registerMissile(ItemReg.MISSILE_NUCLEAR);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_EMP = registerMissile(ItemReg.MISSILE_EMP);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_EXOTHERMIC = registerMissile(ItemReg.MISSILE_EXOTHERMIC);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ENDOTHERMIC = registerMissile(ItemReg.MISSILE_ENDOTHERMIC);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANTIGRAVITATIONAL = registerMissile(ItemReg.MISSILE_ANTIGRAVITATIONAL);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ENDER = registerMissile(ItemReg.MISSILE_ENDER);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_HYPERSONIC = registerMissile(ItemReg.MISSILE_HYPERSONIC);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANTIMATTER = registerMissile(ItemReg.MISSILE_ANTIMATTER);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_REDMATTER = registerMissile(ItemReg.MISSILE_REDMATTER);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_HOMING = registerMissile(ItemReg.MISSILE_HOMING);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANTIBALLISTIC = registerMissile(ItemReg.MISSILE_ANTIBALLISTIC);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CLUSTER = registerMissile(ItemReg.MISSILE_CLUSTER);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CLUSTER_NUCLEAR = registerMissile(ItemReg.MISSILE_CLUSTER_NUCLEAR);

    public static RegistryObject<EntityType<EntityMissile>> registerMissile(RegistryObject<Item> missileItem) {
        return ENTITIES.register(
                missileItem.getId().getPath(),
                () -> {
                        return EntityType.Builder.<EntityMissile>create(
                                (type, world) -> new EntityMissile(type, world, missileItem),
                                EntityClassification.MISC
                        )
                        .immuneToFire().size(1F, 4F).trackingRange(10).func_233608_b_(10)
                        .build(missileItem.getId().getPath());
                }
        );
    }

    /*

    private static int nextEntityID = 0;

    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityEntry> event) {

        event.getRegistry().register(buildEntityEntry(EntityFlyingBlock.class, ICBMEntities.BLOCK_GRAVITY, 128, 15));
        event.getRegistry().register(buildEntityEntry(EntityFragments.class, ICBMEntities.BLOCK_FRAGMENT, 40, 8));
        event.getRegistry().register(buildEntityEntry(EntityExplosive.class, ICBMEntities.BLOCK_EXPLOSIVE, 50, 5));
        event.getRegistry().register(buildEntityEntry(EntityMissile.class, ICBMEntities.MISSILE, 500, 1));
        event.getRegistry().register(buildEntityEntry(EntityExplosion.class, ICBMEntities.EXPLOSION, 100, 5));
        event.getRegistry().register(buildEntityEntry(EntityLightBeam.class, ICBMEntities.BEAM, 80, 5));
        event.getRegistry().register(buildEntityEntry(EntityGrenade.class, ICBMEntities.GRENADE, 50, 5));
        event.getRegistry().register(buildEntityEntry(EntityBombCart.class, ICBMEntities.BOMB_CART, 50, 2));
        event.getRegistry().register(buildEntityEntry(EntityPlayerSeat.class, ICBMEntities.MISSILE_SEAT, 50, 2));

        //Green team
        event.getRegistry().register(buildMobEntry(EntityXmasSkeleton.class, "skeleton.xmas.elf", Color.GREEN, Color.CYAN));
        event.getRegistry().register(buildMobEntry(EntityXmasSkeletonBoss.class, "skeleton.xmas.boss", Color.GREEN, Color.CYAN));
        event.getRegistry().register(buildMobEntry(EntityXmasSnowman.class, "skeleton.xmas.snowman", Color.BLACK, Color.CYAN));

        //Red team
        event.getRegistry().register(buildMobEntry(EntityXmasZombie.class, "zombie.xmas.elf", Color.RED, Color.CYAN));
        event.getRegistry().register(buildMobEntry(EntityXmasZombieBoss.class, "zombie.xmas.boss", Color.RED, Color.CYAN));
        event.getRegistry().register(buildMobEntry(EntityXmasCreeper.class, "zombie.xmas.creeper", Color.RED, Color.CYAN));


        event.getRegistry().register(buildEntityEntry(EntityXmasRPG.class, "skeleton.snowman.rocket", 64, 1));

    }

    private static EntityEntry buildEntityEntry(Class<? extends Entity> entityClass, ResourceLocation name, int trackingRange, int updateFrequency) {
        EntityEntryBuilder builder = EntityEntryBuilder.create();
        builder.name(name.toString());
        builder.id(name, nextEntityID++);
        builder.tracker(trackingRange, updateFrequency, true);
        builder.entity(entityClass);
        return builder.build();
    }

    */

}

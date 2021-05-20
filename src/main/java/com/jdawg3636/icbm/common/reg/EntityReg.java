package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.common.entity.EntityPrimedExplosives;
import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.entity.EntityMissile;
import com.jdawg3636.icbm.common.event.BlastEvent;
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

    // Primed Explosives Registration
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_CONDENSED         = registerPrimedExplosives(BlastEvent.Condensed::new,           ItemReg.EXPLOSIVES_CONDENSED);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_SHRAPNEL          = registerPrimedExplosives(BlastEvent.Shrapnel::new,            ItemReg.EXPLOSIVES_SHRAPNEL);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_INCENDIARY        = registerPrimedExplosives(BlastEvent.Incendiary::new,          ItemReg.EXPLOSIVES_INCENDIARY);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_DEBILITATION      = registerPrimedExplosives(BlastEvent.Debilitation::new,        ItemReg.EXPLOSIVES_DEBILITATION);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_CHEMICAL          = registerPrimedExplosives(BlastEvent.Chemical::new,            ItemReg.EXPLOSIVES_CHEMICAL);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ANVIL             = registerPrimedExplosives(BlastEvent.Anvil::new,               ItemReg.EXPLOSIVES_ANVIL);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_REPULSIVE         = registerPrimedExplosives(BlastEvent.Repulsive::new,           ItemReg.EXPLOSIVES_REPULSIVE);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ATTRACTIVE        = registerPrimedExplosives(BlastEvent.Attractive::new,          ItemReg.EXPLOSIVES_ATTRACTIVE);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_NIGHTMARE         = registerPrimedExplosives(BlastEvent.Nightmare::new,           ItemReg.EXPLOSIVES_NIGHTMARE);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_FRAGMENTATION     = registerPrimedExplosives(BlastEvent.Fragmentation::new,       ItemReg.EXPLOSIVES_FRAGMENTATION);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_CONTAGIOUS        = registerPrimedExplosives(BlastEvent.Contagious::new,          ItemReg.EXPLOSIVES_CONTAGIOUS);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_SONIC             = registerPrimedExplosives(BlastEvent.Sonic::new,               ItemReg.EXPLOSIVES_SONIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_BREACHING         = registerPrimedExplosives(BlastEvent.Breaching::new,           ItemReg.EXPLOSIVES_BREACHING);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_REJUVENATION      = registerPrimedExplosives(BlastEvent.Rejuvenation::new,        ItemReg.EXPLOSIVES_REJUVENATION);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_THERMOBARIC       = registerPrimedExplosives(BlastEvent.Thermobaric::new,         ItemReg.EXPLOSIVES_THERMOBARIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_NUCLEAR           = registerPrimedExplosives(BlastEvent.Nuclear::new,             ItemReg.EXPLOSIVES_NUCLEAR);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_EMP               = registerPrimedExplosives(BlastEvent.Emp::new,                 ItemReg.EXPLOSIVES_EMP);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_EXOTHERMIC        = registerPrimedExplosives(BlastEvent.Exothermic::new,          ItemReg.EXPLOSIVES_EXOTHERMIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ENDOTHERMIC       = registerPrimedExplosives(BlastEvent.Endothermic::new,         ItemReg.EXPLOSIVES_ENDOTHERMIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ANTIGRAVITATIONAL = registerPrimedExplosives(BlastEvent.Antigravitational::new,   ItemReg.EXPLOSIVES_ANTIGRAVITATIONAL);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ENDER             = registerPrimedExplosives(BlastEvent.Ender::new,               ItemReg.EXPLOSIVES_ENDER);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_HYPERSONIC        = registerPrimedExplosives(BlastEvent.Hypersonic::new,          ItemReg.EXPLOSIVES_HYPERSONIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ANTIMATTER        = registerPrimedExplosives(BlastEvent.Antimatter::new,          ItemReg.EXPLOSIVES_ANTIMATTER);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_REDMATTER         = registerPrimedExplosives(BlastEvent.Redmatter::new,           ItemReg.EXPLOSIVES_REDMATTER);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> S_MINE                       = registerPrimedExplosives(BlastEvent.SMine::new,               ItemReg.S_MINE, 0.875F, 0.125F);

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
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_FRAGMENTATION = registerMissile(ItemReg.MISSILE_FRAGMENTATION, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CONTAGIOUS = registerMissile(ItemReg.MISSILE_CONTAGIOUS, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_SONIC = registerMissile(ItemReg.MISSILE_SONIC, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_BREACHING = registerMissile(ItemReg.MISSILE_BREACHING, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_REJUVENATION = registerMissile(ItemReg.MISSILE_REJUVENATION, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_THERMOBARIC = registerMissile(ItemReg.MISSILE_THERMOBARIC, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_NUCLEAR = registerMissile(ItemReg.MISSILE_NUCLEAR, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_EMP = registerMissile(ItemReg.MISSILE_EMP, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_EXOTHERMIC = registerMissile(ItemReg.MISSILE_EXOTHERMIC, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ENDOTHERMIC = registerMissile(ItemReg.MISSILE_ENDOTHERMIC, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANTIGRAVITATIONAL = registerMissile(ItemReg.MISSILE_ANTIGRAVITATIONAL, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ENDER = registerMissile(ItemReg.MISSILE_ENDER, 1F, 6F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_HYPERSONIC = registerMissile(ItemReg.MISSILE_HYPERSONIC, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANTIMATTER = registerMissile(ItemReg.MISSILE_ANTIMATTER);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_REDMATTER = registerMissile(ItemReg.MISSILE_REDMATTER);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_HOMING = registerMissile(ItemReg.MISSILE_HOMING);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANTIBALLISTIC = registerMissile(ItemReg.MISSILE_ANTIBALLISTIC, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CLUSTER = registerMissile(ItemReg.MISSILE_CLUSTER, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CLUSTER_NUCLEAR = registerMissile(ItemReg.MISSILE_CLUSTER_NUCLEAR, 1F, 5F);

    public static RegistryObject<EntityType<EntityPrimedExplosives>> registerPrimedExplosives(BlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm) {
        return registerPrimedExplosives(blastEventProvider, itemForm, 0.98F, 0.98F);
    }

    public static RegistryObject<EntityType<EntityPrimedExplosives>> registerPrimedExplosives(BlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm, float width, float height) {
        return ENTITIES.register(
                itemForm.getId().getPath(),
                () -> {
                    return EntityType.Builder.<EntityPrimedExplosives>create(
                            (type, world) -> new EntityPrimedExplosives(type, world, blastEventProvider, itemForm, 0, 0, 0, null),
                            EntityClassification.MISC
                    )
                            .immuneToFire().size(width, height).trackingRange(10).func_233608_b_(10)
                            .build(itemForm.getId().getPath());
                }
        );
    }

    public static RegistryObject<EntityType<EntityMissile>> registerMissile(RegistryObject<Item> missileItem) {
        return registerMissile(missileItem, 1F, 4F);
    }

    public static RegistryObject<EntityType<EntityMissile>> registerMissile(RegistryObject<Item> missileItem, float width, float height) {
        return ENTITIES.register(
                missileItem.getId().getPath(),
                () -> {
                        return EntityType.Builder.<EntityMissile>create(
                                (type, world) -> new EntityMissile(type, world, missileItem),
                                EntityClassification.MISC
                        )
                        .immuneToFire().size(width, height).trackingRange(10).func_233608_b_(10)
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

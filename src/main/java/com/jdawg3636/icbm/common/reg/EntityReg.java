package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.entity.*;
import com.jdawg3636.icbm.common.event.BlastEventRegistryEntry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
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
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_CONDENSED         = registerPrimedExplosives(BlastEventReg.CONDENSED,           ItemReg.EXPLOSIVES_CONDENSED);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_SHRAPNEL          = registerPrimedExplosives(BlastEventReg.SHRAPNEL,            ItemReg.EXPLOSIVES_SHRAPNEL);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_INCENDIARY        = registerPrimedExplosives(BlastEventReg.INCENDIARY,          ItemReg.EXPLOSIVES_INCENDIARY);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_DEBILITATION      = registerPrimedExplosives(BlastEventReg.DEBILITATION,        ItemReg.EXPLOSIVES_DEBILITATION);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_CHEMICAL          = registerPrimedExplosives(BlastEventReg.CHEMICAL,            ItemReg.EXPLOSIVES_CHEMICAL);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ANVIL             = registerPrimedExplosives(BlastEventReg.ANVIL,               ItemReg.EXPLOSIVES_ANVIL);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_REPULSIVE         = registerPrimedExplosives(BlastEventReg.REPULSIVE,           ItemReg.EXPLOSIVES_REPULSIVE);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ATTRACTIVE        = registerPrimedExplosives(BlastEventReg.ATTRACTIVE,          ItemReg.EXPLOSIVES_ATTRACTIVE);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_NIGHTMARE         = registerPrimedExplosives(BlastEventReg.NIGHTMARE,           ItemReg.EXPLOSIVES_NIGHTMARE);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_FRAGMENTATION     = registerPrimedExplosives(BlastEventReg.FRAGMENTATION,       ItemReg.EXPLOSIVES_FRAGMENTATION);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_CONTAGION         = registerPrimedExplosives(BlastEventReg.CONTAGION,           ItemReg.EXPLOSIVES_CONTAGION);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_SONIC             = registerPrimedExplosives(BlastEventReg.SONIC,               ItemReg.EXPLOSIVES_SONIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_BREACHING         = registerPrimedExplosives(BlastEventReg.BREACHING,           ItemReg.EXPLOSIVES_BREACHING);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_REJUVENATION      = registerPrimedExplosives(BlastEventReg.REJUVENATION,        ItemReg.EXPLOSIVES_REJUVENATION);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_THERMOBARIC       = registerPrimedExplosives(BlastEventReg.THERMOBARIC,         ItemReg.EXPLOSIVES_THERMOBARIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_NUCLEAR           = registerPrimedExplosives(BlastEventReg.NUCLEAR,             ItemReg.EXPLOSIVES_NUCLEAR);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_EMP               = registerPrimedExplosives(BlastEventReg.EMP,                 ItemReg.EXPLOSIVES_EMP);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_EXOTHERMIC        = registerPrimedExplosives(BlastEventReg.EXOTHERMIC,          ItemReg.EXPLOSIVES_EXOTHERMIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ENDOTHERMIC       = registerPrimedExplosives(BlastEventReg.ENDOTHERMIC,         ItemReg.EXPLOSIVES_ENDOTHERMIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ANTIGRAVITATIONAL = registerPrimedExplosives(BlastEventReg.ANTIGRAVITATIONAL,   ItemReg.EXPLOSIVES_ANTIGRAVITATIONAL);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ENDER             = registerPrimedExplosives(BlastEventReg.ENDER,               ItemReg.EXPLOSIVES_ENDER);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_HYPERSONIC        = registerPrimedExplosives(BlastEventReg.HYPERSONIC,          ItemReg.EXPLOSIVES_HYPERSONIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ANTIMATTER        = registerPrimedExplosives(BlastEventReg.ANTIMATTER,          ItemReg.EXPLOSIVES_ANTIMATTER);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_REDMATTER         = registerPrimedExplosives(BlastEventReg.REDMATTER,           ItemReg.EXPLOSIVES_REDMATTER);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> S_MINE                       = registerPrimedExplosives(BlastEventReg.S_MINE,              ItemReg.S_MINE, 0.875F, 0.125F);

    // Missile Registration
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_MODULE            = registerMissile(BlastEventReg.DUMMY,                ItemReg.MISSILE_MODULE);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CONVENTIONAL      = registerMissile(BlastEventReg.CONDENSED,            ItemReg.MISSILE_CONVENTIONAL);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_SHRAPNEL          = registerMissile(BlastEventReg.SHRAPNEL,             ItemReg.MISSILE_SHRAPNEL);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_INCENDIARY        = registerMissile(BlastEventReg.INCENDIARY,           ItemReg.MISSILE_INCENDIARY);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_DEBILITATION      = registerMissile(BlastEventReg.DEBILITATION,         ItemReg.MISSILE_DEBILITATION);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CHEMICAL          = registerMissile(BlastEventReg.CHEMICAL,             ItemReg.MISSILE_CHEMICAL);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANVIL             = registerMissile(BlastEventReg.ANVIL,                ItemReg.MISSILE_ANVIL);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_REPULSIVE         = registerMissile(BlastEventReg.REPULSIVE,            ItemReg.MISSILE_REPULSIVE);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ATTRACTIVE        = registerMissile(BlastEventReg.ATTRACTIVE,           ItemReg.MISSILE_ATTRACTIVE);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_NIGHTMARE         = registerMissile(BlastEventReg.NIGHTMARE,            ItemReg.MISSILE_NIGHTMARE);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_FRAGMENTATION     = registerMissile(BlastEventReg.FRAGMENTATION,        ItemReg.MISSILE_FRAGMENTATION, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CONTAGION         = registerMissile(BlastEventReg.CONTAGION,            ItemReg.MISSILE_CONTAGION, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_SONIC             = registerMissile(BlastEventReg.SONIC,                ItemReg.MISSILE_SONIC, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_BREACHING         = registerMissile(BlastEventReg.BREACHING,            ItemReg.MISSILE_BREACHING, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_REJUVENATION      = registerMissile(BlastEventReg.REJUVENATION,         ItemReg.MISSILE_REJUVENATION, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_THERMOBARIC       = registerMissile(BlastEventReg.THERMOBARIC,          ItemReg.MISSILE_THERMOBARIC, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_NUCLEAR           = registerMissile(BlastEventReg.NUCLEAR,              ItemReg.MISSILE_NUCLEAR, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_EMP               = registerMissile(BlastEventReg.EMP,                  ItemReg.MISSILE_EMP, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_EXOTHERMIC        = registerMissile(BlastEventReg.EXOTHERMIC,           ItemReg.MISSILE_EXOTHERMIC, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ENDOTHERMIC       = registerMissile(BlastEventReg.ENDOTHERMIC,          ItemReg.MISSILE_ENDOTHERMIC, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANTIGRAVITATIONAL = registerMissile(BlastEventReg.ANTIGRAVITATIONAL,    ItemReg.MISSILE_ANTIGRAVITATIONAL, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ENDER             = registerMissile(BlastEventReg.ENDER,                ItemReg.MISSILE_ENDER, 1F, 6F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_HYPERSONIC        = registerMissile(BlastEventReg.HYPERSONIC,           ItemReg.MISSILE_HYPERSONIC, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANTIMATTER        = registerMissile(BlastEventReg.ANTIMATTER,           ItemReg.MISSILE_ANTIMATTER);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_REDMATTER         = registerMissile(BlastEventReg.REDMATTER,            ItemReg.MISSILE_REDMATTER);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_HOMING            = registerMissile(BlastEventReg.DUMMY, /*TODO*/       ItemReg.MISSILE_HOMING);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANTIBALLISTIC     = registerMissile(BlastEventReg.DUMMY, /*TODO*/       ItemReg.MISSILE_ANTIBALLISTIC, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CLUSTER           = registerMissile(BlastEventReg.DUMMY, /*TODO*/       ItemReg.MISSILE_CLUSTER, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CLUSTER_NUCLEAR   = registerMissile(BlastEventReg.DUMMY, /*TODO*/       ItemReg.MISSILE_CLUSTER_NUCLEAR, 1F, 5F);

    // Minecart Registration
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_EXPLOSIVE         = registerMinecart("minecart_explosive",         BlockReg.EXPLOSIVES_CONDENSED,         ItemReg.MINECART_EXPLOSIVE);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_SHRAPNEL          = registerMinecart("minecart_shrapnel",          BlockReg.EXPLOSIVES_SHRAPNEL,          ItemReg.MINECART_SHRAPNEL);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_INCENDIARY        = registerMinecart("minecart_incendiary",        BlockReg.EXPLOSIVES_INCENDIARY,        ItemReg.MINECART_INCENDIARY);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_DEBILITATION      = registerMinecart("minecart_debilitation",      BlockReg.EXPLOSIVES_DEBILITATION,      ItemReg.MINECART_DEBILITATION);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_CHEMICAL          = registerMinecart("minecart_chemical",          BlockReg.EXPLOSIVES_CHEMICAL,          ItemReg.MINECART_CHEMICAL);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_ANVIL             = registerMinecart("minecart_anvil",             BlockReg.EXPLOSIVES_ANVIL,             ItemReg.MINECART_ANVIL);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_REPULSIVE         = registerMinecart("minecart_repulsive",         BlockReg.EXPLOSIVES_REPULSIVE,         ItemReg.MINECART_REPULSIVE);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_ATTRACTIVE        = registerMinecart("minecart_attractive",        BlockReg.EXPLOSIVES_ATTRACTIVE,        ItemReg.MINECART_ATTRACTIVE);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_NIGHTMARE         = registerMinecart("minecart_nightmare",         BlockReg.EXPLOSIVES_NIGHTMARE,         ItemReg.MINECART_NIGHTMARE);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_FRAGMENTATION     = registerMinecart("minecart_fragmentation",     BlockReg.EXPLOSIVES_FRAGMENTATION,     ItemReg.MINECART_FRAGMENTATION);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_CONTAGION         = registerMinecart("minecart_contagion",         BlockReg.EXPLOSIVES_CONTAGION,         ItemReg.MINECART_CONTAGION);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_SONIC             = registerMinecart("minecart_sonic",             BlockReg.EXPLOSIVES_SONIC,             ItemReg.MINECART_SONIC);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_BREACHING         = registerMinecart("minecart_breaching",         BlockReg.EXPLOSIVES_BREACHING,         ItemReg.MINECART_BREACHING);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_REJUVENATION      = registerMinecart("minecart_rejuvenation",      BlockReg.EXPLOSIVES_REJUVENATION,      ItemReg.MINECART_REJUVENATION);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_THERMOBARIC       = registerMinecart("minecart_thermobaric",       BlockReg.EXPLOSIVES_THERMOBARIC,       ItemReg.MINECART_THERMOBARIC);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_NUCLEAR           = registerMinecart("minecart_nuclear",           BlockReg.EXPLOSIVES_NUCLEAR,           ItemReg.MINECART_NUCLEAR);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_EMP               = registerMinecart("minecart_emp",               BlockReg.EXPLOSIVES_EMP,               ItemReg.MINECART_EMP);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_EXOTHERMIC        = registerMinecart("minecart_exothermic",        BlockReg.EXPLOSIVES_EXOTHERMIC,        ItemReg.MINECART_EXOTHERMIC);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_ENDOTHERMIC       = registerMinecart("minecart_endothermic",       BlockReg.EXPLOSIVES_ENDOTHERMIC,       ItemReg.MINECART_ENDOTHERMIC);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_ANTIGRAVITATIONAL = registerMinecart("minecart_antigravitational", BlockReg.EXPLOSIVES_ANTIGRAVITATIONAL, ItemReg.MINECART_ANTIGRAVITATIONAL);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_ENDER             = registerMinecart("minecart_ender",             BlockReg.EXPLOSIVES_ENDER,             ItemReg.MINECART_ENDER);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_HYPERSONIC        = registerMinecart("minecart_hypersonic",        BlockReg.EXPLOSIVES_HYPERSONIC,        ItemReg.MINECART_HYPERSONIC);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_ANTIMATTER        = registerMinecart("minecart_antimatter",        BlockReg.EXPLOSIVES_ANTIMATTER,        ItemReg.MINECART_ANTIMATTER);
    public static final RegistryObject<EntityType<EntityMinecartExplosives>> MINECART_REDMATTER         = registerMinecart("minecart_redmatter",         BlockReg.EXPLOSIVES_REDMATTER,         ItemReg.MINECART_REDMATTER);

    // Grenade Registration
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_CONVENTIONAL = registerGrenade(BlastEventReg.CONDENSED,    ItemReg.GRENADE_CONVENTIONAL);
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_SHRAPNEL     = registerGrenade(BlastEventReg.SHRAPNEL,     ItemReg.GRENADE_SHRAPNEL);
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_INCENDIARY   = registerGrenade(BlastEventReg.INCENDIARY,   ItemReg.GRENADE_INCENDIARY);
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_DEBILITATION = registerGrenade(BlastEventReg.DEBILITATION, ItemReg.GRENADE_DEBILITATION);
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_CHEMICAL     = registerGrenade(BlastEventReg.CHEMICAL,     ItemReg.GRENADE_CHEMICAL);
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_ANVIL        = registerGrenade(BlastEventReg.ANVIL,        ItemReg.GRENADE_ANVIL);
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_REPULSIVE    = registerGrenade(BlastEventReg.REPULSIVE,    ItemReg.GRENADE_REPULSIVE);
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_ATTRACTIVE   = registerGrenade(BlastEventReg.ATTRACTIVE,   ItemReg.GRENADE_ATTRACTIVE);

    // Blast Utility Entity Registration
    public static final RegistryObject<EntityType<EntityLingeringBlast>>          BLAST_CHEMICAL     = registerBlastUtilityEntity("blast_chemical",     EntityLingeringBlastChemical::new);
    public static final RegistryObject<EntityType<EntityLingeringBlast>>          BLAST_CONTAGION    = registerBlastUtilityEntity("blast_contagion",    EntityLingeringBlastContagion::new);
    public static final RegistryObject<EntityType<EntityLingeringBlast>>          BLAST_DEBILITATION = registerBlastUtilityEntity("blast_debilitation", EntityLingeringBlastDebilitation::new);
    public static final RegistryObject<EntityType<EntityLingeringBlastRadiation>> BLAST_RADIATION    = registerBlastUtilityEntity("blast_radiation",    EntityLingeringBlastRadiation::new);
    public static final RegistryObject<EntityType<EntityRedmatterBlast>>          BLAST_REDMATTER    = registerBlastUtilityEntity("blast_redmatter",    EntityRedmatterBlast::new);
    public static final RegistryObject<EntityType<EntitySonicBlast>>              BLAST_SONIC        = registerBlastUtilityEntity("blast_sonic",        EntitySonicBlast::new);
    public static final RegistryObject<EntityType<EntityShrapnel>>                SHRAPNEL           = ENTITIES.register(
        "shrapnel",
        () -> {
            return EntityType.Builder.<EntityShrapnel>of(
                    EntityShrapnel::new,
                    EntityClassification.MISC
            )
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20)
            .build("shrapnel");
        }
    );

    // Other Entity Registration
    public static final RegistryObject<EntityType<EntityAcceleratingParticle>> ACCELERATING_PARTICLE = registerBlastUtilityEntity("accelerating_particle", EntityAcceleratingParticle::new, 1F, 1F);
    public static final RegistryObject<EntityType<EntityLightningVisual>> LIGHTNING_VISUAL = registerBlastUtilityEntity("lightning_visual", EntityLightningVisual::new, 1F, 1F);

    public static RegistryObject<EntityType<EntityPrimedExplosives>> registerPrimedExplosives(RegistryObject<BlastEventRegistryEntry> blastEventProvider, RegistryObject<Item> itemForm) {
        return registerPrimedExplosives(blastEventProvider, itemForm, 0.98F, 0.98F);
    }

    public static RegistryObject<EntityType<EntityPrimedExplosives>> registerPrimedExplosives(RegistryObject<BlastEventRegistryEntry> blastEventProvider, RegistryObject<Item> itemForm, float width, float height) {
        return ENTITIES.register(
                itemForm.getId().getPath(),
                () -> {
                    return EntityType.Builder.<EntityPrimedExplosives>of(
                            (type, world) -> new EntityPrimedExplosives(type, world, blastEventProvider, itemForm, 0, 0, 0, null),
                            EntityClassification.MISC
                    )
                    .fireImmune()
                    .sized(width, height)
                    .clientTrackingRange(10)
                    .updateInterval(10)
                    .build(itemForm.getId().getPath());
                }
        );
    }

    public static RegistryObject<EntityType<EntityMissile>> registerMissile(RegistryObject<BlastEventRegistryEntry> blastEventProvider, RegistryObject<Item> itemForm) {
        return registerMissile(blastEventProvider, itemForm, 1F, 4F);
    }

    public static RegistryObject<EntityType<EntityMissile>> registerMissile(RegistryObject<BlastEventRegistryEntry> blastEventProvider, RegistryObject<Item> itemForm, float width, float height) {
        return ENTITIES.register(
                itemForm.getId().getPath(),
                () -> {
                    return EntityType.Builder.<EntityMissile>of(
                            (type, world) -> {
                                EntityMissile toReturn = new EntityMissile(type, world, blastEventProvider, itemForm);
                                toReturn.setRot(0F, 90F);
                                return toReturn;
                            },
                            EntityClassification.MISC
                    )
                    .fireImmune()
                    .sized(width, height)
                    .clientTrackingRange(10)
                    .updateInterval(2)
                    .build(itemForm.getId().getPath());
                }
        );
    }

    public static <T extends EntityMinecartExplosives> RegistryObject<EntityType<EntityMinecartExplosives>> registerMinecart(String entityName, RegistryObject<Block> blockForm, RegistryObject<Item> itemForm) {
        return ENTITIES.register(
                entityName,
                () -> {
                    return EntityType.Builder.<EntityMinecartExplosives>of(
                            (type, world) -> new EntityMinecartExplosives(type, world, blockForm, itemForm),
                            EntityClassification.MISC
                    )
                    .sized(0.98F, 0.7F)
                    .clientTrackingRange(8)
                    .build(entityName);
                }
        );
    }

    public static <T extends EntityGrenade> RegistryObject<EntityType<EntityGrenade>> registerGrenade(RegistryObject<BlastEventRegistryEntry> blastEventProvider, RegistryObject<Item> itemForm) {
        return ENTITIES.register(
                itemForm.getId().getPath(),
                () -> {
                    return EntityType.Builder.<EntityGrenade>of(
                            (type, world) -> new EntityGrenade(type, world, blastEventProvider, itemForm),
                            EntityClassification.MISC
                    )
                    .fireImmune()
                    .sized(0.25F, 0.25F)
                    .build(itemForm.getId().getPath());
                }
        );
    }

    public static <T extends Entity> RegistryObject<EntityType<T>> registerBlastUtilityEntity(String entityName, EntityType.IFactory<T> entityConstructor) {
        return registerBlastUtilityEntity(entityName, entityConstructor, 1F, 2F);
    }

    public static <T extends Entity> RegistryObject<EntityType<T>> registerBlastUtilityEntity(String entityName, EntityType.IFactory<T> entityConstructor, float width, float height) {
        //noinspection RedundantTypeArguments
        return ENTITIES.register(
                entityName,
                ()->EntityType.Builder.<T>of(
                        entityConstructor,
                        EntityClassification.MISC
                )
                .fireImmune()
                .sized(width, height)
                .build(entityName)
        );
    }

}

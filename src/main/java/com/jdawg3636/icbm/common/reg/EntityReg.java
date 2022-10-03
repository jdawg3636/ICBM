package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.entity.*;
import com.jdawg3636.icbm.common.event.*;
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
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_CONDENSED         = registerPrimedExplosives(EventBlastCondensed::new,           ItemReg.EXPLOSIVES_CONDENSED);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_SHRAPNEL          = registerPrimedExplosives(EventBlastShrapnel::new,            ItemReg.EXPLOSIVES_SHRAPNEL);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_INCENDIARY        = registerPrimedExplosives(EventBlastIncendiary::new,          ItemReg.EXPLOSIVES_INCENDIARY);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_DEBILITATION      = registerPrimedExplosives(EventBlastDebilitation::new,        ItemReg.EXPLOSIVES_DEBILITATION);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_CHEMICAL          = registerPrimedExplosives(EventBlastChemical::new,            ItemReg.EXPLOSIVES_CHEMICAL);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ANVIL             = registerPrimedExplosives(EventBlastAnvil::new,               ItemReg.EXPLOSIVES_ANVIL);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_REPULSIVE         = registerPrimedExplosives(EventBlastRepulsive::new,           ItemReg.EXPLOSIVES_REPULSIVE);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ATTRACTIVE        = registerPrimedExplosives(EventBlastAttractive::new,          ItemReg.EXPLOSIVES_ATTRACTIVE);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_NIGHTMARE         = registerPrimedExplosives(EventBlastNightmare::new,           ItemReg.EXPLOSIVES_NIGHTMARE);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_FRAGMENTATION     = registerPrimedExplosives(EventBlastFragmentation::new,       ItemReg.EXPLOSIVES_FRAGMENTATION);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_CONTAGION         = registerPrimedExplosives(EventBlastContagion::new,           ItemReg.EXPLOSIVES_CONTAGION);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_SONIC             = registerPrimedExplosives(EventBlastSonic::new,               ItemReg.EXPLOSIVES_SONIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_BREACHING         = registerPrimedExplosives(EventBlastBreaching::new,           ItemReg.EXPLOSIVES_BREACHING);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_REJUVENATION      = registerPrimedExplosives(EventBlastRejuvenation::new,        ItemReg.EXPLOSIVES_REJUVENATION);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_THERMOBARIC       = registerPrimedExplosives(EventBlastThermobaric::new,         ItemReg.EXPLOSIVES_THERMOBARIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_NUCLEAR           = registerPrimedExplosives(EventBlastNuclear::new,             ItemReg.EXPLOSIVES_NUCLEAR);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_EMP               = registerPrimedExplosives(EventBlastEmp::new,                 ItemReg.EXPLOSIVES_EMP);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_EXOTHERMIC        = registerPrimedExplosives(EventBlastExothermic::new,          ItemReg.EXPLOSIVES_EXOTHERMIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ENDOTHERMIC       = registerPrimedExplosives(EventBlastEndothermic::new,         ItemReg.EXPLOSIVES_ENDOTHERMIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ANTIGRAVITATIONAL = registerPrimedExplosives(EventBlastAntigravitational::new,   ItemReg.EXPLOSIVES_ANTIGRAVITATIONAL);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ENDER             = registerPrimedExplosives(EventBlastEnder::new,               ItemReg.EXPLOSIVES_ENDER);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_HYPERSONIC        = registerPrimedExplosives(EventBlastHypersonic::new,          ItemReg.EXPLOSIVES_HYPERSONIC);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_ANTIMATTER        = registerPrimedExplosives(EventBlastAntimatter::new,          ItemReg.EXPLOSIVES_ANTIMATTER);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> EXPLOSIVES_REDMATTER         = registerPrimedExplosives(EventBlastRedmatter::new,           ItemReg.EXPLOSIVES_REDMATTER);
    public static final RegistryObject<EntityType<EntityPrimedExplosives>> S_MINE                       = registerPrimedExplosives(EventBlastSMine::new,               ItemReg.S_MINE, 0.875F, 0.125F);

    // Missile Registration
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_MODULE            = registerMissile(EventBlastDummy::new, /*TODO*/       ItemReg.MISSILE_MODULE);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CONVENTIONAL      = registerMissile(EventBlastCondensed::new,            ItemReg.MISSILE_CONVENTIONAL);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_SHRAPNEL          = registerMissile(EventBlastShrapnel::new,             ItemReg.MISSILE_SHRAPNEL);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_INCENDIARY        = registerMissile(EventBlastIncendiary::new,           ItemReg.MISSILE_INCENDIARY);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_DEBILITATION      = registerMissile(EventBlastDebilitation::new,         ItemReg.MISSILE_DEBILITATION);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CHEMICAL          = registerMissile(EventBlastChemical::new,             ItemReg.MISSILE_CHEMICAL);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANVIL             = registerMissile(EventBlastAnvil::new,                ItemReg.MISSILE_ANVIL);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_REPULSIVE         = registerMissile(EventBlastRepulsive::new,            ItemReg.MISSILE_REPULSIVE);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ATTRACTIVE        = registerMissile(EventBlastAttractive::new,           ItemReg.MISSILE_ATTRACTIVE);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_NIGHTMARE         = registerMissile(EventBlastNightmare::new,            ItemReg.MISSILE_NIGHTMARE);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_FRAGMENTATION     = registerMissile(EventBlastFragmentation::new,        ItemReg.MISSILE_FRAGMENTATION, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CONTAGION         = registerMissile(EventBlastContagion::new,            ItemReg.MISSILE_CONTAGION, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_SONIC             = registerMissile(EventBlastSonic::new,                ItemReg.MISSILE_SONIC, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_BREACHING         = registerMissile(EventBlastBreaching::new,            ItemReg.MISSILE_BREACHING, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_REJUVENATION      = registerMissile(EventBlastRejuvenation::new,         ItemReg.MISSILE_REJUVENATION, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_THERMOBARIC       = registerMissile(EventBlastThermobaric::new,          ItemReg.MISSILE_THERMOBARIC, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_NUCLEAR           = registerMissile(EventBlastNuclear::new,              ItemReg.MISSILE_NUCLEAR, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_EMP               = registerMissile(EventBlastEmp::new,                  ItemReg.MISSILE_EMP, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_EXOTHERMIC        = registerMissile(EventBlastExothermic::new,           ItemReg.MISSILE_EXOTHERMIC, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ENDOTHERMIC       = registerMissile(EventBlastEndothermic::new,          ItemReg.MISSILE_ENDOTHERMIC, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANTIGRAVITATIONAL = registerMissile(EventBlastAntigravitational::new,    ItemReg.MISSILE_ANTIGRAVITATIONAL, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ENDER             = registerMissile(EventBlastEnder::new,                ItemReg.MISSILE_ENDER, 1F, 6F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_HYPERSONIC        = registerMissile(EventBlastHypersonic::new,           ItemReg.MISSILE_HYPERSONIC, 1F, 7F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANTIMATTER        = registerMissile(EventBlastAntimatter::new,           ItemReg.MISSILE_ANTIMATTER);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_REDMATTER         = registerMissile(EventBlastRedmatter::new,            ItemReg.MISSILE_REDMATTER);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_HOMING            = registerMissile(EventBlastDummy::new, /*TODO*/       ItemReg.MISSILE_HOMING);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_ANTIBALLISTIC     = registerMissile(EventBlastDummy::new, /*TODO*/       ItemReg.MISSILE_ANTIBALLISTIC, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CLUSTER           = registerMissile(EventBlastDummy::new, /*TODO*/       ItemReg.MISSILE_CLUSTER, 1F, 5F);
    public static final RegistryObject<EntityType<EntityMissile>> MISSILE_CLUSTER_NUCLEAR   = registerMissile(EventBlastDummy::new, /*TODO*/       ItemReg.MISSILE_CLUSTER_NUCLEAR, 1F, 5F);

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
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_CONVENTIONAL = registerGrenade(EventBlastCondensed::new,    ItemReg.GRENADE_CONVENTIONAL);
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_SHRAPNEL     = registerGrenade(EventBlastShrapnel::new,     ItemReg.GRENADE_SHRAPNEL);
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_INCENDIARY   = registerGrenade(EventBlastIncendiary::new,   ItemReg.GRENADE_INCENDIARY);
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_DEBILITATION = registerGrenade(EventBlastDebilitation::new, ItemReg.GRENADE_DEBILITATION);
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_CHEMICAL     = registerGrenade(EventBlastChemical::new,     ItemReg.GRENADE_CHEMICAL);
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_ANVIL        = registerGrenade(EventBlastAnvil::new,        ItemReg.GRENADE_ANVIL);
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_REPULSIVE    = registerGrenade(EventBlastRepulsive::new,    ItemReg.GRENADE_REPULSIVE);
    public static final RegistryObject<EntityType<EntityGrenade>> GRENADE_ATTRACTIVE   = registerGrenade(EventBlastAttractive::new,   ItemReg.GRENADE_ATTRACTIVE);

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

    public static RegistryObject<EntityType<EntityPrimedExplosives>> registerPrimedExplosives(AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm) {
        return registerPrimedExplosives(blastEventProvider, itemForm, 0.98F, 0.98F);
    }

    public static RegistryObject<EntityType<EntityPrimedExplosives>> registerPrimedExplosives(AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm, float width, float height) {
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

    public static RegistryObject<EntityType<EntityMissile>> registerMissile(AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm) {
        return registerMissile(blastEventProvider, itemForm, 1F, 4F);
    }

    public static RegistryObject<EntityType<EntityMissile>> registerMissile(AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm, float width, float height) {
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

    public static <T extends EntityGrenade> RegistryObject<EntityType<EntityGrenade>> registerGrenade(AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm) {
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

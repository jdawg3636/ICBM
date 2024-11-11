package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.event.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class BlastEventReg {

    public static final DeferredRegister<BlastEventRegistryEntry> BLAST_EVENTS = DeferredRegister.create(BlastEventRegistryEntry.class, ICBMReference.MODID);

    public static RegistryObject<BlastEventRegistryEntry> getRegistryObjectFromResourceLocation(ResourceLocation resourceLocation) {
        return BLAST_EVENTS.getEntries().stream().filter(ro -> ro.getId().equals(resourceLocation)).findAny().orElseThrow(() -> new RuntimeException(String.format("Unable to deserialize blast event: \"%s\"", resourceLocation)));
    }

    public static RegistryObject<BlastEventRegistryEntry> ANTIBALLISTIC     = BLAST_EVENTS.register("antiballistic",        () -> new BlastEventRegistryEntry(EventBlastAntiballistic::new));
    public static RegistryObject<BlastEventRegistryEntry> ANTIGRAVITATIONAL = BLAST_EVENTS.register("antigravitational",    () -> new BlastEventRegistryEntry(EventBlastAntigravitational::new));
    public static RegistryObject<BlastEventRegistryEntry> ANTIMATTER        = BLAST_EVENTS.register("antimatter",           () -> new BlastEventRegistryEntry(EventBlastAntimatter::new));
    public static RegistryObject<BlastEventRegistryEntry> ANVIL             = BLAST_EVENTS.register("anvil",                () -> new BlastEventRegistryEntry(EventBlastAnvil::new));
    public static RegistryObject<BlastEventRegistryEntry> ATTRACTIVE        = BLAST_EVENTS.register("attractive",           () -> new BlastEventRegistryEntry(EventBlastAttractive::new));
    public static RegistryObject<BlastEventRegistryEntry> BREACHING         = BLAST_EVENTS.register("breaching",            () -> new BlastEventRegistryEntry(EventBlastBreaching::new));
    public static RegistryObject<BlastEventRegistryEntry> CHEMICAL          = BLAST_EVENTS.register("chemical",             () -> new BlastEventRegistryEntry(EventBlastChemical::new));
    public static RegistryObject<BlastEventRegistryEntry> CONDENSED         = BLAST_EVENTS.register("condensed",            () -> new BlastEventRegistryEntry(EventBlastCondensed::new));
    public static RegistryObject<BlastEventRegistryEntry> CONTAGION         = BLAST_EVENTS.register("contagion",            () -> new BlastEventRegistryEntry(EventBlastContagion::new));
    public static RegistryObject<BlastEventRegistryEntry> DEBILITATION      = BLAST_EVENTS.register("debilitation",         () -> new BlastEventRegistryEntry(EventBlastDebilitation::new));
    public static RegistryObject<BlastEventRegistryEntry> DUMMY             = BLAST_EVENTS.register("dummy",                () -> new BlastEventRegistryEntry(EventBlastDummy::new));
    public static RegistryObject<BlastEventRegistryEntry> EMP               = BLAST_EVENTS.register("emp",                  () -> new BlastEventRegistryEntry(EventBlastEmp::new));
    public static RegistryObject<BlastEventRegistryEntry> ENDER             = BLAST_EVENTS.register("ender",                () -> new BlastEventRegistryEntry(EventBlastEnder::new));
    public static RegistryObject<BlastEventRegistryEntry> ENDOTHERMIC       = BLAST_EVENTS.register("endothermic",          () -> new BlastEventRegistryEntry(EventBlastEndothermic::new));
    public static RegistryObject<BlastEventRegistryEntry> EXOTHERMIC        = BLAST_EVENTS.register("exothermic",           () -> new BlastEventRegistryEntry(EventBlastExothermic::new));
    public static RegistryObject<BlastEventRegistryEntry> FRAGMENTATION     = BLAST_EVENTS.register("fragmentation",        () -> new BlastEventRegistryEntry(EventBlastFragmentation::new));
    public static RegistryObject<BlastEventRegistryEntry> HYPERSONIC        = BLAST_EVENTS.register("hypersonic",           () -> new BlastEventRegistryEntry(EventBlastHypersonic::new));
    public static RegistryObject<BlastEventRegistryEntry> INCENDIARY        = BLAST_EVENTS.register("incendiary",           () -> new BlastEventRegistryEntry(EventBlastIncendiary::new));
    public static RegistryObject<BlastEventRegistryEntry> NIGHTMARE         = BLAST_EVENTS.register("nightmare",            () -> new BlastEventRegistryEntry(EventBlastNightmare::new));
    public static RegistryObject<BlastEventRegistryEntry> NUCLEAR           = BLAST_EVENTS.register("nuclear",              () -> new BlastEventRegistryEntry(EventBlastNuclear::new));
    public static RegistryObject<BlastEventRegistryEntry> REDMATTER         = BLAST_EVENTS.register("redmatter",            () -> new BlastEventRegistryEntry(EventBlastRedmatter::new));
    public static RegistryObject<BlastEventRegistryEntry> REJUVENATION      = BLAST_EVENTS.register("rejuvenation",         () -> new BlastEventRegistryEntry(EventBlastRejuvenation::new));
    public static RegistryObject<BlastEventRegistryEntry> REPULSIVE         = BLAST_EVENTS.register("repulsive",            () -> new BlastEventRegistryEntry(EventBlastRepulsive::new));
    public static RegistryObject<BlastEventRegistryEntry> SHRAPNEL          = BLAST_EVENTS.register("shrapnel",             () -> new BlastEventRegistryEntry(EventBlastShrapnel::new));
    public static RegistryObject<BlastEventRegistryEntry> S_MINE            = BLAST_EVENTS.register("s_mine",               () -> new BlastEventRegistryEntry(EventBlastSMine::new));
    public static RegistryObject<BlastEventRegistryEntry> SONIC             = BLAST_EVENTS.register("sonic",                () -> new BlastEventRegistryEntry(EventBlastSonic::new));
    public static RegistryObject<BlastEventRegistryEntry> THERMOBARIC       = BLAST_EVENTS.register("thermobaric",          () -> new BlastEventRegistryEntry(EventBlastThermobaric::new));

}

package com.jdawg3636.icbm.common.event;

import com.google.common.collect.Lists;
import com.jdawg3636.icbm.ICBMReference;
import com.mojang.datafixers.util.Either;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.palette.UpgradeData;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.ServerWorldLightManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class EventBlastRejuvenation extends AbstractBlastEvent {

    public HashMap<ChunkPos, ChunkPrimer> newChunks = new HashMap<>();

    public EventBlastRejuvenation(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public boolean executeBlast() {

        // Sound/particles
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);

        // Calculate sphere
        final List<BlockPos> rejuvenationCandidates = ICBMBlastEventUtil.getBlockPositionsWithinFuzzySphere(
                getBlastPosition().getX() + 0.5,
                getBlastPosition().getY() + 0.5,
                getBlastPosition().getZ() + 0.5,
                ICBMReference.COMMON_CONFIG.getBlastRadiusRejuvenation(),
                getBlastWorld().random,
                ICBMReference.COMMON_CONFIG.getRejuvenationFuzzinessPercentage()
        );

        // Iterate over sphere
        for(BlockPos pos : rejuvenationCandidates) {
            // Don't affect non-air blocks
            if(getBlastWorld().getBlockState(pos).isAir()) {
                // Lazy-generate replacement chunks and retrieve new block
                final BlockState state = getRejuvenatedChunk(new ChunkPos(pos)).getBlockState(pos);
                // Set position to match replacement. Flags: 32 = ?, 2 = update nav mesh, maybe other stuff?, 1 = cause block update
                getBlastWorld().setBlock(pos, state, 32 + 2 + 1);
            }
        }

        // Always return true - if something went wrong then we probably crashed.
        return true;

    }

    public ChunkPrimer getRejuvenatedChunk(ChunkPos chunkPos) {
        if(!this.newChunks.containsKey(chunkPos)) {
            this.newChunks.put(chunkPos, regenerateChunk(getBlastWorld(), chunkPos));
        }
        return this.newChunks.get(chunkPos);
    }

    /**
     * This code was greatly informed by the source code for ThutEssentials, by Thutmose
     * https://github.com/Thutmose/ThutEssentials/blob/580b1abc05b3d3e57c129ee21cebc7969bab91ce/src/main/java/thut/essentials/commands/structures/Structuregen.java#L90
     */
    public ChunkPrimer regenerateChunk(ServerWorld level, ChunkPos chunkPos) {

        // Prepare the common parameters to the ChunkStatus generate function
        ChunkGenerator chunkGenerator = level.getChunkSource().getGenerator();
        TemplateManager structureManager = level.getStructureManager();
        ServerWorldLightManager lightEngine = level.getChunkSource().getLightEngine();
        final CompletableFuture<Either<IChunk, ChunkHolder.IChunkLoadingError>> future = new CompletableFuture<>();
        final Function<IChunk, CompletableFuture<Either<IChunk, ChunkHolder.IChunkLoadingError>>> loadingFunction = (ichunk) -> future;

        // Instantiate new ChunkPrimers
        ChunkPrimer newChunkPrimer = new ChunkPrimer(chunkPos, UpgradeData.EMPTY);
        ArrayList<IChunk> primers = Lists.newArrayList(newChunkPrimer);

        // Skipping STRUCTURE_STARTS and STRUCTURE_REFERENCES
        ChunkStatus.BIOMES.generate(level, chunkGenerator, structureManager, lightEngine, loadingFunction, primers);
        ChunkStatus.NOISE.generate(level, chunkGenerator, structureManager, lightEngine, loadingFunction, primers);
        ChunkStatus.SURFACE.generate(level, chunkGenerator, structureManager, lightEngine, loadingFunction, primers);
        ChunkStatus.CARVERS.generate(level, chunkGenerator, structureManager, lightEngine, loadingFunction, primers);
        ChunkStatus.LIQUID_CARVERS.generate(level, chunkGenerator, structureManager, lightEngine, loadingFunction, primers);
        // Skipping FEATURES
        ChunkStatus.LIGHT.generate(level, chunkGenerator, structureManager, lightEngine, loadingFunction, primers);
        ChunkStatus.SPAWN.generate(level, chunkGenerator, structureManager, lightEngine, loadingFunction, primers);
        ChunkStatus.HEIGHTMAPS.generate(level, chunkGenerator, structureManager, lightEngine, loadingFunction, primers);
        ChunkStatus.FULL.generate(level, chunkGenerator, structureManager, lightEngine, loadingFunction, primers);

        return newChunkPrimer;

    }

}
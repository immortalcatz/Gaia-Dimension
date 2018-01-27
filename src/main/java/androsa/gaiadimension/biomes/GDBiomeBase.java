package androsa.gaiadimension.biomes;

import androsa.gaiadimension.registry.GDBlocks;
import androsa.gaiadimension.registry.GDFluids;
import androsa.gaiadimension.world.GaiaWorld;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GDBiomeBase extends Biome {
    protected List<SpawnListEntry> undergroundMonsterList;

    public GDBiomeBase(BiomeProperties props) {
        super(props);

        undergroundMonsterList = new ArrayList<SpawnListEntry>();

        //TODO: Replace with Gaia-Specific Monsters. Perhaps underground-exclusive?
        undergroundMonsterList.add(new SpawnListEntry(EntitySpider.class, 10, 4, 4));
        undergroundMonsterList.add(new SpawnListEntry(EntityZombie.class, 10, 4, 4));
        undergroundMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
        undergroundMonsterList.add(new SpawnListEntry(EntitySlime.class, 10, 4, 4));
        undergroundMonsterList.add(new SpawnListEntry(EntityEnderman.class, 1, 1, 4));

        getGDBiomeDecorator().setTreesPerChunk(5);
        getGDBiomeDecorator().setGrassPerChunk(2);
    }

    @Override
    public BiomeDecorator createBiomeDecorator() {
        return new GDBiomeDecorator();
    }

    protected GDBiomeDecorator getGDBiomeDecorator() {
        return (GDBiomeDecorator) this.decorator;
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noiseVal) {
        this.genGaiaBiomeTerrain(world, rand, primer, x, z, noiseVal);
    }

    protected void genGaiaBiomeTerrain(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        int i = GaiaWorld.SEALEVEL;
        IBlockState iblockstate = this.topBlock;
        IBlockState iblockstate1 = this.fillerBlock;
        int j = -1;
        int k = (int) (noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.025D);
        int l = x & 15;
        int i1 = z & 15;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int j1 = 255; j1 >= 0; --j1) {
            if (j1 <= rand.nextInt(5)) {
                chunkPrimerIn.setBlockState(i1, j1, l, BEDROCK);
            } else {
                IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

                if (iblockstate2.getBlock() == Blocks.STONE) {
                    if (getStoneReplacementState() != null) {
                        chunkPrimerIn.setBlockState(i1, j1, l, getStoneReplacementState());
                }

                if (j == -1) {
                        if (k <= 0) {
                            iblockstate = AIR;
                            iblockstate1 = STONE;
                        } else if (j1 >= i - 4 && j1 <= i + 1) {
                            iblockstate = this.topBlock;
                            iblockstate1 = this.fillerBlock;
                        }

                        if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)) {
                            iblockstate = WATER;
                        }
                }

                j = k;

                    if (j1 >= i - 1) {
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
                    } else if (j1 < i - 7 - k) {
                        iblockstate = AIR;
                        iblockstate1 = STONE;
                    } else {
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
                    }
                } else if (j > 0) {
                    --j;
                    chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
                }
            }
        }
    }

    public IBlockState getStoneReplacementState() {
        return null;
    }

    public List<SpawnListEntry> getUndergroundMonsterList() {
        return this.undergroundMonsterList;
    }
}
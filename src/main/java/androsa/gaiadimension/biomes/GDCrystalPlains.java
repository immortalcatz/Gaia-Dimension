package androsa.gaiadimension.biomes;

import androsa.gaiadimension.registry.GDBlocks;
import androsa.gaiadimension.world.gen.GDGenCrystalPlants;
import androsa.gaiadimension.world.gen.GDGenNoTrees;
import androsa.gaiadimension.world.gen.GDGenPinkAgateTree;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class GDCrystalPlains extends GDBiomeBase {

    private WorldGenAbstractTree GaiaGenPinkTrees;

    public GDCrystalPlains(BiomeProperties props) {
        super(props);

        spawnableCreatureList.add(new SpawnListEntry(androsa.gaiadimension.entity.GDCommonGrowthSapper.class, 8, 4, 6));

        GaiaGenPinkTrees = new GDGenPinkAgateTree(false);

        decorator.grassPerChunk = 5;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random par1Random) {
        return par1Random.nextInt(3) == 0 ? new GDGenNoTrees() : par1Random.nextInt(12) == 0 ? GaiaGenPinkTrees : new GDGenNoTrees();
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random rand) {
        return new GDGenCrystalPlants(GDBlocks.crystal_growth);
    }
}

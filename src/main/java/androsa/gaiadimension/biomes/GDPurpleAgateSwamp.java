package androsa.gaiadimension.biomes;

import androsa.gaiadimension.entity.GDGrowthSapper;
import androsa.gaiadimension.entity.GDMuckling;
import androsa.gaiadimension.entity.GDSpellElement;
import androsa.gaiadimension.registry.GDBlocks;
import androsa.gaiadimension.world.gen.GDGenCrystalPlants;
import androsa.gaiadimension.world.gen.GDGenGummyBlob;
import androsa.gaiadimension.world.gen.GDGenNoTrees;
import androsa.gaiadimension.world.gen.GDGenPurpleAgateTree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class GDPurpleAgateSwamp extends GDBiomeBase {

    private WorldGenAbstractTree GaiaGenPurpleTrees;
    private GDGenGummyBlob genGummyBlock = new GDGenGummyBlob(GDBlocks.gummy_glitter_block, 0);

    public GDPurpleAgateSwamp(BiomeProperties props) {
        super(props);

        spawnableCreatureList.add(new SpawnListEntry(GDGrowthSapper.class, 10, 3, 5));
        spawnableCreatureList.add(new SpawnListEntry(GDSpellElement.class, 10, 1, 2));
        spawnableCreatureList.add(new SpawnListEntry(GDMuckling.class, 10, 1, 1));

        GaiaGenPurpleTrees = new GDGenPurpleAgateTree(false);

        skyColorRGB = new short[] { 171, 109, 241 };
        biomeDecorator.muckPoolChance = 0.25F;
    }

    @Override
    public void decorate(World world, Random rand, BlockPos pos) {

        super.decorate(world, rand, pos);

        int dx, dy, dz;
        int maxBoulder = rand.nextInt(2);
        for (int i = 0; i < maxBoulder; ++i) {
            dx = pos.getX() + rand.nextInt(16) + 8;
            dz = pos.getZ() + rand.nextInt(16) + 8;
            genGummyBlock.generate(world, rand, world.getHeight(new BlockPos(dx, 0, dz)));
        }

        for (int i = 0; i < 9; i++) {
            int Xcoord = pos.getX() + rand.nextInt(16);
            int Zcoord = pos.getZ() + rand.nextInt(16);
            int Ycoord = rand.nextInt(100);
            new WorldGenMinable(GDBlocks.thick_glitter_block.getDefaultState(), 33, input -> input == GDBlocks.gaia_stone.getDefaultState()).generate(world, rand, new BlockPos(Xcoord, Ycoord, Zcoord));
        }
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random par1Random) {
        return par1Random.nextInt(5) == 0 ? new GDGenNoTrees() : par1Random.nextInt(3) == 0 ? GaiaGenPurpleTrees : new GDGenNoTrees();
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random rand) {

        if (rand.nextInt(32) == 0) {
            return new GDGenCrystalPlants(GDBlocks.bulbous_hobina);
        } else if (rand.nextInt(16) == 0) {
            if (rand.nextInt(4) == 0) {
                return new GDGenCrystalPlants(GDBlocks.ouzium);
            } else {
                return new GDGenCrystalPlants(GDBlocks.thiscus);
            }
        } else {
            return new GDGenCrystalPlants(GDBlocks.crystal_growth);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return 0x806FB9;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return 0x806FB9;
    }
}

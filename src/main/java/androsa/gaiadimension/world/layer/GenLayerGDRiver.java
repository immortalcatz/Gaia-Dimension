package androsa.gaiadimension.world.layer;

import androsa.gaiadimension.registry.GDBiomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerGDRiver extends GenLayer {

    public GenLayerGDRiver(long l, GenLayer genlayer) {
        super(l);
        super.parent = genlayer;
    }

    @Override
    public int[] getInts(int x, int z, int width, int depth) {
        int nx = x - 1;
        int nz = z - 1;
        int nwidth = width + 2;
        int ndepth = depth + 2;
        int input[] = parent.getInts(nx, nz, nwidth, ndepth);
        int output[] = IntCache.getIntCache(width * depth);
        for (int dz = 0; dz < depth; dz++) {
            for (int dx = 0; dx < width; dx++) {
                int left = input[dx + 0 + (dz + 1) * nwidth];
                int right = input[dx + 2 + (dz + 1) * nwidth];
                int down = input[dx + 1 + (dz + 0) * nwidth];
                int up = input[dx + 1 + (dz + 2) * nwidth];
                int mid = input[dx + 1 + (dz + 1) * nwidth];

                if (shouldRiver(mid, left, down, right, up)) {
                    output[dx + dz * width] = Biome.getIdForBiome(GDBiomes.mineral_river);
                } else {
                    output[dx + dz * width] = -1;
                }
            }
        }

        return output;
    }

    boolean shouldRiver(int mid, int left, int down, int right, int up) {
        if (shouldRiver(mid, left)) {
            return true;
        } else if (shouldRiver(mid, right)) {
            return true;
        } else if (shouldRiver(mid, down)) {
            return true;
        } else if (shouldRiver(mid, up)) {
            return true;
        } else {
            return false;
        }
    }

    boolean shouldRiver(int id1, int id2) {

        Biome biome1 = Biome.getBiomeForId(id1);
        Biome biome2 = Biome.getBiomeForId(id2);

        if (id1 == id2)
            return false;
        if (id1 == -id2)
            return false;

        //The Volcanic Biomes will be too hot for the mineral water. Remove rivers
        if (biome1 == GDBiomes.volcaniclands || biome2 == GDBiomes.volcaniclands)
            return false;

        //For consistency's sake, there will be no Mineral River at Static Wastelands
        if (biome1 == GDBiomes.static_wasteland || biome2 == GDBiomes.static_wasteland)
            return false;

        //Crystal Plains and Pink Agate Forest are too similar for rivers
        if (biome1 == GDBiomes.pink_agate_forest && biome2 == GDBiomes.crystal_plains)
            return false;
        if (biome1 == GDBiomes.crystal_plains && biome2 == GDBiomes.pink_agate_forest)
            return false;

        //If a reservoir gens near another reservoir, remove the river because it would look goofy
        if (biome1 == GDBiomes.mineral_reservoir && biome2 == GDBiomes.mineral_reservoir)
            return false;

        //Salt Dunes and Mineral Reservoirs are similar, no need for river
        if (biome1 == GDBiomes.salt_dunes && biome2 == GDBiomes.mineral_reservoir)
            return false;
        if (biome1 == GDBiomes.mineral_reservoir && biome2 == GDBiomes.salt_dunes)
            return false;

        //Mutated Agate Wildwoods should look like they were any Agate Forest, but with strange growth patterns
        if (biome1 == GDBiomes.mutant_agate_wildwood && biome2 == GDBiomes.pink_agate_forest)
            return false;
        if (biome1 == GDBiomes.mutant_agate_wildwood && biome2 == GDBiomes.blue_agate_taiga)
            return false;
        if (biome1 == GDBiomes.mutant_agate_wildwood && biome2 == GDBiomes.green_agate_jungle)
            return false;
        if (biome1 == GDBiomes.mutant_agate_wildwood && biome2 == GDBiomes.purple_agate_swamp)
            return false;
        if (biome1 == GDBiomes.pink_agate_forest && biome2 == GDBiomes.mutant_agate_wildwood)
            return false;
        if (biome1 == GDBiomes.blue_agate_taiga && biome2 == GDBiomes.mutant_agate_wildwood)
            return false;
        if (biome1 == GDBiomes.green_agate_jungle && biome2 == GDBiomes.mutant_agate_wildwood)
            return false;
        if (biome1 == GDBiomes.purple_agate_swamp && biome2 == GDBiomes.mutant_agate_wildwood)
            return false;

        return true;
    }
}

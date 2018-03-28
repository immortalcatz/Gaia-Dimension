package androsa.gaiadimension.biomes;

import androsa.gaiadimension.registry.GDBlocks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GDStaticWasteland extends GDBiomeBase {

    private short[] skyColorRGB = new short[]{40, 47, 82};

    public GDStaticWasteland(BiomeProperties props) {
        super(props);

        this.topBlock = GDBlocks.wastelandStone.getDefaultState();
        this.fillerBlock = GDBlocks.wastelandStone.getDefaultState();

        this.decorator.treesPerChunk = -1;

        //TODO: Generate Wasteland Stone and Static Stone as Ores
        //TODO: Have random junk generate on the ground
    }

    @SideOnly(Side.CLIENT)
    public final short[] getSkyRGB() {
        return skyColorRGB;
    }
}

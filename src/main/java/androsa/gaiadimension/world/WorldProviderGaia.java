package androsa.gaiadimension.world;

import androsa.gaiadimension.GDConfig;
import androsa.gaiadimension.GaiaDimension;
import androsa.gaiadimension.biomes.GDBiomes;
import androsa.gaiadimension.world.layer.GDBiomeProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.swing.text.html.parser.Entity;

public class WorldProviderGaia extends WorldProviderSurface {

    public WorldProviderGaia() {
        setDimension(GDConfig.dimension.dimensionID);
    }

    @Override
    public Vec3d getFogColor(float f, float f1) {
        float bright = MathHelper.cos(2.25F * 2.25F * 0.25F) * 2.0F + 0.5F;
        if (bright < 0.0F) {
            bright = 0.0F;
        }
        if (bright > 1.0F) {
            bright = 1.0F;
        }
        float red = 1.0F;
        float green = 0.85F;
        float blue = 0.25F;
        red *= bright * 0.94F + 0.06F;
        green *= bright * 0.91F + 0.09F;
        blue += bright * 0.94F + 0.06F;
        return new Vec3d(red, green, blue);
    }

    @Override
    public float calculateCelestialAngle(long par1, float par3) {
        return 1.0f;
    }

    @Override
    public void init() {
        super.init();
        this.biomeProvider = new GDBiomeProvider(world);
    }

    @Override
    public DimensionType getDimensionType() {
        return GaiaDimension.dimType;
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new GaiaChunkGenerator(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled());
    }

    //Let's see what can be done here...
    @Override
    public boolean isSkyColored() {
        return true;
    }

    @Override
    public double getHorizon() {
        return GaiaWorld.SEALEVEL;
    }

    @Override
    public int getAverageGroundLevel() {
        return 63;
    }

    @Override
    public boolean canRespawnHere() {
        return world.getWorldInfo().isInitialized();
    }

    @Override
    public boolean isDaytime() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks) {
        return new Vec3d(198 / 256.0, 157 / 256.0, 88 / 256.0);
    }

    @Override
    public Biome getBiomeForCoords(BlockPos pos) {
        Biome biome = super.getBiomeForCoords(pos);
        if (biome == null) {
            biome = GDBiomes.pinkAgateForest;
        }
        return biome;
    }

    //Do we have a seed override?
    @Override
    public long getSeed() {
        if (GDConfig.dimension.gaiaSeed == null || GDConfig.dimension.gaiaSeed.length() == 0) {
            return super.getSeed();
        } else {
            return GDConfig.dimension.gaiaSeed.hashCode();
        }
    }

    //There's no weather in Gaia
    @Override
    @SideOnly(Side.CLIENT)
    public IRenderHandler getWeatherRenderer() {
        return null;
    }

    @Override
    public float getCloudHeight() {
        return 255.0F;
    }
}

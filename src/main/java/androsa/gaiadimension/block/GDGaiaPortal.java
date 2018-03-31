package androsa.gaiadimension.block;

import androsa.gaiadimension.GDConfig;
import androsa.gaiadimension.TeleporterGaia;
import androsa.gaiadimension.registry.GDBlocks;
import androsa.gaiadimension.registry.GDTabs;
import androsa.gaiadimension.registry.ModelRegisterCallback;
import com.google.common.cache.LoadingCache;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class GDGaiaPortal extends BlockPortal implements ModelRegisterCallback {

    public GDGaiaPortal() {
        super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X));
        this.setTickRandomly(true);
        this.setCreativeTab(GDTabs.tabBlock);
    }

    @Override
    @Deprecated
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    public static int getMetaForAxis(EnumFacing.Axis axis) {
        if (axis == EnumFacing.Axis.X) {
            return 1;
        }
        else {
            return axis == EnumFacing.Axis.Z ? 2 : 0;
        }
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean tryToCreatePortal(World worldIn, BlockPos pos) {
        GDGaiaPortal.Size blockportal$size = new GDGaiaPortal.Size(worldIn, pos, EnumFacing.Axis.X);

        if (blockportal$size.isValid() && blockportal$size.portalBlockCount == 0) {
            blockportal$size.placePortalBlocks();
            return true;
        }
        else {
            GDGaiaPortal.Size blockportal$size1 = new GDGaiaPortal.Size(worldIn, pos, EnumFacing.Axis.Z);

            if (blockportal$size1.isValid() && blockportal$size1.portalBlockCount == 0) {
                blockportal$size1.placePortalBlocks();
                return true;
            }
            else {
                return false;
            }
        }
    }

    @Override
    @Deprecated
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis)state.getValue(AXIS);

        if (enumfacing$axis == EnumFacing.Axis.X) {
            GDGaiaPortal.Size blockportal$size = new GDGaiaPortal.Size(worldIn, pos, EnumFacing.Axis.X);

            if (!blockportal$size.isValid() || blockportal$size.portalBlockCount < blockportal$size.width * blockportal$size.height) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
        else if (enumfacing$axis == EnumFacing.Axis.Z) {
            GDGaiaPortal.Size blockportal$size1 = new GDGaiaPortal.Size(worldIn, pos, EnumFacing.Axis.Z);

            if (!blockportal$size1.isValid() || blockportal$size1.portalBlockCount < blockportal$size1.width * blockportal$size1.height) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        pos = pos.offset(side);
        EnumFacing.Axis enumfacing$axis = null;

        if (blockState.getBlock() == this) {
            enumfacing$axis = (EnumFacing.Axis)blockState.getValue(AXIS);

            if (enumfacing$axis == null) {
                return false;
            }

            if (enumfacing$axis == EnumFacing.Axis.Z && side != EnumFacing.EAST && side != EnumFacing.WEST) {
                return false;
            }

            if (enumfacing$axis == EnumFacing.Axis.X && side != EnumFacing.SOUTH && side != EnumFacing.NORTH) {
                return false;
            }
        }

        boolean flag = blockAccess.getBlockState(pos.west()).getBlock() == this && blockAccess.getBlockState(pos.west(2)).getBlock() != this;
        boolean flag1 = blockAccess.getBlockState(pos.east()).getBlock() == this && blockAccess.getBlockState(pos.east(2)).getBlock() != this;
        boolean flag2 = blockAccess.getBlockState(pos.north()).getBlock() == this && blockAccess.getBlockState(pos.north(2)).getBlock() != this;
        boolean flag3 = blockAccess.getBlockState(pos.south()).getBlock() == this && blockAccess.getBlockState(pos.south(2)).getBlock() != this;
        boolean flag4 = flag || flag1 || enumfacing$axis == EnumFacing.Axis.X;
        boolean flag5 = flag2 || flag3 || enumfacing$axis == EnumFacing.Axis.Z;

        if (flag4 && side == EnumFacing.WEST) {
            return true;
        }
        else if (flag4 && side == EnumFacing.EAST) {
            return true;
        }
        else if (flag5 && side == EnumFacing.NORTH) {
            return true;
        }
        else {
            return flag5 && side == EnumFacing.SOUTH;
        }
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!entityIn.isRiding() && !entityIn.isBeingRidden() && entityIn.isNonBoss()) {
            if (entityIn instanceof EntityPlayerMP) {
                EntityPlayerMP playerMP = (EntityPlayerMP) entityIn;

                if (playerMP.timeUntilPortal > 0) {
                    playerMP.timeUntilPortal = 50;
                } else {
                    //Let's go to Gaia. I need a catchphrase...
                    if (playerMP.dimension != GDConfig.dimension.dimensionID) {
                        if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(playerMP, GDConfig.dimension.dimensionID)) return;

                        playerMP.mcServer.getPlayerList().transferPlayerToDimension(playerMP, GDConfig.dimension.dimensionID, TeleporterGaia.getTeleporterForDim(playerMP.mcServer, GDConfig.dimension.dimensionID));
                        playerMP.setSpawnChunk(new BlockPos(playerMP), true, GDConfig.dimension.dimensionID);
                    } else {
                        if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(playerMP, 0)) return;
                        playerMP.mcServer.getPlayerList().transferPlayerToDimension(playerMP, 0, TeleporterGaia.getTeleporterForDim(playerMP.mcServer, 0));
                    }
                }
            } else {
                if (entityIn.dimension != GDConfig.dimension.dimensionID) {
                    changeDimension(entityIn, GDConfig.dimension.dimensionID);
                } else {
                    changeDimension(entityIn, 0);
                }
            }
        }
    }

    private void changeDimension(Entity toTeleport, int dimensionIn) {
        if (!toTeleport.world.isRemote && !toTeleport.isDead) {
            if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(toTeleport, dimensionIn)) return;
            toTeleport.world.profiler.startSection("changeDimension");
            MinecraftServer minecraftserver = toTeleport.getServer();
            int i = toTeleport.dimension;
            WorldServer worldserver = minecraftserver.getWorld(i);
            WorldServer worldserver1 = minecraftserver.getWorld(dimensionIn);
            toTeleport.dimension = dimensionIn;


            if (i == 1 && dimensionIn == 1) {
                worldserver1 = minecraftserver.getWorld(0);
                toTeleport.dimension = 0;
            }

            toTeleport.world.removeEntity(toTeleport);
            toTeleport.isDead = false;
            toTeleport.world.profiler.startSection("reposition");
            BlockPos blockpos;

            if (dimensionIn == 1) {
                blockpos = worldserver1.getSpawnCoordinate();
            } else {
                double d0 = toTeleport.posX;
                double d1 = toTeleport.posZ;
                double d2 = 8.0D;

                d0 = (double) MathHelper.clamp((int) d0, -29999872, 29999872);
                d1 = (double) MathHelper.clamp((int) d1, -29999872, 29999872);
                float f = toTeleport.rotationYaw;
                toTeleport.setLocationAndAngles(d0, toTeleport.posY, d1, 90.0F, 0.0F);
                Teleporter teleporter = TeleporterGaia.getTeleporterForDim(minecraftserver, dimensionIn);
                teleporter.placeInExistingPortal(toTeleport, f);
                blockpos = new BlockPos(toTeleport);
            }

            worldserver.updateEntityWithOptionalForce(toTeleport, false);
            toTeleport.world.profiler.endStartSection("reloading");
            Entity entity = EntityList.newEntity(toTeleport.getClass(), worldserver1);

            if (entity != null) {
                entity.copyDataFromOld(toTeleport);

                if (i == 1 && dimensionIn == 1) {
                    BlockPos blockpos1 = worldserver1.getTopSolidOrLiquidBlock(worldserver1.getSpawnPoint());
                    entity.moveToBlockPosAndAngles(blockpos1, entity.rotationYaw, entity.rotationPitch);
                } else {
                    entity.setLocationAndAngles((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(), entity.rotationYaw, entity.rotationPitch);
                }

                boolean flag = entity.forceSpawn;
                entity.forceSpawn = true;
                worldserver1.spawnEntity(entity);
                entity.forceSpawn = flag;
                worldserver1.updateEntityWithOptionalForce(entity, false);
            }

            toTeleport.isDead = true;
            toTeleport.world.profiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            toTeleport.world.profiler.endSection();
        }
    }

    @Override
    @Deprecated
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AXIS, (meta & 3) == 2 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(100) == 0) {
            worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
        }

        for (int i = 0; i < 4; ++i) {
            double d0 = (double)((float)pos.getX() + rand.nextFloat());
            double d1 = (double)((float)pos.getY() + rand.nextFloat());
            double d2 = (double)((float)pos.getZ() + rand.nextFloat());
            double d3 = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            double d4 = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            double d5 = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            int j = rand.nextInt(2) * 2 - 1;

            if (worldIn.getBlockState(pos.west()).getBlock() != this && worldIn.getBlockState(pos.east()).getBlock() != this)
            {
                d0 = (double)pos.getX() + 0.5D + 0.25D * (double)j;
                d3 = (double)(rand.nextFloat() * 2.0F * (float)j);
            }
            else
            {
                d2 = (double)pos.getZ() + 0.5D + 0.25D * (double)j;
                d5 = (double)(rand.nextFloat() * 2.0F * (float)j);
            }

            worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return getMetaForAxis((EnumFacing.Axis)state.getValue(AXIS));
    }

    @Override
    @Deprecated
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:

                switch ((EnumFacing.Axis)state.getValue(AXIS)) {
                    case X:
                        return state.withProperty(AXIS, EnumFacing.Axis.Z);
                    case Z:
                        return state.withProperty(AXIS, EnumFacing.Axis.X);
                    default:
                        return state;
                }

            default:
                return state;
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {AXIS});
    }

    public BlockPattern.PatternHelper createPatternHelper(World worldIn, BlockPos pos) {
        EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Z;
        GDGaiaPortal.Size blockportal$size = new GDGaiaPortal.Size(worldIn, pos, EnumFacing.Axis.X);
        LoadingCache<BlockPos, BlockWorldState> loadingcache = BlockPattern.createLoadingCache(worldIn, true);

        if (!blockportal$size.isValid()) {
            enumfacing$axis = EnumFacing.Axis.X;
            blockportal$size = new GDGaiaPortal.Size(worldIn, pos, EnumFacing.Axis.Z);
        }

        if (!blockportal$size.isValid()) {
            return new BlockPattern.PatternHelper(pos, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1);
        } else {
            int[] aint = new int[EnumFacing.AxisDirection.values().length];
            EnumFacing enumfacing = blockportal$size.rightDir.rotateYCCW();
            BlockPos blockpos = blockportal$size.bottomLeft.up(blockportal$size.getHeight() - 1);

            for (EnumFacing.AxisDirection enumfacing$axisdirection : EnumFacing.AxisDirection.values()) {
                BlockPattern.PatternHelper blockpattern$patternhelper = new BlockPattern.PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection ? blockpos : blockpos.offset(blockportal$size.rightDir, blockportal$size.getWidth() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);

                for (int i = 0; i < blockportal$size.getWidth(); ++i) {
                    for (int j = 0; j < blockportal$size.getHeight(); ++j) {
                        BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, 1);

                        if (blockworldstate.getBlockState() != null && blockworldstate.getBlockState().getMaterial() != Material.AIR) {
                            ++aint[enumfacing$axisdirection.ordinal()];
                        }
                    }
                }
            }

            EnumFacing.AxisDirection enumfacing$axisdirection1 = EnumFacing.AxisDirection.POSITIVE;

            for (EnumFacing.AxisDirection enumfacing$axisdirection2 : EnumFacing.AxisDirection.values()) {
                if (aint[enumfacing$axisdirection2.ordinal()] < aint[enumfacing$axisdirection1.ordinal()]) {
                    enumfacing$axisdirection1 = enumfacing$axisdirection2;
                }
            }

            return new BlockPattern.PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection1 ? blockpos : blockpos.offset(blockportal$size.rightDir, blockportal$size.getWidth() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection1, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);
        }
    }

    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    public static class Size {
        private final World world;
        private final EnumFacing.Axis axis;
        private final EnumFacing rightDir;
        private final EnumFacing leftDir;
        private int portalBlockCount;
        private BlockPos bottomLeft;
        private int height;
        private int width;

        public Size(World worldIn, BlockPos p_i45694_2_, EnumFacing.Axis p_i45694_3_) {
            this.world = worldIn;
            this.axis = p_i45694_3_;

            if (p_i45694_3_ == EnumFacing.Axis.X) {
                this.leftDir = EnumFacing.EAST;
                this.rightDir = EnumFacing.WEST;
            }
            else {
                this.leftDir = EnumFacing.NORTH;
                this.rightDir = EnumFacing.SOUTH;
            }

            for (BlockPos blockpos = p_i45694_2_; p_i45694_2_.getY() > blockpos.getY() - 21 && p_i45694_2_.getY() > 0 && this.isEmptyBlock(worldIn.getBlockState(p_i45694_2_.down()).getBlock()); p_i45694_2_ = p_i45694_2_.down()) {
                ;
            }

            int i = this.getDistanceUntilEdge(p_i45694_2_, this.leftDir) - 1;

            if (i >= 0) {
                this.bottomLeft = p_i45694_2_.offset(this.leftDir, i);
                this.width = this.getDistanceUntilEdge(this.bottomLeft, this.rightDir);

                if (this.width < 2 || this.width > 21) {
                    this.bottomLeft = null;
                    this.width = 0;
                }
            }

            if (this.bottomLeft != null) {
                this.height = this.calculatePortalHeight();
            }
        }

        protected int getDistanceUntilEdge(BlockPos p_180120_1_, EnumFacing p_180120_2_) {
            int i;

            for (i = 0; i < 22; ++i) {
                BlockPos blockpos = p_180120_1_.offset(p_180120_2_, i);

                if (!this.isEmptyBlock(this.world.getBlockState(blockpos).getBlock()) || this.world.getBlockState(blockpos.down()).getBlock() != Blocks.GOLD_BLOCK) {
                    break;
                }
            }

            Block block = this.world.getBlockState(p_180120_1_.offset(p_180120_2_, i)).getBlock();
            return block == Blocks.GOLD_BLOCK ? i : 0;
        }

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }

        protected int calculatePortalHeight() {
            label56:

            for (this.height = 0; this.height < 21; ++this.height) {
                for (int i = 0; i < this.width; ++i) {
                    BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i).up(this.height);
                    Block block = this.world.getBlockState(blockpos).getBlock();

                    if (!this.isEmptyBlock(block)) {
                        break label56;
                    }

                    if (block == GDBlocks.gaiaPortal) {
                        ++this.portalBlockCount;
                    }

                    if (i == 0) {
                        block = this.world.getBlockState(blockpos.offset(this.leftDir)).getBlock();

                        if (block != Blocks.GOLD_BLOCK) {
                            break label56;
                        }
                    }
                    else if (i == this.width - 1) {
                        block = this.world.getBlockState(blockpos.offset(this.rightDir)).getBlock();

                        if (block != Blocks.GOLD_BLOCK) {
                            break label56;
                        }
                    }
                }
            }

            for (int j = 0; j < this.width; ++j) {
                if (this.world.getBlockState(this.bottomLeft.offset(this.rightDir, j).up(this.height)).getBlock() != Blocks.GOLD_BLOCK) {
                    this.height = 0;
                    break;
                }
            }

            if (this.height <= 21 && this.height >= 3) {
                return this.height;
            }
            else {
                this.bottomLeft = null;
                this.width = 0;
                this.height = 0;
                return 0;
            }
        }

        protected boolean isEmptyBlock(Block blockIn) {
            return blockIn.getDefaultState().getMaterial() == Material.AIR || blockIn == Blocks.FIRE || blockIn == GDBlocks.gaiaPortal;
        }

        public boolean isValid() {
            return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
        }

        public void placePortalBlocks() {
            for (int i = 0; i < this.width; ++i) {
                BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i);

                for (int j = 0; j < this.height; ++j) {
                    this.world.setBlockState(blockpos.up(j), GDBlocks.gaiaPortal.getDefaultState().withProperty(BlockPortal.AXIS, this.axis), 2);
                }
            }
        }
    }
}
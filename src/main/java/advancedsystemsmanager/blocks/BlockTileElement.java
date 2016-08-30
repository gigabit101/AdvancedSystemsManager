package advancedsystemsmanager.blocks;

import advancedsystemsmanager.AdvancedSystemsManager;
import advancedsystemsmanager.api.tileentities.ITileFactory;
import advancedsystemsmanager.api.tileentities.*;
import advancedsystemsmanager.helpers.BlockHelper;
import advancedsystemsmanager.items.blocks.ItemTileElement;
import advancedsystemsmanager.reference.Mods;
import advancedsystemsmanager.registry.BlockRegistry;
import advancedsystemsmanager.registry.ClusterRegistry;
import advancedsystemsmanager.tileentities.TileEntityCamouflage;
import advancedsystemsmanager.util.SystemCoord;
//import cofh.api.block.IDismantleable;
//import com.cricketcraft.chisel.api.IFacade;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

//@Optional.InterfaceList({
//        @Optional.Interface(iface = "com.cricketcraft.chisel.api.IFacade", modid = Mods.CHISEL),
//        @Optional.Interface(iface = "cofh.api.block.IDismantleable", modid = Mods.COFHCORE)})
public class BlockTileElement extends Block// implements IFacade, ICable, IDismantleable
{
    public static int RENDER_ID;
    private ITileFactory[] factories;
    private int id;

    public BlockTileElement(int id)
    {
        super(Material.IRON);
        this.id = id;
        setCreativeTab(AdvancedSystemsManager.creativeTab);
        setSoundType(SoundType.METAL);
        setUnlocalizedName("element" + id);
        setHardness(1.2f);
        clearFactories();
    }

    public void clearFactories()
    {
        factories = new ITileFactory[16];
    }

    public void setFactories(Collection<ITileFactory> factories)
    {
        for (ITileFactory factory : factories)
        {
            int factoryId = ClusterRegistry.getId(factory);
            if (factoryId >= id * 16 && factoryId < (id + 1) * 16)
            {
                factoryId &= 0xF;
                this.factories[factoryId] = factory;
                factory.setBlock(this);
//                factory.setMetadata(factoryId);
                factory.setBlockState(getStateFromMeta(factoryId));
            }
        }
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public void registerBlockIcons(IIconRegister register)
//    {
//        if (this == BlockRegistry.cableElements[0]) ClusterRegistry.registerIcons(register);
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
//    {
//        ITileFactory factory = getTileFactory(world.getBlockMetadata(x, y, z));
//        if (factory != null)
//        {
//            return ((ITileElement)factory.getTileEntity(world, x, y, z)).getIcon(side);
//        }
//        return super.getIcon(world, x, y, z, side);
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public IIcon getIcon(int side, int meta)
//    {
//        ITileFactory factory = getTileFactory(meta);
//        return factory != null ? factory.getIcon(side, meta / 16) : null;
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public void getSubBlocks(Item item, CreativeTabs tab, List list)
//    {
//        for (ITileFactory element : factories)
//        {
//            if (element != null) element.getSubBlocks(item, tab, list);
//        }
//    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        ITileFactory factory = getTileFactory(state);
        return factory != null && factory.hasTileEntity();
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState meta)
    {
        ITileFactory factory = getTileFactory(meta);
        return factory != null ? factory.createTileEntity(world, meta) : null;
    }

//    @Override
//    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
//    {
//        TileEntity tileEntity = world.getTileEntity(x, y, z);
//        if (tileEntity instanceof IBUDListener)
//        {
//            ((IBUDListener) tileEntity).onNeighborBlockChange();
//        }
//    }


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity instanceof IActivateListener && ((IActivateListener) tileEntity).onBlockActivated(player, side, hitX, hitY, hitZ);
    }

//    @Override
//    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xSide, float ySide, float zSide)
//    {
//        TileEntity tileEntity = world.getTileEntity(x, y, z);
//        return tileEntity instanceof IActivateListener && ((IActivateListener) tileEntity).onBlockActivated(player, side, xSide, ySide, zSide);
//    }


    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IPlaceListener)
        {
            ((IPlaceListener) tileEntity).onBlockPlacedBy(placer, stack);
        }
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity instanceof IRedstoneListener && ((IRedstoneListener) tileEntity).canConnectRedstone(side);
    }

//    @Override
//    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
//        TileEntity tileEntity = world.getTileEntity(pos);
//        return tileEntity instanceof IRedstoneListener ? ((IRedstoneListener) tileEntity).getComparatorInputOverride(side) : 0;
//    }


    @Override
    public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity instanceof IRedstoneListener && ((IRedstoneListener) tileEntity).shouldCheckWeakPower(side);
    }

//
//    @Override
//    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, EnumFacing side)
//    {
//        TileEntity tileEntity = world.getTileEntity(x, y, z);
//        return tileEntity instanceof IRedstoneListener ? ((IRedstoneListener) tileEntity).isProvidingWeakPower(side) : 0;
//    }

//    @Override
//    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
//    {
//        TileEntity tileEntity = world.getTileEntity(x, y, z);
//        return tileEntity instanceof IRedstoneListener ? ((IRedstoneListener) tileEntity).isProvidingStrongPower(side) : 0;
//    }


    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

//    @Override
//    public int damageDropped(int meta)
//    {
//        return meta;
//    }


//    @Override
//    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
//        return getStackData(world, pos);
//    }

//    @Override
//    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
//    {
//        return getStackData(world, x, y, z);
//    }


//    @Override
//    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
//        return new ArrayList<ItemStack>(Collections.singletonList(getPickBlock(null, world, pos, null)));
//    }
//
//
//    public ItemStack getStackData(World world, BlockPos pos)
//    {
//        int damage = world.getBlockState(pos);
//        TileEntity te = world.getTileEntity(pos);
//        NBTTagCompound tag = null;
//        if (te instanceof ITileElement)
//        {
//            damage += 16 * ((ITileElement) te).getSubtype();
//            tag = new NBTTagCompound();
//            ((ITileElement)te).writeItemNBT(tag);
//        }
//        ItemStack result = new ItemStack(this, 1, damageDropped(damage));
//        result.setTagCompound(tag);
//        return result;
//    }
//
    public ITileFactory getTileFactory(ItemStack stack)
    {
        if (stack == null || !(stack.getItem() instanceof ItemTileElement))
        {
            return null;
        }
        return null;//getTileFactory(stack.getItemDamage());
    }
//
    public ITileFactory getTileFactory(IBlockState state)
    {
        return factories[getMetaFromState(state)];
    }


//    @Override
//    public boolean renderAsNormalBlock()
//    {
//        return false;
//    }

//    @Override
//    public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z)
//    {
//        TileEntityCamouflage camouflage = ClusterRegistry.CAMO.getTileEntity(world, x, y, z);
//        return camouflage == null || camouflage.isNormalBlock();
//    }

//    @Override
//    public int getRenderType()
//    {
//        return RENDER_ID;
//    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {
        TileEntityCamouflage camouflage = ClusterRegistry.CAMO.getTileEntity(world, pos);
        if (camouflage != null && camouflage.getCamouflageType().useSpecialShape() && !camouflage.isUseCollision())
        {
            return 600000;
        }
        return super.getBlockHardness(blockState, world, pos);
    }

//    @Override
//    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
//    {
//        if (!setBlockCollisionBoundsBasedOnState(world, x, y, z))
//        {
//            return null;
//        }
//        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
//    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
//    {
//        if (!setBlockCollisionBoundsBasedOnState(world, x, y, z))
//        {
//            setBlockBounds(0, 0, 0, 0, 0, 0);
//        }
//        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
//    }

//    @Override
//    public boolean isOpaqueCube()
//    {
//        return false;
//    }


    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World world, BlockPos pos, Vec3d start, Vec3d end) {
        if (!setBlockCollisionBoundsBasedOnState(world, pos))
        {
//            setBlockBounds(0, 0, 0, 0, 0, 0);
            setBlockBoundsBasedOnState(world, pos);
        }
        return super.collisionRayTrace(blockState, world, pos, start, end);
    }

//    @Override
//    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end)
//    {
//        if (!setBlockCollisionBoundsBasedOnState(world, x, y, z))
//        {
//            setBlockBounds(0, 0, 0, 0, 0, 0);
//        }
//        return super.collisionRayTrace(world, x, y, z, start, end);
//    }

//    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
    {
        TileEntityCamouflage camouflage = ClusterRegistry.CAMO.getTileEntity(world, pos);
        if (camouflage != null && camouflage.getCamouflageType().useSpecialShape())
        {
            camouflage.setBlockBounds(this);
        } else
        {
//            setBlockBoundsForItemRender();
        }
    }

//    @Override
//    public void setBlockBoundsForItemRender()
//    {
//        setBlockBounds(0, 0, 0, 1, 1, 1);
//    }

//    @SideOnly(Side.CLIENT)
//    @Override
//    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer)
//    {
//        TileEntityCamouflage camouflage = ClusterRegistry.CAMO.getTileEntity(worldObj, target.blockX, target.blockY, target.blockZ);
//        if (camouflage != null)
//        {
//            if (camouflage.addBlockEffect(this, target.sideHit, effectRenderer))
//            {
//                return true;
//            }
//        }
//        return false;
//    }

    private boolean setBlockCollisionBoundsBasedOnState(IBlockAccess world, BlockPos pos)
    {
        setBlockBoundsBasedOnState(world, pos);

        TileEntityCamouflage camouflage = ClusterRegistry.CAMO.getTileEntity(world, pos);
        if (camouflage != null && camouflage.getCamouflageType().useSpecialShape())
        {
            if (!camouflage.isUseCollision())
            {
                return false;
            } else if (camouflage.isFullCollision())
            {
//                setBlockBoundsForItemRender();
            }
        }

        return true;
    }

//    @Override
//    @Optional.Method(modid = Mods.CHISEL)
//    public Block getFacade(IBlockAccess world, int x, int y, int z, int side)
//    {
//        if (side != -1)
//        {
//            TileEntityCamouflage camo = ClusterRegistry.CAMO.getTileEntity(world, x, y, z);
//            if (camo != null && camo.hasSideBlock(0))
//            {
//                return camo.getSideBlock(0);
//            }
//        }
//        return this;
//    }
//
//    @Override
//    @Optional.Method(modid = Mods.CHISEL)
//    public int getFacadeMetadata(IBlockAccess world, int x, int y, int z, int side)
//    {
//        if (side != -1)
//        {
//            TileEntityCamouflage camo = ClusterRegistry.CAMO.getTileEntity(world, x, y, z);
//            if (camo != null && camo.hasSideBlock(0))
//            {
//                return camo.getSideMetadata(0);
//            }
//        }
//        return world.getBlockMetadata(x, y, z);
//    }


    @Override
    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
        ITileFactory factory = getTileFactory(world.getBlockState(pos));
        return factory == null ? super.getPlayerRelativeBlockHardness(state, player, world, pos) : factory.getBlockHardness();
    }

//    @Override
//    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z)
//    {
//        ITileFactory factory = getTileFactory(world.getBlockMetadata(x, y, z));
//        return factory == null ? super.getPlayerRelativeBlockHardness(player, world, x, y, z) : factory.getBlockHardness();
//    }


//    @Override
//    public boolean isCable(World world, BlockPos pos)
//    {
//        ITileFactory factory = getTileFactory(world.getBlockState(pos));
//        return factory != null && factory.isCable((ITileElement)factory.getTileEntity(world, pos));
//    }
//
//    @Override
//    public void getConnectedCables(World world, SystemCoord coordinate, List<SystemCoord> visited, Queue<SystemCoord> cables)
//    {
//        BlockHelper.getAdjacentCables(coordinate, visited, cables);
//    }

//    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, BlockPos pos, boolean returnBlock)
//    {
//        ArrayList<ItemStack> list = getDrops(world, pos, world.getBlockMetadata(x, y, z), 0);
//        world.setBlockToAir(pos);
//        if (!returnBlock)
//            for (ItemStack item : list)
//                dropBlockAsItem(world, pos, item);
//        return list;
//    }

    public boolean canDismantle(EntityPlayer entityPlayer, World world, int x, int y, int z)
    {
        return entityPlayer.isSneaking();
    }
}

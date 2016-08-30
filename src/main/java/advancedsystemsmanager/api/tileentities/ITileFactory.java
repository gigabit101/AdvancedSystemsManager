package advancedsystemsmanager.api.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.List;

public interface ITileFactory
{
    void setBlock(Block block);

    Block getBlock();

    IBlockState getState();

    void setBlockState(IBlockState state);

//    void setMetadata(int metadata);
//
//    int getMetadata();

    float getBlockHardness();

    String getUnlocalizedName(int subtype);

    ItemStack getItemStack(int subtype);

    boolean hasTileEntity();

    boolean canBeAddedToCluster(Collection<ITileFactory> existingFactories);

    TileEntity createTileEntity(World world, IBlockState metadata);

    <T extends TileEntity>T getTileEntity(IBlockAccess world, BlockPos pos);

    boolean isInstance(TileEntity tileEntity);

    String getKey();

    boolean canPlaceBlock(World world, BlockPos pos, ItemStack stack);

    boolean isCable(ITileElement element);

//    @SideOnly(Side.CLIENT)
//    void registerIcons(IIconRegister register);

    @SideOnly(Side.CLIENT)
    void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list);

//    @SideOnly(Side.CLIENT)
//    IIcon getIcon(int side, int subtype);
//
//    @SideOnly(Side.CLIENT)
//    IIcon[] getIcons(int subtype);

    @SideOnly(Side.CLIENT)
    void addInformation(ItemStack stack, EntityPlayer player, List<String> information, boolean advanced);

    void saveToClusterTag(ItemStack stack, NBTTagCompound tag);
}

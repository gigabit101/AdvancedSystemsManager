package advancedsystemsmanager.items.blocks;

import advancedsystemsmanager.api.tileentities.ITileFactory;
import advancedsystemsmanager.api.items.IElementItem;
import advancedsystemsmanager.api.tileentities.ITileElement;
import advancedsystemsmanager.blocks.BlockTileElement;
import advancedsystemsmanager.blocks.TileFactory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemTileElement extends ItemBlock implements IElementItem
{
    private BlockTileElement block;

    public ItemTileElement(Block block)
    {
        super(block);
        this.block = (BlockTileElement)block;
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        ITileFactory factory = getTileFactory(stack);
        return factory == null ? null : factory.getUnlocalizedName(stack.getItemDamage() / 16);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List information, boolean advanced)
    {
        ITileFactory factory = getTileFactory(stack);
        if (factory != null)
            factory.addInformation(stack, player, (List<String>)information, advanced);
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!getTileFactory(stack).canPlaceBlock(world, pos, stack))
        {
            return false;
        }
        if (!super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState))
        {
            return false;
        }
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof ITileElement)
        {
            ((ITileElement) te).setSubtype(stack.getItemDamage() / 16);
            if (stack.hasTagCompound())
            {
                ((ITileElement) te).readItemNBT(stack.getTagCompound());
            }
        }
        return true;
    }


    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
        super.onCreated(stack, world, player);
        ITileFactory factory = getTileFactory(stack);
        if (factory instanceof TileFactory)
        {
            ((TileFactory) factory).onCreated(stack, world, player);
        }
    }

    @Override
    public ITileFactory getTileFactory(ItemStack stack)
    {
        return block.getTileFactory(stack);
    }
}

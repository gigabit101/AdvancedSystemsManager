package advancedsystemsmanager.compatibility.waila;

import advancedsystemsmanager.naming.NameRegistry;
import advancedsystemsmanager.reference.Names;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class WailaLabelProvider implements IWailaDataProvider
{
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getWailaHead(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        ITaggedList tagged = (ITaggedList)list;
        if (iWailaDataAccessor.getBlock() != null && tagged.getEntries(Names.LABELLED).isEmpty())
        {
            String label = NameRegistry.getSavedName(iWailaDataAccessor.getWorld().provider.getDimension(), iWailaDataAccessor.getPosition().getX(), iWailaDataAccessor.getPosition().getY(), iWailaDataAccessor.getPosition().getZ());
            if (label != null)
            {
//                tagged.add(StatCollector.translateToLocalFormatted(Names.LABELLED, label), Names.LABELLED);
            }
        }
        return list;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return list;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return list;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP entityPlayerMP, TileEntity tileEntity, NBTTagCompound nbtTagCompound, World world, BlockPos blockPos) {
        return null;
    }

}

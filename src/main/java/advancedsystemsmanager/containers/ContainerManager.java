package advancedsystemsmanager.containers;

import advancedsystemsmanager.network.PacketHandler;
import advancedsystemsmanager.registry.ItemRegistry;
import advancedsystemsmanager.tileentities.manager.TileEntityManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerManager extends ContainerBase<TileEntityManager>
{
    public ContainerManager(TileEntityManager manager, InventoryPlayer player)
    {
        super(manager, player);
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);

        if (listener instanceof EntityPlayerMP && !te.getWorld().isRemote)
        {
            PacketHandler.sendAllData(this, listener, te);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void detectAndSendChanges()
    {
        //TODO:
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return super.canInteractWith(entityplayer) || entityplayer.getHeldItemMainhand() != null && entityplayer.getHeldItemMainhand().getItem() == ItemRegistry.remoteAccessor;
    }
}

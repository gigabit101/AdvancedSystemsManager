package advancedsystemsmanager.containers;

import advancedsystemsmanager.api.network.IPacketSync;
import advancedsystemsmanager.api.tileentities.ITileInterfaceProvider;
import advancedsystemsmanager.network.ASMPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;


public abstract class ContainerBase<T extends TileEntity & ITileInterfaceProvider> extends Container
{
    protected T te;
    int playerInventoryEnd;
    private InventoryPlayer player;

    public ContainerBase(T te, InventoryPlayer player)
    {
        this.te = te;
        this.player = player;
    }

    protected void bindPlayerInventory(int x, int y)
    {
        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(player, i, x + i * 18, y + 58));
        }

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(player, j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }
        playerInventoryEnd = inventorySlots.size();
    }

    public void updateServer(ASMPacket message, EntityPlayerMP player)
    {
        message.sendPlayerPackets(te.readData(message, player), this);
    }

    public void updateClient(ASMPacket message, EntityPlayer player)
    {
        te.readData(message, player);
    }

    public T getTileEntity()
    {
        return te;
    }

    public ITileInterfaceProvider getInterface()
    {
        return te;
    }

    public List<IContainerListener> getCrafters()
    {
        return listeners;
    }

    @SideOnly(Side.CLIENT)
    public void sendFinalUpdate(IPacketSync element)
    {
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slot)
    {
        Slot slotObject = (Slot)inventorySlots.get(slot);
        ItemStack stack = null;

        if (slotObject != null && slotObject.getHasStack())
        {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            if (slot < playerInventoryEnd)
            {
                if (!mergeItemStack(stackInSlot, playerInventoryEnd, inventorySlots.size(), false))
                {
                    return null;
                }
            } else
            {
                if (!mergeItemStack(stackInSlot, 0, playerInventoryEnd, false))
                {
                    return null;
                }
            }
            if (stackInSlot.stackSize == 0)
            {
                slotObject.putStack(null);
            } else
            {
                slotObject.onSlotChanged();
            }
            if (stackInSlot.stackSize == stack.stackSize)
            {
                return null;
            }
            slotObject.onPickupFromSlot(entityPlayer, stackInSlot);
        }
        return stack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
}

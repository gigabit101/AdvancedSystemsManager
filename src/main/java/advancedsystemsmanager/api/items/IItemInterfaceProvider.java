package advancedsystemsmanager.api.items;

import advancedsystemsmanager.network.ASMPacket;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemInterfaceProvider
{
    Container getContainer(ItemStack stack, EntityPlayer player);

    @SideOnly(Side.CLIENT)
    GuiScreen getGui(ItemStack stack, EntityPlayer player);

    void readData(ItemStack stack, ASMPacket buf, EntityPlayer player);
}

package advancedsystemsmanager.api.tileentities;

import advancedsystemsmanager.network.ASMPacket;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITileInterfaceProvider extends IActivateListener
{
    Container getContainer(EntityPlayer player);

    @SideOnly(Side.CLIENT)
    GuiScreen getGui(EntityPlayer player);

    boolean writeData(ASMPacket packet);

    boolean readData(ASMPacket packet, EntityPlayer player);
}

package advancedsystemsmanager.api.gui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IDraggable
{
    @SideOnly(Side.CLIENT)
    void drag(int mouseX, int mouseY);

    void release(int mouseX, int mouseY);
}

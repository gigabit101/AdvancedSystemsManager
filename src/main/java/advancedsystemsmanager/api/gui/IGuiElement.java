package advancedsystemsmanager.api.gui;

import advancedsystemsmanager.client.gui.GuiManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IGuiElement
{
    @SideOnly(Side.CLIENT)
    void draw(GuiManager gui, int mouseX, int mouseY, int zLevel);

    @SideOnly(Side.CLIENT)
    void drawMouseOver(GuiManager gui, int mouseX, int mouseY);

    @SideOnly(Side.CLIENT)
    boolean onKeyStroke(GuiManager gui, char character, int key);

    @SideOnly(Side.CLIENT)
    boolean onClick(int mouseX, int mouseY, int button);

    @SideOnly(Side.CLIENT)
    boolean isVisible();
}

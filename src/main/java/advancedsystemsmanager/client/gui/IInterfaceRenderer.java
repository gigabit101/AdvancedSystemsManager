package advancedsystemsmanager.client.gui;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IInterfaceRenderer
{
    void draw(GuiManager gui, int mX, int mY);

    boolean drawMouseOver(GuiManager gui, int mX, int mY);

    void onClick(GuiManager gui, int mX, int mY, int button);

    void onDrag(GuiManager gui, int mX, int mY);

    void onRelease(GuiManager gui, int mX, int mY, int button);

    boolean onKeyTyped(GuiManager gui, char c, int k);

    void onScroll(int scroll);
}

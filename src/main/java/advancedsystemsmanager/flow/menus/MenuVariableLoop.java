package advancedsystemsmanager.flow.menus;

import advancedsystemsmanager.client.gui.GuiBase;
import advancedsystemsmanager.flow.FlowComponent;
import advancedsystemsmanager.flow.elements.ScrollVariable;
import advancedsystemsmanager.flow.elements.Variable;
import advancedsystemsmanager.reference.Names;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class MenuVariableLoop extends Menu
{
    public static final String NBT_ELEMENT = "Element";
    private ScrollVariable variables;

    public MenuVariableLoop(FlowComponent parent)
    {
        super(parent);
        variables = new ScrollVariable(parent);
    }

    @Override
    public String getName()
    {
        return Names.LOOP_VARIABLE_MENU;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void draw(GuiBase gui, int mX, int mY)
    {
        variables.draw(gui, mX, mY);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean drawMouseOver(GuiBase gui, int mX, int mY)
    {
        return variables.drawMouseOver(gui, mX, mY);
    }

    @Override
    public void onClick(int mX, int mY, int button)
    {
        variables.onClick(mX, mY, button);
    }

    @Override
    public void onRelease(int mX, int mY, int button, boolean isMenuOpen)
    {
        if (isMenuOpen)
        {
            variables.onRelease(mX, mY);
        }
    }

    @Override
    public boolean onKeyStroke(GuiBase gui, char c, int k)
    {
        return variables.onKeyStroke(gui, c, k);
    }

    @Override
    public void copyFrom(Menu menu)
    {
        variables.variable = ((MenuVariableLoop)menu).variables.variable;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound, boolean pickup)
    {
        variables.variable = nbtTagCompound.getInteger(NBT_ELEMENT);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, boolean pickup)
    {
        nbtTagCompound.setInteger(NBT_ELEMENT, variables.variable);
    }

    @Override
    public void addErrors(List<String> errors)
    {
        Variable variable = getVariable();
        if (variable == null || !getVariable().isValid())
        {
            errors.add(Names.LIST_NOT_DECLARED);
        }
    }

    public Variable getVariable()
    {
        return getParent().getManager().getVariable(variables.variable);
    }

}

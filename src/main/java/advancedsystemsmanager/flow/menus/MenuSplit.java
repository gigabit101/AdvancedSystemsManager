package advancedsystemsmanager.flow.menus;

import advancedsystemsmanager.client.gui.GuiBase;
import advancedsystemsmanager.flow.FlowComponent;
import advancedsystemsmanager.flow.elements.CheckBox;
import advancedsystemsmanager.flow.elements.CheckBoxList;
import advancedsystemsmanager.flow.elements.RadioButton;
import advancedsystemsmanager.flow.elements.RadioButtonList;
import advancedsystemsmanager.reference.Names;
import advancedsystemsmanager.registry.ConnectionSet;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;

public class MenuSplit extends Menu
{
    public static final int RADIO_X = 5;
    public static final int RADIO_Y = 5;
    public static final int CHECK_BOX_X = 15;
    public static final int SPACING_Y = 15;
    public static final String NBT_SPLIT = "Split";
    public static final String NBT_FAIR = "Fair";
    public static final String NBT_EMPTY = "Empty";
    public RadioButtonList radioButtons;
    public CheckBoxList checkBoxes;
    public boolean useFair;
    public boolean useEmpty;

    public MenuSplit(FlowComponent parent)
    {
        super(parent);


        radioButtons = new RadioButtonList(getParent());

        radioButtons.add(new RadioButton(RADIO_X, RADIO_Y, Names.SEQUENTIAL));
        radioButtons.add(new RadioButton(RADIO_X, RADIO_Y + SPACING_Y, Names.SPLIT));

        checkBoxes = new CheckBoxList();

        checkBoxes.addCheckBox(new CheckBox(getParent(), Names.FAIR_SPLIT, CHECK_BOX_X, RADIO_Y + 2 * SPACING_Y)
        {
            @Override
            public void setValue(boolean val)
            {
                setFair(val);
            }

            @Override
            public boolean getValue()
            {
                return useFair();
            }
        });

        checkBoxes.addCheckBox(new CheckBox(getParent(), Names.EMPTY_PINS, CHECK_BOX_X, RADIO_Y + 3 * SPACING_Y)
        {
            @Override
            public void setValue(boolean val)
            {
                setEmpty(val);
            }

            @Override
            public boolean getValue()
            {
                return useEmpty();
            }
        });
    }

    public void setFair(boolean val)
    {
        useFair = val;
    }

    public boolean useFair()
    {
        return useFair;
    }

    public void setEmpty(boolean val)
    {
        useEmpty = val;
    }

    public boolean useEmpty()
    {
        return useEmpty;
    }

    @Override
    public String getName()
    {
        return Names.SPLIT_MENU;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void draw(GuiBase gui, int mX, int mY)
    {
        if (useSplit())
        {
            checkBoxes.draw(gui, mX, mY);
        }
        radioButtons.draw(gui, mX, mY);
    }

    public boolean useSplit()
    {
        return radioButtons.getSelectedOption() == 1;
    }

    @Override
    public void onClick(int mX, int mY, int button)
    {
        if (useSplit())
        {
            checkBoxes.onClick(mX, mY);
        }
        radioButtons.onClick(mX, mY, button);
    }

    @Override
    public void copyFrom(Menu menu)
    {
        MenuSplit menuSplit = (MenuSplit)menu;
        setSplit(menuSplit.useSplit());
        setFair(menuSplit.useFair());
        setEmpty(menuSplit.useEmpty());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound, boolean pickup)
    {
        setSplit(nbtTagCompound.getBoolean(NBT_SPLIT));
        if (useSplit())
        {
            setFair(nbtTagCompound.getBoolean(NBT_FAIR));
            setEmpty(nbtTagCompound.getBoolean(NBT_EMPTY));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, boolean pickup)
    {
        nbtTagCompound.setBoolean(NBT_SPLIT, useSplit());
        if (useSplit())
        {
            nbtTagCompound.setBoolean(NBT_FAIR, useFair());
            nbtTagCompound.setBoolean(NBT_EMPTY, useEmpty());
        }
    }

    @Override
    public boolean isVisible()
    {
        return isSplitConnection(getParent());
    }

    public static boolean isSplitConnection(FlowComponent component)
    {
        return component.getConnectionSet() == ConnectionSet.MULTIPLE_OUTPUT_2 || component.getConnectionSet() == ConnectionSet.MULTIPLE_OUTPUT_5;
    }

    public void setSplit(boolean val)
    {
        radioButtons.setSelectedOption(val ? 1 : 0);
    }
}

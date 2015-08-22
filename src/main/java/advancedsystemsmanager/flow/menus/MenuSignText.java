package advancedsystemsmanager.flow.menus;

import advancedsystemsmanager.client.gui.GuiBase;
import advancedsystemsmanager.flow.FlowComponent;
import advancedsystemsmanager.flow.elements.CheckBox;
import advancedsystemsmanager.flow.elements.CheckBoxList;
import advancedsystemsmanager.flow.elements.TextBoxLogic;
import advancedsystemsmanager.helpers.CollisionHelper;
import advancedsystemsmanager.reference.Names;
import advancedsystemsmanager.registry.ThemeHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;


public class MenuSignText extends Menu
{
    public static final int TEXT_BOX_SIZE_U = 49;
    public static final int TEXT_BOX_SIZE_V = 6;
    public static final int TEXT_BOX_SIZE_W = 64;
    public static final int TEXT_BOX_SIZE_H = 12;
    public static final int TEXT_BOX_SRC_X = 113;
    public static final int TEXT_BOX_SRC_Y = 72;
    public static final int TEXT_BOX_X = 5;
    public static final int TEXT_BOX_Y = 5;
    public static final int TEXT_BOX_Y_SPACING = 15;
    public static final int TEXT_BOX_TEXT_X = 3;
    public static final int TEXT_BOX_TEXT_Y = 3;
    public static final int CURSOR_X = 2;
    public static final int CURSOR_Z = 5;
    public static final int CHECK_BOX_X = 75;
    public static final int CHECK_BOX_Y = 2;
    public static final float IDLE_TIME = 1F;
    public static final String NBT_LINES = "Lines";
    public static final String NBT_UPDATE = "Update";
    public static final String NBT_TEXT = "Text";
    public TextBoxLogic[] textBoxes;
    public CheckBoxList checkBoxes;
    public boolean[] update;
    public float[] hasChanged;
    public int selected = -1;

    public MenuSignText(FlowComponent parent)
    {
        super(parent);

        textBoxes = new TextBoxLogic[4];
        update = new boolean[textBoxes.length];
        hasChanged = new float[textBoxes.length];
        checkBoxes = new CheckBoxList();
        for (int i = 0; i < textBoxes.length; i++)
        {
            final int id = i;
            textBoxes[i] = new TextBoxLogic(getParent(), 15, TEXT_BOX_SIZE_W - TEXT_BOX_TEXT_X * 2)
            {
                @Override
                public void onUpdate()
                {
                    hasChanged[id] = IDLE_TIME;
                }
            };
            //textBoxes[i].setMult(0.6F);
            textBoxes[i].setTextAndCursor("");

            checkBoxes.addCheckBox(new CheckBox(getParent(), Names.UPDATE_LINE, CHECK_BOX_X, CHECK_BOX_Y + TEXT_BOX_Y + i * TEXT_BOX_Y_SPACING)
            {
                @Override
                public void setValue(boolean val)
                {
                    update[id] = val;
                }

                @Override
                public boolean getValue()
                {
                    return update[id];
                }

            });
            update[i] = true;
        }

    }

    @Override
    public String getName()
    {
        return Names.SIGN_TEXT;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void draw(GuiBase gui, int mX, int mY)
    {
        for (int i = 0; i < textBoxes.length; i++)
        {
            TextBoxLogic textBox = textBoxes[i];

            int y = TEXT_BOX_Y + i * TEXT_BOX_Y_SPACING;

            gui.drawTextBox(TEXT_BOX_X, y, TEXT_BOX_SIZE_W, TEXT_BOX_SIZE_H, TEXT_BOX_SRC_X, TEXT_BOX_SRC_Y, TEXT_BOX_SIZE_U, TEXT_BOX_SIZE_V, (selected == i ? ThemeHandler.theme.menus.textBoxes.selected : ThemeHandler.theme.menus.textBoxes.background).getColour(), ThemeHandler.theme.menus.textBoxes.border.getColour());
            gui.drawString(textBox.getText(), TEXT_BOX_X + TEXT_BOX_TEXT_X, y + TEXT_BOX_TEXT_Y, ThemeHandler.theme.menus.textBoxes.text.getColourInt());

            if (selected == i)
            {
                gui.drawCursor(TEXT_BOX_X + textBox.getCursorPosition(gui) + CURSOR_X, y, CURSOR_Z, ThemeHandler.theme.menus.textBoxes.text.getColourInt());
            }
        }
        checkBoxes.draw(gui, mX, mY);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onClick(int mX, int mY, int button)
    {
        for (int i = 0; i < textBoxes.length; i++)
        {
            if (CollisionHelper.inBounds(TEXT_BOX_X, TEXT_BOX_Y + TEXT_BOX_Y_SPACING * i, TEXT_BOX_SIZE_W, TEXT_BOX_SIZE_H, mX, mY))
            {
                if (i == selected)
                {
                    selected = -1;
                } else
                {
                    selected = i;
                }
                onSelectedChange();
                break;
            }
        }
        checkBoxes.onClick(mX, mY);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean onKeyStroke(GuiBase gui, char c, int k)
    {
        if (k == 15)
        {
            selected = (selected + 1) % 4;
            onSelectedChange();
            return true;
        } else if (selected == -1)
        {
            return super.onKeyStroke(gui, c, k);
        } else
        {
            textBoxes[selected].onKeyStroke(gui, c, k);
            return true;
        }
    }

    @Override
    public void copyFrom(Menu menu)
    {
        MenuSignText textMenu = (MenuSignText)menu;
        for (int i = 0; i < textBoxes.length; i++)
        {
            textBoxes[i].setText(textMenu.textBoxes[i].getText());
            update[i] = textMenu.update[i];
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound, boolean pickup)
    {
        NBTTagList list = nbtTagCompound.getTagList(NBT_LINES, 10);
        for (int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound element = list.getCompoundTagAt(i);
            update[i] = element.getBoolean(NBT_UPDATE);
            setTextPostSync(i, element.getString(NBT_TEXT));
        }
    }

    public void setTextPostSync(int id, String str)
    {
        if (str == null)
        {
            str = "";
        }

        textBoxes[id].setText(str);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, boolean pickup)
    {
        NBTTagList list = new NBTTagList();

        for (int i = 0; i < textBoxes.length; i++)
        {
            NBTTagCompound element = new NBTTagCompound();
            element.setBoolean(NBT_UPDATE, update[i]);
            element.setString(NBT_TEXT, textBoxes[i].getText());
            list.appendTag(element);
        }

        nbtTagCompound.setTag(NBT_LINES, list);
    }

    @Override
    public void update(float partial)
    {
        for (int i = 0; i < hasChanged.length; i++)
        {
            if (hasChanged[i] > 0)
            {
                hasChanged[i] -= partial;
                if (hasChanged[i] <= 0)
                {
                    textBoxes[i].sendSyncPacket();
                }
            }
        }
    }

    @Override
    public void onGuiClosed()
    {
        update(IDLE_TIME);
    }

    public void onSelectedChange()
    {
        update(IDLE_TIME);
    }

    public boolean shouldUpdate(int id)
    {
        return update[id];
    }

    public String getText(int id)
    {
        return textBoxes[id].getText();
    }
}

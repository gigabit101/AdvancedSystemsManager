package advancedsystemsmanager.flow.elements;

import advancedsystemsmanager.api.network.IPacketProvider;
import advancedsystemsmanager.client.gui.GuiBase;
import advancedsystemsmanager.network.ASMPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ChatAllowedCharacters;


public class TextBoxLogic extends UpdateElement
{
    public String text;
    public int cursor;
    public int cursorPosition;
    public int charLimit;
    public int width;
    public boolean updatedCursor;
    public float mult;

    public TextBoxLogic(IPacketProvider packetProvider, int charLimit, int width)
    {
        super(packetProvider);
        this.charLimit = charLimit;
        this.width = width;
        mult = 1F;
    }

    @SideOnly(Side.CLIENT)
    public void addText(GuiBase gui, String str)
    {
        String newText = text.substring(0, cursor) + str + text.substring(cursor);

        if (newText.length() <= charLimit && gui.getStringWidth(newText) * mult <= width)
        {
            text = newText;
            moveCursor(gui, str.length());
            onUpdate();
        }
    }

    @SideOnly(Side.CLIENT)
    public void deleteText(GuiBase gui, int direction)
    {
        if (cursor + direction >= 0 && cursor + direction <= text.length())
        {
            if (direction > 0)
            {
                text = text.substring(0, cursor) + text.substring(cursor + 1);
            } else
            {
                text = text.substring(0, cursor - 1) + text.substring(cursor);
                moveCursor(gui, direction);
            }
            onUpdate();
        }
    }

    @SideOnly(Side.CLIENT)
    public void moveCursor(GuiBase gui, int steps)
    {
        cursor += steps;

        updateCursor();
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public int getCursorPosition(GuiBase gui)
    {
        if (updatedCursor)
        {
            cursorPosition = (int)(gui.getStringWidth(text.substring(0, cursor)) * mult);
            updatedCursor = false;
        }

        return cursorPosition;
    }

    @SideOnly(Side.CLIENT)
    public void onKeyStroke(GuiBase gui, char c, int k)
    {
        if (k == 203)
        {
            moveCursor(gui, -1);
        } else if (k == 205)
        {
            moveCursor(gui, 1);
        } else if (k == 14)
        {
            deleteText(gui, -1);
        } else if (k == 211)
        {
            deleteText(gui, 1);
        } else if (ChatAllowedCharacters.isAllowedCharacter(c))
        {
            addText(gui, Character.toString(c));
        }
    }

    public void updateCursor()
    {
        if (cursor < 0)
        {
            cursor = 0;
        } else if (cursor > text.length())
        {
            cursor = text.length();
        }

        updatedCursor = true;
    }

    public void setTextAndCursor(String s)
    {
        setText(s);
        resetCursor();
    }

    public void resetCursor()
    {
        cursor = text.length();
        updatedCursor = true;
    }

    public void setMult(float mult)
    {
        this.mult = mult;
    }

    @Override
    public boolean writeData(ASMPacket packet)
    {
        packet.writeStringToBuffer(text);
        return true;
    }

    @Override
    public boolean readData(ASMPacket packet)
    {
        text = packet.readStringFromBuffer();
        return false;
    }
}

package advancedsystemsmanager.flow.elements;

import advancedsystemsmanager.api.gui.IContainerSelection;
import advancedsystemsmanager.flow.FlowComponent;
import advancedsystemsmanager.client.gui.GuiManager;
import advancedsystemsmanager.client.gui.TextColour;
import advancedsystemsmanager.reference.Names;
import advancedsystemsmanager.util.ColourUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Variable implements Comparable<Variable>, IContainerSelection
{
    public static final TIntObjectHashMap<String> defaultVariables = new TIntObjectHashMap<String>();
    public static final long NEGATIVE = (long)1 << 63;
    public static final int VARIABLE_SRC_X = 104;
    public static final int VARIABLE_SRC_Y = 127;
    public static final int VARIABLE_SIZE = 14;
    public static final String NBT_EXECUTED = "Executed";
    public static final String NBT_SELECTION = "Selection";
    public static final String NBT_SELECTION_ID = "Id";
    public static final String NBT_COLOUR = "Colour";
    private static final String NBT_NAME = "Name";
    public int colour;
    public TextColour textColour;
    public FlowComponent declaration;
    public List<Long> containers = new ArrayList<Long>();
    public boolean executed;
    private float[] hsv = new float[3];
    private int[] rgba = new int[4];
    private String defaultName;

    static
    {
        defaultVariables.put(0xFFFFFF, Names.VARIABLE_WHITE);
        defaultVariables.put(0xd97f33, Names.VARIABLE_ORANGE);
        defaultVariables.put(0xb24cd9, Names.VARIABLE_MAGENTA);
        defaultVariables.put(0x6699d9, Names.VARIABLE_LIGHT_BLUE);
        defaultVariables.put(0xe5e533, Names.VARIABLE_YELLOW);
        defaultVariables.put(0x7fcc33, Names.VARIABLE_LIME);
        defaultVariables.put(0xf37fa6, Names.VARIABLE_PINK);
        defaultVariables.put(0x4c4c4c, Names.VARIABLE_GRAY);
        defaultVariables.put(0x999999, Names.VARIABLE_LIGHT_GRAY);
        defaultVariables.put(0x4c7f99, Names.VARIABLE_CYAN);
        defaultVariables.put(0x7f40b2, Names.VARIABLE_PURPLE);
        defaultVariables.put(0x334cb2, Names.VARIABLE_BLUE);
        defaultVariables.put(0x664c33, Names.VARIABLE_BROWN);
        defaultVariables.put(0x667f33, Names.VARIABLE_GREEN);
        defaultVariables.put(0x993333, Names.VARIABLE_RED);
        defaultVariables.put(0x141414, Names.VARIABLE_BLACK);
    }

    public Variable(int colour)
    {
        setColour(colour);
        this.defaultName = getDefaultName();
    }

    private void setColour(int colour)
    {
        this.colour = colour;
        this.rgba[0] = (colour >> 16) & 0xFF;
        this.rgba[1] = (colour >> 8) & 0xFF;
        this.rgba[2] = colour & 0xFF;
        this.rgba[3] = 0xFF;
        textColour = TextColour.getClosestColour(colour);
        ColourUtils.HextoHSV(colour, hsv);
    }

    public String getDefaultName()
    {
        String result = Integer.toHexString(colour);
        while (result.length() < 6) result = "0" + result;
        return "#" + result;
    }

    public Variable(int colour, String name)
    {
        setColour(colour);
        this.defaultName = name;
    }

    public Variable(NBTTagCompound tagCompound)
    {
        setColour(tagCompound.getInteger(NBT_COLOUR));
        readFromNBT(tagCompound);
        if (defaultName != null && defaultVariables.containsKey(colour))
        {
            defaultName = defaultVariables.get(colour);
        } else
        {
            defaultName = getDefaultName();
        }
    }

    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        executed = nbtTagCompound.getBoolean(NBT_EXECUTED);
        defaultName = nbtTagCompound.getString(NBT_NAME);
        containers.clear();
        NBTTagList tagList = nbtTagCompound.getTagList(NBT_SELECTION, 10);

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound selectionTag = tagList.getCompoundTagAt(i);
            containers.add(selectionTag.getLong(NBT_SELECTION_ID));
        }
    }

    public static TIntObjectHashMap<Variable> getDefaultVariables()
    {
        TIntObjectHashMap<Variable> defaults = new TIntObjectHashMap<Variable>(20, 0.8F, -1);
        for (int colour : defaultVariables.keys())
        {
            if (colour != defaultVariables.getNoEntryKey())
                defaults.put(colour, new Variable(colour, defaultVariables.get(colour)));
        }
        return defaults;
    }

    public boolean isValid()
    {
        return declaration != null;
    }

    @Override
    public long getId()
    {
        return NEGATIVE | colour;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void draw(GuiManager gui, int x, int y)
    {
        gui.drawColouredTexture(x + 1, y + 1, VARIABLE_SRC_X, VARIABLE_SRC_Y, VARIABLE_SIZE, VARIABLE_SIZE, rgba);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getDescription(GuiManager gui)
    {
        return textColour.toString() + getNameFromColor();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getName(GuiManager gui)
    {
        return getNameFromColor();
    }

    @Override
    public boolean isVariable()
    {
        return true;
    }

    public String getNameFromColor()
    {
        if (getDeclaration() == null || getDeclaration().getComponentName() == null)
        {
            return StatCollector.translateToLocal(defaultName);
        } else
        {
            return getDeclaration().getComponentName();
        }

    }

    public FlowComponent getDeclaration()
    {
        return declaration;
    }

    public void setDeclaration(FlowComponent flowComponent)
    {
        if (flowComponent == null || declaration == null)
        {
            declaration = flowComponent;
        }
    }

    public List<Long> getContainers()
    {
        return containers;
    }

    public void setContainers(List<Long> containers)
    {
        this.containers = containers;
    }

    public void add(long id)
    {
        if (!containers.contains(id))
        {
            containers.add(id);
        }
    }

    public boolean hasBeenExecuted()
    {
        return executed;
    }

    public void setExecuted(boolean executed)
    {
        this.executed = executed;
    }

    public void clearContainers()
    {
        containers.clear();
    }

    public void remove(long id)
    {
        containers.remove(id);
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setBoolean(NBT_EXECUTED, executed);
        nbtTagCompound.setInteger(NBT_COLOUR, colour);
        nbtTagCompound.setString(NBT_NAME, defaultName);
        NBTTagList tagList = new NBTTagList();

        for (Long container : containers)
        {
            NBTTagCompound selectionTag = new NBTTagCompound();
            selectionTag.setLong(NBT_SELECTION_ID, container);
            tagList.appendTag(selectionTag);
        }

        nbtTagCompound.setTag(NBT_SELECTION, tagList);
    }

    @Override
    public int compareTo(@Nonnull Variable o)
    {
        if (hsv[0] < o.hsv[0])
            return -1;
        if (hsv[0] > o.hsv[0])
            return 1;
        if (hsv[1] < o.hsv[1])
            return -1;
        if (hsv[1] > o.hsv[1])
            return 1;
        if (hsv[2] > o.hsv[2])
            return -1;
        if (hsv[2] < o.hsv[2])
            return 1;
        return 0;
    }
}

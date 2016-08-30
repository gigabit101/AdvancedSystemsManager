package advancedsystemsmanager.api.execution;

import advancedsystemsmanager.flow.Connection;
import advancedsystemsmanager.flow.FlowComponent;
import advancedsystemsmanager.flow.execution.Executor;
import advancedsystemsmanager.flow.menus.Menu;
import advancedsystemsmanager.registry.ConnectionSet;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface ICommand
{
    int getId();

    public ConnectionSet[] getSets();

    void getMenus(FlowComponent component, List<Menu> menus);

    public String getName();

    public String getLongName();

    public CommandType getCommandType();

    int[] getColour();

    void setColour(int[] colour);

    int getEnergyCost();

    void setEnergyCost(int amount);

    int getX();

    int getY();

    @SideOnly(Side.CLIENT)
    ResourceLocation getTexture();

    void execute(FlowComponent command, int connectionId, Executor executor);

    List<Connection> getActiveChildren(FlowComponent command, int connectionId);

    void moveComponent(FlowComponent component, FlowComponent oldParent, FlowComponent newParent);

    public static enum CommandType
    {
        TRIGGER,
        INPUT,
        OUTPUT,
        COMMAND_CONTROL,
        CRAFTING,
        MISC,
        GROUP,
        VARIABLE
    }
}

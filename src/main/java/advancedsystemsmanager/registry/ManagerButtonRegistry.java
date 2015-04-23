package advancedsystemsmanager.registry;

import advancedsystemsmanager.api.gui.IManagerButton;
import advancedsystemsmanager.api.gui.IManagerButtonProvider;
import advancedsystemsmanager.api.gui.ManagerButtonList;
import advancedsystemsmanager.flow.FlowComponent;
import advancedsystemsmanager.flow.menus.MenuGroup;
import advancedsystemsmanager.helpers.Localization;
import advancedsystemsmanager.network.DataReader;
import advancedsystemsmanager.network.DataWriter;
import advancedsystemsmanager.settings.Settings;
import advancedsystemsmanager.tileentities.manager.DefaultButtonProvider;
import advancedsystemsmanager.tileentities.manager.ManagerButton;
import advancedsystemsmanager.tileentities.manager.TileEntityManager;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public class ManagerButtonRegistry
{
    private static List<IManagerButtonProvider> buttonProviders = new ArrayList<IManagerButtonProvider>();

    static
    {
        registerButtonProvider(new DefaultButtonProvider());
    }

    public static ManagerButtonList getButtons(TileEntityManager manager)
    {
        ManagerButtonList buttons = new ManagerButtonList();
        getButtons(manager, buttons);
        return buttons;
    }

    public static void getButtons(TileEntityManager manager, List<IManagerButton> buttons)
    {
        for (IManagerButtonProvider provider : buttonProviders) buttons.addAll(provider.getButtons(manager));
        buttons.add(new ManagerButton(manager, Localization.PREFERENCES.toString(), 230 - IManagerButton.BUTTON_ICON_SIZE, IManagerButton.BUTTON_ICON_SIZE * 2)
        {
            @Override
            public void onClick(DataReader dr)
            {

            }

            @Override
            public boolean onClick(DataWriter dw)
            {
                Settings.openMenu(manager);
                return false;
            }
        });

        buttons.add(new ManagerButton(manager, Localization.EXIT_GROUP.toString(), 230 - IManagerButton.BUTTON_ICON_SIZE, IManagerButton.BUTTON_ICON_SIZE * 3)
        {
            @Override
            public void onClick(DataReader dr)
            {
                int id = dr.readComponentId();
                FlowComponent component = manager.getFlowItems().get(id);
                boolean moveCluster = dr.readBoolean();
                if (component.getParent() != null)
                {
                    MenuGroup.moveComponents(component, component.getParent().getParent(), moveCluster);
                }
            }

            @Override
            public boolean onClick(DataWriter dw)
            {
                for (FlowComponent item : manager.getFlowItems())
                {
                    if (item.isBeingMoved())
                    {
                        //For the server only
                        manager.justSentServerComponentRemovalPacket = true;
                        dw.writeComponentId(manager, item.getId());
                        dw.writeBoolean(GuiScreen.isShiftKeyDown());
                        item.resetPosition();
                        return true;
                    }
                }

                //Client only
                manager.selectedComponent = manager.selectedComponent.getParent();
                return false;
            }

            @Override
            public boolean isVisible()
            {
                return manager.selectedComponent != null;
            }

            @Override
            public boolean activateOnRelease()
            {
                for (FlowComponent item : manager.getFlowItems())
                {
                    if (item.isBeingMoved())
                    {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String getMouseOver()
            {
                for (FlowComponent item : manager.getFlowItems())
                {
                    if (item.isBeingMoved())
                    {
                        return Localization.EXIT_GROUP_DROP.toString();
                    }
                }
                return super.getMouseOver();
            }
        });
    }

    public static void registerButtonProvider(IManagerButtonProvider provider)
    {
        buttonProviders.add(provider);
    }
}

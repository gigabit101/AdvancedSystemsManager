package advancedsystemsmanager.compatibility.rf.menus;

import advancedsystemsmanager.api.ISystemType;
import advancedsystemsmanager.flow.FlowComponent;
import advancedsystemsmanager.flow.menus.MenuContainer;
import advancedsystemsmanager.network.ASMPacket;
import advancedsystemsmanager.reference.Names;
import advancedsystemsmanager.tileentities.TileEntityRFNode;
import advancedsystemsmanager.util.SystemCoord;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

public class MenuRF extends MenuContainer
{
    public MenuRF(FlowComponent parent, ISystemType validType)
    {
        super(parent, validType);
    }

    public boolean isSelected(TileEntityRFNode node)
    {
        for (long id : getSelectedInventories())
        {
            if (getParent().getManager().getInventory(id).getTileEntity() == node) return true;
        }
        return false;
    }

    @Override
    public boolean readData(ASMPacket packet)
    {
        boolean result = super.readData(packet);
        if (packet.getSide() == Side.SERVER)
            updateConnectedNodes();
        return result;
    }

    public void updateConnectedNodes()
    {
        if (!getParent().getManager().getWorldObj().isRemote)
        {
            for (SystemCoord connection : getParent().getManager().getConnectedInventories())
            {
                if (connection.getTileEntity() instanceof TileEntityRFNode)
                    ((TileEntityRFNode)connection.getTileEntity()).update(getParent());
            }
        }
    }
}
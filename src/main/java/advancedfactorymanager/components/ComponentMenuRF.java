package advancedfactorymanager.components;

import advancedfactorymanager.blocks.ConnectionBlock;
import advancedfactorymanager.blocks.ConnectionBlockType;
import advancedfactorymanager.tileentities.TileEntityRFNode;

public abstract class ComponentMenuRF extends ComponentMenuContainer
{
    public ComponentMenuRF(FlowComponent parent, ConnectionBlockType validType)
    {
        super(parent, validType);
    }

    public boolean isSelected(TileEntityRFNode node)
    {
        for (ConnectionBlock block : getParent().getManager().getConnectedInventories())
        {
            if (block.getTileEntity() == node) return getSelectedInventories().contains(block.getId());
        }
        return false;
    }

    public void updateConnectedNodes()
    {
        if (!getParent().getManager().getWorldObj().isRemote)
        {
            for (ConnectionBlock connection : getParent().getManager().getConnectedInventories())
            {
                if (connection.getTileEntity() instanceof TileEntityRFNode)
                    ((TileEntityRFNode)connection.getTileEntity()).update(getParent());
            }
        }
    }
}
package advancedsystemsmanager.api.tileentities;

import net.minecraft.util.EnumFacing;

import java.util.List;

public interface ICluster
{
    List<ITileElement> getTiles();

    EnumFacing getFacing();
}

package advancedsystemsmanager.api.tileentities;

import net.minecraft.util.EnumFacing;

public interface IRedstoneListener
{
    boolean canConnectRedstone(EnumFacing side);

    int getComparatorInputOverride(EnumFacing side);

    int isProvidingWeakPower(EnumFacing side);

    int isProvidingStrongPower(EnumFacing side);

    boolean shouldCheckWeakPower(EnumFacing side);
}

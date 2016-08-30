package advancedsystemsmanager.api.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IActivateListener
{
    boolean onBlockActivated(EntityPlayer player, EnumFacing side, float xSide, float ySide, float zSide);
}

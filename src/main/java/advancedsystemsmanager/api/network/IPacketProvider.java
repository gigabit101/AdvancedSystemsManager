package advancedsystemsmanager.api.network;

import advancedsystemsmanager.network.ASMPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IPacketProvider
{
    @SideOnly(Side.CLIENT)
    ASMPacket getSyncPacket();

    void registerSyncable(IPacketSync networkSync);

    @SideOnly(Side.CLIENT)
    void sendPacketToServer(ASMPacket packet);
}

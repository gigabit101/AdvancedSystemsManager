package advancedsystemsmanager.network;

import advancedsystemsmanager.api.network.IPacketBlock;
import advancedsystemsmanager.commands.ParentCommand;
import advancedsystemsmanager.containers.ContainerBase;
import advancedsystemsmanager.naming.NameRegistry;
import advancedsystemsmanager.registry.FactoryMappingRegistry;
import advancedsystemsmanager.threading.SearchItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketEventHandler
{
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event)
    {
        handlePacket(event.getPacket().payload(), FMLClientHandler.instance().getClient().thePlayer, Side.CLIENT);
    }

    public void handlePacket(ByteBuf buffer, EntityPlayer player, Side side)
    {
        ASMPacket packet = new ASMPacket(buffer, side);
        if (player instanceof EntityPlayerMP) packet.setPlayer((EntityPlayerMP)player);
        int action = packet.readUnsignedByte();

        switch (action)
        {
            case PacketHandler.CONTAINER:
                int containerId = packet.readByte();
                Container container = player.openContainer;

                if (container != null && container.windowId == containerId && container instanceof ContainerBase)
                {
                    if (player instanceof EntityPlayerMP)
                    {
                        ((ContainerBase)container).updateServer(packet, (EntityPlayerMP)player);
                    } else
                    {
                        ((ContainerBase)container).updateClient(packet, player);
                    }

                }
                break;
            case PacketHandler.BLOCK:
                int x = packet.readInt();
                int y = packet.readUnsignedByte();
                int z = packet.readInt();
                BlockPos pos = new BlockPos(x,y,z);

                TileEntity te = player.worldObj.getTileEntity(pos);
                if (te != null && te instanceof IPacketBlock)
                {
                    int id = packet.readByte();
                    ((IPacketBlock)te).readData(packet, id);
                }
                break;
            case PacketHandler.COMMAND:
                ParentCommand.handlePacket(packet);
                break;
            case PacketHandler.NAME:
                if (player instanceof EntityPlayerMP)
                {
                    NameRegistry.updateServer(packet);
                } else
                {
                    NameRegistry.updateClient(packet);
                }
                break;
            case PacketHandler.ON_JOIN:
                SearchItems.setItems();
                FactoryMappingRegistry.INSTANCE.readData(packet);
                break;
        }
    }

    @SubscribeEvent
    public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event)
    {
        handlePacket(event.getPacket().payload(), ((NetHandlerPlayServer)event.getHandler()).playerEntity, Side.SERVER);
    }
}

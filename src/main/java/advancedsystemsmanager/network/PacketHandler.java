package advancedsystemsmanager.network;

import advancedsystemsmanager.api.gui.IManagerButton;
import advancedsystemsmanager.api.network.IPacketBlock;
import advancedsystemsmanager.api.tileentities.ITileInterfaceProvider;
import advancedsystemsmanager.flow.FlowComponent;
import advancedsystemsmanager.flow.menus.Menu;
import advancedsystemsmanager.containers.ContainerBase;
import advancedsystemsmanager.containers.ContainerManager;
import advancedsystemsmanager.registry.FactoryMappingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketHandler
{
    public static final double BLOCK_UPDATE_RANGE = 128;
    public static final int SYNC_ALL = 0;
    public static final int SETTING_MESSAGE = 1;
    public static final int SYNC_COMPONENT = 2;
    public static final int NEW_VARIABLE = 4;
    public static final int BUTTON_CLICK = 5;
    public static final int SPECIAL_DATA = 42;

    public static final int CONTAINER = 0;
    public static final int BLOCK = 1;
    public static final int COMMAND = 2;
    public static final int NAME = 3;
    public static final int ON_JOIN = 4;

    public static void sendAllData(Container container, IContainerListener crafting, ITileInterfaceProvider te)
    {
        ASMPacket packet = new ASMPacket();

        packet.writeByte(CONTAINER);
        packet.writeByte(container.windowId);
        te.writeData(packet);

        sendDataToPlayer(crafting, packet);
    }

    public static void sendDataToPlayer(IContainerListener crafting, ASMPacket packet)
    {
        if (crafting instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP)crafting;

            packet.sendPlayerPacket(player);
        }
    }

    public static ASMPacket getWriterForUpdate(Container container)
    {
        ASMPacket packet = new ASMPacket();

        packet.writeByte(CONTAINER);
        packet.writeByte(container.windowId);
        packet.writeBoolean(true); //updated data

        return packet;
    }

    public static ASMPacket getCommandPacket()
    {
        ASMPacket packet = new ASMPacket();
        packet.writeByte(COMMAND);
        return packet;
    }

    public static ASMPacket getContainerPacket(Container container)
    {
        ASMPacket packet = new ASMPacket();
        packet.writeByte(CONTAINER);
        packet.writeByte(container.windowId);
        return packet;
    }

    public static void sendDataToListeningClients(ContainerBase container, ASMPacket packet)
    {
        packet.sendPlayerPackets(true, container);
    }

    public static ASMPacket getComponentPacket(FlowComponent component)
    {
        ASMPacket packet = PacketHandler.getServerPacket();
        createComponentPacket(packet, component);
        return packet;
    }

    @SideOnly(Side.CLIENT)
    public static ASMPacket getServerPacket()
    {
        Container container = Minecraft.getMinecraft().thePlayer.openContainer;
        return getContainerPacket(container);
    }

    private static void createComponentPacket(ASMPacket packet, FlowComponent component)
    {
        packet.writeByte(SYNC_COMPONENT);
        packet.writeVarIntToBuffer(component.getId());
    }

    public static ASMPacket getWriterForClientComponentPacket(ContainerManager container, FlowComponent component, Menu menu)
    {
        ASMPacket packet = PacketHandler.getContainerPacket(container);
        createComponentPacket(packet, component);
        return packet;
    }

    public static void sendBlockPacket(IPacketBlock block, EntityPlayer player, int id)
    {
        if (block instanceof TileEntity)
        {
            TileEntity te = (TileEntity)block;
            ASMPacket packet = constructBlockPacket(te, block, id);
            boolean onServer = player == null || !player.worldObj.isRemote;

            if (!onServer)
            {
                packet.sendServerPacket();
            } else if (player != null)
            {
                packet.sendPlayerPacket((EntityPlayerMP)player);
            } else
            {
                packet.sendPlayerPackets(te.getPos().getX() + 0.5, te.getPos().getY() + 0.5, te.getPos().getZ() + 0.5, BLOCK_UPDATE_RANGE, te.getWorld().provider.getDimension());
            }
        }
    }

    public static ASMPacket constructBlockPacket(TileEntity te, IPacketBlock block, int id)
    {
        ASMPacket packet = new ASMPacket(20);
        packet.writeByte(BLOCK);
        packet.writeInt(te.getPos().getX());
        packet.writeByte(te.getPos().getY());
        packet.writeInt(te.getPos().getZ());
        packet.writeByte(id);

        block.writeData(packet, id);
        return packet;
    }

    public static void sendButtonPacket(int index, IManagerButton button)
    {
        ASMPacket packet = PacketHandler.getServerPacket();
        packet.writeByte(BUTTON_CLICK);
        packet.writeByte(index);
        if (button.writeData(packet))
            packet.sendServerPacket();
    }

    public static void sendDataToServer(ASMPacket packet)
    {
        packet.sendServerPacket();
    }

    public static void sendVariablePacket(int colour)
    {
        ASMPacket packet = PacketHandler.getServerPacket();
        packet.writeByte(NEW_VARIABLE);
        packet.writeMedium(colour);
        packet.sendServerPacket();
    }

    public static ASMPacket getNamePacket()
    {
        ASMPacket packet = new ASMPacket();
        packet.writeByte(NAME);
        return packet;
    }

    public static void sendLogonMessage(EntityPlayerMP player)
    {
        ASMPacket packet = new ASMPacket();
        packet.writeByte(ON_JOIN);
        FactoryMappingRegistry.INSTANCE.writeData(packet);
        packet.sendPlayerPacket(player);
    }
}

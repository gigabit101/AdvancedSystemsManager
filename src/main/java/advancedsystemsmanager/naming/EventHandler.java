package advancedsystemsmanager.naming;

import advancedsystemsmanager.items.ItemDuplicator;
import advancedsystemsmanager.items.ItemLabeler;
import advancedsystemsmanager.network.PacketHandler;
import advancedsystemsmanager.reference.Names;
import advancedsystemsmanager.registry.ItemRegistry;
import advancedsystemsmanager.tileentities.manager.TileEntityManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

public class EventHandler
{
    @SubscribeEvent
    public void playerLogIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            NameRegistry.syncNameData((EntityPlayerMP)event.player);
            PacketHandler.sendLogonMessage((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void blockBreak(BlockEvent.BreakEvent event)
    {
        NameRegistry.removeName(event.world, event.x, event.y, event.z);
    }

    @SubscribeEvent
    public void playerInteract(PlayerInteractEvent event)
    {
        ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
        if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && stack != null)
        {
            World world = event.world;
            int x = event.x;
            int y = event.y;
            int z = event.z;
            EntityPlayer player = event.entityPlayer;
            if (stack.getItem() == ItemRegistry.labeler)
            {
                if (ItemLabeler.isValidTile(world, x, y, z))
                {
                    String label = ItemLabeler.getLabel(stack);
                    if (label.isEmpty())
                    {
                        if (NameRegistry.removeName(world, x, y, z))
                        {
                            player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal(Names.LABEL_CLEARED)));
                        }
                    } else
                    {
                        NameRegistry.saveName(world, x, y, z, label);
                        player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(Names.LABEL_SAVED, label)));
                    }
                    event.setCanceled(true);
                }
            } else if (stack.getItem() == ItemRegistry.duplicator && player.isSneaking())
            {
                TileEntity te = world.getTileEntity(x, y, z);
                if (te instanceof TileEntityManager)
                {
                    world.removeTileEntity(x, y, z);
                    TileEntityManager manager = new TileEntityManager();
                    if (stack.hasTagCompound() && ItemDuplicator.validateNBT(stack))
                    {
                        manager.readFromNBT(stack.getTagCompound());
                        stack.setTagCompound(null);
                    }
                    world.setTileEntity(x, y, z, manager);
                    event.setCanceled(true);
                }
            }
        }
    }
}

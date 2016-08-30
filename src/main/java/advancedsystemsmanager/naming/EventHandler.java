package advancedsystemsmanager.naming;

import advancedsystemsmanager.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class EventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
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
        NameRegistry.removeName(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
    }

    @SubscribeEvent
    public void playerInteract(PlayerInteractEvent event)
    {
        ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
//        if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && stack != null)
//        {
//            if (stack.getItem() instanceof ILeftClickItem)
//            {
//                if (((ILeftClickItem) stack.getItem()).leftClick(event.getEntityPlayer(), stack, event.getWorld(), event.x, event.y, event.z, event.face))
//                {
//                    event.setCanceled(true);
//                }
//            }
//        }
    }
}

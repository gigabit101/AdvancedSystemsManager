package advancedsystemsmanager.network.message;

import advancedsystemsmanager.containers.ContainerBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Iterator;

public class SecretMessage implements IMessage, IMessageHandler<SecretMessage, IMessage>
{
    int x, y, z;

    @Override
    public void fromBytes(ByteBuf buf)
    {
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
    }

    @Override
    public IMessage onMessage(SecretMessage message, MessageContext ctx)
    {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        Container container = player.openContainer;
        if (container instanceof ContainerBase)
        {
            World world = player.getEntityWorld();
            NBTTagCompound tagCompound;
            if (!player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG))
            {
                player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
            }
            tagCompound = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            if (!tagCompound.getBoolean("KONAMI"))
            {
                EntityCreeper creeper = new EntityCreeper(world);
                for (int attempts = 0; attempts < 4; attempts++)
                {
                    setCoords(player, world);
                    creeper.setLocationAndAngles(x + world.rand.nextDouble(), y, z + world.rand.nextDouble(), world.rand.nextFloat(), world.rand.nextFloat());
                    if (!creeper.isEntityInsideOpaqueBlock())
                    {
                        creeper.setAlwaysRenderNameTag(true);
                        creeper.setCustomNameTag("hilburn");

                        Iterator itr = creeper.tasks.taskEntries.iterator();
                        while (itr.hasNext())
                        {
                            Object ai = itr.next();
//                            if (ai instanceof EntityAICreeperSwell || ai instanceof EntityAIAttackOnCollide)
//                                itr.remove();
                        }
//                        if (world.rand.nextInt(10) == 0) creeper.getDataWatcher().updateObject(17, 1);
//                        world.spawnEntityInWorld(creeper);
//                        tagCompound.setBoolean("KONAMI", true);
                        break;
                    }
                }
            } else
            {
                world.createExplosion(player, player.posX, player.posY + player.eyeHeight / 2, player.posZ, 4F, false);
            }

        }
        return null;
    }

    private void setCoords(EntityPlayer player, World world)
    {
        x = (int)(Math.floor(player.posX) + 4D - world.rand.nextDouble() * 8);
        z = (int)(Math.floor(player.posZ) + 4D - world.rand.nextDouble() * 8);
        y = (int)player.posY;
        BlockPos pos = new BlockPos(x,y,z);
        while (world.isAirBlock(pos)) y--;
        while (!world.isAirBlock(pos)) y++;
    }
}

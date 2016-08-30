package advancedsystemsmanager.helpers;

import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.ArrayList;
import java.util.List;

public class WorldSaveHelper
{
    private List<SavableData> savableData = new ArrayList<SavableData>();
    private World world;

    public void save(SavableData data)
    {
        if (world != null)
        {
            loadData(data);
        }
        this.savableData.add(data);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        if (event.getWorld().provider.getDimension() == 0 && !event.getWorld().isRemote)
        {
            this.world = event.getWorld();
            for (SavableData data : savableData)
            {
                if (data.needsLoading())
                {
                    loadData(data);
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        if (event.getWorld().provider.getDimension() == 0 && !event.getWorld().isRemote)
        {
            unload();
        }
    }

    @SubscribeEvent
    public void onPlayerQuit(FMLNetworkEvent.ClientDisconnectionFromServerEvent e)
    {
        unload();
    }

    private void unload()
    {
        world = null;
        for (SavableData data : savableData)
        {
            data.unload();
        }
    }

    private boolean loadData(SavableData data)
    {
        WorldSavedData saved = world.getMapStorage().getOrLoadData(data.getClass(), data.mapName);
        world.getMapStorage().setData(data.mapName, data);
        return data.copy(saved);
    }
}

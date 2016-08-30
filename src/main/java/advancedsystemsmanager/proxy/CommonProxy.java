package advancedsystemsmanager.proxy;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thevault.registry.IRenderRegistry;

public class CommonProxy implements IRenderRegistry
{
    public void init()
    {

    }

    public World getClientWorld()
    {
        return null;
    }

    public void initRenderers()
    {
    }

    public void initHandlers()
    {
    }

//    @Override
//    public void registerItemRenderer(Item item, IItemRenderer renderer)
//    {
//
//    }
//
//    @Override
//    public void registerSimpleBlockRenderer(int id, ISimpleBlockRenderingHandler renderer)
//    {
//
//    }

    @Override
    public void registerTileEntityRenderer(Class<? extends TileEntity> tileEntity, TileEntitySpecialRenderer renderer)
    {

    }
}

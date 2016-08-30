package thevault.registry;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public interface IRenderRegistry
{
//    void registerItemRenderer(Item item, IItemRenderer renderer);
//
//    void registerSimpleBlockRenderer(int id, ISimpleBlockRenderingHandler renderer);

    void registerTileEntityRenderer(Class<? extends TileEntity> tileEntity, TileEntitySpecialRenderer renderer);
}

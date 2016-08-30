package advancedsystemsmanager.recipes;

import advancedsystemsmanager.registry.ClusterRegistry;
import advancedsystemsmanager.tileentities.TileEntityCluster;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.oredict.RecipeSorter;

public class ClusterUncraftingRecipe implements IRecipe
{

    public ClusterUncraftingRecipe()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    static
    {
        RecipeSorter.register("asm:cluster", ClusterUncraftingRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
    }

    @Override
    public boolean matches(InventoryCrafting crafting, World world)
    {
        return matches(crafting);
    }

    public boolean matches(IInventory crafting)
    {
        ItemStack cluster = ClusterRegistry.CLUSTER.getItemStack();
        ItemStack advcluster = ClusterRegistry.CLUSTER.getItemStack(1);
        boolean hasCluster = false;
        for (int i = 0; i < crafting.getSizeInventory(); i++)
        {
            ItemStack stack = crafting.getStackInSlot(i);
            if (stack != null)
            {
                if (!hasCluster && (stack.isItemEqual(cluster) || stack.isItemEqual(advcluster)))
                {
                    hasCluster = true;
                    continue;
                }
                return false;
            }
        }
        return hasCluster;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting crafting)
    {
        ItemStack result = ClusterRegistry.CLUSTER.getItemStack();
        result.setItemDamage(getCluster(crafting).getItemDamage());
        return result;
    }

    public ItemStack getCluster(IInventory crafting)
    {
        ItemStack cluster = ClusterRegistry.CLUSTER.getItemStack();
        ItemStack advcluster = ClusterRegistry.CLUSTER.getItemStack(1);
        for (int i = 0; i < crafting.getSizeInventory(); i++)
        {
            ItemStack stack = crafting.getStackInSlot(i);
            if (stack != null)
            {
                if (stack.isItemEqual(cluster) || stack.isItemEqual(advcluster)) return stack;
            }
        }
        return null;
    }

    @Override
    public int getRecipeSize()
    {
        return 1;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return null;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return new ItemStack[0];
    }

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent e)
    {
        if (matches(e.craftMatrix))
        {
            ItemStack stack = getCluster(e.craftMatrix);
            if (stack != null)
            {
                if (stack.hasTagCompound())
                {
                    int stackSize = e.crafting.stackSize;
                    stackSize = stackSize == 0 ? 1 : stackSize;
                    for (ItemStack component : TileEntityCluster.getSubblocks(stack))
                    {
                        component.stackSize = stackSize;
                        if (!e.player.inventory.addItemStackToInventory(component))
                            e.player.dropItem(component, false);
                    }
                }
            }
        }
    }
}
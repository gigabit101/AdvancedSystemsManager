package advancedsystemsmanager.recipes;

import advancedsystemsmanager.registry.ClusterRegistry;
import advancedsystemsmanager.tileentities.TileEntityQuantumCable;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class QuantumCraftingRecipe implements IRecipe
{
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world)
    {
        int enderPearl = 0;
        int enderEye = 0;
        boolean hasCable = false;
        ItemStack cable = ClusterRegistry.CABLE.getItemStack();
        for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++)
        {
            ItemStack item = inventoryCrafting.getStackInSlot(i);

            if (item != null)
            {
                if (item.isItemEqual(cable))
                {
                    if (hasCable) return false;
                    else hasCable = true;
                } else if (item.getItem() == Items.ENDER_PEARL)
                {
                    enderPearl++;
                } else if (item.getItem() == Items.ENDER_EYE)
                {
                    enderEye++;
                } else
                {
                    return false;
                }
            }
        }
        return hasCable && enderEye + enderPearl > 0;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting)
    {
        int enderPearl = 0;
        int enderEye = 0;
        boolean hasCable = false;
        ItemStack cable = ClusterRegistry.CABLE.getItemStack();
        for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++)
        {
            ItemStack item = inventoryCrafting.getStackInSlot(i);

            if (item != null)
            {
                if (item.isItemEqual(cable))
                {
                    if (hasCable) return null;
                    else hasCable = true;
                } else if (item.getItem() == Items.ENDER_PEARL)
                {
                    enderPearl++;
                } else if (item.getItem() == Items.ENDER_EYE)
                {
                    enderEye++;
                } else
                {
                    return null;
                }
            }
        }
        int quantumRange = enderEye < 8 ? enderPearl + enderEye : 9;
        if (quantumRange == 0)
        {
            return null;
        }
        ItemStack result = ClusterRegistry.QUANTUM.getItemStack();
        result.stackSize = 2;
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger(TileEntityQuantumCable.NBT_QUANTUM_RANGE, quantumRange);
        result.setTagCompound(tagCompound);
        return result;
    }

    @Override
    public int getRecipeSize()
    {
        return 9;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return ClusterRegistry.QUANTUM.getItemStack();
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return new ItemStack[0];
    }
}

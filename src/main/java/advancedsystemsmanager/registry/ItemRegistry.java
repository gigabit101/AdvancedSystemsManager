package advancedsystemsmanager.registry;

import advancedsystemsmanager.items.ItemDuplicator;
import advancedsystemsmanager.items.ItemLabeler;
import advancedsystemsmanager.items.ItemRemoteAccessor;
import advancedsystemsmanager.reference.Names;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import thevault.registry.Register;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemRegistry
{
    @Register(name = Names.DUPLICATOR)
    public static ItemDuplicator duplicator;

    @Register(name = Names.LABELER)
    public static ItemLabeler labeler;
    public static ItemStack defaultLabeler;

    @Register(name = Names.REMOTE_ACCESS)
    public static ItemRemoteAccessor remoteAccessor;

    public static void registerRecipes()
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(duplicator), " x ", "xyx", " x ", 'x', "ingotIron", 'y', ClusterRegistry.MANAGER.getItemStack()));
        defaultLabeler = new ItemStack(labeler);
        ItemLabeler.saveStrings(defaultLabeler, new ArrayList<String>(Arrays.asList("Energy Receiver", "Energy Provider", "Input Inventory", "Input Tank", "Output Inventory", "Output Tank")));
        GameRegistry.addRecipe(new ShapedOreRecipe(defaultLabeler, "ppp", " i ", "rxr", 'p', new ItemStack(Items.PAPER), 'i', "dyeBlack", 'r', "dustRedstone", 'x', new ItemStack(Blocks.PISTON)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(remoteAccessor), "ggg", "rlc", "dmd", 'g', "blockGlass", 'r', Items.REPEATER, 'l', Blocks.REDSTONE_LAMP, 'c', Items.COMPARATOR, 'd', Items.GLOWSTONE_DUST, 'm', ClusterRegistry.MANAGER.getItemStack()));
        GameRegistry.addShapelessRecipe(new ItemStack(remoteAccessor, 1, 1), new ItemStack(remoteAccessor), Blocks.BEACON);
    }
}

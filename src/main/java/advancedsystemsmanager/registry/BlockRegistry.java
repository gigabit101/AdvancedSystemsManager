package advancedsystemsmanager.registry;

import advancedsystemsmanager.blocks.*;
import advancedsystemsmanager.compatibility.rf.RFCompat;
import advancedsystemsmanager.items.blocks.*;
import advancedsystemsmanager.recipes.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BlockRegistry
{
    public static int BLOCKS_TO_REGISTER = 2;
    public static BlockTileElement[] cableElements;

    public static void registerBlocks()
    {
        cableElements = new BlockTileElement[BLOCKS_TO_REGISTER];
        for (int i = 0; i < BLOCKS_TO_REGISTER; i++)
        {
            GameRegistry.registerBlock(cableElements[i] = new BlockTileElement(i), ItemTileElement.class, "element" + i);
        }
    }


    public static void registerRecipes()
    {
        GameRegistry.addRecipe(ClusterRegistry.MANAGER.getItemStack(),
                "III",
                "IRI",
                "SPS",
                'R', Blocks.REDSTONE_BLOCK,
                'P', Blocks.PISTON,
                'S', Blocks.STONE
        );

        ItemStack cable = ClusterRegistry.CABLE.getItemStack();
        cable.stackSize = 8;

        GameRegistry.addRecipe(cable,
                "GPG",
                "IRI",
                "GPG",
                'R', Items.REDSTONE,
                'G', Blocks.GLASS,
                'I', Items.IRON_INGOT,
                'P', Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE
        );

        cable = ClusterRegistry.CABLE.getItemStack();

        GameRegistry.addShapelessRecipe(ClusterRegistry.RELAY.getItemStack(),
                cable,
                Blocks.HOPPER
        );

        GameRegistry.addShapelessRecipe(ClusterRegistry.EMITTER.getItemStack(),
                cable,
                Items.REDSTONE,
                Items.REDSTONE,
                Items.REDSTONE
        );


        GameRegistry.addShapelessRecipe(ClusterRegistry.RECEIVER.getItemStack(),
                cable,
                Items.REDSTONE
        );

        GameRegistry.addShapelessRecipe(ClusterRegistry.RELAY.getItemStack(1),
                ClusterRegistry.RELAY.getItemStack(),
                new ItemStack(Items.DYE, 1, 4)
        );

        GameRegistry.addShapelessRecipe(ClusterRegistry.VALVE.getItemStack(),
                cable,
                Blocks.HOPPER,
                Blocks.HOPPER,
                Blocks.DROPPER
        );

        GameRegistry.addShapelessRecipe(ClusterRegistry.BUD.getItemStack(),
                cable,
                Items.QUARTZ,
                Items.QUARTZ,
                Items.QUARTZ
        );


        GameRegistry.addShapelessRecipe(ClusterRegistry.BLOCK_GATE.getItemStack(),
                cable,
                Items.IRON_PICKAXE,
                Blocks.DISPENSER
        );

        GameRegistry.addShapelessRecipe(ClusterRegistry.FLUID_GATE.getItemStack(),
                cable,
                Items.BUCKET,
                Blocks.DISPENSER
        );

        GameRegistry.addShapelessRecipe(ClusterRegistry.VALVE.getItemStack(1),
                ClusterRegistry.RECEIVER.getItemStack(),
                Items.GOLD_INGOT
        );

        GameRegistry.addShapelessRecipe(ClusterRegistry.CLUSTER.getItemStack(),
                cable,
                Items.ENDER_PEARL,
                Items.SLIME_BALL,
                Items.ENDER_PEARL
        );

        GameRegistry.addRecipe(new ShapelessOreRecipe(ClusterRegistry.CAMO.getItemStack(), cable, "wool", "wool", "wool"));

        GameRegistry.addShapelessRecipe(ClusterRegistry.CAMO.getItemStack(1),
                ClusterRegistry.CAMO.getItemStack(),
                ClusterRegistry.CAMO.getItemStack(),
                Blocks.IRON_BARS,
                Blocks.IRON_BARS
        );

        GameRegistry.addShapelessRecipe(ClusterRegistry.CAMO.getItemStack(2),
                ClusterRegistry.CAMO.getItemStack(1),
                Blocks.STICKY_PISTON
        );

        GameRegistry.addShapelessRecipe(ClusterRegistry.SIGN.getItemStack(),
                cable,
                new ItemStack(Items.DYE, 0),
                Items.FEATHER
        );

        GameRegistry.addShapelessRecipe(ClusterRegistry.VOID.getItemStack(), cable, Items.MAGMA_CREAM, Items.BLAZE_ROD);

        if (RFCompat.RF != null)
        {
            GameRegistry.addRecipe(RFCompat.RF.getItemStack(), "RRR", "RCR", "RRR", 'R', Items.REDSTONE, 'C', cable);
        }
//        if (AECompat.AE != null)
//        {
//            ItemStack aeInterface = new ItemStack(GameRegistry.findBlock("appliedenergistics2", "tile.BlockInterface"));
//            Item quartz = GameRegistry.findItem("appliedenergistics2", "item.ItemMultiMaterial");
//            ItemStack fluix = new ItemStack(quartz, 1, 12);
//            ItemStack certus = new ItemStack(quartz, 1, 10);
//            Block fluidBlock = GameRegistry.findBlock("extracells", "ecbaseblock");
//            GameRegistry.addRecipe(AECompat.AE.getItemStack(), "FRQ", "ACB", "QRF", 'R', Blocks.redstone_block, 'C', cable, 'A', aeInterface, 'B', fluidBlock == null ? aeInterface : new ItemStack(fluidBlock), 'F', fluix, 'Q', certus);
//        }

        GameRegistry.addRecipe(new ClusterUpgradeRecipe());
        GameRegistry.addRecipe(new ClusterRecipe());
        GameRegistry.addRecipe(new ClusterUncraftingRecipe());
        GameRegistry.addRecipe(new QuantumCraftingRecipe());
        GameRegistry.addRecipe(new QuantumPairingRecipe());
    }

    public static void registerTiles()
    {
        for (BlockTileElement block : cableElements)
        {
            block.clearFactories();
            block.setFactories(ClusterRegistry.getFactories());
        }
        registerRecipes();
        ItemRegistry.registerRecipes();
    }
}

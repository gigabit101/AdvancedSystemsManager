package advancedsystemsmanager.registry;

import advancedsystemsmanager.api.tileentities.ITileElement;
import advancedsystemsmanager.api.tileentities.ITileFactory;
import advancedsystemsmanager.blocks.TileFactory;
import advancedsystemsmanager.client.gui.TextColour;
import advancedsystemsmanager.helpers.LocalizationHelper;
import advancedsystemsmanager.reference.Names;
import advancedsystemsmanager.tileentities.*;
import advancedsystemsmanager.tileentities.manager.TileEntityManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static advancedsystemsmanager.reference.Names.*;
import static advancedsystemsmanager.reference.Names.CABLE_VOID;

public class ClusterRegistry
{
    private static final Map<String, ITileFactory> registry = new TreeMap<String, ITileFactory>();
    public static TileFactory CABLE = registerFactory(new TileFactory(TileEntityCable.class, new String[]{CABLE_BLOCK})
    {
        @Override
        public boolean isCable(ITileElement element)
        {
            return true;
        }

        @Override
        public float getBlockHardness()
        {
            return 0.4f;
        }
    });
    public static TileFactory MANAGER = registerFactory(new TileFactory(TileEntityManager.class, new String[]{Names.MANAGER}, TOP_SUFFIX, BOTTOM_SUFFIX)
    {
        @Override
        public float getBlockHardness()
        {
            return 2f;
        }

//        @Override
//        @SideOnly(Side.CLIENT)
//        public IIcon getIcon(int side, int subtype)
//        {
//            return super.icons[0][side == 0 ? 2 : side == 1 ? 1 : 0];
//        }

        @Override
        @SideOnly(Side.CLIENT)
        public void addInformation(ItemStack stack, EntityPlayer player, List<String> information, boolean advanced)
        {
            int amount = 0;
            if (stack.hasTagCompound())
            {
                NBTTagList components = stack.getTagCompound().getTagList(TileEntityManager.NBT_COMPONENTS, 10);
                amount = components.tagCount();
            }
            if (amount > 0) information.add(LocalizationHelper.translateFormatted(Names.COMMANDS, amount));
        }
    });
    public static TileFactory BLOCK_GATE = registerDirectionElement(TileEntityBlockGate.class, new String[]{CABLE_BLOCK_GATE}, SIDE_SUFFIX, FACE_SUFFIX, DIRECTION_SUFFIX);
    public static TileFactory BUD = registerClusterElement(TileEntityBUD.class, new String[]{CABLE_BUD});
    public static TileFactory CAMO = registerClusterElement(TileEntityCamouflage.class, new String[]{CABLE_CAMO, CABLE_CAMO_INSIDE, CABLE_CAMO_TRANSFORM});
    public static TileFactory CLUSTER = registerFactory(new TileFactory(TileEntityCluster.class, new String[]{CABLE_CLUSTER, CABLE_CLUSTER + ADVANCED_SUFFIX}, SIDE_SUFFIX)
    {

        @Override
        public boolean canPlaceBlock(World world, BlockPos pos, ItemStack stack)
        {
            return TileEntityCluster.hasSubBlocks(stack);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void addInformation(ItemStack stack, EntityPlayer player, List<String> information, boolean advanced)
        {
            if (TileEntityCluster.hasSubBlocks(stack))
            {
                for (ItemStack itemStack : TileEntityCluster.getSubblocks(stack))
                {
                    information.add(itemStack.getDisplayName());
                }
            } else
            {
                information.add(LocalizationHelper.translate(Names.EMPTY_CLUSTER));
            }
        }

        @Override
        public boolean isCable(ITileElement element)
        {
            return element.getSubtype() == 1;
        }
    });
//    public static TileFactory CREATIVE = registerClusterElement(TileEntityCreative.class, new String[]{CABLE_CREATIVE});
    public static TileFactory EMITTER = registerClusterElement(TileEntityEmitter.class, new String[]{CABLE_OUTPUT}, WEAK_SUFFIX, IDLE_SUFFIX);
    public static TileFactory FLUID_GATE = registerDirectionElement(TileEntityFluidGate.class, new String[]{CABLE_FLUID_GATE}, SIDE_SUFFIX);
    public static TileFactory RECEIVER = registerClusterElement(TileEntityReceiver.class, new String[]{CABLE_INPUT});
    public static TileFactory RELAY = registerInterfaceElement(TileEntityRelay.class, new String[]{CABLE_RELAY, CABLE_RELAY + ADVANCED_SUFFIX}, SIDE_SUFFIX);
    public static TileFactory SIGN = registerDirectionElement(TileEntitySignUpdater.class, new String[]{CABLE_SIGN}, SIDE_SUFFIX);
    public static TileFactory VALVE = registerDirectionElement(TileEntityValve.class, new String[]{CABLE_VALVE, CABLE_VALVE + ADVANCED_SUFFIX}, SIDE_SUFFIX);
    public static TileFactory VOID = registerInterfaceElement(TileEntityVoid.class, new String[]{CABLE_VOID});
    public static TileFactory QUANTUM = registerFactory(new TileFactory(TileEntityQuantumCable.class, new String[]{CABLE_QUANTUM})
    {
        @Override
        public void onCreated(ItemStack stack, World world, EntityPlayer player)
        {
            if (!world.isRemote && stack.hasTagCompound() && stack.getTagCompound().hasKey(TileEntityQuantumCable.NBT_QUANTUM_KEY))
            {
                TileEntityQuantumCable.getNextQuantumKey();
            }
        }

        @Override
        public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
        {
            for (int i : new int[]{1, 8, 9})
            {
                ItemStack stack = getItemStack();
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setInteger(TileEntityQuantumCable.NBT_QUANTUM_RANGE, i);
                stack.setTagCompound(tagCompound);
                list.add(stack);
            }
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void addInformation(ItemStack stack, EntityPlayer player, List<String> information, boolean advanced)
        {
            if (!stack.hasTagCompound())
            {
                stack.setTagCompound(new NBTTagCompound());
            }
            NBTTagCompound tag = stack.getTagCompound();

            if (tag.hasKey(TileEntityQuantumCable.NBT_QUANTUM_KEY))
            {
                information.add(LocalizationHelper.translateFormatted(Names.QUANTUM_PAIRING, TileEntityQuantumCable.getSpinString(tag.getInteger(TileEntityQuantumCable.NBT_QUANTUM_KEY))));
            } else
            {
                information.add(TextColour.RED.toString() + LocalizationHelper.translate(Names.QUANTUM_UNPAIRED));
            }
            int range = tag.getInteger(TileEntityQuantumCable.NBT_QUANTUM_RANGE);
            if (range < 9)
            {
                information.add(LocalizationHelper.translateFormatted(Names.QUANTUM_RANGE, TileEntityQuantumCable.getRange(range)));
            } else
            {
                information.add(LocalizationHelper.translate(Names.QUANTUM_INTERDIMENSIONAL));
            }
        }

        @Override
        public boolean canPlaceBlock(World world, BlockPos pos, ItemStack stack)
        {
            return stack.hasTagCompound() && stack.getTagCompound().hasKey(TileEntityQuantumCable.NBT_QUANTUM_KEY);
        }
    });

    private static TileFactory registerClusterElement(Class<? extends TileEntityElementBase> clazz, String[] subtypes, String... iconNames)
    {
        return registerFactory(new TileFactory.Cluster(clazz, subtypes, iconNames));
    }

    private static TileFactory registerDirectionElement(Class<? extends TileEntityElementBase> clazz, String[] subtypes, String... iconNames)
    {
        return registerFactory(new TileFactory.Directional(clazz, subtypes, iconNames));
    }
    private static TileFactory registerInterfaceElement(Class<? extends TileEntityElementBase> clazz, String[] subtype, String... iconNames)
    {
        return registerFactory(new TileFactory.Interface(clazz, subtype, iconNames));
    }

    private static TileFactory registerFactory(TileFactory factory)
    {
        register(factory);
        return factory;
    }

    public static void register(ITileFactory tileFactory)
    {
        if (registry.containsKey(tileFactory.getKey()))
            throw new IllegalArgumentException("ID: " + tileFactory.getKey() + " is already registered by " + registry.get(tileFactory.getKey()).getClass());
        registry.put(tileFactory.getKey(), tileFactory);
    }

    public static ITileFactory get(String id)
    {
        return registry.get(id);
    }

    public static int getId(ITileFactory factory)
    {
        return FactoryMappingRegistry.INSTANCE.getId(factory.getKey());
    }

    public static Collection<String> getKeys()
    {
        return registry.keySet();
    }

    public static Collection<ITileFactory> getFactories()
    {
        return registry.values();
    }

//    @SideOnly(Side.CLIENT)
//    public static void registerIcons(IIconRegister register)
//    {
//        for (ITileFactory factory : getFactories()) factory.registerIcons(register);
//    }
}

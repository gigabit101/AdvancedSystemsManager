package advancedsystemsmanager.items;

import advancedsystemsmanager.AdvancedSystemsManager;
import advancedsystemsmanager.reference.Names;
import advancedsystemsmanager.registry.ClusterRegistry;
import advancedsystemsmanager.tileentities.manager.TileEntityManager;
import crazypants.enderio.machine.monitor.StatCollector;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemRemoteAccessor extends ItemBase
{
    public static final String WORLD = "world";
    public static final String DIMENSION = "dim";

//    @SideOnly(Side.CLIENT)
//    private IIcon[] icons;

    public ItemRemoteAccessor()
    {
        super(Names.REMOTE_ACCESS);
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public IIcon getIconIndex(ItemStack stack)
//    {
//        return getIcon(stack, Minecraft.getMinecraft().theWorld);
//    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote && !player.isSneaking() && stack.hasTagCompound())
        {
            World managerWorld = stack.getItemDamage() == 0 ? world : DimensionManager.getWorld(stack.getTagCompound().getByte(WORLD));
            int x = stack.getTagCompound().getInteger(X);
            int y = stack.getTagCompound().getInteger(Y);
            int z = stack.getTagCompound().getInteger(Z);
//            if (managerWorld.blockExists(x, y, z))
//            {
//                if (ClusterRegistry.MANAGER.getTileEntity(managerWorld, pos) instanceof TileEntityManager)
//                    FMLNetworkHandler.openGui(player, AdvancedSystemsManager.INSTANCE, 1, world, x, y, z);
//            }
        }
        return super.onItemRightClick(stack, world, player, hand);
 }


//
//    @Override
//    public EnumActionResult onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
//    {
//        if (!world.isRemote && !player.isSneaking() && stack.hasTagCompound())
//        {
//            World managerWorld = stack.getItemDamage() == 0 ? world : DimensionManager.getWorld(stack.getTagCompound().getByte(WORLD));
//            int x = stack.getTagCompound().getInteger(X);
//            int y = stack.getTagCompound().getInteger(Y);
//            int z = stack.getTagCompound().getInteger(Z);
//            if (managerWorld.blockExists(x, y, z))
//            {
//                if (ClusterRegistry.MANAGER.getTileEntity(managerWorld, x, y, z) instanceof TileEntityManager)
//                    FMLNetworkHandler.openGui(player, AdvancedSystemsManager.INSTANCE, 1, world, x, y, z);
//            }
//        }
//        return super.onItemRightClick(stack, world, player);
//    }

//    @Override
//    public String getUnlocalizedName(ItemStack stack)
//    {
//        return super.getUnlocalizedName(stack) + (stack.getItemDamage() != 0 ? Names.ADVANCED_SUFFIX : "");
//    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_)
    {
        if (stack.hasTagCompound())
        {
            int dim = stack.getTagCompound().getByte(WORLD);
            if (stack.getItemDamage() == 0 && player.getEntityWorld().provider.getDimension() != dim)
            {
//                list.add("§c" + StatCollector.translateToLocal(Names.WRONG_DIMENSION));
            }
            int x = stack.getTagCompound().getInteger(X);
            int y = stack.getTagCompound().getInteger(Y);
            int z = stack.getTagCompound().getInteger(Z);
//            list.add(StatCollector.translateToLocal(Names.LINKED_LOCATION));
//            list.add(StatCollector.translateToLocalFormatted(Names.LOCATION, x, y, z));
            list.add(stack.getTagCompound().getString(DIMENSION));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings(value = "unchecked")
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public void registerIcons(IIconRegister register)
//    {
//        icons = new IIcon[5];
//        icons[0] = register.registerIcon(getIconString());
//        icons[1] = register.registerIcon(getIconString() + "_advanced");
//        icons[2] = register.registerIcon(getIconString() + "_off");
//        icons[3] = register.registerIcon(getIconString() + "_off_advanced");
//        icons[4] = register.registerIcon(getIconString() + "_disabled");
//    }


    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (!world.isRemote && player.isSneaking())
        {
            TileEntity te = ClusterRegistry.MANAGER.getTileEntity(world, pos);
            if (te instanceof TileEntityManager)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte(WORLD, (byte) world.provider.getDimension());
                tagCompound.setString(DIMENSION, world.provider.getDimensionType().getName());
                tagCompound.setInteger(X, te.getPos().getX());
                tagCompound.setInteger(Y, te.getPos().getY());
                tagCompound.setInteger(Z, te.getPos().getZ());
                stack.setTagCompound(tagCompound);
            } else
            {
                stack.setTagCompound(null);
            }
            return EnumActionResult.PASS;
        }
        return EnumActionResult.FAIL;
    }


//    @Override
//    @SideOnly(Side.CLIENT)
//    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
//    {
//        return getIcon(stack, player.getEntityWorld());
//    }
//
//    @SideOnly(Side.CLIENT)
//    private IIcon getIcon(ItemStack stack, World world)
//    {
//        if (stack.hasTagCompound() && stack.getItemDamage() == 0 && world.provider.dimensionId != stack.getTagCompound().getByte(WORLD))
//        {
//            return icons[4];
//        }
//        return icons[stack.getItemDamage() + (stack.hasTagCompound() ? 0 : 2)];
//    }
}

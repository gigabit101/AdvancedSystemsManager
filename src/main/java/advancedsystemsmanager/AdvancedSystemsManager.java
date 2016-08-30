package advancedsystemsmanager;

import advancedsystemsmanager.commands.ParentCommand;
import advancedsystemsmanager.compatibility.ModCompat;
import advancedsystemsmanager.helpers.*;
import advancedsystemsmanager.client.gui.GuiHandler;
import advancedsystemsmanager.naming.EventHandler;
import advancedsystemsmanager.network.MessageHandler;
import advancedsystemsmanager.network.PacketEventHandler;
import advancedsystemsmanager.proxy.CommonProxy;
import advancedsystemsmanager.reference.Files;
import advancedsystemsmanager.reference.Metadata;
import advancedsystemsmanager.reference.Reference;
import advancedsystemsmanager.registry.*;
import advancedsystemsmanager.tileentities.TileEntityQuantumCable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.classloading.FMLForgePlugin;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import thevault.registry.Registerer;
import thevault.utils.LogHelper;

import java.io.File;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION_FULL)
public class AdvancedSystemsManager
{
    public static FMLEventChannel packetHandler;

    @SidedProxy(clientSide = "advancedsystemsmanager.proxy.ClientProxy", serverSide = "advancedsystemsmanager.proxy.CommonProxy")
    public static CommonProxy PROXY;

    @Mod.Instance(value = Reference.ID)
    public static AdvancedSystemsManager INSTANCE;

    @Mod.Metadata(Reference.ID)
    public static ModMetadata metadata;

    public static GuiHandler guiHandler = new GuiHandler();
    public static LogHelper log = new LogHelper(Reference.ID);
    public static ConfigHandler configHandler;
    public static CreativeTabs creativeTab;
    public static Registerer registerer;
    public static ThemeHandler themeHandler;
    public static WorldSaveHelper worldSave;



    public static boolean DEV_ENVIRONMENT = FMLForgePlugin.RUNTIME_DEOBF;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = Metadata.init(metadata);
        Files.CONFIG_DIR = new File(event.getModConfigurationDirectory() + File.separator + Reference.ID);
        if (!Files.CONFIG_DIR.exists()) Files.CONFIG_DIR.mkdir();
        configHandler = new ConfigHandler(new File(Files.CONFIG_DIR.getAbsolutePath() + File.separator + event.getSuggestedConfigurationFile().getName()));
        configHandler.init();
        creativeTab = new CreativeTabs(Reference.ID)
        {
            @Override
            public ItemStack getIconItemStack()
            {
                return ClusterRegistry.MANAGER.getItemStack();
            }

            @Override
            public Item getTabIconItem()
            {
                return null;
            }
        };

        registerer = new Registerer(log, PROXY, configHandler);
        registerer.scan(BlockRegistry.class, event.getSide());
        registerer.scan(ItemRegistry.class, event.getSide());

        BlockRegistry.registerBlocks();

        MessageHandler.init();

        packetHandler = NetworkRegistry.INSTANCE.newEventDrivenChannel(Reference.NETWORK_ID);

        ModCompat.preInit();

        worldSave = new WorldSaveHelper();
        MinecraftForge.EVENT_BUS.register(worldSave);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        PROXY.init();

        packetHandler.register(new PacketEventHandler());
        NetworkRegistry.INSTANCE.registerGuiHandler(AdvancedSystemsManager.INSTANCE, guiHandler);
        OreDictionaryHelper.registerUsefulThings();
        EventHandler handler = new EventHandler();
        FMLCommonHandler.instance().bus().register(handler);
        MinecraftForge.EVENT_BUS.register(handler);
        PROXY.initHandlers();
        RemapHelper.registerMappings();
        ModCompat.init();
    }

    @Mod.EventHandler
    @SuppressWarnings(value = "unchecked")
    public void postInit(FMLPostInitializationEvent event)
    {
        ModCompat.postInit();
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {
        configHandler.loadPowerValues();
    }

    @Mod.EventHandler
    public void missingMapping(FMLMissingMappingsEvent event)
    {
        RemapHelper.handleRemap(event);
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event)
    {
        create(Files.WORLD_SAVE_DIR = new File(DimensionManager.getCurrentSaveRootDirectory().getPath() + File.separator + "asm_data"));
        create(Files.MANAGER_SAVE_DIR = new File(Files.WORLD_SAVE_DIR, "managers"));
        FactoryMappingRegistry.load();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
        ModItemHelper.init();
        event.registerServerCommand(ParentCommand.instance);
    }

    private void create(File file)
    {
        if (!file.exists()) file.mkdir();
    }

    @Mod.EventHandler
    public void serverStop(FMLServerStoppingEvent event)
    {
        TileEntityQuantumCable.clearRegistry();
    }
}

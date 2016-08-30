package advancedsystemsmanager.helpers;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

public final class ModItemHelper
{
    public static TObjectIntMap<Item> items;

    public static void init()
    {
        FMLControlledNamespacedRegistry<Item> itemRegistry = GameData.getItemRegistry();
        Map<String, Integer> modMappings = new HashMap<String, Integer>();
        modMappings.put(null, -1);
        Object[] keys = itemRegistry.getKeys().toArray();

        items = new TObjectIntHashMap<Item>(keys.length);
        int modVal = 1;
        for (Object key : keys)
        {
            Item item = (Item)itemRegistry.getObject(key);
            GameRegistry.UniqueIdentifier uniqueIdentity = GameRegistry.findUniqueIdentifierFor(item);
            String modId = uniqueIdentity == null ? null : uniqueIdentity.modId;
            if (modMappings.containsKey(modId))
            {
                items.put(item, modMappings.get(modId));
            } else
            {
                modMappings.put(modId, modVal);
                items.put(item, modVal);
                modVal++;
            }
        }
    }

    public static boolean areItemsFromSameMod(Item item1, Item item2)
    {
        if (item1 == null || item2 == null)
        {
            return false;
        } else
        {
            int mod1 = items.get(item1);
            int mod2 = items.get(item2);

            return mod1 > 0 && mod1 == mod2;
        }
    }
}

package advancedsystemsmanager.threading;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@SideOnly(Side.CLIENT)
public class ThreadSafeHandler
{
    private static List<SearchItems> handle = new ArrayList<SearchItems>();

    public static void handle(SearchItems search)
    {
        SearchItems existing;
        for (ListIterator<SearchItems> itr = handle.listIterator(); itr.hasNext(); )
        {
            if ((existing = itr.next()).getScrollController() == search.getScrollController())
            {
                if (existing.getTime() < search.getTime())
                {
                    itr.remove();
                    itr.add(search);
                }
                return;
            }
        }
        handle.add(search);
    }

    @SubscribeEvent
    public void renderEvent(RenderWorldLastEvent e)
    {
        if (handle.size() > 0)
        {
            for (SearchItems search : handle)
            {
                search.setResult();
            }
            handle.clear();
        }
    }
}

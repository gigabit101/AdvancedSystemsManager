package advancedsystemsmanager.reference;

import advancedsystemsmanager.api.network.IPacketProvider;
import advancedsystemsmanager.api.network.IPacketSync;
import advancedsystemsmanager.network.ASMPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class Null
{
    public static IInventory NULL_INVENTORY = new IInventory()
    {
        @Override
        public String getName() {
            return null;
        }

        @Override
        public boolean hasCustomName() {
            return false;
        }

        @Override
        public ITextComponent getDisplayName() {
            return null;
        }

        @Override
        public int getSizeInventory()
        {
            return 0;
        }

        @Override
        public ItemStack getStackInSlot(int slot)
        {
            return null;
        }

        @Override
        public ItemStack decrStackSize(int slot, int amount)
        {
            return null;
        }

        @Nullable
        @Override
        public ItemStack removeStackFromSlot(int index) {
            return null;
        }


        @Override
        public void setInventorySlotContents(int slot, ItemStack stack)
        {

        }

        @Override
        public int getInventoryStackLimit()
        {
            return Integer.MAX_VALUE;
        }

        @Override
        public void markDirty()
        {
        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer player)
        {
            return false;
        }

        @Override
        public void openInventory(EntityPlayer player) {

        }

        @Override
        public void closeInventory(EntityPlayer player) {

        }

        @Override
        public boolean isItemValidForSlot(int slot, ItemStack stack)
        {
            return false;
        }

        @Override
        public int getField(int id) {
            return 0;
        }

        @Override
        public void setField(int id, int value) {

        }

        @Override
        public int getFieldCount() {
            return 0;
        }

        @Override
        public void clear() {

        }

    };
    public static ItemStack NULL_STACK = new ItemStack(Blocks.END_PORTAL, 0);
    public static IPacketProvider NULL_PACKET = new IPacketProvider()
    {
        @Override
        public ASMPacket getSyncPacket()
        {
            return new ASMPacket();
        }

        @Override
        public void registerSyncable(IPacketSync networkSync)
        {
        }

        @Override
        public void sendPacketToServer(ASMPacket packet)
        {
        }
    };

}

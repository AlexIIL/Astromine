package com.github.chainmailstudios.astromine.common.block.entity.base;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

import java.util.Set;
import java.util.stream.IntStream;

public abstract class DefaultedItemBlockEntity extends DefaultedBlockEntity implements ComponentProvider, InventoryProvider, SidedInventory, BlockEntityClientSerializable {
	protected final SimpleItemInventoryComponent itemComponent = new SimpleItemInventoryComponent(1);
	private final ItemInventoryFromInventoryComponent inventory = () -> itemComponent;

	public DefaultedItemBlockEntity(BlockEntityType<?> type) {
		super(type);

		addComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT, itemComponent);
	}

	@Override
	public int size() {
		return inventory.size();
	}

	@Override
	public boolean isEmpty() {
		return inventory.isEmpty();
	}

	@Override
	public ItemStack getStack(int slot) {
		return inventory.getStack(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return inventory.removeStack(slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return inventory.removeStack(slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		inventory.setStack(slot, stack);
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		inventory.clear();
	}

	@Override
	public int count(Item item) {
		return inventory.count(item);
	}

	@Override
	public boolean containsAny(Set<Item> items) {
		return inventory.containsAny(items);
	}

	@Override
	public int[] getAvailableSlots(Direction direction) {
		if (transferComponent.get(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT).get(direction) != TransferType.NONE) {
			return IntStream.rangeClosed(0, inventory.size() - 1).toArray();
		} else {
			return new int[0];
		}
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction direction) {
		if (transferComponent.get(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT).get(direction).canInsert()) {
			return getSidedComponent(direction, AstromineComponentTypes.ITEM_INVENTORY_COMPONENT).canInsert(stack, slot);
		} else {
			return false;
		}
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction direction) {
		if (transferComponent.get(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT).get(direction).canExtract()) {
			return getSidedComponent(direction, AstromineComponentTypes.ITEM_INVENTORY_COMPONENT).canExtract(stack, slot);
		} else {
			return false;
		}
	}

	@Override
	public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
		return this;
	}
}

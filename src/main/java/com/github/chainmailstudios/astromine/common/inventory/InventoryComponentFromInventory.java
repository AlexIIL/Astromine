package com.github.chainmailstudios.astromine.common.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An InventoryComponentFromInventory
 * is a wrapper over an Inventory
 * that provides the functions and utilities
 * of an InventoryComponent.
 */
public class InventoryComponentFromInventory implements InventoryComponent {
	Inventory inventory;
	List<Runnable> listeners = new ArrayList<>();

	private InventoryComponentFromInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public static InventoryComponentFromInventory of(Inventory inventory) {
		return new InventoryComponentFromInventory(inventory);
	}

	@Override
	public AbstractMap<Integer, ItemStack> getContents() {
		HashMap<Integer, ItemStack> contents = new HashMap<>();
		for (int i = 0; i < inventory.size(); ++i) {
			contents.put(i, inventory.getStack(i));
		}
		return contents;
	}

	@Override
	public ActionResult canInsert() {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canExtract() {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canInsert(ItemStack stack) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canExtract(ItemStack stack) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canInsert(int slot) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canExtract(int slot) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canInsert(ItemStack stack, int slot) {
		return ActionResult.SUCCESS;
	}

	@Override
	public ActionResult canExtract(ItemStack stack, int slot) {
		return ActionResult.SUCCESS;
	}

	@Override
	public int getSize() {
		return inventory.size();
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		inventory.setStack(slot, stack);
	}

	@Override
	public ItemStack getStack(int slot) {
		return inventory.getStack(slot);
	}

	@Override
	public List<Runnable> getListeners() {
		return listeners;
	}
}
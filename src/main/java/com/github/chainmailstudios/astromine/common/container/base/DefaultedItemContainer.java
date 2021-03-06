package com.github.chainmailstudios.astromine.common.container.base;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedItemBlockEntity;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;

import java.util.Collection;

public class DefaultedItemContainer extends DefaultedBlockEntityContainer {
	public final Collection<WSlot> playerSlots;

	public DefaultedItemBlockEntity blockEntity;

	public DefaultedItemContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);

		WInterface mainInterface = getInterface();

		playerSlots = WSlot.addHeadlessPlayerInventory(mainInterface);

		blockEntity = (DefaultedItemBlockEntity) world.getBlockEntity(position);
	}
}

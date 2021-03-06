package com.github.chainmailstudios.astromine.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyItemContainerScreen;
import com.github.chainmailstudios.astromine.common.block.entity.SorterBlockEntity;
import com.github.chainmailstudios.astromine.common.container.SorterContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyItemContainer;
import com.github.chainmailstudios.astromine.common.widget.WHorizontalArrow;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class SorterContainerScreen extends DefaultedEnergyItemContainerScreen<SorterContainer> {
	public SorterContainerScreen(Text name, DefaultedEnergyItemContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);

		SorterBlockEntity sorter = (SorterBlockEntity) linkedContainer.blockEntity;

		WSlot input = mainPanel.createChild(WSlot::new, Position.of(energyBar), Size.of(18, 18)).setInventoryNumber(1).setSlotNumber(0);
		WSlot output = mainPanel.createChild(WSlot::new, Position.of(energyBar), Size.of(18, 18)).setInventoryNumber(1).setSlotNumber(1);

		input.centerX();
		input.setPosition(Position.of(input.getX() + 29, input.getY() + 15, input.getZ()));

		WHorizontalArrow arrow = mainPanel.createChild(WHorizontalArrow::new, Position.of(input, -31, 0, 0), Size.of(22, 16))
				.setLimitSupplier(() -> sorter.limit)
				.setProgressSupplier(() -> sorter.progress);

		output.centerX();
		output.setPosition(Position.of(arrow, -27, 0, 0));
	}
}

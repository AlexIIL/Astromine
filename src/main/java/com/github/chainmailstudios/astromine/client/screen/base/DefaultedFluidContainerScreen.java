package com.github.chainmailstudios.astromine.client.screen.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedBlockEntityContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedFluidContainer;
import com.github.chainmailstudios.astromine.common.widget.WFluidVolumeFractionalVerticalBar;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public abstract class DefaultedFluidContainerScreen<T extends DefaultedBlockEntityContainer> extends DefaultedBlockEntityContainerScreen<T> {
	public WFluidVolumeFractionalVerticalBar fluidBar;

	public DefaultedFluidContainerScreen(Text name, DefaultedFluidContainer linkedContainer, PlayerEntity player) {
		super(name, (T) linkedContainer, player);

		fluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(mainPanel, 7,  20, 0), Size.of(24, 48));

		ComponentProvider componentProvider = linkedContainer.blockEntity;

		fluidBar.setFluidVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(0));
	}
}

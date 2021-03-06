package com.github.chainmailstudios.astromine.common.block.entity;

import net.minecraft.block.AirBlock;
import net.minecraft.block.FacingBlock;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.world.WorldAtmosphereComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;

public class VentBlockEntity extends DefaultedEnergyFluidBlockEntity implements Tickable, NetworkMember {
	public VentBlockEntity() {
		super(AstromineBlockEntityTypes.VENT);

		energyComponent.getVolume(0).setSize(new Fraction(16, 1));
		fluidComponent.getVolume(0).setSize(new Fraction(16, 1));
	}

	@Override
	public void tick() {
		if (energyComponent.getVolume(0).hasStored(Fraction.BOTTLE) && (fluidComponent.getVolume(0).hasStored(Fraction.BUCKET))) {
			BlockPos position = getPos();

			Direction direction = world.getBlockState(position).get(FacingBlock.FACING);

			BlockPos output = position.offset(direction);

			if (world.getBlockState(output).getBlock() instanceof AirBlock) {
				ComponentProvider componentProvider = ComponentProvider.fromWorld(world);

				WorldAtmosphereComponent atmosphereComponent = componentProvider.getComponent(AstromineComponentTypes.WORLD_ATMOSPHERE_COMPONENT);

				FluidVolume volume = atmosphereComponent.get(output);

				fluidComponent.getVolume(0).pushVolume(volume, Fraction.BUCKET);
				energyComponent.getVolume(0).extractVolume(Fraction.BOTTLE);

				atmosphereComponent.add(output, volume);

			}
		}
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.FLUID || type == AstromineNetworkTypes.ENERGY;
	}

	@Override
	public <T extends NetworkType> boolean isRequester(T type) {
		return true;
	}
}

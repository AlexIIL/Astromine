package com.github.chainmailstudios.astromine.client.particle;

import net.minecraft.client.particle.AbstractSlowingParticle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.world.ClientWorld;

public class RocketFlameParticle extends AbstractSlowingParticle {
	public RocketFlameParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
		super(clientWorld, d, e, f, g, h, i);
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}
}

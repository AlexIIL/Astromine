package com.github.chainmailstudios.astromine.registry;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import com.mojang.serialization.Codec;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.world.AsteroidBeltBiome;
import com.github.chainmailstudios.astromine.common.world.generation.AstromineBiomeSource;

public class AstromineBiomes {
	public static final Codec<AstromineBiomeSource> SPACE = Registry.register(Registry.BIOME_SOURCE, AstromineCommon.identifier("space"), AstromineBiomeSource.CODEC);
	public static Biome ASTEROID_BELT;

	public static void initialize() {
		ASTEROID_BELT = Registry.register(Registry.BIOME, AstromineCommon.identifier("asteroid_belt"), new AsteroidBeltBiome());
	}

	public <T extends BiomeSource> Codec<T> register(Identifier id, Codec<BiomeSource> codec) {
		return (Codec<T>) Registry.register(Registry.BIOME_SOURCE, id, codec);
	}
}

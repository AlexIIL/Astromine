package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import java.util.function.Function;

public class ClientUtilities {
	public static void addEntity(PersistentProjectileEntity persistentProjectileEntity) {
		MinecraftClient.getInstance().world.addEntity(persistentProjectileEntity.getEntityId(), persistentProjectileEntity);
	}

	public static void playSound(BlockPos position, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance) {
		MinecraftClient.getInstance().world.playSoundFromEntity(MinecraftClient.getInstance().player, MinecraftClient.getInstance().player, sound, category, volume, pitch);
	}

	public static final class Weapon {
		public static boolean isAiming() {
			return MinecraftClient.getInstance().options.keyUse.isPressed();
		}
	}

	@Environment(EnvType.CLIENT)
	public static void buildClient(String name, int tint, Fluid still, Fluid flowing) {
		final Identifier stillSpriteIdentifier = new Identifier("block/water_still");
		final Identifier flowingSpriteIdentifier = new Identifier("block/water_flow");
		final Identifier listenerIdentifier = AstromineCommon.identifier(name + "_reload_listener");

		final Sprite[] fluidSprites = {null, null};

		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX).register((atlasTexture, registry) -> {
			registry.register(stillSpriteIdentifier);
			registry.register(flowingSpriteIdentifier);
		});

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return listenerIdentifier;
			}

			@Override
			public void apply(ResourceManager resourceManager) {
				final Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
				fluidSprites[0] = atlas.apply(stillSpriteIdentifier);
				fluidSprites[1] = atlas.apply(flowingSpriteIdentifier);
			}
		});

		final FluidRenderHandler handler = new FluidRenderHandler() {
			@Override
			public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state) {
				return fluidSprites;
			}

			@Override
			public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state) {
				return tint;
			}
		};

		FluidRenderHandlerRegistry.INSTANCE.register(still, handler);
		FluidRenderHandlerRegistry.INSTANCE.register(flowing, handler);
	}
}

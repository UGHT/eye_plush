package com.github.UGHT.eternis_plush;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.IdentityHashMap;

public class EternisPlush implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod name as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("Eternis Plush");

	public static final Block ETERNIS_PLUSH = new Block(QuiltBlockSettings.create());

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
		Registry.register(Registries.BLOCK, new Identifier(mod.metadata().id(), "eternis_plush"), ETERNIS_PLUSH);
		Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "eternis_plush"), new BlockItem(ETERNIS_PLUSH, new QuiltItemSettings().fireproof()));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL_BLOCKS).register(entries -> {
			entries.addItem(ETERNIS_PLUSH.asItem());
		});
	}
}

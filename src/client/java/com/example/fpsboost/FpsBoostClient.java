package com.example.fpsboost;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

public final class FpsBoostClient implements ClientModInitializer {
	private static final int PERFORMANCE_RENDER_DISTANCE = 8;
	private static final int PERFORMANCE_SIMULATION_DISTANCE = 6;
	private static final int UNSET_DISTANCE = -1;

	private static boolean enabled;
	private static int originalRenderDistance = UNSET_DISTANCE;
	private static int originalSimulationDistance = UNSET_DISTANCE;

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			var fpsboostCommand = ClientCommandManager.literal("fpsboost");
			fpsboostCommand.then(ClientCommandManager.literal("status").executes(context -> {
				MinecraftClient client = context.getSource().getClient();
				if (client.player == null) {
					sendChatMessage(client, "Join a world before checking FPS Boost.", true);
					return 0;
				}

				sendChatMessage(client, enabled ? "FPS Boost is currently enabled." : "FPS Boost is currently disabled.", false);
				return Command.SINGLE_SUCCESS;
			}));
			fpsboostCommand.then(ClientCommandManager.literal("toggle").executes(context -> toggleCommand(context.getSource().getClient())));
			fpsboostCommand.executes(context -> toggleCommand(context.getSource().getClient()));
			dispatcher.register(fpsboostCommand);
		});
	}

	private static int toggleCommand(MinecraftClient client) {
		if (client.player == null) {
			sendChatMessage(client, "Join a world before toggling FPS Boost.", true);
			return 0;
		}

		toggle(client);
		sendChatMessage(client, enabled
			? "FPS Boost enabled: render distance " + PERFORMANCE_RENDER_DISTANCE + ", simulation distance " + PERFORMANCE_SIMULATION_DISTANCE + "."
			: "FPS Boost disabled: previous distances restored.", false);
		return Command.SINGLE_SUCCESS;
	}

	private static void sendChatMessage(MinecraftClient client, String message, boolean error) {
		if (client.inGameHud == null) {
			return;
		}

		if (error) {
			client.inGameHud.getChatHud().addMessage(Text.literal(message));
			return;
		}

		client.inGameHud.getChatHud().addMessage(Text.literal(message));
	}

	private static void toggle(MinecraftClient client) {
		GameOptions options = client.options;
		SimpleOption<Integer> renderDistance = options.getViewDistance();
		SimpleOption<Integer> simulationDistance = options.getSimulationDistance();

		if (enabled) {
			restoreOriginalDistances(renderDistance, simulationDistance);
			return;
		}

		captureOriginalDistances(renderDistance, simulationDistance);
		renderDistance.setValue(Math.min(originalRenderDistance, PERFORMANCE_RENDER_DISTANCE));
		simulationDistance.setValue(Math.min(originalSimulationDistance, PERFORMANCE_SIMULATION_DISTANCE));
		enabled = true;
	}

	private static void captureOriginalDistances(SimpleOption<Integer> renderDistance, SimpleOption<Integer> simulationDistance) {
		originalRenderDistance = renderDistance.getValue();
		originalSimulationDistance = simulationDistance.getValue();
	}

	private static void restoreOriginalDistances(SimpleOption<Integer> renderDistance, SimpleOption<Integer> simulationDistance) {
		if (originalRenderDistance == UNSET_DISTANCE || originalSimulationDistance == UNSET_DISTANCE) {
			enabled = false;
			return;
		}

		renderDistance.setValue(originalRenderDistance);
		simulationDistance.setValue(originalSimulationDistance);
		originalRenderDistance = UNSET_DISTANCE;
		originalSimulationDistance = UNSET_DISTANCE;
		enabled = false;
	}
}
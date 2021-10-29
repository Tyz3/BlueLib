package ru.kronos.bluelib.api.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;

import java.util.ArrayList;
import java.util.List;

public class WorldUtil {
	
	public static List<BlueLibPlayer> getNearestPlayers3D(Location center, double radius) {
		 List<BlueLibPlayer> nearest = new ArrayList<>();
		 OnlineEngine.getOnline().entrySet().stream()
				.filter(en -> en.getValue().getWorld().equals(center.getWorld()) && MathOperation.distance3D(center, en.getValue().getLocation()) <= radius)
				.forEach(e -> nearest.add(e.getValue()));
		 return nearest;
	}
	
	public static List<BlueLibPlayer> getNearestPlayers2D(Location center, double radius) {
		 List<BlueLibPlayer> nearest = new ArrayList<>();
		 OnlineEngine.getOnline().entrySet().stream()
				.filter(en -> en.getValue().getWorld().equals(center.getWorld()) && MathOperation.distance2D(center, en.getValue().getLocation()) <= radius)
				.forEach(e -> nearest.add(e.getValue()));
		 return nearest;
	}
	
	public static List<Chunk> getChunks(Location center, int chunkRadius) {
		List<Chunk> chunks = new ArrayList<>();
		World w = center.getWorld();
		int X = center.getBlockX();
		int Z = center.getBlockZ();
		int x = (int) Math.ceil(X < 0 ? (X >> 4) - 1 : X >> 4);
		int z = (int) Math.ceil(Z < 0 ? (Z >> 4) - 1 : Z >> 4);
		for (int i = x - chunkRadius; i < x + chunkRadius; i++) {
			for (int j = z - chunkRadius; j < z + chunkRadius; j++) {
				chunks.add(w.getChunkAt(i, j));
			}
		}
		return chunks;
	}
	
	public static List<LivingEntity> getNearbyLivingEntitiesInChunks3D(List<Chunk> chunks, Location center, double radius) {
		List<LivingEntity> entities = new ArrayList<>();
		for (Chunk c : chunks) {
			for (Entity e : c.getEntities()) {
				if (e instanceof LivingEntity && MathOperation.distance3D(e.getLocation(), center) <= radius) {
					entities.add((LivingEntity) e);
				}
			}
		}
		return entities;
	}
	
	public static List<LivingEntity> getNearbyLivingEntitiesInChunks2D(List<Chunk> chunks, Location center, double radius) {
		List<LivingEntity> entities = new ArrayList<>();
		for (Chunk c : chunks) {
			for (Entity e : c.getEntities()) {
				if (e instanceof LivingEntity && MathOperation.distance2D(e.getLocation(), center) <= radius) {
					entities.add((LivingEntity) e);
				}
			}
		}
		return entities;
	}
	
	
}

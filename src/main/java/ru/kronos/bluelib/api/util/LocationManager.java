package ru.kronos.bluelib.api.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationManager {
	public static Location deserializeLocation(String src) {
		if(src == null) return null;
		
		String[] arr = src.split(" ");
		if(arr.length < 4) return null;
		World world = Bukkit.getWorld(arr[0]);
		if(world == null) return null;
		
		try {
			double x = Double.parseDouble(arr[1]);
			double y = Double.parseDouble(arr[2]);
			double z = Double.parseDouble(arr[3]);
			float yaw = arr.length > 4 ? Float.parseFloat(arr[4]) : 0;
			float pitch = arr.length > 5 ? Float.parseFloat(arr[5]) : 0;
			return new Location(world, x, y, z, yaw, pitch);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String serializeLocation(Location src) {
		if (src == null) return null;
		StringBuilder loc = new StringBuilder();
		loc.append(src.getWorld().getName());
		loc.append(" ");
		loc.append(src.getX());
		loc.append(" ");
		loc.append(src.getY());
		loc.append(" ");
		loc.append(src.getZ());
		loc.append(" ");
		loc.append(src.getYaw());
		loc.append(" ");
		loc.append(src.getPitch());
		return loc.toString();
	}
	
	
}

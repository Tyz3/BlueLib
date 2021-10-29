package ru.kronos.bluelib.api.util;

import org.bukkit.Location;

public class MathOperation {

	public static double roundAvoid(double value, int places) {
	    double scale = Math.pow(10, places);
	    return Math.round(value * scale) / scale;
	}

	public static double roundAvoid(double value) {
	    return Math.round(value);
	}
	
	public static double randValue(double start, double end) {
		return start + Math.random() * (end - start);
	}
	
	public static boolean procChance(double chance) {
		double d = Math.random();
		return .5D - chance/2 < d && d < .5D + chance/2;
	}
	
	public static double calcValueFromString(String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
	
	public static double distance2D(Location loc1, Location loc2) {
		double x1 = loc1.getBlockX();
		double z1 = loc1.getBlockZ();
		double x2 = loc2.getBlockX();
		double z2 = loc2.getBlockZ();
		return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(z2-z1, 2));
	}
	
	public static double distance3D(Location loc1, Location loc2) {
		double x1 = loc1.getX();
		double y1 = loc1.getY();
		double z1 = loc1.getZ();
		double x2 = loc2.getX();
		double y2 = loc2.getY();
		double z2 = loc2.getZ();
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
	}
	
	public static boolean containsCuboid(Location pos1, Location pos2, Location point) {
		int x1 = pos1.getBlockX();
		int y1 = pos1.getBlockY();
		int z1 = pos1.getBlockZ();
		
		int x2 = pos2.getBlockX();
		int y2 = pos2.getBlockY();
		int z2 = pos2.getBlockZ();
		
		int obj_x = point.getBlockX();
		int obj_y = point.getBlockY();
		int obj_z = point.getBlockZ();
		
		if (x1 <= x2) {
			if (obj_x < x1 || x2 <= obj_x) return false;
		} else {
			if (obj_x < x2 || x1 <= obj_x) return false;
		}
		
		if (y1 <= y2) {
			if (obj_y < y1 || y2 <= obj_y) return false;
		} else {
			if (obj_y < y2 || y1 <= obj_y) return false;
		}
		
		if (z1 <= z2) {
			if (obj_z < z1 || z2 <= obj_z) return false;
		} else {
			if (obj_z < z2 || z1 <= obj_z) return false;
		}
		
		return true;
	}
	
	public static boolean containsEllipsoid(Location point, Location pos, int length, int width, int height) {
		length >>= 1;
		width >>= 1;
		height >>= 1;
		
		double x0 = pos.getX();
		double y0 = pos.getY();
		double z0 = pos.getZ();
		
		double x = point.getX();
		double y = point.getY();
		double z = point.getZ();
		
		double result = Math.pow((x - x0)/length, 2) + Math.pow((y - y0)/height, 2) + Math.pow((z - z0)/width, 2);
		
		return result <= 1;
	}
}

package ru.kronos.bluelib.junk;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.kronos.bluelib.api.template.online.BlueLibSender;
import ru.kronos.bluelib.api.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class TellRawText {

	public enum Color {
		black, dark_blue, dark_green, dark_aqua,
		dark_red, dark_purple, gold, gray,
		dark_gray, blue, green, aqua, 
		red, light_purple, yellow, white;
	}
	
	public enum Style { bold, italic, underlined, strikethrough, obfuscated; }
	
	public enum ClickEvent { open_url, run_command, suggest_command; }
	
	private static final Line blankLine = new Line(
			new Element().setText(""));
	private static final Line separatorLine1 = new Line(
			new Element().setText("=====================================================").setColor(Color.black));
	private static final Line separatorLine2 = new Line(
			new Element().setText("-----------------------------------------------------").setColor(Color.dark_gray));
	
	
	private final List<Line> lines = new ArrayList<>();
	
	public void clear() {
		lines.forEach(Line::clear);
		lines.clear();
	}
	
	public void addLine(Line textLine) {
		lines.add(textLine);
	}
	
	public void addBlankLine() {
		lines.add(blankLine);
	}
	
	public void addSeparator1() {
		lines.add(separatorLine1);
	}
	
	public void addSeparator2() {
		lines.add(separatorLine2);
	}
	
	public List<Line> getLines() {
		return lines;
	}
	
	public void send(BlueLibSender s, String... args) {
		getLines().forEach(l -> s.sendMessage("json: ".concat(l.getJson()), args));
	}
	
	public void send(List<BlueLibSender> ss, String... args) {
		ss.forEach(s -> getLines().forEach(l -> s.sendMessage("json: ".concat(l.getJson()), args)));
	}
	
	public Line createLine(Element... elements) {
		return new Line(elements);
	}
	
	public Line createLine(List<Element> elements) {
		return new Line(elements);
	}
	
	public Line createLine() {
		return new Line();
	}
	
	public Line createLine(String result) {
		return new Line(result);
	}
	
	public Element createElement() {
		return new Element();
	}
	
	public static class Line {
		private List<Element> elements = new ArrayList<>();
		private String result;
		
		public Line(Element... elements) {
			this.elements.addAll(Arrays.asList(elements));
		}
		
		public Line(List<Element> elements) {
			this.elements = elements;
		}
		
		public Line() {
			
		}
		
		public Line(String result) {
			this.result = result;
		}
		
		public Line addElement(Element element) {
			elements.add(element);
			return this;
		}
		
		public void clear() {
			elements.forEach(Element::clear);
		}
		
		public Line build() {
			result = toString().replace("&", "§");
			elements.clear();
			return this;
		}
		
		public String getJson() {
			return result;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[\"\"");
			for (Element e : elements) {
				sb.append(",").append(e.toString());
			}
			return sb.append("]").toString();
		}
	}
	
	public static class Element {
		private final HashMap<String, String> parts = new HashMap<>();
		
		public void clear() {
			parts.clear();
		}
		
		public Element setText(String text) {
			text = text.replace("\"", "\\\"");
			parts.put("\"text\"", "\"".concat(text).concat("\""));
			return this;
		}
		
		public Element setColor(Color color) {
			parts.put("\"color\"", "\"".concat(color.name()).concat("\""));
			return this;
		}
		
		public Element setStyle(Style style) {
			parts.put("\"".concat(style.name()).concat("\""), "\"true\"");
			return this;
		}
		
		public Element setClickEvent(ClickEvent clickEvent, String text) {
			parts.put("\"clickEvent\"", "{\"action\":\"".concat(clickEvent.name()).concat("\",\"value\":\"").concat(text).concat("\"}"));
			return this;
		}
		
		public Element setTip(String text) {
			text = text.replace("\"", "\\\"");
			String[] args = text.split("\\n");
			if (args.length > 1) text = Utils.rawArray(Arrays.asList(args));
			parts.put("\"hoverEvent\"", "{\"action\":\"show_text\",\"value\":\"".concat(text).concat("\"}"));
			return this;
		}
		
		public Element setTip(String... args) {
			setTip(String.join("\\n", args));
			return this;
		}
		
		public Element setTip(List<String> list) {
			String[] args = new String[list.size()];
			for (int i = 0; i < args.length; i++) args[i] = list.get(i);
			setTip(args);
			return this;
		}
		
		public Element setTip(ItemStack item, boolean useDisplayName) {
			// "action":"show_item","value":"{id:{name},Damage:{data},tag:{display:{Name:\"{displayName}\",Lore:{lore}}, ench:{enchantments}}}"
			String value = null;
			ItemMeta meta = item.getItemMeta();
			if (useDisplayName && meta.hasDisplayName()) {
				value = "{id:{id},Damage:{data},tag:{display:{Name:\\\"{displayName}\\\",Lore:{lore}},ench:{enchantments}}}";
			} else value = "{id:{id},Damage:{data},tag:{display:{Lore:{lore}},ench:{enchantments}}}";
			
			if (useDisplayName && meta.hasDisplayName()) value = value.replace("{displayName}", meta.getDisplayName());
			assert meta != null;
			value = value.replace("{id}", String.valueOf(item.getType())) // 1710 getTypeId
					.replace("{data}", String.valueOf(item.getDurability()))
					.replace("{lore}", Utils.rawLore(meta.getLore()))
					.replace("{enchantments}", Utils.rawEnchants(meta.getEnchants()));
			parts.put("\"hoverEvent\"", "{\"action\":\"show_item\",\"value\":\"".concat(value).concat("\"}"));
			return this;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			int size = parts.entrySet().size();
			for (Entry<String, String> part : parts.entrySet()) {
				sb.append(part.getKey()).append(":").append(part.getValue());
				if (size > 1) sb.append(",");
				size--;
			}
			return sb.append("}").toString();
		}
	}
}

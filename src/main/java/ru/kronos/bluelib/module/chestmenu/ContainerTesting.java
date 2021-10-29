package ru.kronos.bluelib.module.chestmenu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.api.engine.CooldownEngine;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.engine.RequestEngine;
import ru.kronos.bluelib.api.engine.WarmUpEngine;
import ru.kronos.bluelib.api.template.BlueLibCommand;
import ru.kronos.bluelib.api.template.BlueItemStack;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.module.chestmenu.item.Icon;
import ru.kronos.bluelib.module.chestmenu.item.action.ChestActionExec;
import ru.kronos.bluelib.module.chestmenu.item.action.ChestActionOpen;
import ru.kronos.bluelib.module.chestmenu.item.action.ChestActionRunnable;
import ru.kronos.bluelib.module.scoreboard.BlueScoreboard;
import ru.kronos.bluelib.module.scoreboard.Trigger;

import java.util.List;

public class ContainerTesting extends BlueLibCommand implements Listener {
	
	private ContainerTesting() {
		super(Main.inst, "MenuTesting");
		Bukkit.getPluginManager().registerEvents(this, Main.inst);
	}
	
	public static void make() {
		new ContainerTesting();
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (CooldownEngine.hasCooldown(Main.PLUGIN_ID, sender, "menuTesting")) {
			CooldownEngine.sendDefaultCooldownMessage(Main.PLUGIN_ID, sender, "menuTesting");
			return true;
		}
		
		RequestEngine.createRequest("menuTesting", sender, () -> {

			WarmUpEngine.startWarmUp(Main.PLUGIN_ID, sender.getName(), "menuTesting", () -> {
				BlueLibPlayer p = OnlineEngine.getPlayer(sender);
				Container c = new Container(16, "§f\uEF02");
				Container c2 = new Container(16, "Второе окно");

				Icon i = new Icon(new BlueItemStack(Material.STONE));
				i.putAction(ClickType.LEFT, new ChestActionExec(false, "gamemode creative {player}"));
				i.putAction(ClickType.RIGHT, new ChestActionExec(false, "gamemode survival {player}"));
				i.putAction(ClickType.MIDDLE, new ChestActionOpen(c2));

				Icon i2 = new Icon(new BlueItemStack(new ItemStack(Material.APPLE)));
				i2.putAction(ClickType.LEFT, new ChestActionRunnable(() -> {
					assert p != null;
					if (p.hasScoreboard()) {
						if (p.getScoreboard().hasObjective(DisplaySlot.SIDEBAR)) {
							p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).incScore(p.getName());
						}
					}

					i2.setType(Material.GOLDEN_APPLE);
					c.changeItem(1, i2);
				}));

				i2.putAction(ClickType.RIGHT, new ChestActionRunnable(() -> {
					assert p != null;
					if (p.hasScoreboard()) {
						if (p.getScoreboard().hasObjective(DisplaySlot.SIDEBAR)) {
							p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).decScore(p.getName());
						}
					}
					// TODO нужно добавить изменение предмета через часм ChestElement: добавить ссылку на контейнер при putElement().
					// i2.changeItem(item.setType(Material.APPLE)) - результат
					i2.setType(Material.APPLE);
					c.changeItem(1, i2);
				}));


				Icon i3 = new Icon(new BlueItemStack(new ItemStack(Material.FERMENTED_SPIDER_EYE)));
				i3.putAction(ClickType.LEFT, new ChestActionRunnable(() -> {
					assert p != null;
					p.clearObjective(DisplaySlot.SIDEBAR);
					i3.setType(Material.SPIDER_EYE);
					c.changeItem(2, i3);
				}));

				i3.putAction(ClickType.RIGHT, new ChestActionRunnable(() -> {
					BlueScoreboard sc = new BlueScoreboard();
					assert p != null;
					sc.registerNewObjective("TEST2", "ПРивет", DisplaySlot.SIDEBAR, Trigger.dummy).setScore(p.getName(), 0);
					p.setScoreboard(sc);
					i3.setType(Material.FERMENTED_SPIDER_EYE);
					c.changeItem(2, i3);

				}));


				c.putElement(1, i2).putElement(2, i3);
				c.setFullyFrozen(true);
				c.setFrozenDoubleBottomClick(true);
				c.setFrozenShiftBottomClick(true);
				assert p != null;
				c.openFor(p);

				CooldownEngine.setCooldown(Main.PLUGIN_ID, "menuTesting", sender, 7);

			}, 10, false, "10,7,3-1");

		}, 10);
		
		return true;
	}

	@Override
	public List<String> onTabComplete( CommandSender sender, Command cmd, String label, String[] args) {
		
		return null;
	}
	
	
}

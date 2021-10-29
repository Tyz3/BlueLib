package ru.kronos.bluelib.module.storage;

import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.api.template.config.SQLDatabase;

public final class StorageTesting {
	
	public StorageTesting() {
		try {
			SQLDatabase db1 = new SQLDatabase();
			SQLDatabase db2 = new SQLDatabase();
			
			db1.installConfig(new SQLDatabase.MySQLConfiguration(Main.inst.getDataFolder(), "Test.sqlite"));
			db2.installConfig(new SQLDatabase.MySQLConfiguration("croc2w.beget.tech", 3306, "croc2w_t", "croc2w_t", "yVr*7yCG"));
			
			if (db1.openConnection()) {
				db1.executeUpdate("CREATE TABLE IF NOT EXISTS test_table (`time` LONG, `name` TEXT, `action` TEXT, `info` TEXT);");
			}
			db2.openConnection();
			
			db1.closeConnection();
			db2.closeConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void make() {
		new StorageTesting();
	}
}

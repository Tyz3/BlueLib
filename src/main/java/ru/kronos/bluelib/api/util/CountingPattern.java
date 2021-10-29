package ru.kronos.bluelib.api.util;

import java.util.ArrayList;
import java.util.List;

public class CountingPattern {
	private List<Long> numbers = new ArrayList<>();
	private String pattern;
	
	@Override
	public String toString() {
		return pattern;
	}

	public CountingPattern(String pattern) {
		this.pattern = pattern;
		if (pattern == null || pattern.trim().isEmpty()) {
			return;
		}

		if (!pattern.contains(",")) {
			parseSQ(pattern);
			return;
		}

		String[] arr = pattern.split(",");
		for (String s : arr) {
			parseSQ(s);
		}
	}

	private void parseSQ(String sq) {
		try {
			if (!sq.contains("-")) {
				Long a = Long.parseLong(sq);
				numbers.add(a);
				return;
			}

			String[] arr = sq.split("\\-");
			if (arr.length != 2)
				throw new IllegalArgumentException(
						"Interval must contain 2 numbers\n" + "Pattern: " + pattern + "\n" + "Error at: " + sq);

			Long a = Long.parseLong(arr[0]);
			Long b = Long.parseLong(arr[1]);

			if (a == b) {
				numbers.add(a);
				return;
			}

			if (a > b) {
				for (long i = a.longValue(); i >= b.longValue(); i--) {
					numbers.add(i);
				}
				return;
			}

			for (long i = a.longValue(); i <= b.longValue(); i++) {
				numbers.add(i);
			}

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Must be an integer!\n" + "Pattern: " + pattern + "\n" + "Error at: " + sq, e);
		}

	}

	public boolean contains(Long i) {
		return numbers.contains(i);
	}
}

package ru.kronos.bluelib.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CompleteHelper {
	
	public static final List<String> filter(String[] args, String... strs) {
		String last = args[args.length - 1];
		List<String> r = new ArrayList<String>();
		for(String s : strs) {
			if(s.toLowerCase().startsWith(last.toLowerCase())) r.add(s);
		}
		return r;
	}
	
	public static final List<String> filter(String[] args, Collection<String> list) {
		String last = args[args.length - 1];
		List<String> r = new ArrayList<String>();
		for(String s : list) {
			if(s.toLowerCase().startsWith(last.toLowerCase())) r.add(s);
		}
		return r;
	}
	
	public static final List<String> filter(String[] args, Collection<String> list, String... strs) {
		List<String> r = new ArrayList<String>();
		r.addAll(filter(args, list));
		r.addAll(filter(args, strs));
		return r;
	}
}

package ru.kronos.bluelib.api.template;

import ru.kronos.bluelib.junk.TellRawText;

public abstract class ChatControlPanel {
		
	private TellRawText[] pages;
	private int currentPage = 0;
	private boolean inited = false;
	
	protected abstract void reloadPanel();
	
	protected void initialize(int pagesAmount) {
		pages = new TellRawText[pagesAmount];
		currentPage = 0;
		inited = true;
	}
	
	protected TellRawText getInstallPage(int number) {
		return inited ? pages[number] : null;
	}
	
	protected void addInstallPage(TellRawText installPage) {
		if (!inited) return;
		pages[currentPage] = installPage;
		currentPage++;
	}
}

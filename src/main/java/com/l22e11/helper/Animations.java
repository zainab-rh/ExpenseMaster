package com.l22e11.helper;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;

public class Animations {
	public static void animateScroll(ScrollPane scrollPane) {
		scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
			double deltaY = event.getDeltaY();
			double contentHeight = scrollPane.getContent().getBoundsInLocal().getHeight();
			double viewportHeight = scrollPane.getViewportBounds().getHeight();
			double scrollableHeight = contentHeight - viewportHeight;

			double newVvalue = scrollPane.getVvalue() - (deltaY / scrollableHeight);

			// Ensure the new value stays within [0, 1] range
			if (newVvalue > 1) newVvalue = 1;
			else if (newVvalue < 0 || Double.isNaN(newVvalue)) newVvalue = 0;
			
			scrollPane.setVvalue(newVvalue);
			event.consume();
		});
    }
}

package org.vaadin.alump.fancylayouts.gwt.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.ui.Widget;

/**
 * Adds optinal automatic removal timer to FancyCssLayout
 */
public class GwtFancyTimedCssLayout extends GwtFancyCssLayout {
	
	protected int removeTimeMs = 0;
	
	public GwtFancyTimedCssLayout() {
		super();
	}
	
	/**
	 * Set automatic fancyRemove handling timeout
	 * @param millisecs Timeout in millisecs, if 0 automatic fancyremoval isn't
	 * used.
	 */
	public void setAutomaticRemoveTimeout(int millisecs) {
		if (millisecs < 0) {
			throw new IllegalArgumentException("invalid time");
		}
		removeTimeMs = millisecs;
	}
	
	@Override
	public void add(Widget widget, int index) {
		super.add(widget, index);

	    if (removeTimeMs > 0) {
	         final Widget removeWidget = widget;
	        
	         Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
	        	 public boolean execute() {
	        		 GwtFancyTimedCssLayout.this.fancyRemove(removeWidget);
	        		 return false;
	        	 }

	         }, removeTimeMs);
	    }
	}
}

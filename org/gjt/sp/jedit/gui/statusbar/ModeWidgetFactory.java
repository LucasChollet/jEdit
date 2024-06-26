/*
 * ModeWidgetFactory.java - The mode widget service
 * :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2008 Matthieu Casanova
 * Portions Copyright (C) 2001, 2004 Slava Pestov
 * Portions copyright (C) 2001 Mike Dillon
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.gjt.sp.jedit.gui.statusbar;

//{{{ Imports
import java.awt.*;
import java.awt.event.MouseEvent;

import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.gui.BufferOptions;
import org.gjt.sp.jedit.gui.DialogChooser;

import javax.swing.*;

import static org.gjt.sp.util.StandardUtilities.castUnchecked;
//}}}

/**
 * @author Matthieu Casanova
 * @since jEdit 4.3pre14
 */
public class ModeWidgetFactory implements StatusWidgetFactory
{
	//{{{ getWidget() method
	@Override
	public Widget getWidget(View view)
	{
		return new ModeWidget(view);
	} //}}}

	//{{{ ModeWidget class
	private static class ModeWidget extends AbstractLabelWidget
	{
		ModeWidget(View view)
		{
			super(view);
			label.setToolTipText(jEdit.getProperty("view.status.mode-tooltip"));
		}

		@Override
		protected void singleClick(MouseEvent e)
		{
			Mode[] modes = jEdit.getModes();
			Buffer buffer = view.getBuffer();
			Mode currentMode = buffer.getMode();
			DialogChooser.openListChooserWindow(label,
				currentMode,
				listSelectionEvent -> EventQueue.invokeLater(() ->
				{
					JList<Mode> list = castUnchecked(listSelectionEvent.getSource());
					buffer.setMode(list.getSelectedValue());
				}),
				modes);
		}

		@Override
		protected void rightClick(MouseEvent e)
		{
			new BufferOptions(view, view.getBuffer());
		}

		@Override
		public void update()
		{
			Buffer buffer = view.getBuffer();
			if (buffer.isLoaded())
				label.setText(buffer.getMode().toString());
		}

		@Override
		public boolean test(StatusBarEventType statusBarEventType)
		{
			return statusBarEventType == StatusBarEventType.Buffer;
		}
	} //}}}
}

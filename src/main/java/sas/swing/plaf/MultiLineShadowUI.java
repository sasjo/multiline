/*
 * The MIT License
 *
 * Copyright (c) 2009 Samuel Sjoberg
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package sas.swing.plaf;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.LabelUI;

/**
 * <code>MultiLineLabelUI</code> that paints the text with a drop shadow.
 * 
 * @see MultiLineLabelUI
 * @see Effects#paintTextShadow(Graphics2D, String)
 * 
 * @author Samuel Sjoberg, http://samuelsjoberg.com
 * @version 1.0.0
 */
public class MultiLineShadowUI extends MultiLineLabelUI {
	
    /** Shared UI instance. */
    public static LabelUI labelUI = new MultiLineShadowUI();

	/**
	 * Get the shared UI instance.
	 * 
	 * @param c
	 *            the component requesting a UI delegate
	 * @return the shared UI instance.
	 */
    public static ComponentUI createUI(JComponent c) {
    	return labelUI;
    }

    /**
     * Paint the text with a text effect.
     * 
     * @param g
     *            graphics component used to paint on
     * @param s
     *            the string to paint
     * @param textX
     *            the x coordinate
     * @param textY
     *            the y coordinate
     */
    private void paintText(Graphics g, String s, int textX, int textY) {
        g.translate(textX, textY);
        Effects.paintTextShadow((Graphics2D) g, s);
        g.translate(-textX, -textY);
    }

    /** {@inheritDoc} */
    protected void paintEnabledText(JLabel l, Graphics g, String s, int textX,
            int textY) {
        g.setColor(l.getForeground());
        paintText(g, s, textX, textY);
    }

    /** {inheritDoc} */
    protected void paintDisabledText(JLabel l, Graphics g, String s, int textX,
            int textY) {
        g.setColor(l.getBackground().darker());
        paintText(g, s, textX, textY);
    }
}

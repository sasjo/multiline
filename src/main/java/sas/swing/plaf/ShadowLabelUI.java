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

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.LabelUI;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 * Label UI delegate painting the text with a subtle drop shadow.
 * 
 * @author Samuel Sjoberg, http://samuelsjoberg.com
 * @version 1.0.0
 */
public class ShadowLabelUI extends BasicLabelUI {
    
    /** Static reference to the UI. */
    public static LabelUI labelUI = new ShadowLabelUI();
    
    /** {@inheritDoc} */
    protected void installDefaults(JLabel c) {
        super.installDefaults(c);
        if (c.getBorder() == null) {
            c.setBorder(new EmptyBorder(2, 2, 2, 2));
        }
    }
    
    /** {@inheritDoc} */
    protected void paintDisabledText(JLabel l, Graphics g, String s, int textX,
            int textY) {
        g.setColor(l.getBackground().darker());
        g.translate(textX, textY);
        Effects.paintTextShadow((Graphics2D) g, s);
        g.translate(-textX, -textY);
    }

    /** {@inheritDoc} */
    protected void paintEnabledText(JLabel l, Graphics g, String s, int textX,
            int textY) {
        g.setColor(l.getForeground());
        g.translate(textX, textY);
        Effects.paintTextShadow((Graphics2D) g, s);
        g.translate(-textX, -textY);
    }
}

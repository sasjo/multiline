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
package sas.swing;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import sas.swing.plaf.Effects.GradientPainter;

/**
 * Opaque component with a gradient background.
 * 
 * @author Samuel Sjoberg, http://samuelsjoberg.com
 * @version 1.0.0
 */
public class GradientPanel extends JPanel {

    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The gradient helper. */
    private GradientPainter painter;

    /** Flag used to keep track of times when the component is repainting. */
    private boolean isPainting;

    /**
     * Create a new gradient background panel.
     * 
     * @param c1
     *            start color
     * @param c2
     *            stop color
     */
    public GradientPanel(Color c1, Color c2) {
        // Note: Non-cyclic gradient would be better but doesn't work with
        // JList since it will repaint in strange ways when it cycles.
        painter = new GradientPainter(this, c1, c2);
    }

    /** {@inheritDoc} */
    public boolean isOpaque() {
        if (isPainting) {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    protected void paintComponent(Graphics g) {
        isPainting = true;
        painter.paint(g, this);
        super.paintComponent(g);
        isPainting = false;
    }
}

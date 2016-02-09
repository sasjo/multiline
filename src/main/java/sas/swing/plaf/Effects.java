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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;

/**
 * Static graphics and look and feel effects.
 * 
 * @author Samuel Sjoberg, http://samuelsjoberg.com
 * @version 1.3.0
 */
public final class Effects {

    /** Prevent initialization. */
    private Effects() {
    }

    /** Text drop shadow size. */
    public static final int TEXT_SHADOW_SIZE = 2;

    /**
     * Set the color to black with given alpha value.
     * 
     * @param g
     *            the graphics to paint on
     * @param alpha
     *            the alpha value between 0-1.
     */
    public static void setAlpha(Graphics g, float alpha) {
        g.setColor(new Color(0, 0, 0, Math.round(255 * alpha)));
    }

    /**
     * Remove the alpha channel from the passed color.
     * 
     * @param c
     *            a color
     * @return the same color, but without an alpha channel
     */
    public static Color removeAlpha(Color c) {
        if (c.getAlpha() != 100) {
            c = new Color(c.getRGB());
        }
        return c;
    }

    /**
     * Draw a string with a blur or shadow effect. The light angle is assumed to
     * be 0 degrees, (i.e., window is illuminated from top). The effect is
     * intended to be subtle to be usable in as many text components as
     * possible. The effect is generated with multiple calls to draw string.
     * This method paints the text on coordinates <code>tx</code>,
     * <code>ty</code>. If text should be painted elsewhere, a transform should
     * be applied to the graphics before passing it.
     * <p>
     * All modifications to the graphics object is restored by this method
     * before returning.
     * <p>
     * Based on code by Romain Guy, {@linkplain http://filthyrichclients.org/},
     * see examples from chapter 16.
     * 
     * @param g
     *            graphics component to paint on
     * @param s
     *            the string to paint
     * @param c
     *            effect color
     * @param size
     *            effect size
     * @param tx
     *            x-coordinate translation (i.e, pixels to move the center of
     *            the x-axis)
     * @param ty
     *            y-coordinate translation (i.e, pixels to move the center of
     *            the y-axis)
     * @param isShadow
     *            <code>true</code> if this is a shadow being painted that
     *            should be slightly offset to look more like a shadow being
     *            casted, otherwise <code>false</code>.
     */
    private static void paintTextEffect(Graphics2D g, String s, Color c,
            int size, double tx, double ty, boolean isShadow) {

        prepareGraphics(g);

        final float opacity = 0.8f; // Effect "darkness".
        final Composite oldComposite = g.getComposite();
        final Color oldColor = g.getColor();

        // Use a alpha blend smaller than 1 to prevent the effect from becoming
        // too dark when multiple paints occur on top of each other.
        float preAlpha = 0.4f;
        if (oldComposite instanceof AlphaComposite
                && ((AlphaComposite) oldComposite).getRule() == AlphaComposite.SRC_OVER) {
            preAlpha = Math.min(((AlphaComposite) oldComposite).getAlpha(),
                    preAlpha);
        }
        g.setColor(c);

        g.translate(tx, ty);

        // If the effect is a shadow it looks better to stop painting a bit to
        // early... (shadow will look softer).
        int maxSize = isShadow ? size - 1 : size;

        for (int i = -size; i <= maxSize; i++) {
            for (int j = -size; j <= maxSize; j++) {
                double distance = i * i + j * j;
                float alpha = opacity;
                if (distance > 0.0d) {
                    alpha = (float) (1.0f / ((distance * size) * opacity));
                }
                alpha *= preAlpha;
                if (alpha > 1.0f) {
                    alpha = 1.0f;
                }
                g.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, alpha));
                g.drawString(s, i + size, j + size);
            }
        }

        // Restore graphics
        g.translate(-tx, -ty);
        g.setComposite(oldComposite);
        g.setColor(oldColor);

        g.drawString(s, 0, 0);
    }

    private static void prepareGraphics(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    /**
     * Draw a string with a drop shadow. The light angle is assumed to be 0
     * degrees, (i.e., window is illuminated from top) and the shadow size is 2,
     * with a 1 pixel vertical displacement. The shadow is intended to be subtle
     * to be usable in as many text components as possible. The shadow is
     * generated with multiple calls to draw string. This method paints the text
     * on coordinates 0, 1. If text should be painted elsewhere, a transform
     * should be applied to the graphics before passing it.
     * <p>
     * All modifications to the graphics object is restored by this method
     * before returning.
     * 
     * @see #paintTextEffect(Graphics2D, String, Color, int, boolean)
     * 
     * @param g
     *            graphics component to paint on
     * @param s
     *            the string to paint
     * @param c
     *            the color of the shadow. Any alpha channel will be discarded
     */
    public static void paintTextShadow(Graphics2D g, String s, Color c) {
        paintTextEffect(g, s, removeAlpha(c), TEXT_SHADOW_SIZE,
                -TEXT_SHADOW_SIZE, 1 - TEXT_SHADOW_SIZE, true);
    }

    /**
     * Draw a string with a drop shadow. The light angle is assumed to be 0
     * degrees, (i.e., window is illuminated from top) and the shadow size is 2,
     * with a 1 pixel vertical displacement. The shadow is intended to be subtle
     * to be usable in as many text components as possible. The shadow is
     * generated with multiple calls to draw string. This method paints the text
     * on coordinates 0, 1. If text should be painted elsewhere, a transform
     * should be applied to the graphics before passing it.
     * <p>
     * All modifications to the graphics object is restored by this method
     * before returning.
     * 
     * @see #paintTextEffect(Graphics2D, String, Color, int, boolean)
     * 
     * @param g
     *            graphics component to paint on
     * @param s
     *            the string to paint
     */
    public static void paintTextShadow(Graphics2D g, String s) {
        paintTextShadow(g, s, Color.BLACK);
    }

    /**
     * Draw a string with a glow effect. Glow differs from a drop shadow in that
     * it isn't offset in any direction (i.e., not affected by
     * "lighting conditions").
     * <p>
     * All modifications to the graphics object is restored by this method
     * before returning.
     * 
     * @param g
     *            graphics component to paint on
     * @param s
     *            the string to draw
     * @param glow
     *            the solid glow color. Do not use the alpha channel as this
     *            will be discarded
     */
    public static void paintTextGlow(Graphics2D g, String s, Color glow) {
        paintTextEffect(g, s, removeAlpha(glow), TEXT_SHADOW_SIZE,
                -TEXT_SHADOW_SIZE, -TEXT_SHADOW_SIZE, false);
    }

    /**
     * Utility to assist with painting of a vertical gradient. The opaque
     * property of the component being painted is not honored. If
     * {@link #paint(Graphics, JComponent)} is invoked a gradient background is
     * always painted.
     * <p>
     * <strong>A note on cyclic gradients:</strong><br>
     * Cyclic gradients yields better performance, however they cannot be safely
     * used to paint backgrounds on components that perform partial repaints
     * (e.g., <code>JList</code>). Cyclic gradients can be turned on using
     * {@link #setCyclic(boolean)} but are not used by default in this painter.
     * 
     * @author Samuel Sjoberg, http://samuelsjoberg.com
     */
    public static class GradientPainter {

        /** Start color. */
        private Color c1;

        /** End color. */
        private Color c2;

        /** Cached gradient paint. */
        private GradientPaint paint;

        /** Cyclic gradient. */
        private boolean cyclic = false;

        /**
         * Create a new <code>GradientPainter</code> using the background color
         * of the passed component. The gradient will fade from the background
         * color in to a darker shade of the same color.
         * 
         * @param c
         *            the component owning the painter
         */
        public GradientPainter(JComponent c) {
            this(c, c.getBackground(), c.getBackground().darker().darker());
        }

        /**
         * Create a new <code>GradientPainter</code> using the passed colors.
         * The gradient will fade from <code>c1</code> to <code>c2</code>.
         * 
         * @param c
         *            the component owning the painter
         * @param c1
         *            the start color
         * @param c2
         *            the end color
         */
        public GradientPainter(JComponent c, Color c1, Color c2) {
            this.c1 = c1;
            this.c2 = c2;
            c.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    paint = null;
                }
            });
        }

        /**
         * Set the gradient colors. For changes to take effect, the component
         * must be repainted by invoking e.g. {@link JComponent#repaint()}.
         * 
         * @param c1
         *            the start color
         * @param c2
         *            the end color
         */
        public void setColors(Color c1, Color c2) {
            this.c1 = c1;
            this.c2 = c2;
            paint = null;
        }

        /**
         * Use cyclic gradients. Default behavior is non-cyclic gradients.
         * 
         * @param cyclic
         *            If <code>true</code>, cyclic gradients a used, otherwise
         *            non-cyclic gradients are used.
         */
        public void setCyclic(boolean cyclic) {
            this.cyclic = cyclic;
            paint = null;
        }

        /**
         * Fill the component with a gradient background. This method does not
         * honor the opaque property.
         * 
         * @param g
         *            graphics to paint upon
         * @param c
         *            the component being painted
         */
        public void paint(Graphics g, JComponent c) {
            if (paint == null) {
                paint = new GradientPaint(0, 0, c1, 0, c.getHeight(), c2,
                        cyclic);
            }
            ((Graphics2D) g).setPaint(paint);
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }
    }
}

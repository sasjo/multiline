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
package sas.swing.samples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.LabelUI;

import sas.swing.GradientPanel;
import sas.swing.MultiLineLabel;
import sas.swing.plaf.MultiLineLabelUI;
import sas.swing.plaf.MultiLineShadowUI;

/**
 * Sample demonstration some of the capabilities of the {@link MultiLineLabel}.
 * 
 * @author Samuel Sjoberg, http://samuelsjoberg.com
 * @version 1.0.0
 */
public class MultiLineSample implements Runnable {

    /**
     * Create and show the GUI components.
     */
    public void run() {
        JFrame frame = new JFrame("Multiline JLabels");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use a GradientPanel as ContentPane.
        Container contentPane = new GradientPanel(Color.GRAY, Color.DARK_GRAY);
        contentPane.setLayout(new BorderLayout());

        // Shows how to use a regular JLabel as a multi-line label.
        final JLabel title = new JLabel("Multiline label's sample");
        title.setUI(MultiLineLabelUI.labelUI);
        title.setFont(title.getFont().deriveFont(22f));
        title.setForeground(Color.WHITE);
        contentPane.add(title, BorderLayout.NORTH);

        // Using the MultiLineLabel class.
        final MultiLineLabel mLabel = new MultiLineLabel(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                        + "Phasellus non sapien quam. Fusce posuere, nisl "
                        + "vitae tristique volutpat, augue erat faucibus nisl, "
                        + "nec venenatis metus sem vel enim. Cras in libero "
                        + "sapien, vitae euismod neque. Proin hendrerit, odio "
                        + "et faucibus suscipit, eros tellus blandit justo, "
                        + "ac cursus risus elit ut risus.");
        mLabel.setForeground(Color.WHITE);
        contentPane.add(mLabel, BorderLayout.CENTER);

        JCheckBox effectBox = new JCheckBox(new AbstractAction("Drop shadow") {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                boolean selected = ((JCheckBox) e.getSource()).isSelected();
                LabelUI ui = selected ? MultiLineShadowUI.labelUI
                        : MultiLineLabelUI.labelUI;
                installUI(ui, mLabel, title);
            }

            private void installUI(LabelUI ui, JLabel... labels) {
                for (JLabel label : labels) {
                    // Switching the UI resets the font, so we'll store it to
                    // reinstall it after the UI is changed.
                    Font font = label.getFont();
                    label.setUI(ui);
                    label.setFont(font);
                }
            }
        });

        JCheckBox alignBox = new JCheckBox(new AbstractAction("Alignment") {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                if (((JCheckBox) e.getSource()).isSelected()) {
                    mLabel.setHorizontalTextAlignment(JLabel.CENTER);
                    mLabel.setVerticalTextAlignment(JLabel.BOTTOM);
                } else {
                    mLabel.setHorizontalTextAlignment(JLabel.LEFT);
                    mLabel.setVerticalTextAlignment(JLabel.CENTER);
                }
                mLabel.repaint();
            }

        });

        JPanel boxes = new JPanel(new FlowLayout());
        boxes.setOpaque(false);
        boxes.add(effectBox);
        boxes.add(alignBox);

        contentPane.add(boxes, BorderLayout.SOUTH);

        frame.setContentPane(contentPane);
        frame.setSize(240, 320);
        frame.setVisible(true);
    }

    /**
     * Program entry point.
     * 
     * @param args
     *            not used
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MultiLineSample());
    }
}

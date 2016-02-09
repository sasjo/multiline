# Multiline labels in Swing with drop shadow

This project was created 19th October 2009. Below is the original accompanying blog post. 

Originally published at http://samuelsjoberg.com/archive/2009/10/multiline-labels-in-swing (dead link).

The code is released under the MIT-license, so you're basically free to use it in any ways you find useful.

# How to compile and run example

![Sample application](https://github.com/sasjo/multiline/blob/master/screenshot.png)

Pull the repository and compile it:

```bash
mvn package
```

To run the example application, double click the jar in the target folder or run:

```bash
java -jar target/multiline-1.0-SNAPSHOT.jar
```

------

Continuing the Swing trail, I'm presenting my solution for multiline text labels in Swing. I wrote this during this spring and had simply forgotten about it.

The problem with `JLabel` is the way in which it supports multiple lines (with wrapping) and centered text. You need to use HTML to activate this functionality. Enabling HTML is pretty simple:

```java
JLabel label = new JLabel("<html>Text that'll wrap if necessary");
```

To center the text, you'll add the &lt;center&gt; tag to your string. Not pretty, but it gets the job done.

This approach has two drawbacks. First, it will bloat your code or resource strings since you'll need to add the HTML tags somewhere. Second, you'll loose control of how text is being drawn once HTML mode is set. When you use HTML in a Swing component, it will be rendered as a View (see [`javax.swing.plaf.basic.BasicHTML`](http://www.j2ee.me/javase/6/docs/api/javax/swing/plaf/basic/BasicHTML.html) for implementation details).

## It's all about text effects

The motivation behind implementing multiline support for labels is:

* Remove the need for using HTML to support multiple lines and alignment.
* Improve support for horizontal and vertical text alignment.
* Support for custom text effects (such as drop shadow).

![Text effect in OS X](https://github.com/sasjo/multiline/blob/master/dropshadow.png)

To be honest, my main motivation was that I needed drop shadow on the text in labels, like on the desktop in OS X and Windows. I tried two solutions, the obvious one with a offscreen image that you apply a gaussian filter on and one that simply painted the text in various offset positions and alpha blends until it looked kind of like a drop shadow.

I'd read about this second approach in the book [Filthy Rich Clients](http://filthyrichclients.org/) where an example produces a text glow effect in this way. After playing around with this approach I decided to go for it. The results looked better than with a gaussian blur and it felt like a bad idea to have an offscreen image for each drop shadow label in the GUI.

I first implemented the solution as an UI delegate that painted my custom text effect. Happy to see it work, I quickly realized I wanted this effect in combination with multiline support.

## Introducing the general purpose `MultiLineLabelUI`

A couple (okay, many) hours later, I'd written a general purpose `MultiLineLabelUI` that enabled any `JLabel` to support multiline text with line wrapping and preserving hard line breaks. On top of that, I'd made the `MultiLineShadowUI`, enabling text drop shadow in any JLabel.

To enable support for multiple lines, simply do this:

```java
JLabel label = new JLabel("Text that'll wrap if necessary");
label.setUI(MultiLineLabelUI.labelUI);
```

To use the drop shadow, instead to this:

```java
label.setUI(MultiLineShadowUI.labelUI);
```

### Details, details, details...

The multiline labels will only wrap the text when needed. It can be useful to know that the preferred height reported by the UI delegate will be the height required to render lines without any additional wrapping (i.e., hard wraps, inserted by the user, is accounted for). It is not until the label is painted, and the actual width budget is known, that the line wrapping can be correctly computed. The wrapped lines are stored as a client property on the label to avoid unnecessary calculations.

The label listens to dimension changes and recalculate line breaks as the component is resized. Resizing the window on OS X will for example rearrange the line wraps (note: this will not work on windows since component bounds don't seem to be updated correctly when the window is resized).

## Improving alignment in the `JLabel`

To enable better support for text alignment in the JLabel I've created the `MultiLineLabel` class. It's using the `MultiLineLabelUI` by default and adds the properties `verticalTextAlignment` and `horizontalTextAlignment`.

This makes it possible (and really easy) to have a multiline label that's for example centered at the bottom of the component.

```java
MultiLineLabel label = new MultiLineLabel("Text that'll wrap if necessary");
label.setHorizontalTextAlignment(JLabel.CENTER);
label.setVerticalTextAlignment(JLabel.BOTTOM);
```

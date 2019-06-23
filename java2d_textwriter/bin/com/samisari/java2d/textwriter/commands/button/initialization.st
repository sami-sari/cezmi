initialization(button, indent, name, width, height) ::= <<
<indent><name> = new JButton();
<indent><name>.setPreferredSize(new Dimension(<width>, <height>));
<indent><name>.setText(\"<button.text>\");

>>

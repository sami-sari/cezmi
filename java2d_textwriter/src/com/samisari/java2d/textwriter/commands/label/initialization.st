initialization(label, indent, name) ::= <<
<indent><name> = new JLabel("<label.text>");
<indent><name>.setPreferredSize(new Dimension(<label.bounds.width>, <label.bounds.height>));

>>

actionListener(indent, name, button) ::= <<
<indent><name>.addActionListener( new ActionListener() {
<indent>	public void actionPerformed(ActionEvent e) {
<indent>		<button.actionHandler>
<indent>	}
<indent>});

>>
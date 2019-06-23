package com.samisari.gui.panel;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.CommandSelectionChangeListener;
import com.samisari.cezmi.core.HistoryListener;
import com.samisari.gui.dockablepanel.DockablePanel;
import com.samisari.gui.tree.HistoryTree;

public class HistoryPanel extends DockablePanel implements CommandSelectionChangeListener, HistoryListener {
	private static final long	serialVersionUID	= 6990001543652276902L;
	private Logger				logger				= Logger.getLogger(HistoryPanel.class);
	private HistoryTree			tree;
	private JScrollPane			sc1;

	public HistoryPanel(JSplitPane splitPane, DockablePanel.Position position) {
		super(splitPane, position);
		setLayout(new GridLayout());
		//
		// table = new HistoryTable();
		// sc1 = new JScrollPane(table);
		tree = new HistoryTree();
		sc1 = new JScrollPane(tree);
		add(sc1);
		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				Object userObject = selectedNode.getUserObject();
				if (userObject instanceof AbstractCommand) {
					logger.debug("History Panel tree selection change listener fireing");
					CommandManager.getDeaultInstance().fireCommandSelectionChanged((AbstractCommand) userObject);
				}

			}
		});
		CommandManager.getDeaultInstance().addSelectionChangeListener(this);
		CommandManager.getDeaultInstance().getHistory().addListener(this);
	}

	@Override
	public void handleCommandSelectionChange(AbstractCommand cmd) {
		if (cmd.isSelected())
			tree.setSelectedCommand(cmd);
		else {
			List<AbstractCommand> commandList = CommandManager.getDeaultInstance().getSelectedCommands();
			if (commandList.size() > 0) {
				tree.setSelectedCommand(commandList.get(commandList.size() - 1));
			}
		}
	}

	@Override
	public void onRemoved(Object o) {
		this.tree.refresh();
	}

	@Override
	public void onInserted(Object o) {
		this.tree.refresh();
	}

	@Override
	public void onModelChanged() {
		this.tree.refresh();
	}
	

}

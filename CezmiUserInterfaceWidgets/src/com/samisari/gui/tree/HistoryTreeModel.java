package com.samisari.gui.tree;

import java.lang.reflect.Method;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.History;

public class HistoryTreeModel implements TreeModel {
	DefaultMutableTreeNode root;

	public HistoryTreeModel() {
		root = new DefaultMutableTreeNode();
		root.setUserObject("Bileşenler");
		History history = CommandManager.getDeaultInstance().getHistory();
		buildSubTree(root, history);
	}

	private void buildSubTree(DefaultMutableTreeNode parent, History history) {
		for (AbstractCommand c : history) {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode();
			root.setUserObject(c);
			parent.add(root);
			History h = getSubComponents(c);
			if (h != null) {
				buildSubTree(root, h);
			}
		}
	}

	private History getSubComponents(AbstractCommand c) {
		History history = null;
		try {
			Method getComponentsMethod = c.getClass().getMethod("getComponents");
			history = (History) getComponentsMethod.invoke(c);
		} catch (Exception e) {
		}

		return history;
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public Object getChild(Object parent, int index) {
		return ((DefaultMutableTreeNode) parent).getChildAt(index);
	}

	@Override
	public int getChildCount(Object parent) {
		return ((DefaultMutableTreeNode) parent).getChildCount();
	}

	@Override
	public boolean isLeaf(Object node) {
		return ((DefaultMutableTreeNode) node).isLeaf();
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		return;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		return ((DefaultMutableTreeNode) parent).getIndex((DefaultMutableTreeNode) child);
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub

	}
}

package com.samisari.cezmi.networkcommands;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import com.samisari.cezmi.component.CmdConnectableRectangle;
import com.samisari.cezmi.core.ContextMenu;
import com.samisari.cezmi.core.ContextMenuAction;
import com.samisari.cezmi.core.ContextMenuItem;
import com.samisari.cezmi.networkcommands.actions.CmdServerActions;

public class CmdServer extends CmdConnectableRectangle {
	private static final long serialVersionUID = -6069726639180334594L;
	private static final int MARGIN_LEFT = 10;
	private static final int MARGIN_TOP = 15;
	private String hostName;
	private String description;
	private String ipAddress;
	private String osType;
	private String sshUser;
	private String sshPassword;
	private String rootPassword;
	private String services;
	private String applications;
	
	

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	private Stroke buildStroke() {
		Stroke stroke = new BasicStroke();
		return stroke;
	}
	
	@Override 
	public void paint(Graphics g) {
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.GRAY);
		g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		g2d.setStroke(buildStroke());
		g2d.fillRect(getX(), getY(), getWidth(), 20);
		g2d.setColor(Color.WHITE);
		
		g2d.drawString(getName() + " " + getHostName(), getX()+ MARGIN_LEFT, getY()+MARGIN_TOP);
		g2d.setColor(Color.GRAY);
		super.paint(g);
		g.setColor(oldColor);
		g.setFont(oldFont);
	}
	
	@Override
	public void openContextMenu() {
		ContextMenu menu = new ContextMenu(this);
		menu.pack();
		menu.setVisible(true);
	}

	@Override
	public List<ContextMenuItem> getContextMenuItems() {
		List<ContextMenuItem> list = new ArrayList<ContextMenuItem>();
		ContextMenuItem cmi = new ContextMenuItem();
		ContextMenuAction action=new ContextMenuAction(){
			@Override
			public void run() {
				new CmdServerActions(CmdServer.this).sshConsoleConnect();
			}
		};
		cmi.setAction(action);
		cmi.setText("SSH Connect");
		list.add(cmi);
		cmi = new ContextMenuItem();
		action=new ContextMenuAction(){
			@Override
			public void run() {
				new CmdServerActions(CmdServer.this).showWizard();
			}
		};
		cmi.setAction(action);
		cmi.setText("Server Wizard");
		list.add(cmi);
		
		return list;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getSshUser() {
		return sshUser;
	}

	public void setSshUser(String sshUser) {
		this.sshUser = sshUser;
	}

	public String getSshPassword() {
		return sshPassword;
	}

	public void setSshPassword(String sshPassword) {
		this.sshPassword = sshPassword;
	}

	public String getRootPassword() {
		return rootPassword;
	}

	public void setRootPassword(String rootPassword) {
		this.rootPassword = rootPassword;
	}

	public String getServices() {
		return services;
	}

	public void setServices(String services) {
		this.services = services;
	}

	public String getApplications() {
		return applications;
	}

	public void setApplications(String applications) {
		this.applications = applications;
	}
}
package com.samisari.cezmi.networkcommands.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

import com.jcraft.jcterm.Connection;
import com.jcraft.jcterm.Term;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import com.samisari.cezmi.networkcommands.CmdServer;
import com.samisari.cezmi.networkcommands.wizards.CmdServerWizard;

public class CmdServerActions {
	CmdServer server;
	private Term con;

	public CmdServerActions(CmdServer server) {
		setServer(server);
	}

	public void showWizard() {
		CmdServerWizard wiz = new CmdServerWizard(server);
		wiz.display();
	}

	public CmdServer getServer() {
		return server;
	}

	public void setServer(CmdServer server) {
		this.server = server;
	}

	public void sshConsoleConnect() {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				ConsoleDialog dialog = new ConsoleDialog(server);
				con = dialog.getConsole();

				JSch sch = new JSch();
				try {
					// Session session = sch.getSession("root",
					// "www.uygunecza.com",22);
					// session.setPassword("zb_u*fln");
					Session session = sch.getSession(server.getSshUser(), server.getHostName(), 22);
					session.setPassword(server.getSshPassword());
					UserInfo ui = new MyUserInfo() {
						public void showMessage(String message) {
							JOptionPane.showMessageDialog(null, message);
						}

						public boolean promptYesNo(String message) {
							Object[] options = { "yes", "no" };
							int foo = JOptionPane.showOptionDialog(null, message, "Warning", JOptionPane.DEFAULT_OPTION,
									JOptionPane.WARNING_MESSAGE, null, options, options[0]);
							return foo == 0;
						}
					};

					session.setUserInfo(ui);
					session.connect(10000);
					Channel channel = session.openChannel("shell");
					channel = session.openChannel("shell");
					channel.connect();

					final OutputStream out = channel.getOutputStream();
					final InputStream in = channel.getInputStream();
					final OutputStream fout = out;
					final InputStream fin = in;
					final Channel fchannel = channel;

					Connection connection = new Connection() {
						public InputStream getInputStream() {
							return fin;
						}

						public OutputStream getOutputStream() {
							return fout;
						}

						public void requestResize(Term term) {
							if (fchannel instanceof ChannelShell) {
								int c = term.getColumnCount();
								int r = term.getRowCount();
								((ChannelShell) fchannel).setPtySize(c, r, c * term.getCharWidth(),
										r * term.getCharHeight());
							}
						}

						public void close() {
							fchannel.disconnect();
						}
					};
					dialog.setConnection(connection);
					// channel.setInputStream(new
					// TextAreaInputStream(con),false);

					// channel.setInputStream(new InputStream() {
					// private StringBuffer buffer;
					//
					// @Override
					// public int read() throws IOException {
					// int val = buffer.charAt(0);
					// buffer.delete(0, 1);
					// return val;
					// }
					// });
					// channel.setOutputStream(new OutputStream() {
					// private Buffer buffer;
					//
					// @Override
					// public void write(int arg0) throws IOException {
					// buffer.append(arg0);
					//
					// }
					// });
					con.start(connection);
					// channel.connect();
				} catch (JSchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};

		if (server.getHostName() != null && server.getSshUser() != null && server.getSshPassword() != null) {
			Thread thread = new Thread(task);
			thread.start();
		} else {
			JOptionPane.showMessageDialog(null, "Server definition incomplete");
		}

	}

	public static abstract class MyUserInfo implements UserInfo, UIKeyboardInteractive {
		public String getPassword() {
			return null;
		}

		public boolean promptYesNo(String str) {
			return false;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return false;
		}

		public boolean promptPassword(String message) {
			return false;
		}

		public void showMessage(String message) {
		}

		public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt,
				boolean[] echo) {
			return null;
		}
	}
}

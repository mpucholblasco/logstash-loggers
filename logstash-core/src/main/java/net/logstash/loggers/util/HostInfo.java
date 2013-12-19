package net.logstash.loggers.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Obtain information regarding current host.
 * 
 * @author mpucholblasco
 * 
 */
public class HostInfo {
	private String hostName = "localhost";

	public HostInfo() {
		setCurrentHostInfo();
	}

	/**
	 * Sets current host info. Such as:
	 * <ul>
	 * <li>Host name: tries to obtain from localhost info or keeps previous
	 * value ('localhost' by default).
	 * </ul>
	 */
	public void setCurrentHostInfo() {
		try {
			setHostName(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
		}
	}

	public void setHostName(final String hostName) {
		this.hostName = hostName;
	}

	public String getHostName() {
		return hostName;
	}
}

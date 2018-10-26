/********************************************************************************
This file is part of ShoX.

ShoX is free software; you can redistribute it and/or modify it under the terms
of the GNU General Public License as published by the Free Software Foundation;
either version 2 of the License, or (at your option) any later version.

ShoX is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with 
ShoX; if not, write to the Free Software Foundation, Inc., 51 Franklin Street,
Fifth Floor, Boston, MA 02110-1301, USA

Copyright 2006 The ShoX developers as defined under http://shox.sourceforge.net
********************************************************************************/

package br.ufla.dcc.grubix.simulator.event;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.node.Link;

/**
 * Super-class of all packets generated by the MACLayer. This is to ensure some
 * basic information to be present in all MAC packets regardless of the particular
 * implementation of the MAC layer, so lower layers can make use of it.
 * @author jlsx
 */
public class MACPacket extends Packet {

	public enum PacketType {
		/** this frame is of a undefined type. */
		VOID,
		/** this frame is a normal data packet. */
		DATA,
		/** this frame is an ACK. */
		ACK,
		/** this frame is a NACK. */
		NACK,
		/** this frame is a RTS. */
		RTS,
		/** this frame is a CTS. */
		CTS,  
		/** shall be used for all kind of special purpose control packets (like SYNC, BUSYTONE) */
		CONTROL, CTS_DATA
	}
	
	/** The signal strength with which this packet is to be transmitted in dBm. */
	//@NoHeaderData
	private double signalStrength = Double.NaN;
	
	/** The type of the MAC packet (e.g. ACK, NACK, etc.). */
	//@NoHeaderData
	private PacketType type = PacketType.VOID;

	/** the number of retries performed so far. */
	//@NoHeaderData
	protected int retryCount = 0;

	/** set this flag, if this packet will be answered with an ack packet. */
	//@NoHeaderData
	protected boolean ackRequested;
	
	/**
	 * method to initialize this object uppon creation.
	 * 
	 * @param ss the signalstrength to use.
	 */
	private void init(double ss) {
		this.signalStrength = ss;
		this.type = PacketType.VOID;
	}
	
	/**
	 * Default constructor of class Packet to create a terminal packet
	 * with no enclosed packet.
	 * @param sender Sender address of the packet
	 * @param receiver Id of the packet's receiver
	 * @param signalStrength The strength of the signal to transmit in mW
	 */
	public MACPacket(Address sender, NodeId receiver, double signalStrength) {
		super(sender, receiver);
		init(signalStrength);
	}

	/**
	 * Overloaded constructor to create non-terminal packets by
	 * specifying a packet to enclose.
	 * ReceiverID of the new packet taken from enclosedPacket.
	 * 
	 * @param sender Senderaddress of the packet
	 * @param packet The packet to enclose inside the new packet.
	 */
	public MACPacket(Address sender, LogLinkPacket packet) {
		super(sender, packet);
		// get link from meta data
		Link link = packet.getMetaInfos().getDownwardsLLCMetaInfo().getLink();
		init(link.getTransmissionPower());
		this.type = PacketType.DATA;
	}
	
	/** @return true if its a control packet. */
	public final boolean isControl() {
		return this.type != PacketType.DATA;
	}
	
	/**
	 * Sets the type of the MAC packet.
	 * @param type The MAC packet type
	 */
	public void setType(PacketType type) {
		this.type = type;
	}
	
	/**
	 * @return The MAC packet type.
	 */
	public PacketType getType() {
		return this.type;
	}

	/**
	 * @return the signalStrength
	 */
	public double getSignalStrength() {
		return signalStrength;
	}

	/**
	 * @param signalStrength the signalStrength to set
	 */
	public void setSignalStrength(double signalStrength) {
		this.signalStrength = signalStrength;
	}
	
	/** @return the retryCount */
	public final int getRetryCount() {
		return retryCount;
	}

	/** @param retryCount the retryCount to set */
	public final void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	/** @return the ackRequested. */
	public boolean isAckRequested() {
		return ackRequested;
	}

	/** @param ackRequested the ackRequested to set. */
	public final void setAckRequested(boolean ackRequested) {
		this.ackRequested = ackRequested;
	}
}

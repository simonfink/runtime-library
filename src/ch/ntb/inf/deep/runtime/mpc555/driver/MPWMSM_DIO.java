package ch.ntb.inf.deep.runtime.mpc555.driver;

import ch.ntb.inf.deep.runtime.mpc555.ntbMpc555HB;
import ch.ntb.inf.deep.unsafe.US;

/**
*
* Driver to use the MPWM module as digital I/O.<br>
* Please prefer the Mpiosm module for I/O applications.
* 
* @author 09.12.2009 simon.pertschy@ntb.ch
*/
public class MPWMSM_DIO implements ntbMpc555HB {

	/**
	 * Initialize an MPWM pin as digital I/O.
	 * @param channel select module channel 0-3 and 6-9 or 0-3 and 16-19
	 * @param out set I/O direction, <code>true</code>=>output, <code>false</code> => input.
	 */
	public static void init(int channel, boolean out){
		if(channel >=6  && channel <= 9) channel += 10;
		if(out) US.PUT2(MPWMSM0SCR + channel * 8, 0x4000);
		else US.PUT2(MPWMSM0SCR + channel * 8, 0x0);
	}
	
	
	/**
	 * Set the TTL-Signal value <code>val</code> to the corresponding channel.
	 * @param channel channel select module channel 0-3 and 6-9 or 0-3 and 16-19
	 * @param val the TTL-Signal value
	 */
	public static void out(int channel, boolean val){
		if(channel >=6  && channel <= 9) channel += 10;
		if(val) US.PUT2(MPWMSM0SCR + channel * 8, 0x4800);
		else US.PUT2(MPWMSM0SCR + channel * 8, 0x4000);
	}
	
	/**
	 * 
	 * Read the TTL-Signal of the corresponding channel.
	 * @param channel channel select module channel 0-3 and 6-9 or 0-3 and 16-19
	 * @return the TTL value of the corresponding channel.
	 */
	public static boolean in(int channel){
		if(channel >=6  && channel <= 9) channel += 10;
		return (US.GET2(MPWMSM0SCR + channel * 8) & 0x8000) != 0;
	}
}
/*
 * Copyright (c) 2011 NTB Interstate University of Applied Sciences of Technology Buchs.
 * All rights reserved.
 *
 * http://www.ntb.ch/inf
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the project's author nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package java.lang;

import ch.ntb.inf.deep.lowLevel.LL;

/*changes:
 30.06.11	NTB/Urs Graf	ported to deep
 04.09.09	NTB/MZ			ceil, floor, fix, class moved to java.lang
 27.10.06	NTB/ED			SYS.ADR(param)	==> SYS.ADR(locVar)
 25.08.06	NTB/ED			powOf10, sqrt
 14.08.04	NTB/ED			creation, powOf10, powIntExp
 */

public class Math {
//	private static final boolean _$bigEndian = true; // big-endian

	/**
	 * do not allow instances.
	 */
	private Math() {
	}

	/**
	 * The <code>double</code> value that is closer than any other to <i>e</i>,
	 * the base of the natural logarithms.
	 */
	public static final double E = 2.7182818284590452354;

	/**
	 * The <code>double</code> value that is closer than any other to <i>pi</i>,
	 * the ratio of the circumference of a circle to its diameter.
	 */
	public static final double PI = 3.14159265358979323846;

	private static final double twoPI = 2 * PI;

	private static final double pio2 = PI / 2, pio4 = PI / 4;

//	private static final int dNaN16MSBs = 0x7ff8;

//	private static double twoPow52 = 1L << 52; // 2^52

	private static final int dExpOffset = 0x3ff;

	/*
	 * private static final int dExpINF = 0x7ff; private static final int
	 * dMaxNofFractionDigits = 16; private static double dMinValueNormalized =
	 * Double.MIN_VALUE * (double)(1L << 52);
	 */

	/**
	 * Calculation of power with exponent being an integer value
	 * 
	 * @param base base
	 * @param exp exponent
	 * @return power base<sup>exp</sup>.
	 */
	public static double powIntExp(double base, int exp) {
		double p = 1.0;
		if (exp < 0) {
			base = 1.0 / base;
			exp = -exp;
		}
		while (exp != 0) {
			if ((exp & 1) != 0)
				p = p * base;
			exp = exp >>> 1;
			base = base * base;
		}
		return p;
	}

	/**
	 * Returns absolute value<br>
	 * 
	 * @param a
	 * @return absolute value
	 */
	public static int abs(int a) {
		if (a < 0)
			a = -a;
		return a;
	}

	/**
	 * Returns absolute value<br>
	 * 
	 * @param a
	 * @return absolute value
	 */
	public static double abs(double a) {
		if (a < 0)
			a = -a;
		return a;
	}

	/**
	 * Returns maximum value<br>
	 * 
	 * @param a
	 * @param b
	 * @return b if b > a, else a
	 */
	public static int max(int a, int b) {
		if (b > a)
			a = b;
		return a;
	}

	/**
	 * Returns maximum value<br>
	 * 
	 * @param a
	 * @param b
	 * @return b if b > a, else a
	 */
	public static double max(double a, double b) {
		if (b > a)
			a = b;
		return a;
	}

	/**
	 * Returns minimum value<br>
	 * 
	 * @param a
	 * @param b
	 * @return b if b < a, else a
	 */
	public static int min(int a, int b) {
		if (b < a)
			a = b;
		return a;
	}

	/**
	 * Returns minimum value<br>
	 * 
	 * @param a
	 * @param b
	 * @return b if b < a, else a
	 */
	public static double min(double a, double b) {
		if (b < a)
			a = b;
		return a;
	}

	/**
	 * Calculates square root.<br>
	 * 
	 * @param arg
	 * @return square root
	 */
	public static double sqrt(double arg) {
		double a = arg;
		long bits = LL.doubleToBits(a); 
		int e = (int)(bits >>> 52);
		if ((e & 0x800) == 0x800)
			return Double.NaN;
		if ((e & 0x7FF) == 0x7FF)
			return a;

		if (e < 0x0010)
			return 0.0; 

		int exp = e - dExpOffset; // exponent without offset
		int expDiv2 = exp >> 1;
		int expMod2 = exp & 1;
		exp = expMod2 + dExpOffset;
		bits = bits & 0xfffffffffffffL | ((long)(exp) << 52);
		a = LL.bitsToDouble(bits);

		double xa = (a + 1) / 2;
		double xn = (xa + a / xa) / 2;
		xa = (xn + a / xn) / 2; // 1
		xn = (xa + a / xa) / 2; // 2
		xa = (xn + a / xn) / 2; // 3
		xn = (xa + a / xa) / 2; // 4
		a = (xn + a / xn) / 2; // 5
		bits = LL.doubleToBits(a);
		bits += (long)expDiv2 << 52;
		a = LL.bitsToDouble(bits);
		return a;
	}

	private static final double // cos polynomial coefficients
			cosC2 = -0.4999999963,
			cosC8 = 0.0000247609, cosC4 = 0.0416666418,
			cosC10 = -0.0000002605,
			cosC6 = -0.0013888397;

	/**
	 * Returns the trigonometric cosine of an angle. Special cases:
	 * <ul>
	 * <li>If the argument is NaN or an infinity, then the result is NaN.
	 * </ul>
	 * 
	 * <p>
	 * The computed result must be within 1 ulp of the exact result. Results
	 * must be semi-monotonic.
	 * 
	 * @param arg an angle, in radians.
	 * @return the cosine of the argument.
	 */
	public static double cos(double arg) {
		if (Double.getExponent(arg) == (int)Double.INF_EXPONENT)
			return Double.NaN;

		if (arg < 0)
			arg = -arg;
		arg = arg % twoPI;

		int quadrant = (int) (arg / pio2);
		switch (quadrant) {
		case 1:
			arg = PI - arg;
			break;
		case 2:
			arg = arg - PI;
			break;
		case 3:
			arg = twoPI - arg;
			break;
		}

		double arg2 = arg * arg;
		double res = ((((cosC10 * arg2 + cosC8) * arg2 + cosC6) * arg2 + cosC4)
				* arg2 + cosC2)
				* arg2 + 1.0;
		if (((quadrant + 1) & 2) != 0)
			res = -res;
		return res;
	}

	private static final double // sine polynomial coefficients
			sinC2 = -0.1666666664,
			sinC8 = 0.0000027526, sinC4 = 0.0083333315,
			sinC10 = -0.0000000239,
			sinC6 = -0.0001984090;

	/**
	 * Returns the trigonometric sine of an angle. Special cases:
	 * <ul>
	 * <li>If the argument is NaN or an infinity, then the result is NaN.
	 * <li>If the argument is zero, then the result is a zero with the same sign
	 * as the argument.
	 * </ul>
	 * 
	 * <p>
	 * The computed result must be within 1 ulp of the exact result. Results
	 * must be semi-monotonic.
	 * 
	 * @param arg an angle, in radians.
	 * @return the sine of the argument.
	 */
	public static double sin(double arg) {
		if (Double.getExponent(arg) == (int)Double.INF_EXPONENT)
			return Double.NaN;

		arg = arg % twoPI;
		if (arg < 0)
			arg = arg + twoPI;

		int quadrant = (int) (arg / pio2);
		switch (quadrant) {
		case 1:
			arg = PI - arg;
			break;
		case 2:
			arg = arg - PI;
			break;
		case 3:
			arg = twoPI - arg;
			break;
		}
		if (arg == 0.0)
			return arg;

		double arg2 = arg * arg;
		double res = ((((sinC10 * arg2 + sinC8) * arg2 + sinC6) * arg2 + sinC4)
				* arg2 + sinC2)
				* arg2 * arg + arg;
		if (quadrant >= 2)
			res = -res;
		return res;
	}

	private static final double // tanInPio4 polynomial coefficients
			tanC2 = 0.3333314036,
			tanC8 = 0.0245650893, tanC4 = 0.1333923995,
			tanC10 = 0.0029005250,
			tanC6 = 0.0533740603, tanC12 = 0.0095168091;

	private static double tanInPio4(double arg) {
		double arg2 = arg * arg;
		double res = (((((tanC12 * arg2 + tanC10) * arg2 + tanC8) * arg2 + tanC6)
				* arg2 + tanC4)
				* arg2 + tanC2)
				* arg2 * arg + arg;
		return res;
	}

	private static final double // cotInPio4 polynomial coefficients
			cotC2 = -0.3333333410,
			cotC8 = -0.0002078504,
			cotC4 = -0.0222220287,
			cotC10 = -0.0000262619, cotC6 = -0.0021177168;

	private static double cotInPio4(double arg) {
//		final double maxCot = 1.633123935319537E16;

		if (arg < 1.0E-16)
			return 1.633123935319537E16;

		double arg2 = arg * arg;
		double res = ((((cotC10 * arg2 + cotC8) * arg2 + cotC6) * arg2 + cotC4)
				* arg2 + cotC2)
				* arg2 + 1.0;
		return res / arg;
	}

	private static final double tanMax = 1.633123935319537E16;
	private static final double tanMinArg = 1.0E-17;

	/**
	 * Returns the trigonometric tangent of an angle. Special cases:
	 * <ul>
	 * <li>If the argument is NaN or an infinity, then the result is NaN.
	 * <li>If the argument is zero, then the result is a zero with the same sign
	 *     as the argument.
	 * </ul>
	 * 
	 * <p>
	 * The computed result must be within 1 ulp of the exact result. Results
	 * must be semi-monotonic.
	 * 
	 * @param arg an angle, in radians.
	 * @return the tangent of the argument.
	 */
	public static double tan(double arg) {
		if (Double.getExponent(arg) == (int)Double.INF_EXPONENT)
			return Double.NaN;

		arg = arg % PI;
		if (arg == 0.0)
			return arg;

		if (arg < 0) {
			arg = arg + PI;
		}
		int octant = (int) (arg / pio4);
		arg = arg % pio4;

		double res;
		if (arg <= tanMinArg) {
			switch (octant & 3) {
			case 0:
				res = arg;
				break;
			case 1:
				res = 1.0;
				break;
			case 2:
				res = -tanMax;
				break;
			case 3:
				res = -1.0;
				break;
			default:
				res = Double.NaN;
			}
		} else {
			switch (octant & 3) {
			case 0:
				res = tanInPio4(arg);
				break;
			case 1:
				res = cotInPio4(pio4 - arg);
				break;
			case 2:
				res = -cotInPio4(arg);
				break;
			case 3:
				res = -tanInPio4(pio4 - arg);
				break;
			default:
				res = Double.NaN;
			}
		}
		return res;
	}

	private static final double // atan polynomial coefficients
			atanC2 = -0.3333314528,
			atanC10 = -0.0752896400,
			atanC4 = 0.1999355085,
			atanC12 = 0.0429096138,
			atanC6 = -0.1420889944,
			atanC14 = -0.0161657367,
			atanC8 = 0.1065626393,
			atanC16 = 0.0028662257;

	/**
	 * Returns the arc tangent of an angle, in the range of -<i>pi</i>/2 through
	 * <i>pi</i>/2. Special cases:
	 * <ul>
	 * <li>If the argument is NaN, then the result is NaN.
	 * <li>If the argument is zero, then the result is a zero with the same sign
	 * as the argument.
	 * </ul>
	 * 
	 * <p>
	 * The computed result must be within 1 ulp of the exact result. Results
	 * must be semi-monotonic.
	 * 
	 * @param arg the value whose arc tangent is to be returned.
	 * @return the arc tangent of the argument.
	 */
	public static double atan(double arg) {
		boolean neg = false, octant1 = false;
		if (arg < 0) {
			neg = true;
			arg = -arg;
		}
		if (arg > 1.0) {
			octant1 = true;
			arg = 1.0 / arg;
		}

		double arg2 = arg * arg;
		double res = ((((((((atanC16 * arg2 + atanC14) * arg2 + atanC12) * arg2 + atanC10)
				* arg2 + atanC8)
				* arg2 + atanC6)
				* arg2 + atanC4)
				* arg2 + atanC2)
				* arg2 + 1.0)
				* arg;

		if (octant1)
			res = pio2 - res;
		if (neg)
			res = -res;
		return res;
	}

	/**
	 * Round toward zero
	 * 
	 * @param x value to round
	 * @return rounded value
	 */
	public static int fix(float x) {
		return (int) x;
	}

	/**
	 * Round toward positive infinity
	 * 
	 * @param x value to round
	 * @return rounded value
	 */
	public static int ceil(float x) {
		if (x > 0)
			x += 1f;
		return fix(x);
	}

	/**
	 * Round toward negative infinity
	 * 
	 * @param x value to round
	 * @return rounded value
	 */
	public static int floor(float x) {
		if (x < 0)
			x -= 1f;
		return fix(x);
	}
}

package com.github.chainmailstudios.astromine.common.fraction;

import com.google.common.base.Objects;
import com.google.common.math.LongMath;
import net.minecraft.nbt.CompoundTag;

import java.math.RoundingMode;

public class Fraction extends Number implements Comparable<Fraction> {
	private final long numerator;
	private final long denominator;

	public static final Fraction EMPTY = new Fraction(0, 1);
	public static final Fraction BUCKET = new Fraction(1, 1);
	public static final Fraction BOTTLE = new Fraction(1, 3);

	public Fraction(long numerator, long denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	/**
	 * Fraction addition method, lossless.
	 */
	public static Fraction add(Fraction fractionA, Fraction fractionB) {
		long denominator = lowestCommonDenominator(fractionA.denominator, fractionB.denominator);

		try {
			return new Fraction(LongMath.checkedMultiply(fractionA.numerator, LongMath.divide(denominator, fractionA.denominator, RoundingMode.DOWN)) +
					LongMath.checkedMultiply(fractionB.numerator, LongMath.divide(denominator, fractionB.denominator, RoundingMode.DOWN)), denominator);
		} catch (ArithmeticException exception) {
			throw new UnsupportedOperationException("Attempt to add fractions whose numerator, adjusted for lowest common denominator, would cause an overflow!");
		}
	}

	/**
	 * Fraction subtraction method, lossless.
	 */
	public static Fraction subtract(Fraction fractionA, Fraction fractionB) {
		long denominator = lowestCommonDenominator(fractionA.denominator, fractionB.denominator);

		try {
			return new Fraction(LongMath.checkedMultiply(fractionA.numerator, LongMath.divide(denominator, fractionA.denominator, RoundingMode.DOWN)) -
					LongMath.checkedMultiply(fractionB.numerator, LongMath.divide(denominator, fractionB.denominator, RoundingMode.DOWN)), denominator);
		} catch (ArithmeticException exception) {
			throw new UnsupportedOperationException("Attempt to subtract fractions whose numerator, adjusted for lowest common denominator, would cause an overflow!");
		}
	}

	/**
	 * Fraction multiplication method, lossless.
	 */
	public static Fraction multiply(Fraction fractionA, Fraction fractionB) {
		try {
			return new Fraction(LongMath.checkedMultiply(fractionA.numerator, fractionB.numerator), LongMath.checkedMultiply(fractionA.denominator, fractionB.denominator));
		} catch (ArithmeticException exception) {
			throw new UnsupportedOperationException("Attempted to multiply fractions whose lowest common denominator would cause an overflow!");
		}
	}

	/**
	 * Fraction division method, lossless.
	 * Fractions are divided sequentially following
	 * array order.
	 */
	public static Fraction divide(Fraction fractionA, Fraction fractionB) {
		return multiply(fractionA, Fraction.inverse(fractionB));
	}

	public static Fraction inverse(Fraction fraction) {
		return new Fraction(fraction.denominator, fraction.numerator);
	}

	/**
	 * Fraction simplification method, lossless.
	 */
	public static Fraction simplify(Fraction fraction) {
		if (fraction.numerator == 0) return Fraction.EMPTY;

		long divisor = greatestCommonDivisor(fraction.numerator, fraction.denominator);

		return new Fraction(fraction.numerator / divisor, fraction.denominator / divisor);
	}

	/**
	 * Fraction denominator limiter method, lossy.
	 * Loss of precision with indivisible denominators,
	 * should only be used for the front-end display.
	 */
	public static Fraction limit(Fraction source, Fraction target) {
		try {
			return new Fraction(LongMath.checkedMultiply(source.numerator, (target.denominator / source.denominator)), target.denominator);
		} catch (ArithmeticException exception) {
			throw new UnsupportedOperationException("Attempted to equalize fractions whose lowest common denominator would cause an overflow!");
		}
	}

	/**
	 * Fraction comparison method.
	 */
	public boolean isBiggerThan(Fraction fraction) {
		if (this.denominator == fraction.denominator) return this.numerator > fraction.numerator;

		long denominator = lowestCommonDenominator(this.denominator, fraction.denominator);

		try {
			return LongMath.checkedMultiply(numerator, (LongMath.divide(this.denominator, denominator, RoundingMode.DOWN))) >
					LongMath.checkedMultiply(fraction.numerator, LongMath.divide(fraction.denominator, denominator, RoundingMode.DOWN));
		} catch (ArithmeticException exception) {
			throw new UnsupportedOperationException("Attempted to compare fractions whose lowest common denominator would cause an overflow!");
		}
	}

	/**
	 * Fraction comparison method.
	 */
	public boolean isSmallerThan(Fraction fraction) {
		return !isBiggerThan(fraction);
	}

	/**
	 * Fraction comparison method.
	 */
	public static Fraction min(Fraction fractionA, Fraction fractionB) {
		return (fractionA.isSmallerThan(fractionB) ? fractionA : fractionB);
	}

	/**
	 * Fraction comparison method.
	 */
	public static Fraction max(Fraction fractionA, Fraction fractionB) {
		return (fractionA.isBiggerThan(fractionB) ? fractionA : fractionB);
	}

	/**
	 * Iterative version of Stein's Algorithm for
	 * greatest common divisor.
	 */
	private static long greatestCommonDivisor(long a, long b) {
		long shift = 0;

		if (a == 0) return b;
		if (b == 0) return a;

		while (((a | b) & 1) == 0) {
			++shift;
			a >>= 1;
			b >>= 1;
		}

		while ((a & 1) == 0)
			a >>= 1;

		do {
			while ((b & 1) == 0)
				b >>= 1;
			if (a > b) {
				long t = b;
				b = a;
				a = t;
			}

			b -= a;
		} while (b != 0);

		return a << shift;
	}

	/**
	 * Returns the lowest common denominator between a series of fractions.
	 */
	private static long lowestCommonDenominator(long a, long b) {
		try {
			return a == b ? a : a == 1 ? b : b == 1 ? a : LongMath.divide(a, LongMath.checkedMultiply(greatestCommonDivisor(a, b), b), RoundingMode.DOWN);
		} catch (ArithmeticException exception) {
			throw new UnsupportedOperationException("Attempted to find lowest common denominator fractions whose lowest common denominator would cause an overflow!");
		}
	}

	public CompoundTag toTag(CompoundTag tag) {
		tag.putLongArray("values", new long[]{numerator, denominator});

		return tag;
	}

	public static Fraction fromTag(CompoundTag tag) {
		long[] values = tag.getLongArray("values");

		return new Fraction(values[0], values[1]);
	}

	public long getNumerator() {
		return numerator;
	}

	public long getDenominator() {
		return denominator;
	}

	@Override
	public String toString() {
		return "Fraction{" +
				"numerator=" + numerator +
				", denominator=" + denominator +
				'}';
	}

	@Override
	public int compareTo(Fraction fraction) {
		return isBiggerThan(fraction) ? 1 : isSmallerThan(fraction) ? -1 : 0;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof Fraction)) return false;

		Fraction target = (Fraction) object;

		Fraction simplifiedA = Fraction.simplify(this);
		Fraction simplifiedB = Fraction.simplify(target);

		return simplifiedA.numerator == simplifiedB.numerator && simplifiedA.denominator == simplifiedB.denominator;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(numerator, denominator);
	}

	@Override
	public long longValue() {
		try {
			return LongMath.divide(numerator, denominator, RoundingMode.DOWN);
		} catch (ArithmeticException exception) {
			throw new UnsupportedOperationException("Attempted to retrieve value of fraction whose numerator, divided by denominator, would cause an overflow!");
		}
	}

	@Override
	public int intValue() {
		return (int) longValue();
	}

	@Override
	public float floatValue() {
		return (float) longValue();
	}

	@Override
	public double doubleValue() {
		return (double) longValue();
	}
}
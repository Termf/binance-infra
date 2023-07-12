package com.binance.platform.monitor.utils;

public class Pair<A, B> {

	private final A a;
	private final B b;

	/**
	 * Create a pair and store two objects.
	 *
	 * @param a the first object to store
	 * @param b the second object to store
	 */
	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	/**
	 * Returns the first stored object.
	 *
	 * @return first object stored
	 */
	public final A getA() {
		return a;
	}

	/**
	 * Returns the second stored object.
	 *
	 * @return second object stored
	 */
	public final B getB() {
		return b;
	}
}
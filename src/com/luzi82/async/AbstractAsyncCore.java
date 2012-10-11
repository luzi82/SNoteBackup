package com.luzi82.async;

public abstract class AbstractAsyncCore {

	abstract void start(Callback aCallback);

	static abstract public class Callback {
		public abstract void acFire();
	}

}

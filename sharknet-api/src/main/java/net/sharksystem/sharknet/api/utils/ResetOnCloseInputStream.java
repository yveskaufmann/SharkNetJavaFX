package net.sharksystem.sharknet.api.utils;

import java.io.IOException;
import java.io.InputStream;


/**
 * A Proxy stream which passes the method calls on to the proxied stream and
 * ensures that the stream is reset to its initial state when
 * the proxy stream is closed.
 */
public class ResetOnCloseInputStream extends InputStream {

	private InputStream proxiedInputStream;
	private volatile boolean isClosed = false;

	/**
	 * Construct a new ResetOnCloseInputStream
	 *
	 * @param in the InputStream to delegate to which must supports marks
	 * @see InputStream#markSupported()
     */
	public ResetOnCloseInputStream(InputStream in) {
		if (! in.markSupported()) {
			throw new IllegalArgumentException("The specified passed input stream must supports marks");
		}
		proxiedInputStream = in;
		proxiedInputStream.mark(Integer.MAX_VALUE);
	}

	@Override
	public int read() throws IOException {
		ensureStreamIsNotClosed();
		return proxiedInputStream.read();
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public synchronized void mark(int readlimit) {
	}

	@Override
	public synchronized void reset() throws IOException {
		ensureStreamIsNotClosed();
		throw new IOException("mark/reset not supported");
	}

	@Override
	public int available() throws IOException {
		ensureStreamIsNotClosed();
		return proxiedInputStream.available();
	}

	@Override
	public long skip(long n) throws IOException {
		ensureStreamIsNotClosed();
		return proxiedInputStream.skip(n);
	}

	@Override
	synchronized
	public void close() throws IOException {
		ensureStreamIsNotClosed();
		proxiedInputStream.reset();
		isClosed = true;
	}

	synchronized
	private void ensureStreamIsNotClosed() throws IOException {
		if (isClosed) {
			throw new IOException("stream already closed");
		}
	}
}

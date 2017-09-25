package io.datty.msgpack.lite.support;

/**
 * LiteException - is the root exception of the library
 * 
 * @author Alex Shvid
 *
 */

public class LiteException extends RuntimeException {

	private static final long serialVersionUID = -387649865196967964L;

	public LiteException(String msg) {
    super(msg);
  }
  
  public LiteException(String msg, Throwable t) {
    super(msg, t);
  } 
  
}

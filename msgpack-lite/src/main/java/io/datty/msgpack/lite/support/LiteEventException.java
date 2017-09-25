package io.datty.msgpack.lite.support;

/**
 * LiteEventException - is the exception for MessageBox
 * 
 * @author Alex Shvid
 *
 */

public class LiteEventException extends LiteException {

	private static final long serialVersionUID = -6681262602329832399L;

	public LiteEventException(String msg) {
    super(msg);
  }
  
  public LiteEventException(String msg, Throwable t) {
    super(msg, t);
  } 
  
}

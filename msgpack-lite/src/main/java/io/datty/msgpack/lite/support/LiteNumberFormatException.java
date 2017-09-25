package io.datty.msgpack.lite.support;

/**
 * LiteNumberFormatException - wrapped exception for wrong numbers
 * 
 * @author Alex Shvid
 *
 */

public class LiteNumberFormatException extends LiteException {

	private static final long serialVersionUID = -6636837527625149364L;

	public LiteNumberFormatException(String msg) {
    super(msg);
  }
  
  public LiteNumberFormatException(String msg, Throwable t) {
    super(msg, t);
  } 
  
}

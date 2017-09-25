package io.datty.msgpack.lite.support;

/**
 * LiteParseException - wrapped exception for strings
 * 
 * @author Alex Shvid
 *
 */

public class LiteParseException extends LiteException {

	private static final long serialVersionUID = -3552348772637149118L;

	public LiteParseException(String msg) {
    super(msg);
  }
  
  public LiteParseException(String msg, Throwable t) {
    super(msg, t);
  } 
  
}

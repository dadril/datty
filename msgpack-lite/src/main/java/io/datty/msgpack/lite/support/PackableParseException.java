package io.datty.msgpack.lite.support;

/**
 * PackableParseException - wrapped exception for strings
 * 
 * @author Alex Shvid
 *
 */

public class PackableParseException extends PackableException {

	private static final long serialVersionUID = -3552348772637149118L;

	public PackableParseException(String msg) {
    super(msg);
  }
  
  public PackableParseException(String msg, Throwable t) {
    super(msg, t);
  } 
  
}

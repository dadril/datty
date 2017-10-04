package io.datty.msgpack.table.support;

/**
 * PackableMessageException - is the exception for PackableMessage
 * 
 * @author Alex Shvid
 *
 */

public class PackableMessageException extends PackableException {

	private static final long serialVersionUID = -6681262602329832399L;

	public PackableMessageException(String msg) {
    super(msg);
  }
  
  public PackableMessageException(String msg, Throwable t) {
    super(msg, t);
  } 
  
}

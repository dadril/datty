package io.datty.msgpack.table.support;

/**
 * PackableException - is the root exception of the library
 * 
 * @author Alex Shvid
 *
 */

public class PackableException extends RuntimeException {

	private static final long serialVersionUID = -387649865196967964L;

	public PackableException(String msg) {
    super(msg);
  }
  
  public PackableException(String msg, Throwable t) {
    super(msg, t);
  } 
  
}

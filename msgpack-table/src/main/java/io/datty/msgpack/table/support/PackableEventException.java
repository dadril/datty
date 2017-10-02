package io.datty.msgpack.table.support;

/**
 * PackableEventException - is the exception for MessageBox
 * 
 * @author Alex Shvid
 *
 */

public class PackableEventException extends PackableException {

	private static final long serialVersionUID = -6681262602329832399L;

	public PackableEventException(String msg) {
    super(msg);
  }
  
  public PackableEventException(String msg, Throwable t) {
    super(msg, t);
  } 
  
}

package io.datty.msgpack.table.support;

/**
 * PackableNumberFormatException - wrapped exception for wrong numbers
 * 
 * @author Alex Shvid
 *
 */

public class PackableNumberFormatException extends PackableException {

	private static final long serialVersionUID = -6636837527625149364L;

	public PackableNumberFormatException(String msg) {
    super(msg);
  }
  
  public PackableNumberFormatException(String msg, Throwable t) {
    super(msg, t);
  } 
  
}

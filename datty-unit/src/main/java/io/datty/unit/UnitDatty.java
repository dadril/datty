package io.datty.unit;

import java.util.List;

import io.datty.api.Datty;
import io.datty.api.DattyKey;
import io.datty.api.DattyOperation;
import io.datty.api.DattyResult;
import io.datty.api.DattySingle;
import io.netty.buffer.ByteBuf;
import rx.Observable;
import rx.Single;

public class UnitDatty implements Datty {

	private final DattySingle single;
	
	public UnitDatty(DattySingle single) {
		this.single = single;
	}
	
	@Override
	public <O extends DattyOperation<O, R>, R extends DattyResult<O>> Single<R> execute(O operation) {
		return single.execute(operation);
	}

	@Override
	public Single<List<DattyResult>> executeBatch(List<DattyOperation> operations) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Single<List<DattyResult>> executeBatch(List<DattyOperation> operations, int timeoutMillis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Observable<DattyResult> executeSequence(Observable<DattyOperation> operations) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Observable<DattyResult> executeSequence(Observable<DattyOperation> operations, int totalTimeoutMillis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Observable<DattyResult> streamOut(DattyKey key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Observable<DattyResult> streamOut(DattyKey key, int totalTimeoutMillis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Single<DattyResult> streamIn(DattyKey key, Observable<ByteBuf> value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Single<DattyResult> streamIn(DattyKey key, Observable<ByteBuf> value, int totalTimeoutMillis) {
		// TODO Auto-generated method stub
		return null;
	}

}

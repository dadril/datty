package io.datty.spi;

import io.datty.api.DattyOperation;
import io.datty.api.DattyResult;
import io.datty.api.DattySingle;
import io.datty.api.result.AbstractResult;
import rx.Single;
import rx.functions.Func1;

/**
 * Client side
 * 
 * @author dadril
 *
 */

public class AbstractDattyProvider implements DattySingle {

	private final DattySingle driver;
	
	public AbstractDattyProvider(DattySingle driver) {
		this.driver = driver;
	}
	
	@Override
	public <O extends DattyOperation<O, R>, R extends DattyResult<O>> Single<R> execute(final O operation) {

		final R fallback = operation.getFallback();

		try {
			Single<R> result = driver.execute(operation);
			
			result = result.map(new Func1<R, R>() {

				@Override
				public R call(R res) {
					if (res instanceof AbstractResult) {
						AbstractResult<O, R> abstractResult = (AbstractResult<O, R>) res;
						abstractResult.setOperation(operation);
					}
					return res;
				}
				
			});
			
			if (fallback != null) {
				
				result = result.onErrorReturn(new Func1<Throwable, R>() {

					@Override
					public R call(Throwable t) {
						return fallback;
					}
					
				});
			}
			
			return result;
		}
		catch(RuntimeException e) {
			if (fallback != null) {
				return Single.just(fallback);
			}
			throw e;
		}
	}


}

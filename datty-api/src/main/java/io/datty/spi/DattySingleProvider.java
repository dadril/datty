package io.datty.spi;

import io.datty.api.DattySingle;
import io.datty.api.operation.TypedOperation;
import io.datty.api.result.AbstractResult;
import io.datty.api.result.TypedResult;
import rx.Single;
import rx.functions.Func1;

/**
 * Client side
 * 
 * @author Alex Shvid
 *
 */

public class DattySingleProvider implements DattySingle {

	private final DattySingle driver;
	
	public DattySingleProvider(DattySingle driver) {
		this.driver = driver;
	}
	
	@Override
	public <O extends TypedOperation<O, R>, R extends TypedResult<O>> Single<R> execute(final O operation) {

		final R fallback = operation.getFallback();

		try {
			Single<R> result = driver.execute(operation);
			
			result = result.map(new Func1<R, R>() {

				@Override
				public R call(R res) {
					if (res instanceof AbstractResult) {
						@SuppressWarnings("unchecked")
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

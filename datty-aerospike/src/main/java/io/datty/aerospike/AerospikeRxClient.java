/*
 * Copyright (C) 2016 Datty.io Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.datty.aerospike;

import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.Value;
import com.aerospike.client.async.AsyncClient;
import com.aerospike.client.listener.DeleteListener;
import com.aerospike.client.listener.ExecuteListener;
import com.aerospike.client.listener.ExistsListener;
import com.aerospike.client.listener.RecordListener;
import com.aerospike.client.listener.WriteListener;
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.policy.WritePolicy;

import io.datty.aerospike.support.ExceptionTransformer;
import rx.Single;
import rx.Single.OnSubscribe;
import rx.SingleSubscriber;

/**
 * AerospikeRxClient
 * 
 * @author Alex Shvid
 *
 */

public final class AerospikeRxClient {

	private final AsyncClient client;
	
	public AerospikeRxClient(AsyncClient client) {
		this.client = client;
	}

	public AsyncClient getClient() {
		return client;
	}

	/**
	 * Checks if record exists or not
	 * 
	 * @param queryPolicy - query policy
	 * @param key - major key of the record
	 * @param exceptionTransformer - exception transformer
	 * @return true if exists
	 */
	
	public Single<Boolean> exists(final QueryPolicy queryPolicy, final Key key, final ExceptionTransformer<?> exceptionTransformer) {
		
		return Single.<Boolean>create(new OnSubscribe<Boolean>() {

			@Override
			public void call(final SingleSubscriber<? super Boolean> subscriber) {

				client.exists(queryPolicy, new ExistsListener() {

					@Override
					public void onSuccess(Key key, boolean exists) {
						subscriber.onSuccess(exists);
					}

					@Override
					public void onFailure(AerospikeException exception) {
						subscriber.onError(exceptionTransformer.transformException(exception));
					}
					
				}, key);

			}
			
		});
	}
	
	/**
	 * Gets record for the specific key
	 * 
	 * @param queryPolicy - query policy
	 * @param key - major key
	 * @param exceptionTransformer - exception transformer
	 * @return record or null
	 */
	
	public Single<Record> get(final QueryPolicy queryPolicy, final Key key, final ExceptionTransformer<?> exceptionTransformer) {
		
		return Single.<Record>create(new OnSubscribe<Record>() {

			@Override
			public void call(final SingleSubscriber<? super Record> subscriber) {

				client.get(queryPolicy, new RecordListener() {

					@Override
					public void onSuccess(Key key, Record record) {
						subscriber.onSuccess(record);
					}

					@Override
					public void onFailure(AerospikeException exception) {
						subscriber.onError(exceptionTransformer.transformException(exception));
					}
					
				}, key);

			}
			
		});
	}	
	
	/**
	 * Gets record for the specific key and required bin names
	 * 
	 * @param queryPolicy - query policy
	 * @param key - major key
	 * @param exceptionTransformer - exception transformer
	 * @return record or null
	 */
	
	public Single<Record> getHeader(final QueryPolicy queryPolicy, final Key key, final ExceptionTransformer<?> exceptionTransformer) {
		
		return Single.<Record>create(new OnSubscribe<Record>() {

			@Override
			public void call(final SingleSubscriber<? super Record> subscriber) {

				client.getHeader(queryPolicy, new RecordListener() {

					@Override
					public void onSuccess(Key key, Record record) {
						subscriber.onSuccess(record);
					}

					@Override
					public void onFailure(AerospikeException exception) {
						subscriber.onError(exceptionTransformer.transformException(exception));
					}
					
				}, key);

			}
			
		});
	}	
	
	/**
	 * Gets record for the specific key and required bin names
	 * 
	 * @param queryPolicy - query policy
	 * @param key - major key
	 * @param binNames - bin names
	 * @param exceptionTransformer - exception transformer
	 * @return record or null
	 */
	
	public Single<Record> get(final QueryPolicy queryPolicy, final Key key, final String[] binNames, final ExceptionTransformer<?> exceptionTransformer) {
		
		return Single.<Record>create(new OnSubscribe<Record>() {

			@Override
			public void call(final SingleSubscriber<? super Record> subscriber) {

				client.get(queryPolicy, new RecordListener() {

					@Override
					public void onSuccess(Key key, Record record) {
						subscriber.onSuccess(record);
					}

					@Override
					public void onFailure(AerospikeException exception) {
						subscriber.onError(exceptionTransformer.transformException(exception));
					}
					
				}, key, binNames);

			}
			
		});
	}	
	
	/**
	 * Puts bins to the record with specific write policy
	 * 
	 * @param writePolicy - write policy
	 * @param key - major key
	 * @param bins - updating or replacing bins
	 * @param exceptionTransformer - exception transformer
	 * @return key if success
	 */
	
	public Single<Key> put(final WritePolicy writePolicy, final Key key, final Bin[] bins, final ExceptionTransformer<?> exceptionTransformer) {
		
		return Single.<Key>create(new OnSubscribe<Key>() {

			@Override
			public void call(final SingleSubscriber<? super Key> subscriber) {
				
				client.put(writePolicy, new WriteListener() {

					@Override
					public void onSuccess(Key key) {
						subscriber.onSuccess(key);
					}

					@Override
					public void onFailure(AerospikeException exception) {
						subscriber.onError(exceptionTransformer.transformException(exception));
					}
					
				}, key, bins);

			}
			
		});
		
	}
	
	/**
	 * Remove record
	 * 
	 * @param writePolicy - write policy
	 * @param key - major key
	 * @param exceptionTransformer - exception transformer
	 * @return true if record existed in the storage
	 */
	
	public Single<Boolean> remove(final WritePolicy writePolicy, final Key key, final ExceptionTransformer<?> exceptionTransformer) {
		
		return Single.<Boolean>create(new OnSubscribe<Boolean>() {

			@Override
			public void call(final SingleSubscriber<? super Boolean> subscriber) {
				
				client.delete(writePolicy, new DeleteListener() {

					@Override
					public void onSuccess(Key key, boolean existed) {
						subscriber.onSuccess(existed);
					}

					@Override
					public void onFailure(AerospikeException exception) {
						subscriber.onError(exceptionTransformer.transformException(exception));
					}
					
				}, key);

			}
			
		});
		
	}
	
	/**
	 * Execute UDF function under majorKey
	 * 
	 * @param writePolicy - write policy
	 * @param key - major key
	 * @param packageName - package name
	 * @param functionName - function name
	 * @param arguments - function arguments
	 * @param exceptionTransformer - exception transformer
	 * @return true if record existed in the storage
	 */
	
	public Single<Object> execute(final WritePolicy writePolicy, final Key key, final String packageName, final String functionName, final Value[] arguments, final ExceptionTransformer<?> exceptionTransformer) {
		
		return Single.<Object>create(new OnSubscribe<Object>() {

			@Override
			public void call(final SingleSubscriber<? super Object> subscriber) {
				
				client.execute(writePolicy, new ExecuteListener() {

					@Override
					public void onSuccess(Key key, Object result) {
						subscriber.onSuccess(result);
					}

					@Override
					public void onFailure(AerospikeException exception) {
						subscriber.onError(exceptionTransformer.transformException(exception));
					}
					
				}, key, packageName, functionName, arguments);

			}
			
		});
		
	}
	
}

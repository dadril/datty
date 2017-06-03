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
package io.datty.aerospike.info;

import java.util.concurrent.Callable;

import com.aerospike.client.Info;
import com.aerospike.client.cluster.Node;

import io.datty.aerospike.AerospikeDattyManager;
import io.datty.aerospike.support.RandomUtil;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * AerospikeInfoCallable
 * 
 * @author Alex Shvid
 *
 */

public final class AerospikeInfoCallable implements Callable<AerospikeInfoResponse> {
	
	private final AerospikeDattyManager manager;
	private final Node node;
	private final AerospikeInfoRequest request;
	
	public AerospikeInfoCallable(AerospikeDattyManager manager, AerospikeInfoRequest request) {
		this.manager = manager;
		Node[] nodes = manager.getClient().getClient().getNodes();
		this.node = RandomUtil.selectRandom(nodes);
		this.request = request;
	}

	@Override
	public AerospikeInfoResponse call() throws Exception {
		String response = Info.request(manager.getConfig().getClientPolicy().infoPolicyDefault, node, request.toCommand());
		return new AerospikeInfoResponse(response);
	}
	
	public Observable<AerospikeInfoResponse> toOservable() {
		return Observable.fromCallable(this).subscribeOn(Schedulers.io());
	}
	
}
/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.atomic;

import net.kuujo.copycat.atomic.internal.DefaultAsyncReference;
import net.kuujo.copycat.cluster.ClusterConfig;
import net.kuujo.copycat.cluster.internal.coordinator.CoordinatedResourceConfig;
import net.kuujo.copycat.collections.AsyncCollectionConfig;
import net.kuujo.copycat.state.StateLogConfig;
import net.kuujo.copycat.util.internal.Assert;

import java.util.Map;

/**
 * Asynchronous atomic reference configuration.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class AsyncReferenceConfig extends AsyncCollectionConfig<AsyncReferenceConfig> {
  private static final String DEFAULT_CONFIGURATION = "atomic-defaults";
  private static final String CONFIGURATION = "atomic";

  public AsyncReferenceConfig() {
    super(CONFIGURATION, DEFAULT_CONFIGURATION);
  }

  public AsyncReferenceConfig(Map<String, Object> config) {
    super(config, CONFIGURATION, DEFAULT_CONFIGURATION);
  }

  public AsyncReferenceConfig(String resource) {
    super(resource, CONFIGURATION, DEFAULT_CONFIGURATION);
  }

  protected AsyncReferenceConfig(AsyncReferenceConfig config) {
    super(config);
  }

  @Override
  public AsyncReferenceConfig copy() {
    return new AsyncReferenceConfig(this);
  }

  @Override
  public CoordinatedResourceConfig resolve(ClusterConfig cluster) {
    Assert.config(getReplicas(), getReplicas().isEmpty() || cluster.getMembers().containsAll(getReplicas()), "Resource replica set must contain only active cluster members");
    return new StateLogConfig(toMap())
      .resolve(cluster)
      .withResourceType(DefaultAsyncReference.class);
  }

}

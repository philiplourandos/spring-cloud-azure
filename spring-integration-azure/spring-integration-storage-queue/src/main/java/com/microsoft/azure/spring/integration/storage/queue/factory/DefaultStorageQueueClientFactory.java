/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.microsoft.azure.spring.integration.storage.queue.factory;

import com.microsoft.azure.spring.cloud.context.core.api.ResourceManager;
import com.microsoft.azure.spring.cloud.context.core.util.Tuple;
import com.microsoft.azure.storage.queue.CloudQueue;
import org.springframework.lang.NonNull;

public class DefaultStorageQueueClientFactory implements StorageQueueClientFactory {
    private final ResourceManager<CloudQueue, Tuple<String, String>> storageQueueManager;

    public DefaultStorageQueueClientFactory(
            @NonNull ResourceManager<CloudQueue, Tuple<String, String>> storageQueueManager) {
        this.storageQueueManager = storageQueueManager;
    }

    @Override
    public CloudQueue getOrCreateQueueClient(String storageAccountName, String queueName) {
        return storageQueueManager.getOrCreate(Tuple.of(storageAccountName, queueName));
    }
}

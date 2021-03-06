/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.microsoft.azure.spring.integration.servicebus.converter;

import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.spring.integration.core.converter.AbstractAzureMessageConverter;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A converter to turn a {@link org.springframework.messaging.Message} to {@link IMessage}
 * and vice versa.
 *
 * @author Warren Zhu
 */
public class ServiceBusMessageConverter extends AbstractAzureMessageConverter<IMessage> {

    @Override
    protected byte[] getPayload(IMessage azureMessage) {
        return azureMessage.getBody();
    }

    @Override
    protected IMessage fromString(String payload) {
        return new Message(payload);
    }

    @Override
    protected IMessage fromByte(byte[] payload) {
        return new Message(payload);
    }

    @Override
    protected void setCustomHeaders(MessageHeaders headers, IMessage serviceBusMessage) {

        if (headers.containsKey(MessageHeaders.CONTENT_TYPE)) {
            Object contentType = headers.get(MessageHeaders.CONTENT_TYPE);

            if (contentType instanceof MimeType) {
                serviceBusMessage.setContentType(((MimeType) contentType).getType());
            } else /* contentType is String */ {
                serviceBusMessage.setContentType((String) contentType);
            }
        }

        if (headers.containsKey(MessageHeaders.ID)) {
            serviceBusMessage.setMessageId(headers.get(MessageHeaders.ID, UUID.class).toString());
        }

        if (headers.containsKey(MessageHeaders.REPLY_CHANNEL)) {
            serviceBusMessage.setReplyTo(headers.get(MessageHeaders.REPLY_CHANNEL, String.class));
        }
    }

    @Override
    protected MessageHeaders buildCustomHeaders(IMessage serviceBusMessage) {
        Map<String, Object> headers = new HashMap<>();

        headers.put(MessageHeaders.ID, UUID.fromString(serviceBusMessage.getMessageId()));
        headers.put(MessageHeaders.CONTENT_TYPE, serviceBusMessage.getContentType());
        if (StringUtils.hasText(serviceBusMessage.getReplyTo())) {
            headers.put(MessageHeaders.REPLY_CHANNEL, serviceBusMessage.getReplyTo());
        }

        return new MessageHeaders(headers);
    }
}

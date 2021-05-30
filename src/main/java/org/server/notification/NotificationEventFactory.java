package org.server.notification;

import com.lmax.disruptor.EventFactory;

public class NotificationEventFactory implements EventFactory<NotificationEvent> {

    @Override
    public NotificationEvent newInstance() {
        return new NotificationEvent();
    }
}

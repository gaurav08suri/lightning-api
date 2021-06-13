package org.server.core;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.server.notification.NotificationEvent;
import org.server.notification.NotificationEventHandler;

public class ApplicationModule {
    public static RingBuffer<NotificationEvent> init() {

        // The factory for the event
        EventFactory<NotificationEvent> eventFactory = () -> new NotificationEvent();

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor
        Disruptor<NotificationEvent> disruptor = new Disruptor<>(eventFactory, bufferSize, DaemonThreadFactory.INSTANCE);

        // Connect the handler
        EventHandler handler = new NotificationEventHandler();
        disruptor.handleEventsWith(handler);
        disruptor.handleExceptionsFor(handler).with(new IgnoreExceptionHandler());

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        return disruptor.getRingBuffer();
    }

}

/**
 *
 * Copyright © 2016 Mycelium.
 * Use of this source code is governed by an ISC
 * license that can be found in the LICENSE file.
 *
 */

package com.shuffle.mock;

import com.shuffle.chan.Send;
import com.shuffle.p2p.Channel;
import com.shuffle.p2p.Connection;
import com.shuffle.p2p.Listener;
import com.shuffle.p2p.Session;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * You know things are bad when you need unit tests for your mock objects.
 *
 * Created by Daniel Krawisz on 3/4/16.
 */
public class TestMockChannel {

    public class MockListener implements Listener<Integer, String> {
        public final Map<Integer, Send<String>> receivers = new HashMap<>();

        public Send<String> getSend(Integer from) {
            Send<String> receiver = receivers.get(from);
            if (receiver == null) {
                receiver = new MockSend();
                receivers.put(from, receiver);
            }
            return receiver;
        }

        @Override
        public Send<String> newSession(Session<Integer, String> session) {
            return getSend(session.peer().identity());
        }
    }

    public class MockSend implements Send<String> {
        final List<String> messages = new LinkedList<>();
        boolean closed = false;

        @Override
        public boolean send(String s) {
            return !closed && messages.add(s);

        }

        @Override
        public void close() {
            closed = true;
        }
    }

    @Test
    public void testMockChannel() throws InterruptedException {
        MockNetwork<Integer, String> mock = new MockNetwork<>();

        // Create channels.
        Map<Integer, Channel<Integer, String>> knownHosts = new HashMap<>();
        Map<Integer, Connection<Integer>> connections = new HashMap<>();
        Map<Integer, MockListener> listeners = new HashMap<>();
        Map<Integer, MockSend> receivers = new HashMap<>();
        knownHosts.put(1, mock.node(1));
        knownHosts.put(2, mock.node(2));
        listeners.put(1, new MockListener());
        listeners.put(2, new MockListener());
        receivers.put(1, new MockSend());
        receivers.put(2, new MockSend());

        connections.put(1, knownHosts.get(1).open(listeners.get(1)));
        connections.put(2, knownHosts.get(2).open(listeners.get(2)));

        Assert.assertNotNull(connections.get(1));
        Assert.assertNotNull(connections.get(2));

        // Can't have myself as a peer.
        Assert.assertNull(knownHosts.get(1).getPeer(1));
        Assert.assertNull(knownHosts.get(2).getPeer(2));

        // Can't have unknown peers
        Assert.assertNull(knownHosts.get(1).getPeer(3));
        Assert.assertNull(knownHosts.get(2).getPeer(3));

        // Can get these peers.
        Assert.assertNotNull(knownHosts.get(1).getPeer(2));
        Assert.assertNotNull(knownHosts.get(2).getPeer(1));

        // Try to open sessions.
        Assert.assertNotNull(knownHosts.get(1).getPeer(2).openSession(receivers.get(1)));
        Assert.assertNull(knownHosts.get(1).getPeer(2).openSession(receivers.get(1)));
        Assert.assertNull(knownHosts.get(2).getPeer(1).openSession(receivers.get(2)));

        connections.get(1).close();
        connections.get(2).close();

        // Avoid memory leak.
        knownHosts.clear();
    }
}

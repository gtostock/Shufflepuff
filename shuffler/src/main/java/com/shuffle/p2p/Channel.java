/**
 *
 * Copyright © 2016 Mycelium.
 * Use of this source code is governed by an ISC
 * license that can be found in the LICENSE file.
 *
 */

package com.shuffle.p2p;

import java.io.Serializable;

/**
 * A channel through which connections can be created to other peers.
 *
 * Identity -- the object used to identify a particular tcppeer.
 *
 * Message  -- the object which will be sent back and forth over this channel.
 *
 * Created by Daniel Krawisz on 12/16/15.
 */
public interface Channel<Identity, Message extends Serializable> {
    // Our own identity on this channel. For TCP, this would be a IP address.
    Identity identity();

    // Returns null if a peer could not be created for this identity.
    Peer<Identity, Message> getPeer(Identity you);

    // Returns null if the connection could not be opened.
    Connection<Identity> open(final Listener<Identity, Message> listener) throws InterruptedException;
}

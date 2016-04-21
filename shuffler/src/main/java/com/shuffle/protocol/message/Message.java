/**
 *
 * Copyright © 2016 Mycelium.
 * Use of this source code is governed by an ISC
 * license that can be found in the LICENSE file.
 *
 */

package com.shuffle.protocol.message;

import com.shuffle.bitcoin.Address;
import com.shuffle.bitcoin.EncryptionKey;
import com.shuffle.bitcoin.Signature;
import com.shuffle.bitcoin.VerificationKey;
import com.shuffle.protocol.FormatException;
import com.shuffle.protocol.blame.Blame;

/**
 * Created by Daniel Krawisz on 12/19/15.
 */
public interface Message {
    boolean isEmpty();

    Message attach(EncryptionKey ek);

    Message attach(Address addr);

    Message attach(Signature sig);

    Message attach(Blame blame);

    EncryptionKey readEncryptionKey() throws FormatException;

    Signature readSignature() throws FormatException;

    Address readAddress() throws FormatException;

    Blame readBlame() throws FormatException;

    Message rest() throws FormatException;

    // Prepare message to be sent.
    Packet prepare(Phase phase, VerificationKey to);

    Message hashed();
}
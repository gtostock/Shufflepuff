package com.shuffle.protocol;

/**
 * Representing a digital signature of a bitcoin transaction.
 *
 * Created by Daniel Krawisz on 12/5/15.
 */
public interface CoinSignature {
    boolean equals(CoinSignature sig) throws InvalidImplementationError;
}

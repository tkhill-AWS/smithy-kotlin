/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0.
 */
package aws.smithy.kotlin.runtime.hashing

import aws.smithy.kotlin.runtime.util.InternalApi

/**
 * A cryptographic hash function (algorithm)
 */
@InternalApi
interface HashFunction {
    /**
     * The size of the hashing block in bytes.
     */
    val blockSizeBytes: Int

    /**
     * The size of the digest output in bytes.
     */
    val digestSizeBytes: Int

    /**
     * Update the running hash with [input] bytes. This can be called multiple times.
     */
    fun update(input: ByteArray)

    /**
     * Finalize the hash computation and return the digest bytes. The hash function will be [reset] after the call is
     * made.
     */
    fun digest(): ByteArray

    /**
     * Resets the digest to its initial state discarding any accumulated digest state.
     */
    fun reset()
}

internal fun hash(fn: HashFunction, input: ByteArray): ByteArray = fn.apply { update(input) }.digest()

/**
 * Compute a hash of the current [ByteArray]
 */
@InternalApi
fun ByteArray.hash(fn: HashFunction): ByteArray = hash(fn, this)

/**
 * A function that returns a new instance of a [HashFunction].
 */
@InternalApi
typealias HashSupplier = () -> HashFunction

/**
 * Compute a hash of the current [ByteArray]
 */
@InternalApi
fun ByteArray.hash(hashSupplier: HashSupplier): ByteArray = hash(hashSupplier(), this)
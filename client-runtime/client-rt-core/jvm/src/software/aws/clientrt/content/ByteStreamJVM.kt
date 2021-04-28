/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0.
 */

package software.aws.clientrt.content

import software.aws.clientrt.io.SdkByteReadChannel
import software.aws.clientrt.io.copyTo
import software.aws.clientrt.io.writeChannel
import java.io.File
import java.nio.file.Path

// JVM specific extensions for dealing with ByteStream's

/**
 * Create a [ByteStream] from a file
 */
fun ByteStream.Companion.fromFile(file: File): ByteStream = file.asByteStream()

/**
 * Create a [ByteStream] from a file
 */
fun File.asByteStream(): ByteStream = FileContent(this)

/**
 * Create a [ByteStream] from a path
 */
fun Path.asByteStream(): ByteStream {
    val f = toFile()
    require(f.isFile) { "cannot create a ByteStream from a directory: $this" }
    require(f.exists()) { "cannot create ByteStream, invalid file: $this" }
    return f.asByteStream()
}

/**
 * Write the contents of this ByteStream to file and close it
 * @return the number of bytes written
 */
suspend fun ByteStream.writeToFile(file: File): Long {
    require(file.isFile) { "cannot write contents of ByteStream to a directory: ${file.absolutePath}" }
    val writer = file.writeChannel()
    val src = when (this) {
        is ByteStream.Buffer -> SdkByteReadChannel(bytes())
        is ByteStream.Reader -> readFrom()
    }

    try {
        return src.copyTo(writer)
    } finally {
        writer.close()
        src.close()
    }
}

/**
 * Write the contents of this ByteStream to file at the given path
 * @return the number of bytes written
 */
suspend fun ByteStream.writeToFile(path: Path): Long = writeToFile(path.toFile())
package io.github.mike10004.torch.packaging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * Utility methods for byte streams.
 * <pre>
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * </pre>
 * @author Chris Nokleberg
 * @author Colin Decker
 */
public class ByteStreams {

    private ByteStreams() {
    }

    /** Max array length on JVM. */
    private static final int MAX_ARRAY_LEN = Integer.MAX_VALUE - 8;

    /** Large enough to never need to expand, given the geometric progression of buffer sizes. */
    private static final int TO_BYTE_ARRAY_DEQUE_SIZE = 20;
    private static final int BUFFER_SIZE = 8192;

    private static final class IntMath  {
        private IntMath() {}
        public static int saturatedMultiply(int a, int b) {
            long c = ((long)a) * ((long)b);
            if (c > Integer.MAX_VALUE) {
                c = Integer.MAX_VALUE;
            }
            if (c < Integer.MIN_VALUE) {
                c = Integer.MIN_VALUE;
            }
            return (int) c;
        }
    }

    /**
     * Returns a byte array containing the bytes from the buffers already in {@code bufs} (which have
     * a total combined length of {@code totalLen} bytes) followed by all bytes remaining in the given
     * input stream.
     */
    private static byte[] toByteArrayInternal(InputStream in, Deque<byte[]> bufs, int totalLen)
            throws IOException {
        // Starting with an 8k buffer, double the size of each sucessive buffer. Buffers are retained
        // in a deque so that there's no copying between buffers while reading and so all of the bytes
        // in each new allocated buffer are available for reading from the stream.
        for (int bufSize = BUFFER_SIZE;
             totalLen < MAX_ARRAY_LEN;
             bufSize = IntMath.saturatedMultiply(bufSize, 2)) {
            byte[] buf = new byte[Math.min(bufSize, MAX_ARRAY_LEN - totalLen)];
            bufs.add(buf);
            int off = 0;
            while (off < buf.length) {
                // always OK to fill buf; its size plus the rest of bufs is never more than MAX_ARRAY_LEN
                int r = in.read(buf, off, buf.length - off);
                if (r == -1) {
                    return combineBuffers(bufs, totalLen);
                }
                off += r;
                totalLen += r;
            }
        }

        // read MAX_ARRAY_LEN bytes without seeing end of stream
        if (in.read() == -1) {
            // oh, there's the end of the stream
            return combineBuffers(bufs, MAX_ARRAY_LEN);
        } else {
            throw new OutOfMemoryError("input is too large to fit in a byte array");
        }
    }

    private static byte[] combineBuffers(Deque<byte[]> bufs, int totalLen) {
        byte[] result = new byte[totalLen];
        int remaining = totalLen;
        while (remaining > 0) {
            byte[] buf = bufs.removeFirst();
            int bytesToCopy = Math.min(remaining, buf.length);
            int resultOffset = totalLen - remaining;
            System.arraycopy(buf, 0, result, resultOffset, bytesToCopy);
            remaining -= bytesToCopy;
        }
        return result;
    }

    /**
     * Reads all bytes from an input stream into a byte array. Does not close the stream.
     *
     * @param in the input stream to read from
     * @return a byte array containing all the bytes from the stream
     * @throws IOException if an I/O error occurs
     */
    public static byte[] toByteArray(InputStream in) throws IOException {
        Objects.requireNonNull(in);
        return toByteArrayInternal(in, new ArrayDeque<>(TO_BYTE_ARRAY_DEQUE_SIZE), 0);
    }

    /** Creates a new byte array for buffering reads or writes. */
    static byte[] createBuffer() {
        return new byte[BUFFER_SIZE];
    }

    /**
     * Copies all bytes from the input stream to the output stream. Does not close or flush either
     * stream.
     *
     * @param from the input stream to read from
     * @param to the output stream to write to
     * @return the number of bytes copied
     * @throws IOException if an I/O error occurs
     */
    public static long copy(InputStream from, OutputStream to) throws IOException {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        byte[] buf = createBuffer();
        long total = 0;
        while (true) {
            int r = from.read(buf);
            if (r == -1) {
                break;
            }
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }
}

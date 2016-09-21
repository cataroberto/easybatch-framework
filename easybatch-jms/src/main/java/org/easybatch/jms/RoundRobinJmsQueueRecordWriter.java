/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package org.easybatch.jms;

import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.writer.RecordWriter;

import javax.jms.Message;
import javax.jms.QueueSender;
import java.util.List;

/**
 * Dispatch records to a list of Jms queues in round-robin fashion.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class RoundRobinJmsQueueRecordWriter implements RecordWriter {

    /**
     * The total number of queues this writer operates on.
     */
    private int queuesNumber;

    /**
     * Next queue to which write next incoming record.
     */
    private int next;

    /**
     * List of queues to which records should be written.
     */
    private List<QueueSender> queues;

    /**
     * Create a {@link RoundRobinJmsQueueRecordWriter}.
     *
     * @param queues the list of queues to which records should be written
     */
    public RoundRobinJmsQueueRecordWriter(List<QueueSender> queues) {
        this.queues = queues;
        this.queuesNumber = queues.size();
    }

    @Override
    public void open() throws Exception {

    }

    @Override
    public void writeRecords(Batch batch) throws Exception {
        for (Record record : batch.getRecords()) {
            //dispatch records to queues in round-robin fashion
            QueueSender queue = queues.get(next++ % queuesNumber);
            queue.send((Message) record.getPayload());
        }
    }

    @Override
    public void close() throws Exception {

    }
}

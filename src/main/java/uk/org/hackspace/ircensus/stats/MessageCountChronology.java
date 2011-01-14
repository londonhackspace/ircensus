package uk.org.hackspace.ircensus.stats;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

class MessageCountChronology extends AbstractChronology<AtomicLong> {

  MessageCountChronology(int bucketSizeMs) {
    super(bucketSizeMs);
  }

  void increment() {
    AtomicLong counter = getCurrentBucket();
    counter.incrementAndGet();
  }

  long getCountForInterval(long periodMs) {
    long counter = 0;
    List<AtomicLong> counts = getDataForInterval(periodMs);
    for (AtomicLong count : counts) {
      counter += count.get();
    }
    return counter;
  }

  @Override
  AtomicLong newBucketInstance() {
    return new AtomicLong();
  }

}

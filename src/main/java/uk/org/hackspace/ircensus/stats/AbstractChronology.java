package uk.org.hackspace.ircensus.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

abstract class AbstractChronology<T> {

  private final int bucketLengthMs;

  private ConcurrentHashMap<Long, T> buckets = new ConcurrentHashMap<Long, T>();

  AbstractChronology(int bucketLengthMs) {
    this.bucketLengthMs = bucketLengthMs;
    buckets = new ConcurrentHashMap<Long, T>();
  }

  abstract T newBucketInstance();

  List<T> getDataForInterval(long periodMs) {
    List<T> dataPoints = new ArrayList<T>();
    long cutOff = getPeriodStartTimeStamp(periodMs);
    for (Long timeStamp : buckets.keySet()) {
      if (timeStamp >= cutOff) {
        T bucket = buckets.get(timeStamp);
        if (bucket != null) {
          dataPoints.add(bucket);
        }
      }
    }
    return dataPoints;
  }

  T getCurrentBucket() {
    T block = getBucket(getCurrentTimeStamp());
    return block;
  }

  void purgeOlderThan(long periodMs) {
    long cutOff = getPeriodStartTimeStamp(periodMs);
    for (Long timeStamp : buckets.keySet()) {
      if (timeStamp < cutOff) {
        buckets.remove(timeStamp);
      }
    }
  }

  private T getBucket(long bucketIndex) {
    T bucket = buckets.get(bucketIndex);
    if (bucket == null) {
      T newBucket = newBucketInstance();
      bucket = buckets.putIfAbsent(bucketIndex, newBucket);
      if (bucket == null) {
        bucket = newBucket;
      }
    }
    return bucket;
  }

  private long getPeriodStartTimeStamp(long durationMs) {
    long timeStamp = (System.currentTimeMillis() - durationMs) / bucketLengthMs;
    return timeStamp;
  }

  private long getCurrentTimeStamp() {
    long timeStamp = System.currentTimeMillis() / bucketLengthMs;
    return timeStamp;
  }

}

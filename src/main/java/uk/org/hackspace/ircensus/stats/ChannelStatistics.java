package uk.org.hackspace.ircensus.stats;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

public class ChannelStatistics {

  private static final double TWO_POINT_FIVE_PERCENT = 0.025;

  private final MessageCountChronology messageCountChronology;

  private final ActiveUsersChronology activeUsersChronology;

  private final long samplePeriodMs;

  private final int bucketSizeMs;

  private final PircBot ircClient;

  private final String channel;

  ChannelStatistics(String channel, PircBot ircClient, long samplePeriodMs) {
    this.channel = channel;
    this.ircClient = ircClient;
    this.samplePeriodMs = samplePeriodMs;
    bucketSizeMs = (int) (samplePeriodMs * TWO_POINT_FIVE_PERCENT);
    messageCountChronology = new MessageCountChronology(bucketSizeMs);
    activeUsersChronology = new ActiveUsersChronology(bucketSizeMs);
  }

  void message(String user) {
    messageCountChronology.increment();
    activeUsersChronology.markUserAsActive(user);
  }

  public long getMessageCount() {
    return messageCountChronology.getCountForInterval(samplePeriodMs);
  }

  public long getActiveUserCount() {
    return activeUsersChronology.getActiveUserCountForInterval(samplePeriodMs);
  }

  public long getTotalUserCount() {
    User[] users = ircClient.getUsers(channel);
    if (users != null) {
      return users.length;
    }
    return 0;
  }

  void purge() {
    messageCountChronology.purgeOlderThan(samplePeriodMs * 2);
    activeUsersChronology.purgeOlderThan(samplePeriodMs * 2);
  }

  @Override
  public String toString() {
    return channel + ": messages=" + getMessageCount() + ", activeUsers=" + getActiveUserCount() + ", totalUsers="
        + getTotalUserCount();
  }

}

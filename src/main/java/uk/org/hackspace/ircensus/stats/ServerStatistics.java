package uk.org.hackspace.ircensus.stats;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jibble.pircbot.PircBot;

public class ServerStatistics {

  private final ConcurrentMap<String, ChannelStatistics> statisticsByChannel;

  private final long samplePeriodMs;

  private final PircBot ircClient;

  public ServerStatistics(PircBot ircClient, long samplePeriodMs) {
    this.ircClient = ircClient;
    statisticsByChannel = new ConcurrentHashMap<String, ChannelStatistics>();
    this.samplePeriodMs = samplePeriodMs;
    startPurgeScheduler();
  }

  public void message(String channel, String user) {
    ChannelStatistics statistics = getStatisticsForChannel(channel);
    statistics.message(user);
  }

  public Map<String, ChannelStatistics> getStatisticsByChannel() {
    return new TreeMap<String, ChannelStatistics>(statisticsByChannel);
  }

  private ChannelStatistics getStatisticsForChannel(String channel) {
    ChannelStatistics statistics = statisticsByChannel.get(channel);
    if (statistics == null) {
      statistics = new ChannelStatistics(channel, ircClient, samplePeriodMs);
      ChannelStatistics existingValue = statisticsByChannel.putIfAbsent(channel, statistics);
      if (existingValue != null) {
        statistics = existingValue;
      }
    }
    return statistics;
  }

  private void startPurgeScheduler() {
    final long cutOff = samplePeriodMs * 2;
    TimerTask purgeTask = new TimerTask() {
      @Override
      public void run() {
        for (ChannelStatistics statistics : statisticsByChannel.values()) {
          statistics.purge();
        }
      }
    };
    Timer timer = new Timer("ChannelStatistics-purge", true);
    timer.scheduleAtFixedRate(purgeTask, samplePeriodMs, cutOff);
  }

  @Override
  public String toString() {
    boolean first = true;
    StringBuilder response = new StringBuilder("Channel statistics for the last ");
    response.append(windowSizeInMinutes());
    response.append(" minutes: ");
    for (Entry<String, ChannelStatistics> channel : statisticsByChannel.entrySet()) {
      ChannelStatistics channelStatistics = channel.getValue();
      if (first) {
        first = false;
      } else {
        response.append(", ");
      }
      response.append("[");
      response.append(channelStatistics);
      response.append("]");
    }
    return response.toString();
  }

  private long windowSizeInMinutes() {
    return samplePeriodMs / 1000 / 60;
  }

}

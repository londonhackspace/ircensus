package uk.org.hackspace.ircensus.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.hackspace.ircensus.AbstractIrcHandler;
import uk.org.hackspace.ircensus.IrcClient;
import uk.org.hackspace.ircensus.IrcHandler;

public class ServerStatisticsHandler extends AbstractIrcHandler implements IrcHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ServerStatisticsHandler.class);

  private static final String STATS_COMMAND = "stats";

  private final ServerStatistics statistics;

  public ServerStatisticsHandler(IrcClient ircClient, long samplePeriodMs) {
    super(ircClient);
    statistics = new ServerStatistics(ircClient, samplePeriodMs);
  }

  public ServerStatistics getStatistics() {
    return statistics;
  }

  @Override
  public void onMessage(String channel, String sender, String login, String hostname, String message) {
    if (isMessageFromTrustedUser(channel, sender, message)) {
      LOG.debug("Updated statistics for channel '{}'", channel);
      statistics.message(channel, sender);
    }
  }

  @Override
  public String executeCommand(String method, String[] tokens, String sender) {
    String response = null;
    if (method.equals(STATS_COMMAND)) {
      response = statistics.toString();
      LOG.debug("Asked for stats by {} - responded with: {}", sender, response);
    }
    return response;
  }

  @Override
  public void onJoin(String channel, String sender, String login, String hostname) {
    statistics.join(channel);
    LOG.debug("Informed statistics that we joined channel '{}'", channel);
  }

  private boolean isMessageFromTrustedUser(String channel, String sender, String message) {
    return channel != null && ircClient.isTrustedUser(sender);
  }

}

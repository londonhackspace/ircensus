package uk.org.hackspace.ircensus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ChannelControlHandler extends AbstractIrcHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ChannelControlHandler.class);

  private static final String CHANNELS_COMMAND = "channels";
  private static final String JOIN_COMMAND = "join";
  private static final String LEAVE_COMMAND = "leave";

  public ChannelControlHandler(IrcClient ircClient) {
    super(ircClient);
  }

  @Override
  public String executeCommand(String method, String[] tokens, String sender) {
    String response = null;
    if (JOIN_COMMAND.equals(method)) {
      response = commandJoinChannel(tokens);
    } else if (LEAVE_COMMAND.equals(method)) {
      response = commandLeaveChannel(tokens);
    } else if (CHANNELS_COMMAND.equals(method)) {
      response = commandChannels();
    }
    return response;
  }

  private String commandJoinChannel(String[] tokens) {
    String response;
    if (tokens.length == 2) {
      String channelName = tokens[1];
      LOG.info("Joining channel '{}' ...", channelName);
      ircClient.joinChannel(channelName);
      response = "Joined channel: " + channelName;
      LOG.info(response);
    } else if (tokens.length == 3) {
      String channelName = tokens[1];
      String password = tokens[2];
      LOG.info("Joining locked channel '{}' ...", channelName);
      ircClient.joinChannel(channelName, password);
      response = "Joined channel '" + channelName + "'";
      ;
      LOG.info(response);
    } else {
      response = "Usage: join <channel> [<password>]";
      LOG.debug(response);
    }
    return response;
  }

  private String commandLeaveChannel(String[] tokens) {
    String response;
    if (tokens.length == 2) {
      String channelName = tokens[1];
      LOG.info("Leaving channel '{}' ...", channelName);
      ircClient.partChannel(channelName);
      response = "Left channel '" + channelName + "'";
      LOG.info(response);
    } else {
      response = "Usage: leave <channel>";
      LOG.debug(response);
    }
    return response;
  }

  private String commandChannels() {
    String[] channelNames = ircClient.getChannels();
    StringBuilder response = new StringBuilder("I am in " + channelNames.length + " channels: ");
    for (int i = 0; i < channelNames.length; ++i) {
      response.append(channelNames[i] + " ");
    }
    LOG.debug("Asked for channels - responded with: {}", response);
    return response.toString();
  }

}

package uk.org.hackspace.ircensus;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LifecycleHandler extends AbstractIrcHandler implements IrcHandler {

  private static final Logger LOG = LoggerFactory.getLogger(LifecycleHandler.class);

  private static final String CHANNELS_COMMAND = "channels";
  private static final String EXIT_COMMAND = "exit";

  private final String nickname;

  private final Channel defaultChannel;

  private final String nickNamePassword;

  private final Set<Channel> channels;

  LifecycleHandler(IrcClient ircClient, String nickname, String nickNamePassword, Channel defaultChannel,
      Set<Channel> channels) {
    super(ircClient);
    this.nickname = nickname;
    this.defaultChannel = defaultChannel;
    this.nickNamePassword = nickNamePassword;
    this.channels = channels;
  }

  @Override
  public void onConnect() {
    joinChannels();
    identifyNickName();
    LOG.info("Default channel: {}", defaultChannel.getIdentifier());
  }

  @Override
  public void onDisconnect() {
    while (!ircClient.isConnected()) {
      try {
        ircClient.reconnect();
      } catch (Exception ex) {
        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          break;
        }
      }
    }
  }

  @Override
  public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname,
      String recipientNick, String reason) {
    if (!recipientNick.equals(nickname)) {
      return;
    }
    LOG.warn("We were kicked from channel '{}', by user '{}' for reason: {}", new Object[] { channel, kickerNick,
        reason });
  }

  @Override
  public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
    if (!sourceNick.equals(nickname)) {
      return;
    }
    LOG.info("Exiting due to onQuit()");
    System.exit(-1);
  }

  @Override
  public String executeCommand(String method, String[] tokens, String sender) {
    if (method.equals(CHANNELS_COMMAND)) {
      String[] channelNames = ircClient.getChannels();
      StringBuilder response = new StringBuilder("I am in " + channelNames.length + " channels: ");
      for (int i = 0; i < channelNames.length; ++i) {
        response.append(channelNames[i] + " ");
      }
      LOG.debug("Asked for channels by {} - responded with: {}", sender, response);
      return response.toString();
    }

    if (method.equals(EXIT_COMMAND)) {
      LOG.warn("Exiting on command from {}", sender);
      System.exit(0);
    }
    return null;
  }

  private void joinChannels() {
    for (Channel channel : channels) {
      LOG.debug("Joining channel: {}...", channel.getIdentifier());
      ircClient.joinChannel(channel.getCredentials());
      LOG.info("Joined channel: {}", channel.getIdentifier());
    }
  }

  private void identifyNickName() {
    String nickpass = nickNamePassword;
    if (nickpass != "") {
      ircClient.identify(nickpass);
    }
  }

}

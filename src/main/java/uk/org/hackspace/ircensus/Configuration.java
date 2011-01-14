package uk.org.hackspace.ircensus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {

  private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

  private final String nickName;
  private final Set<Channel> channels;
  private Channel defaultChannel;
  private final long messageDelay;
  private final long samplePeriodMs;
  private final String finger;
  private final String serverAddress;
  private final int serverPort;
  private final String serverPassword;
  private final String nickNamePassword;
  private final int httpPort;

  Configuration(XMLConfiguration xmlConfiguration) {
    nickName = xmlConfiguration.getString("bot.nick");
    channels = getConfiguredChannels(xmlConfiguration);
    messageDelay = xmlConfiguration.getLong("bot.messagedelay", 1000);
    finger = xmlConfiguration.getString("bot.finger", "IrcStats - an IRC statistics gathering bot");
    samplePeriodMs = xmlConfiguration.getLong("bot.sampleperiod", 5 * 60 * 1000);
    serverAddress = xmlConfiguration.getString("server.address");
    serverPort = xmlConfiguration.getInt("server.port", 6667);
    serverPassword = xmlConfiguration.getString("server.password", "");
    nickNamePassword = xmlConfiguration.getString("server.identify", "");
    httpPort = xmlConfiguration.getInt("http.port", 8080);
    LOG.debug(toString());
  }

  @SuppressWarnings("unchecked")
  private Set<Channel> getConfiguredChannels(XMLConfiguration xmlConfiguration) {
    Set<Channel> channels = new HashSet<Channel>();
    List<HierarchicalConfiguration> confChannels = xmlConfiguration.configurationsAt("channels.channel");
    for (HierarchicalConfiguration confChannel : confChannels) {
      String name = confChannel.getString("name");
      String password = confChannel.getString("password", "");
      Channel channel = new Channel(name, password);
      channels.add(channel);
      // first one in the list considered default:
      if (defaultChannel == null) {
        defaultChannel = channel;
      }
    }
    return channels;
  }

  public String getNickName() {
    return nickName;
  }

  public Set<Channel> getChannels() {
    return channels;
  }

  public Channel getDefaultChannel() {
    return defaultChannel;
  }

  public long getMessageDelay() {
    return messageDelay;
  }

  public String getFinger() {
    return finger;
  }

  public long getSamplePeriodMs() {
    return samplePeriodMs;
  }

  public String getServerAddress() {
    return serverAddress;
  }

  public int getServerPort() {
    return serverPort;
  }

  public String getServerPassword() {
    return serverPassword;
  }

  public String getNickNamePassword() {
    return nickNamePassword;
  }

  public int getHttpPort() {
    return httpPort;
  }

  @Override
  public String toString() {
    return "Configuration [channels=" + channels + ", defaultChannel=" + defaultChannel + ", finger=" + finger
        + ", httpPort=" + httpPort + ", messageDelay=" + messageDelay + ", nickName=" + nickName
        + ", nickNamePassword=" + nickNamePassword + ", serverAddress=" + serverAddress + ", serverPassword="
        + serverPassword + ", serverPort=" + serverPort + ", samplePeriodMs=" + samplePeriodMs + "]";
  }

}

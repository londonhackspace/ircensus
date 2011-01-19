package uk.org.hackspace.ircensus;

abstract public class AbstractIrcHandler implements IrcHandler {

  protected final IrcClient ircClient;

  public AbstractIrcHandler(IrcClient ircClient) {
    this.ircClient = ircClient;
  }

  @Override
  public void onConnect() {
  }

  @Override
  public void onDisconnect() {
  }

  @Override
  public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname,
      String recipientNick, String reason) {
  }

  @Override
  public void onMessage(String channel, String sender, String login, String hostname, String message) {
  }

  @Override
  public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
  }

  @Override
  public void onJoin(String channel, String sender, String login, String hostname) {
  }

  @Override
  public String executeCommand(String method, String[] tokens, String sender) {
    return null;
  }

}

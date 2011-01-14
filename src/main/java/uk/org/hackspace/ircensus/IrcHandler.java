package uk.org.hackspace.ircensus;

public interface IrcHandler {

  void onDisconnect();

  void onConnect();

  void onMessage(String channel, String sender, String login, String hostname, String message);

  void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason);

  void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick,
      String reason);

  String executeCommand(String method, String tokens[], String sender);

}

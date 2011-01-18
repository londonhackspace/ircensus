package uk.org.hackspace.ircensus;

public class ChannelControlHandler extends AbstractIrcHandler {

    public ChannelControlHandler(IrcClient ircClient) {
        super(ircClient);
    }

    @Override
    public String executeCommand(String method, String[] tokens, String sender) {
        if ("join".equals(method)) {
            return commandJoinChannel(tokens);
        } else if ("leave".equals(method)) {
            return commandLeaveChannel(tokens);
        } else if ("channels".equals(method)) {
            return commandChannels();
        } else {
            return "Unrecognized command: " + method;
        }
    }

    private String commandJoinChannel(String[] tokens) {
        if (tokens.length == 1) {
            ircClient.joinChannel(tokens[0]);
            return "Joined channel: " + tokens[0];
        } else if (tokens.length == 2) {
            ircClient.joinChannel(tokens[0], tokens[1]);
            return "Joined channel: " + tokens[0];
        } else {
            return "Usage: join <channel> [<password>]";
        }
    }

    private String commandLeaveChannel(String[] tokens) {
        if (tokens.length == 1) {
            ircClient.partChannel(tokens[0]);
            return "Left channel: " + tokens[0];
        } else {
            return "Usage: leave <channel>";
        }
    }

    private String commandChannels() {
        StringBuilder builder = new StringBuilder("Channels: ");
        String[] channels = ircClient.getChannels();
        boolean first = true;
        for (String channel : channels) {
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }
            builder.append(channel);
        }
        return builder.toString();
    }

}

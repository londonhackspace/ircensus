package uk.org.hackspace.ircensus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ChannelControlHandler extends AbstractIrcHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ChannelControlHandler.class);

    public ChannelControlHandler(IrcClient ircClient) {
        super(ircClient);
    }

    @Override
    public String executeCommand(String method, String[] tokens, String sender) {
        String response;
        if ("join".equals(method)) {
            response = commandJoinChannel(tokens);
        } else if ("leave".equals(method)) {
            response = commandLeaveChannel(tokens);
        } else if ("channels".equals(method)) {
            response = commandChannels();
            LOG.debug(response);
        } else {
            response = "Unrecognized command: " + method;
            LOG.debug(response);
        }
        return response;
    }

    private String commandJoinChannel(String[] tokens) {
        String response;
        if (tokens.length == 1) {
            ircClient.joinChannel(tokens[0]);
            response = "Joined channel: " + tokens[0];
            LOG.info(response);
        } else if (tokens.length == 2) {
            ircClient.joinChannel(tokens[0], tokens[1]);
            response = "Joined channel: " + tokens[0];
            LOG.info(response);
        } else {
            response = "Usage: join <channel> [<password>]";
            LOG.debug(response);
        }
        return response;
    }

    private String commandLeaveChannel(String[] tokens) {
        String response;
        if (tokens.length == 1) {
            ircClient.partChannel(tokens[0]);
            response = "Left channel: " + tokens[0];
            LOG.info(response);
        } else {
            response = "Usage: leave <channel>";
            LOG.debug(response);
        }
        return response;
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
        String response = builder.toString();
        return response;
    }

}

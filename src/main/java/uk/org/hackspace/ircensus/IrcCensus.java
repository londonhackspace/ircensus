package uk.org.hackspace.ircensus;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.StringUtils;
import org.mortbay.jetty.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.hackspace.ircensus.http.HttpEndpointHandler;
import uk.org.hackspace.ircensus.stats.ServerStatisticsHandler;


class IrcCensus {

  private static final Logger LOG = LoggerFactory.getLogger(IrcCensus.class);

  private static final String IRCENSUS_CONFIG_FILE_PROP = "ircensus.config.file";

  private IrcClient ircClient;

  private Server server;

  void start() throws Exception {
    String configFile = System.getProperty(IRCENSUS_CONFIG_FILE_PROP);
    if (StringUtils.isEmpty(configFile)) {
      throw new IllegalStateException("No configuration file defined in system property '" + IRCENSUS_CONFIG_FILE_PROP
          + "'");
    }

    XMLConfiguration xml = null;
    try {
      xml = new XMLConfiguration(configFile);
    } catch (ConfigurationException cex) {
      throw new IllegalStateException("Configuration error in: " + configFile, cex);
    }

    Configuration config = new Configuration(xml);

    ircClient = createIrcClient(config);
    LifecycleHandler defaultHandler = createLifecycleHandler(config, ircClient);
    ServerStatisticsHandler statisticsHandler = createStatisticsHandler(config, ircClient);

    List<IrcHandler> handlers = new ArrayList<IrcHandler>();
    handlers.add(defaultHandler);
    handlers.add(statisticsHandler);

    ircClient.setHandlers(handlers);

    server = new Server(config.getHttpPort());
    server.setHandler(new HttpEndpointHandler(statisticsHandler.getStatistics()));

    server.start();
    ircClient.start();
  }

  void stop() {
    try {
      server.stop();
    } catch (Exception e) {
      LOG.error("Error shutting down HTTP server", e);
    }
    ircClient.dispose();
  }

  private ServerStatisticsHandler createStatisticsHandler(Configuration config, IrcClient ircClient) {
    ServerStatisticsHandler statisticsHandler = new ServerStatisticsHandler(ircClient, config.getSamplePeriodMs());
    return statisticsHandler;
  }

  private LifecycleHandler createLifecycleHandler(Configuration config, IrcClient ircClient) {
    LifecycleHandler defaultHandler = new LifecycleHandler(ircClient, config.getNickName(), config
        .getNickNamePassword(), config.getDefaultChannel(), config.getChannels());
    return defaultHandler;
  }

  private IrcClient createIrcClient(Configuration config) throws Exception {
    IrcClient ircClient = new IrcClient(config.getNickName(), config.getDefaultChannel(), config.getServerAddress(),
        config.getServerPort(), config.getServerPassword());
    return ircClient;
  }

}

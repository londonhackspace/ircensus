package uk.org.hackspace.ircensus;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;

public class IrcCensusDaemon implements Daemon {

  private IrcCensus ircCensus;

  @Override
  public void init(DaemonContext context) throws Exception {
    ircCensus = new IrcCensus();
  }

  @Override
  public void start() throws Exception {
    ircCensus.start();
  }

  @Override
  public void stop() throws Exception {
    ircCensus.stop();
  }

  @Override
  public void destroy() {
  }

}

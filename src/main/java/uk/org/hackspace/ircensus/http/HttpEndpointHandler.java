package uk.org.hackspace.ircensus.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.AbstractHandler;

import uk.org.hackspace.ircensus.stats.ChannelStatistics;
import uk.org.hackspace.ircensus.stats.ServerStatistics;

public class HttpEndpointHandler extends AbstractHandler {

  private final ServerStatistics serverStatistics;

  public HttpEndpointHandler(ServerStatistics serverStatistics) {
    this.serverStatistics = serverStatistics;
  }

  @Override
  public void handle(String target, HttpServletRequest request, HttpServletResponse response, int something)
    throws IOException, ServletException {
    response.setContentType("text/plain;charset=utf-8");
    response.setStatus(HttpServletResponse.SC_OK);

    PrintWriter writer = response.getWriter();

    if (target.length() > 1) {
      String channelName = "#" + target.substring(1);
      ChannelStatistics channelStatistics = serverStatistics.getStatisticsByChannel().get(channelName);
      if (channelStatistics != null) {
        writeChannelStatistics(writer, channelName, channelStatistics);
      }
    }

    Request baseRequest;
    if (request instanceof Request) {
      baseRequest = (Request) request;
    } else {
      baseRequest = HttpConnection.getCurrentConnection().getRequest();
    }
    baseRequest.setHandled(true);
  }

  private void writeChannelStatistics(PrintWriter writer, String channelName, ChannelStatistics channelStatistics) {
    writer.print(channelName.substring(1));
    writer.print("Messages:");
    writer.print(channelStatistics.getMessageCount());
    writer.print(" ");
    writer.print(channelName.substring(1));
    writer.print("ActiveUsers:");
    writer.print(channelStatistics.getActiveUserCount());
    writer.print(" ");
    writer.print(channelName.substring(1));
    writer.print("TotalUsers:");
    writer.print(channelStatistics.getTotalUserCount());
  }

}

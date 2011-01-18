ircensus is loosely based on the code for irccat:

  https://github.com/RJ/irccat

Notes:
  You may need to add pircbot into your local maven repository by hand

Building:
  mvn dependency:copy-dependencies install

Installing:
  sudo dpkg -i target/ircensus_0.0.1+SNAPSHOT.deb

Configure:
  vi /etc/ircensus/ircensus.xml

Running:
  sudo /etc/init.d/ircensus start|stop

Monitoring:
  tail -f /var/log/ircensus/ircensus.log

Elliot


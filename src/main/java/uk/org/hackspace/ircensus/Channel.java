package uk.org.hackspace.ircensus;

class Channel {

  private final String name;

  private final String password;

  Channel(String name) {
    this(name, null);
  }

  Channel(String name, String password) {
    this.name = name;
    this.password = password;
  }

  String getName() {
    return name;
  }

  String getPassword() {
    return password;
  }

  String getIdentifier() {
    return "#" + getName();
  }

  String getCredentials() {
    return getIdentifier() + " " + getPassword();
  }

  @Override
  public String toString() {
    return getIdentifier();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (name == null ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Channel)) {
      return false;
    }
    Channel other = (Channel) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

}

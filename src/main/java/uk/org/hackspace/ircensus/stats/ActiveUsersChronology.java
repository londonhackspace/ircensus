package uk.org.hackspace.ircensus.stats;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ActiveUsersChronology extends AbstractChronology<Set<String>> {

  ActiveUsersChronology(int bucketSizeMs) {
    super(bucketSizeMs);
  }

  void markUserAsActive(String user) {
    Set<String> users = getCurrentBucket();
    users.add(user);
  }

  long getActiveUserCountForInterval(long periodMs) {
    Set<String> allUsers = new HashSet<String>();
    List<Set<String>> userData = getDataForInterval(periodMs);
    for (Set<String> users : userData) {
      allUsers.addAll(users);
    }
    return allUsers.size();
  }

  @Override
  Set<String> newBucketInstance() {
    return new HashSet<String>();
  }

}

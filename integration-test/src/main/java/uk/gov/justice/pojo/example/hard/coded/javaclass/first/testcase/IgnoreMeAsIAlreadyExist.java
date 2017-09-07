package uk.gov.justice.pojo.example.hard.coded.javaclass.first.testcase;

import java.util.Optional;

@SuppressWarnings("unused")
public class IgnoreMeAsIAlreadyExist {

  private final Optional<String> badgeNumber;
  private final Optional<String> personId;
  private final Optional<String> rank;

  public IgnoreMeAsIAlreadyExist(final Optional<String> badgeNumber, final Optional<String> personId, final Optional<String> rank) {
    this.badgeNumber = badgeNumber;
    this.personId = personId;
    this.rank = rank;
  }

  public Optional<String> getBadgeNumber() {
    return badgeNumber;
  }

  public Optional<String> getPersonId() {
    return personId;
  }

  public Optional<String> getRank() {
    return rank;
  }

  public static Builder ignoreMeAsIAlreadyExist() {
    return new Builder();
  }

  public static class Builder {

    private Optional<String> badgeNumber;
    private Optional<String> personId;
    private Optional<String> rank;

    public Builder withBadgeNumber(final Optional<String> badgeNumber) {
      this.badgeNumber = badgeNumber;
      return this;
    }

    public Builder withPersonId(final Optional<String> personId) {
      this.personId = personId;
      return this;
    }

    public Builder withRank(final Optional<String> rank) {
      this.rank = rank;
      return this;
    }

    public IgnoreMeAsIAlreadyExist build() {
      return new IgnoreMeAsIAlreadyExist(badgeNumber, personId, rank);
    }
  }
}

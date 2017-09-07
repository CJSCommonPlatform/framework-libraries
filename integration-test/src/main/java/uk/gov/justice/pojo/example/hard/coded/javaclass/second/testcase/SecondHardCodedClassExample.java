package uk.gov.justice.pojo.example.hard.coded.javaclass.second.testcase;

@SuppressWarnings("unused")
public class SecondHardCodedClassExample {

  private final String id;
  private final String urn;

  public SecondHardCodedClassExample(final String id, final String urn) {
    this.id = id;
    this.urn = urn;
  }

  public String getId() {
    return id;
  }

  public String getUrn() {
    return urn;
  }

  public static Builder secondHardCodedClassExample() {
    return new SecondHardCodedClassExample.Builder();
  }

  public static class Builder {
    private String id;

    private String urn;

    public Builder withId(final String id) {
      this.id = id;
      return this;
    }

    public Builder withUrn(final String urn) {
      this.urn = urn;
      return this;
    }

    public SecondHardCodedClassExample build() {
      return new SecondHardCodedClassExample(id, urn);
    }
  }
}

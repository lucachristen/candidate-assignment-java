package ch.aaap.assignment.model.impl;

import ch.aaap.assignment.model.PoliticalCommunity;
import ch.aaap.assignment.model.PostalCommunity;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class PostalCommunityImpl implements PostalCommunity {

  private final @NonNull String zipCode;
  private final @NonNull String zipCodeAddition;
  private final @NonNull String name;
  private final @NonNull Set<PoliticalCommunity> politicalCommunities;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostalCommunityImpl that = (PostalCommunityImpl) o;
    return zipCode.equals(that.zipCode) &&
        zipCodeAddition.equals(that.zipCodeAddition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zipCode, zipCodeAddition);
  }

}

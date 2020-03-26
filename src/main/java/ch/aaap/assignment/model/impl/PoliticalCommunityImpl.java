package ch.aaap.assignment.model.impl;

import ch.aaap.assignment.model.District;
import ch.aaap.assignment.model.PoliticalCommunity;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class PoliticalCommunityImpl implements PoliticalCommunity {

  private final @NonNull String number;
  private final @NonNull String name;
  private final @NonNull String shortName;
  private final @NonNull LocalDate lastUpdate;
  private final @NonNull District district;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PoliticalCommunityImpl that = (PoliticalCommunityImpl) o;
    return number.equals(that.number);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number);
  }

}

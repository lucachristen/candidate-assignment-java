package ch.aaap.assignment.model.impl;

import ch.aaap.assignment.model.Canton;
import ch.aaap.assignment.model.District;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class DistrictImpl implements District {

  private final @NonNull String number;
  private final @NonNull String name;
  private final @NonNull Canton canton;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DistrictImpl district = (DistrictImpl) o;
    return number.equals(district.number);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number);
  }

}

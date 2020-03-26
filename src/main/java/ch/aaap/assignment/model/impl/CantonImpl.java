package ch.aaap.assignment.model.impl;

import ch.aaap.assignment.model.Canton;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class CantonImpl implements Canton {

  private final @NonNull String code;
  private final @NonNull String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CantonImpl canton = (CantonImpl) o;
    return code.equals(canton.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

}

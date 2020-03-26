package ch.aaap.assignment.model.impl;

import ch.aaap.assignment.model.Canton;
import ch.aaap.assignment.model.District;
import ch.aaap.assignment.model.Model;
import ch.aaap.assignment.model.PoliticalCommunity;
import ch.aaap.assignment.model.PostalCommunity;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class ModelImpl implements Model {

  private final @NonNull Set<PoliticalCommunity> politicalCommunities;
  private final @NonNull Set<PostalCommunity> postalCommunities;
  private final @NonNull Set<Canton> cantons;
  private final @NonNull Set<District> districts;

}

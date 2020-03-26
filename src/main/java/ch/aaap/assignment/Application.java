package ch.aaap.assignment;

import ch.aaap.assignment.model.Canton;
import ch.aaap.assignment.model.District;
import ch.aaap.assignment.model.Model;
import ch.aaap.assignment.model.PoliticalCommunity;
import ch.aaap.assignment.model.PostalCommunity;
import ch.aaap.assignment.model.impl.CantonImpl;
import ch.aaap.assignment.model.impl.DistrictImpl;
import ch.aaap.assignment.model.impl.ModelImpl;
import ch.aaap.assignment.model.impl.PoliticalCommunityImpl;
import ch.aaap.assignment.model.impl.PostalCommunityImpl;
import ch.aaap.assignment.raw.CSVPoliticalCommunity;
import ch.aaap.assignment.raw.CSVPostalCommunity;
import ch.aaap.assignment.raw.CSVUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Application {

  private Model model = null;

  public Application() {
    initModel();
  }

  public static void main(String[] args) {
    new Application();
  }

  /**
   * Reads the CSVs and initializes a in memory model
   */
  private void initModel() {
    Set<CSVPoliticalCommunity> csvPoliticalCommunities = CSVUtil.getPoliticalCommunities();
    Set<CSVPostalCommunity> csvPostalCommunities = CSVUtil.getPostalCommunities();

    Map<String, Canton> cantons = buildCantons(csvPoliticalCommunities);
    Map<String, District> districts = buildDistricts(csvPoliticalCommunities, cantons);
    Map<String, PoliticalCommunity> politicalCommunities = buildPoliticalCommunities(
        csvPoliticalCommunities, districts);
    Map<String, PostalCommunity> postalCommunities = buildPostalCommunities(csvPostalCommunities,
        politicalCommunities);

    model = new ModelImpl(
        politicalCommunities.values().stream().collect(Collectors.toUnmodifiableSet()),
        postalCommunities.values().stream().collect(Collectors.toUnmodifiableSet()),
        cantons.values().stream().collect(Collectors.toUnmodifiableSet()),
        districts.values().stream().collect(Collectors.toUnmodifiableSet()));
  }

  /**
   * Returns a map of all cantons contained in {@code csvPoliticalCommunities}
   *
   * @param csvPoliticalCommunities set of political communities from a CSV file
   * @return map of cantons where the canton code is the key
   */
  private Map<String, Canton> buildCantons(Set<CSVPoliticalCommunity> csvPoliticalCommunities) {
    return csvPoliticalCommunities.stream()
        .map(politicalCommunity -> new CantonImpl(
            politicalCommunity.getCantonCode(),
            politicalCommunity.getCantonName()))
        .distinct()
        .collect(Collectors.toMap(CantonImpl::getCode, Function.identity()));
  }

  /**
   * Returns a map of all districts contained in {@code csvPoliticalCommunities}
   *
   * @param csvPoliticalCommunities set of political communities from a CSV file
   * @param cantons                 map of cantons
   * @return map of districts where the district code is the key
   */
  private Map<String, District> buildDistricts(Set<CSVPoliticalCommunity> csvPoliticalCommunities,
      Map<String, Canton> cantons) {
    return csvPoliticalCommunities.stream()
        .map(politicalCommunity -> new DistrictImpl(
            politicalCommunity.getDistrictNumber(),
            politicalCommunity.getDistrictName(),
            cantons.get(politicalCommunity.getCantonCode())))
        .distinct()
        .collect(Collectors.toMap(District::getNumber, Function.identity()));
  }

  /**
   * Returns a map of all political communities contained in {@code csvPoliticalCommunities}
   *
   * @param csvPoliticalCommunities set of political communities from a CSV file
   * @param districts               map of districts
   * @return map of political communities where the political community number is the key
   */
  private Map<String, PoliticalCommunity> buildPoliticalCommunities(
      Set<CSVPoliticalCommunity> csvPoliticalCommunities, Map<String, District> districts) {
    return csvPoliticalCommunities.stream()
        .map(politicalCommunity -> new PoliticalCommunityImpl(
            politicalCommunity.getNumber(),
            politicalCommunity.getName(),
            politicalCommunity.getShortName(),
            politicalCommunity.getLastUpdate(),
            districts.get(politicalCommunity.getDistrictNumber())))
        .distinct()
        .collect(Collectors.toMap(PoliticalCommunity::getNumber, Function.identity()));
  }

  /**
   * Returns a map of all postal communities contained in {@code csvPostalCommunities}
   *
   * @param csvPostalCommunities set of postal communities from a CSV file
   * @param politicalCommunities map of political communities
   * @return map of postal communities where the key is the zip code with its addition
   */
  private Map<String, PostalCommunity> buildPostalCommunities(
      Set<CSVPostalCommunity> csvPostalCommunities,
      Map<String, PoliticalCommunity> politicalCommunities) {
    Map<String, List<CSVPostalCommunity>> groupedPostalCommunities = csvPostalCommunities.stream()
        .collect(Collectors.groupingBy(
            csvPostalCommunity -> csvPostalCommunity.getZipCode() + csvPostalCommunity
                .getZipCodeAddition()));

    return groupedPostalCommunities.values()
        .stream()
        .map(postalCommunities -> new PostalCommunityImpl(
            postalCommunities.get(0).getZipCode(),
            postalCommunities.get(0).getZipCodeAddition(),
            postalCommunities.get(0).getName(),
            postalCommunities.stream().map(postalCommunity -> politicalCommunities
                .get(postalCommunity.getPoliticalCommunityNumber())).collect(
                Collectors.toUnmodifiableSet())
        ))
        .collect(Collectors.toMap(
            postalCommunity -> postalCommunity.getZipCode() + postalCommunity.getZipCodeAddition(),
            Function.identity()));
  }

  /**
   * @return model
   */
  public Model getModel() {
    return model;
  }

  /**
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of political communities in given canton
   */
  public long getAmountOfPoliticalCommunitiesInCanton(String cantonCode)
      throws IllegalArgumentException {
    if (model.getCantons().stream().noneMatch(canton -> canton.getCode().equals(cantonCode))) {
      throw new IllegalArgumentException();
    }

    return model.getPoliticalCommunities().stream()
        .filter(
            politicalCommunity -> politicalCommunity.getDistrict().getCanton().getCode()
                .equals(cantonCode))
        .count();
  }

  /**
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of districts in given canton
   */
  public long getAmountOfDistrictsInCanton(String cantonCode) throws IllegalArgumentException {
    if (model.getCantons().stream().noneMatch(canton -> canton.getCode().equals(cantonCode))) {
      throw new IllegalArgumentException();
    }

    return model.getDistricts().stream()
        .filter(district -> district.getCanton().getCode().equals(cantonCode))
        .count();
  }

  /**
   * @param districtNumber of a district (e.g. 101)
   * @return amount of districts in given canton
   */
  public long getAmountOfPoliticalCommunitiesInDistict(String districtNumber)
      throws IllegalArgumentException {
    if (model.getDistricts().stream()
        .noneMatch(district -> district.getNumber().equals(districtNumber))) {
      throw new IllegalArgumentException();
    }

    return model.getPoliticalCommunities().stream()
        .filter(politicalCommunity -> politicalCommunity.getDistrict().getNumber()
            .equals(districtNumber))
        .count();
  }

  /**
   * @param zipCode 4 digit zip code
   * @return district that belongs to specified zip code
   */
  public Set<String> getDistrictsForZipCode(String zipCode) {
    return model.getPostalCommunities().stream()
        .filter(postalCommunity -> postalCommunity.getZipCode().equals(zipCode))
        .flatMap(postalCommunity -> postalCommunity.getPoliticalCommunities().stream()
            .map(politicalCommunity -> politicalCommunity.getDistrict().getName()))
        .collect(Collectors.toSet());
  }

  /**
   * @param postalCommunityName name
   * @return lastUpdate of the political community by a given postal community name
   */
  public LocalDate getLastUpdateOfPoliticalCommunityByPostalCommunityName(
      String postalCommunityName) throws IllegalArgumentException {
    return model.getPostalCommunities().stream()
        .filter(postalCommunity -> postalCommunity.getName().equals(postalCommunityName))
        .flatMap(postalCommunity -> postalCommunity.getPoliticalCommunities().stream()
            .map(politicalCommunity -> politicalCommunity.getLastUpdate()))
        .max(LocalDate::compareTo)
        .orElseThrow(IllegalArgumentException::new);
  }

  /**
   * https://de.wikipedia.org/wiki/Kanton_(Schweiz)
   *
   * @return amount of canton
   */
  public long getAmountOfCantons() {
    return model.getCantons().size();
  }

  /**
   * https://de.wikipedia.org/wiki/Kommunanz
   *
   * @return amount of political communities without postal communities
   */
  public long getAmountOfPoliticalCommunityWithoutPostalCommunities() {
    long politicalCommunityWithPostalCommunitiesCount = model.getPostalCommunities().stream()
        .flatMap(postalCommunity -> postalCommunity.getPoliticalCommunities().stream())
        .distinct()
        .count();

    return model.getPoliticalCommunities().size()
        - politicalCommunityWithPostalCommunitiesCount;
  }
}

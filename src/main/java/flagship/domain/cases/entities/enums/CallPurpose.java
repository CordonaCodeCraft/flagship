package flagship.domain.cases.entities.enums;

public enum CallPurpose {
  LOADING("Loading"),
  UNLOADING("Unloading"),
  LOADING_AND_UNLOADING("Loading and unloading"),
  RESUPPLY("Supply"),
  RECRUITMENT("Crew change"),
  POSTAL("Postal"),
  REPAIR("Repair"),
  SPECIAL_PURPOSE_PORT_VISIT("Ship repair yard");

  public final String type;

  CallPurpose(String name) {
    this.type = name;
  }
}

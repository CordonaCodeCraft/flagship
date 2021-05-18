package flagship.domain.cases.entities.enums;

public enum ShipType {

  BULK_CARRIER("Bulk carrier"),
  REEFER("Reefer vessel"),
  CONTAINER("Container vessel"),
  PASSENGER("Passenger vessel"),
  RECREATIONAL("Recreational vessel"),
  OIL_TANKER("Oil tanker"),
  NAVY("Navy vessel"),
  WORK_SHIP("Work ship");

  public final String type;

  ShipType(String name) {
    this.type = name;
  }
}

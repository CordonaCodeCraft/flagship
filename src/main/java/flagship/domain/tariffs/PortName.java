package flagship.domain.tariffs;

public enum PortName {
  VARNA_EAST("Varna East"),
  PETROL("Petrol"),
  BULYARD("Bulyard"),
  BULPORT_LOGISTIK("Bulport Logistik"),
  SRY("SRY"),
  PCHMV("PCHMV"),
  TEC_POWER_STATION("TEC (Power station)"),
  BALCHIK_PORT("Balchik port"),
  LESPORT("Lesport"),
  TEREM_FA("Terem FA"),
  SRY_DOLPHIN("SRY Dolphin"),
  TRANSSTROI_VARNA("Transstroi Varna"),
  ODESSOS_PBM("Odessos PBM"),
  BUOY_9("Buoy 9"),
  ANCHORAGE("Anchorage"),
  VARNA_WEST("Varna West"),
  FERRY_COMPLEX("Ferry Complex"),
  BOURGAS_CENTER("Bourgas – Center"),
  BOURGAS_EAST_2("Bourgas East 2"),
  BMF_PORT_BOURGAS("BMF Port Bourgas"),
  BOURGAS_WEST_TERMINAL("Bourgas West Terminal"),
  SRY_PORT_BOURGAS("Ship Repair Yard Port Bourgas"),
  PORT_BULGARIA_WEST("Port Bulgaria West"),
  BOURGAS_SHIPYARD("Bourgas Shipyard"),
  PORT_EUROPA("Port Europa"),
  TRANSSTROI_BOURGAS("Transstroi Bourgas"),
  PORT_ROSENETZ("Port Rosenetz"),
  NESSEBAR("Nessebar"),
  POMORIE("Pomorie"),
  SOZOPOL("Sozopol"),
  TZAREVO("Tzarevo"),
  SHIFTING_ANCHORAGE_AREA("Shifting of the anchorage area"),
  DEVIATION("Deviation"),
  XX_A_K_M("XX A.k.m"),
  XX_B_K_M("XX B.k.m"),
  SRY_ODESSOS("SRY Odessos"),
  MTG_DOLPHIN("MTG Dolphin"),
  SHIFTING_BULYARD("Shifting in Bulyard"),
  SHIFTING_SRY_ODESSOS("Shifting in SRY Odessos"),
  SHIFTING_MTG_DOLPHIN("Shifting in MTG Dolphin"),
  SHIFTING_TEREM_FA("Shifting in Terem FA"),
  TEC_EZEROVO("TEC Ezerovo");

  public final String name;

  PortName(String name) {
    this.name = name;
  }
}

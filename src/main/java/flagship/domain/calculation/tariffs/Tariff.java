package flagship.domain.calculation.tariffs;

import java.io.Serializable;

public abstract class Tariff implements Serializable {

  public static final Integer MIN_GT = 150;
  public static final Integer MAX_GT = 650000;

  private static final long serialVersionUID = 7488308811075008968L;
}

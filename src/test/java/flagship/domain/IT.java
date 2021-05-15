package flagship.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IT {

    @Autowired PdaComposer pdaComposer;

  @Test
  void name() {

    System.out.println();
  }

}

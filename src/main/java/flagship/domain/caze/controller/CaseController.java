package flagship.domain.caze.controller;

import flagship.domain.calculation.tariffs.TariffsFactory;
import flagship.domain.caze.composer.PdaCaseComposer;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.caze.model.createrequest.CreateCaseRequest;
import flagship.domain.caze.service.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(CaseController.PATH)
@RequiredArgsConstructor
public class CaseController {

  public static final String PATH = "api/v1/cases/";
  private final CaseService caseService;
  private final TariffsFactory tariffsFactory;

  @GetMapping("create")
  @ResponseStatus(HttpStatus.CREATED)
  public PdaCase createCase(@RequestBody final CreateCaseRequest input) {

    final PdaCase pdaCase = PdaCaseComposer.composeFrom(input, tariffsFactory);

    System.out.println();

    return pdaCase;
  }
}

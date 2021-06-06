package flagship.domain.country.service.impl;

import flagship.domain.country.entity.Country;
import flagship.domain.country.repository.CountryRepository;
import flagship.domain.country.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryServiceImpl implements CountryService {

  private final CountryRepository countryRepository;

  @Override
  public Country create(Country target) {
    return null;
  }

  @Override
  public Country update(Country target) {
    return null;
  }

  @Override
  public Country delete(Country target) {
    return null;
  }

  @Override
  public Optional<Country> findById(UUID uuid) {
    return Optional.empty();
  }
}

package flagship.persistence.services.implementations;

import flagship.domain.entities.Country;
import flagship.persistence.repositories.CountryRepository;
import flagship.persistence.services.CountryService;
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

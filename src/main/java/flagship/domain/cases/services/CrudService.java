package flagship.domain.cases.services;

import java.util.Optional;

public interface CrudService<T, ID> {

  T create(T target);

  T update(T target);

  T delete(T target);

  Optional<T> findById(ID id);
}

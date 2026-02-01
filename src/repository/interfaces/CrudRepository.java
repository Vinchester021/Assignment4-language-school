package repository.interfaces;

import java.util.List;

public interface CrudRepository<T> {
    T create(T t);
    List<T> getAll();
    T getById(int id);
    void update(int id, T t);
    void delete(int id);
}

package ru.javawebinar.topjava.repository.jdbc;

import java.time.LocalDateTime;

/**
 * Created by admin on 10/11/2016.
 */
public interface DbFieldConverter<T> {
    T fromDateTime(LocalDateTime dateTime);
}

package com.liyaqing.download.database;

public class DatabaseUtils {
    public interface DataCallback<T> {
        void dataSuccess(T t);

        void dataError(Throwable throwable);
    }
}

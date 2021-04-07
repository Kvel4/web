package ru.itmo.wp.model.repository.impl;

import freemarker.template.utility.ClassUtil;
import ru.itmo.wp.model.database.DatabaseUtils;
import ru.itmo.wp.model.domain.Entity;
import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.exception.RepositoryException;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract class BasicRepositoryImpl<T extends Entity> {
    private final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();
    private Class<T> clazz;

    BasicRepositoryImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    T findSingle(String query, List<Object> parameters) {
        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setParameters(statement, parameters);
            try (ResultSet resultSet = statement.executeQuery()) {
                return toClass(statement.getMetaData(), resultSet);
            }
        } catch (SQLException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RepositoryException("Can't find " + clazz.getSimpleName(), e);
        }
    }

    List<T> findMany(String query, List<Object> parameters) {
        List<T> all = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setParameters(statement, parameters);
            try (ResultSet resultSet = statement.executeQuery()) {
                T t;
                while ((t = toClass(statement.getMetaData(), resultSet)) != null) {
                    all.add(t);
                }
            }
        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RepositoryException("Can't find " + clazz.getSimpleName(), e);
        }
        return all;
    }

    void save(String query, T t, List<Object> parameters) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                setParameters(statement, parameters);
                if (statement.executeUpdate() != 1) {
                    throw new RepositoryException("Can't save " + t.getClass().getSimpleName() + ".");
                } else {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        t.setId(generatedKeys.getLong(1));
                        t.setCreationTime(findSingle(
                                "SELECT * FROM " + t.getClass().getSimpleName() + " WHERE id=?",
                                List.of(t.getId())).getCreationTime());
                    } else {
                        throw new RepositoryException("Can't save " + t.getClass().getSimpleName() + " [no autogenerated fields].");
                    }
                }
            }
        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RepositoryException("Can't save " + t.getClass().getSimpleName(), e);
        }
    }

    private T toClass(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }
        final Map<String, String> notMatchingTypes = Map.of("java.sql.Timestamp", "java.util.Date");
        T clazzInstance;

        try {
            clazzInstance = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RepositoryException("Cant convert to Class " + clazz.getName(), e);
        }


        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnClassName = metaData.getColumnClassName(i);
            if (notMatchingTypes.containsKey(columnClassName)) {
                columnClassName = notMatchingTypes.get(columnClassName);
            }
            String[] splitedColumnClassName = columnClassName.split("\\.");
            String columnClassSimpleName = splitedColumnClassName[splitedColumnClassName.length - 1];

            // id, login
            String columnName = capitalize(metaData.getColumnName(i));

            //setId setLogin
            String clazzInstanceMethodName = "set" + columnName;
            //getLong getString
            String resultSetMethodName = "get" + columnClassSimpleName;

            try {
                Method clazzInstanceMethod;
                try {
                    clazzInstanceMethod = clazz.getMethod(clazzInstanceMethodName, Class.forName(columnClassName));
                } catch (NoSuchMethodException e) {
                    clazzInstanceMethod = clazz.getMethod(
                            clazzInstanceMethodName, ClassUtil.boxingClassToPrimitiveClass(Class.forName(columnClassName)));
                }
                Method resultSetMethod = resultSet.getClass().getMethod(resultSetMethodName, int.class);
                clazzInstanceMethod.invoke(clazzInstance, resultSetMethod.invoke(resultSet, i));
            } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                throw new RepositoryException("Cant convert to Class " + clazz.getName(), e);
            } catch (NoSuchMethodException ignored) {
                // No operations.
            }
        }
        return clazzInstance;
    }

    private void setParameters(PreparedStatement statement, List<Object> parameters)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (int i = 0; i < parameters.size(); i++) {
            Class parameterClass = getParameterClass(parameters.get(i));
            String statementMethodName = "set" + capitalize(parameterClass.getSimpleName());

            Method statementMethod;
            try {
                statementMethod = statement.getClass().getMethod(statementMethodName, int.class, parameterClass);
            } catch (NoSuchMethodException e) {
                statementMethod = statement.getClass().getMethod(
                        statementMethodName, int.class, ClassUtil.boxingClassToPrimitiveClass(parameterClass));
            }
            statementMethod.invoke(statement, i + 1, parameterClass.cast(parameters.get(i)));
        }
    }

    private Class getParameterClass(Object object) {
        if (object instanceof Long) {
            return Long.class;
        } else if (object instanceof Integer) {
            return Integer.class;
        } else if (object instanceof String) {
            return String.class;
        } else if (object instanceof Date) {
            return Date.class;
        }
        throw new RepositoryException("Cant assign parameter's class");
    }

    private String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}

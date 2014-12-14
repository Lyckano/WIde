
/*
 * Copyright (c) 2013 - 2014 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.core.framework.storage.server.builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ObservableValue;

import com.github.naios.wide.core.framework.storage.server.AliasUtil;
import com.github.naios.wide.core.framework.storage.server.EnumAlias;
import com.github.naios.wide.core.framework.storage.server.NameAlias;
import com.github.naios.wide.core.framework.storage.server.ServerStorageChangeHolder;
import com.github.naios.wide.core.framework.storage.server.ServerStorageStructure;
import com.github.naios.wide.core.framework.storage.server.helper.ObservableValueStorageInfo;
import com.github.naios.wide.core.framework.storage.server.types.EnumProperty;
import com.github.naios.wide.core.framework.storage.server.types.FlagProperty;
import com.github.naios.wide.core.framework.util.CrossIterator;
import com.github.naios.wide.core.framework.util.FlagUtil;
import com.github.naios.wide.core.framework.util.FormatterWrapper;
import com.github.naios.wide.core.framework.util.Pair;
import com.github.naios.wide.core.framework.util.StringUtil;

public class SQLMaker
{
    protected static final String DELEMITER = ";";

    protected static final String SPACE = " ";

    protected static final String COMMA = ",";

    protected static final String WHERE = "WHERE";

    protected static final String UPDATE = "UPDATE";

    protected static final String DELETE = "DELETE";

    protected static final String INSERT = "INSERT";

    protected static final String INTO = "INTO";

    protected static final String VALUES = "VALUES";

    protected static final String FROM = "FROM";

    protected static final String SET = "SET";

    protected static final String OR = "OR";

    protected static final String AND = "AND";

    protected static final String IN = "IN";

    protected static final String ASSIGN = ":=";

    protected static final String EQUAL = "=";

    protected static final String NAME_ENCLOSURE = "`";

    protected static final String FLAG_DELEMITER = " | ";

    protected static final String NEWLINE = "\n";

    /**
     * Adds the delemiter to a query
     */
    protected static String addDelemiter(final String query)
    {
        return query + DELEMITER;
    }

    /**
     * Creates sql single or multiline comments
     */
    public static String createComment(final String text)
    {
        if (text.contains(NEWLINE))
            return "/*\n * " + text.replaceAll(NEWLINE, "\n * ") + "\n */";
        else
            return "-- " + text;
    }

    /**
     * Creates a table name
     */
    protected static String createName(final String table)
    {
        return NAME_ENCLOSURE + table + NAME_ENCLOSURE;
    }

    protected static String createName(final Field field)
    {
        return createName(field.getName());
    }

    protected static String createVariableFormat(final int varNameMaxLength, final int varValueMaxLength)
    {
        return " %-" + varNameMaxLength + "s := %" + varValueMaxLength + "s";
    }

    /**
     * Creates a sql variable.
     * @param varNameMaxLength
     */
    protected static String createVariable(final String format, final String name, final String value)
    {
        return addDelemiter(SET + String.format(format, name, value));
    }

    /**
     * Creates a sql in clause.
     */
    protected static String createInClause(final Field field, final String query)
    {
        return createName(field) + SPACE + IN + "(" + query + ")";
    }

    /**
     * Creates name equals value clause.
     */
    protected static String createNameEqualsName(final String name, final String value)
    {
        return StringUtil.fillWithSpaces(name, EQUAL, value);
    }

    private static ObservableValue<?> getObservableValueByFieldAndStructure(final Field field, final ServerStorageStructure structure)
    {
        final ObservableValue<?> value;
        try
        {
            if (!field.isAccessible())
                field.setAccessible(true);

            return (ObservableValue<?>)field.get(structure);

        } catch (final Exception e)
        {
            return null;
        }
    }

    /**
     * Creates field equals value clause.
     */
    protected static String createFieldEqualsValue(final SQLVariableHolder vars, final ServerStorageChangeHolder changeHolder,
            final Field field, final ObservableValue<?> value, final boolean variablize)
    {
        return createNameEqualsName(createName(field), createValueOfObservableValue(vars, changeHolder, field, value, variablize));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static String createValueOfObservableValue(final SQLVariableHolder vars, final ServerStorageChangeHolder changeHolder, final Field field, @SuppressWarnings("rawtypes") final ObservableValue value, final boolean variablize)
    {
        if (variablize)
        {
            // If the observable has a custom var use it
            final String customVar = changeHolder.getCustomVariable(value);
            if (customVar != null)
                return vars.addVariable(customVar, value.getValue());

            // Enum alias
            if (field.isAnnotationPresent(EnumAlias.class))
            {
                final Class<? extends Enum> enumeration = AliasUtil.getEnum(field);

                // Enum Property (Absolute value)
                if (value instanceof EnumProperty)
                    return vars.addVariable(enumeration.getEnumConstants()[(int)value.getValue()].name(), value.getValue());
                // Flag Property (Relative value)
                else if (value instanceof FlagProperty)
                {
                    // FlagProperties only occur in set statements
                    final int currentFlagValue = ((FlagProperty) value).get();
                    final int oldFlagValue;
                    final Object oldValue = changeHolder.getValueAtOrigin(value);
                    if (oldValue == null || !(oldValue instanceof Integer))
                        oldFlagValue = 0;
                    else
                        oldFlagValue = (int) oldValue;

                    // Get flag values of flags in database and in the current observable
                    final List<? extends Enum> currentFlags = FlagUtil.getFlagList(enumeration, currentFlagValue);
                    final List<? extends Enum> oldFlags = FlagUtil.getFlagList(enumeration, oldFlagValue);

                    // Now we calculate the difference
                    // Add Flags:
                    final List<Enum> addFlags = new ArrayList<>();
                    addFlags.addAll(currentFlags);
                    addFlags.removeAll(oldFlags);

                    // Remove Flags:
                    final List<Enum> removeFlags = new ArrayList<>();
                    removeFlags.addAll(oldFlags);
                    removeFlags.removeAll(currentFlags);

                    if ((!addFlags.isEmpty()) || (!removeFlags.isEmpty()))
                    {
                        final StringBuilder builder = new StringBuilder();

                        if (!removeFlags.isEmpty())
                            builder.append("(");

                        if (!oldFlags.isEmpty())
                            builder.append(createName(field));

                        if (!removeFlags.isEmpty())
                        {
                            builder.append(" &~ (");

                            builder.append(concatFlags(vars, removeFlags));

                            builder.append("))");
                        }

                        if (!addFlags.isEmpty())
                        {
                            if (!oldFlags.isEmpty())
                                builder.append(FLAG_DELEMITER);

                            builder.append(concatFlags(vars, addFlags));
                        }

                        return builder.toString();
                    }
                    else if (currentFlags.isEmpty())
                        return String.valueOf(FlagUtil.DEFAULT_VALUE);
                    else // If Values are not different
                        return concatFlags(vars, currentFlags);
                }
            }
            // Namestorage alias
            else if ((value instanceof ReadOnlyIntegerProperty) && field.isAnnotationPresent(NameAlias.class))
            {
                final String name = AliasUtil.getNamstorageEntry(field, (int)value.getValue());
                if (name != null)
                    return vars.addVariable(name, value.getValue());
            }
        }

        return new FormatterWrapper(value.getValue(), FormatterWrapper.Options.NO_FLOAT_DOUBLE_POSTFIX).toString();
    }

    /**
     * Helper to concat a list of flags as variables
     */
    @SuppressWarnings({ "rawtypes" })
    private static String concatFlags(final SQLVariableHolder vars, final List<? extends Enum> currentFlags)
    {
        return StringUtil.concat(FLAG_DELEMITER, new Iterator<String>()
        {
            int i = 0;

            @Override
            public boolean hasNext()
            {
                return i < currentFlags.size();
            }

            @Override
            public String next()
            {
                return vars.addVariable(currentFlags.get(i).name(),
                        StringUtil.asHex(FlagUtil.createFlag(currentFlags.get(i++))));
            }
        });
    }

    /**
     * creates the key part of an structure
     */
    protected static String createKeyPart(final SQLVariableHolder vars, final ServerStorageChangeHolder changeHolder, final ServerStorageStructure... structures)
    {
        if (structures.length == 0)
            return "";

        final List<Field> keys = structures[0].getPrimaryFields();

        // If only 1 primary key exists its possible to use IN clauses
        // otherwise we use nested AND/ OR clauses
        if (keys.size() == 1 && (structures.length > 1))
        {
            return createInClause(keys.get(0), StringUtil.concat(COMMA + SPACE,
                            new Iterator<String>()
                            {
                                int i = 0;

                                @Override
                                public boolean hasNext()
                                {
                                    return i < structures.length;
                                }

                                @Override
                                public String next()
                                {
                                    return createValueOfObservableValue(vars, changeHolder, keys.get(0),
                                            getObservableValueByFieldAndStructure(keys.get(0), structures[i++]), true);
                                }
                            }));
        }
        else
        {
            // Yay, nested concat iterator!
            return StringUtil.concat(SPACE + OR + SPACE, new Iterator<String>()
            {
                private int strucI = 0;

                @Override
                public boolean hasNext()
                {
                    return strucI < structures.length;
                }

                @Override
                public String next()
                {
                    ++strucI;
                    return "(" + StringUtil.concat(SPACE + AND + SPACE,
                            new Iterator<String>()
                            {
                                private int keyI = 0;

                                @Override
                                public boolean hasNext()
                                {
                                    return keyI < keys.size();
                                }

                                @Override
                                public String next()
                                {
                                    final Field field = keys.get(keyI++);
                                    return createFieldEqualsValue(vars, changeHolder, field,
                                            getObservableValueByFieldAndStructure(field, structures[strucI - 1]), true);
                                }
                            }) + ")";
                }
            });
        }
    }

    /**
     * Creates only the update fields part of a collection containing observables with storage infos
     */
    protected static String createUpdateFields(final SQLVariableHolder vars, final ServerStorageChangeHolder changeHolder,
            final Collection<Pair<ObservableValue<?>, ObservableValueStorageInfo>> fields)
    {
        final Set<String> statements = new TreeSet<>();

        for (final Pair<ObservableValue<?>, ObservableValueStorageInfo> value : fields)
            statements.add(createFieldEqualsValue(vars, changeHolder, value.second().getField(), value.first(), true));

        return StringUtil.concat(COMMA + SPACE, statements.iterator());
    }

    /**
     * Creates an update query from table name, updateFields and keyPart.
     */
    protected static String createUpdateQuery(final String tableName, final String updateFields, final String keyPart)
    {
        return addDelemiter(StringUtil.fillWithSpaces(UPDATE, createName(tableName), SET, updateFields, WHERE, keyPart));
    }

    /**
     * Creates an delete query from table name and keyPart.
     */
    public static String createDeleteQuery(final String tableName, final String keyPart)
    {
        return addDelemiter(StringUtil.fillWithSpaces(DELETE, FROM, createName(tableName), WHERE, keyPart));
    }

    private static String createInsertHeaderPart(final String tableName, final List<Field> fields)
    {
        return StringUtil.fillWithSpaces(INSERT, INTO, createName(tableName), createInsertDeclareValuesPart(fields), VALUES);
    }

    private static String createInsertDeclareValuesPart(final List<Field> fields)
    {
        return "(" + StringUtil.concat(COMMA + SPACE,
                new CrossIterator<Field, String>(fields, (field) -> createName(field))) + ")";
    }

    public static String createInsertValuePart(final SQLVariableHolder vars, final ServerStorageChangeHolder changeHolder,
            final Collection<ServerStorageStructure> structures)
    {
        return StringUtil.concat(COMMA + NEWLINE,
                new CrossIterator<ServerStorageStructure, String>(structures, (structure) ->
                {
                    return "(" + StringUtil.concat(COMMA + SPACE,
                            new CrossIterator<Pair<ObservableValue<?>, Field>, String>(structure,
                                    (entry) -> createValueOfObservableValue(vars, changeHolder, entry.second(), entry.first(), true))) + ")";
                }));
    }

    public static String createInsertQuery(final String tableName, final List<Field> fields, final String valuePart)
    {
        return addDelemiter(StringUtil.fillWithNewLines(createInsertHeaderPart(tableName, fields), valuePart));
    }
}

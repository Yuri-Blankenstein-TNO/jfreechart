/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ------------------------
 * JDBCCategoryDataset.java
 * ------------------------
 * (C) Copyright 2002-present, by Bryan Scott and Contributors.
 *
 * Original Author:  Bryan Scott; Andy;
 * Contributor(s):   David Gilbert;
 *                   Thomas Morgner;
 *
 */

package org.jfree.data.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * A {@link CategoryDataset} implementation over a database JDBC result set.
 * The dataset is populated via a call to {@link #executeQuery(String)} with
 * the string SQL query.  The SQL query must return at least two columns.  The
 * first column will be the category name and remaining columns values (each
 * column represents a series).  Subsequent calls to
 * {@link #executeQuery(String)} will refresh the dataset.
 * <p>
 * The database connection is read-only and no write back facility exists.
 * <p>
 * NOTE: Many people have found this class too restrictive in general use.
 * For the greatest flexibility, please consider writing your own code to read
 * data from a{@code ResultSet} and populate a {@link DefaultCategoryDataset} 
 * directly.
 */
public class JDBCCategoryDataset extends DefaultCategoryDataset {

    /** For serialization. */
    static final long serialVersionUID = -3080395327918844965L;

    /** The database connection. */
    private transient Connection connection;

    /**
     * A flag the controls whether the table is transposed.  The default
     * is 'true' because this provides the behaviour described in the
     * documentation.
     */
    private boolean transpose = true;


    /**
     * Creates a new dataset with a database connection.
     *
     * @param  url  the URL of the database connection.
     * @param  driverName  the database driver class name.
     * @param  user  the database user.
     * @param  passwd  the database user's password.
     *
     * @throws ClassNotFoundException if the driver cannot be found.
     * @throws SQLException if there is an error obtaining a connection to the
     *                      database.
     */
    public JDBCCategoryDataset(String url,
                               String driverName,
                               String user,
                               String passwd)
        throws ClassNotFoundException, SQLException {

        Class.forName(driverName);
        this.connection = DriverManager.getConnection(url, user, passwd);
    }

    /**
     * Create a new dataset with the given database connection.
     *
     * @param connection  the database connection.
     */
    public JDBCCategoryDataset(Connection connection) {
        if (connection == null) {
            throw new NullPointerException("A connection must be supplied.");
        }
        this.connection = connection;
    }

    /**
     * Creates a new dataset with the given database connection, and executes
     * the supplied query to populate the dataset.
     *
     * @param connection  the connection.
     * @param query  the query.
     *
     * @throws SQLException if there is a problem executing the query.
     */
    public JDBCCategoryDataset(Connection connection, String query)
        throws SQLException {
        this(connection);
        executeQuery(query);
    }

    /**
     * Returns a flag that controls whether the table values are
     * transposed when added to the dataset.
     *
     * @return A boolean.
     */
    public boolean getTranspose() {
        return this.transpose;
    }

    /**
     * Sets a flag that controls whether the table values are transposed
     * when added to the dataset.
     *
     * @param transpose  the flag.
     */
    public void setTranspose(boolean transpose) {
        this.transpose = transpose;
    }

    /**
     * Populates the dataset by executing the supplied query against the
     * existing database connection.  If no connection exists then no action
     * is taken.
     * <p>
     * The results from the query are extracted and cached locally, thus
     * applying an upper limit on how many rows can be retrieved successfully.
     *
     * @param query  the query.
     *
     * @throws SQLException if there is a problem executing the query.
     */
    public void executeQuery(String query) throws SQLException {
        executeQuery(this.connection, query);
    }

    /**
     * Populates the dataset by executing the supplied query against the
     * existing database connection.  If no connection exists then no action
     * is taken.
     * <p>
     * The results from the query are extracted and cached locally, thus
     * applying an upper limit on how many rows can be retrieved successfully.
     *
     * @param con  the connection.
     * @param query  the query.
     *
     * @throws SQLException if there is a problem executing the query.
     */
    public void executeQuery(Connection con, String query) throws SQLException {

        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.createStatement();
            resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();

            if (columnCount < 2) {
                throw new SQLException(
                    "JDBCCategoryDataset.executeQuery() : insufficient columns "
                    + "returned from the database.");
            }

            // Remove any previous old data
            int i = getRowCount();
            while (--i >= 0) {
                removeRow(i);
            }

            while (resultSet.next()) {
                // first column contains the row key...
                Comparable rowKey = resultSet.getString(1);
                for (int column = 2; column <= columnCount; column++) {

                    Comparable columnKey = metaData.getColumnName(column);
                    int columnType = metaData.getColumnType(column);

                    switch (columnType) {
                        case Types.TINYINT:
                        case Types.SMALLINT:
                        case Types.INTEGER:
                        case Types.BIGINT:
                        case Types.FLOAT:
                        case Types.DOUBLE:
                        case Types.DECIMAL:
                        case Types.NUMERIC:
                        case Types.REAL: {
                            Number value = (Number) resultSet.getObject(column);
                            if (this.transpose) {
                                setValue(value, columnKey, rowKey);
                            }
                            else {
                                setValue(value, rowKey, columnKey);
                            }
                            break;
                        }
                        case Types.DATE:
                        case Types.TIME:
                        case Types.TIMESTAMP: {
                            Date date = (Date) resultSet.getObject(column);
                            Number value = date.getTime();
                            if (this.transpose) {
                                setValue(value, columnKey, rowKey);
                            }
                            else {
                                setValue(value, rowKey, columnKey);
                            }
                            break;
                        }
                        case Types.CHAR:
                        case Types.VARCHAR:
                        case Types.LONGVARCHAR: {
                            String string
                                = (String) resultSet.getObject(column);
                            try {
                                Number value = Double.valueOf(string);
                                if (this.transpose) {
                                    setValue(value, columnKey, rowKey);
                                }
                                else {
                                    setValue(value, rowKey, columnKey);
                                }
                            }
                            catch (NumberFormatException e) {
                                // suppress (value defaults to null)
                            }
                            break;
                        }
                        default:
                            // not a value, can't use it (defaults to null)
                            break;
                    }
                }
            }

            fireDatasetChanged();
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }
                catch (Exception e) {
                    // report this?
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                }
                catch (Exception e) {
                    // report this?
                }
            }
        }
    }

}

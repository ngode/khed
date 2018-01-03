/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GStatement
{

    /**
     * The statement this object is wrapping.
     */
    private PreparedStatement statement;

    /**
     * Maps parameter names to arrays of ints which are the parameter indices.
     */
    private final Map indexMap;

    private Connection connection;
    private String query;

    /**
     * Creates a NamedParameterStatement. Wraps a call to      * c.{@link Connection#prepareStatement(java.lang.String) 
prepareStatement}.
     *
     * @param connection the database connection
     * @param query the parameterized query
     * @throws SQLException if the statement could not be created
     */
    public GStatement(Connection connection)
    {
        this.connection = connection;
        indexMap = new HashMap();
        query = "";
    }

    public GStatement a(String s)
    {
        query += s + " ";
        return this;
    }

    private void compile() throws SQLException
    {
        if (statement == null)
        {
            String parsedQuery = parse(query, indexMap);
            statement = connection.prepareStatement(parsedQuery);
        }
    }

    /**
     * Parses a query with named parameters. The parameter-index mappings are
     * put into the map, and the parsed query is returned. DO NOT CALL FROM
     * CLIENT CODE. This method is non-private so JUnit code can test it.
     *
     * @param query query to parse
     * @param paramMap map to hold parameter-index mappings
     * @return the parsed query
     */
    static final String parse(String query, Map paramMap)
    {
        // I was originally using regular expressions, but they didn't work well for ignoring
        // parameter-like strings inside quotes.
        int length = query.length();
        StringBuffer parsedQuery = new StringBuffer(length);
        int index = 1;

        for (int i = 0; i < length; i++)
        {
            char c = query.charAt(i);
            
            if (c == ':' && i + 1 < length && Character.isJavaIdentifierStart(query.charAt(i + 1)))
            {
                int j = i + 2;
                
                while (j < length && Character.isJavaIdentifierPart(query.charAt(j)))
                {
                    j++;
                }
                
                String name = query.substring(i + 1, j);
                c = '?'; // replace the parameter with a question mark
                i += name.length(); // skip past the end if the parameter

                List indexList = (List) paramMap.get(name);
                
                if (indexList == null)
                {
                    indexList = new LinkedList();
                    paramMap.put(name, indexList);
                }
                
                indexList.add(new Integer(index));

                index++;
            }
            else if (c == '{')
            {
                int j = i + 2;
                
                while (j < length && query.charAt(j) != '}')
                {
                    j++;
                }
                
                String name = query.substring(i + 1, j);
                c = '?'; // replace the parameter with a question mark
                i += name.length() + 1; // skip past the end if the parameter + '}'

                List indexList = (List) paramMap.get(name);
                
                if (indexList == null)
                {
                    indexList = new LinkedList();
                    paramMap.put(name, indexList);
                }
                
                indexList.add(new Integer(index));

                index++;
            }
            
            parsedQuery.append(c);
        }

        // replace the lists of Integer objects with arrays of ints
        for (Iterator itr = paramMap.entrySet().iterator(); itr.hasNext();)
        {
            Map.Entry entry = (Map.Entry) itr.next();
            List list = (List) entry.getValue();
            int[] indexes = new int[list.size()];
            int i = 0;
            for (Iterator itr2 = list.iterator(); itr2.hasNext();)
            {
                Integer x = (Integer) itr2.next();
                indexes[i++] = x.intValue();
            }
            entry.setValue(indexes);
        }

        return parsedQuery.toString();
    }
    
    static final String parseOld(String query, Map paramMap)
    {
        // I was originally using regular expressions, but they didn't work well for ignoring
        // parameter-like strings inside quotes.
        int length = query.length();
        StringBuffer parsedQuery = new StringBuffer(length);
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        int index = 1;

        for (int i = 0; i < length; i++)
        {
            char c = query.charAt(i);
            if (inSingleQuote)
            {
                if (c == '\'')
                {
                    inSingleQuote = false;
                }
            }
            else if (inDoubleQuote)
            {
                if (c == '"')
                {
                    inDoubleQuote = false;
                }
            }
            else if (c == '\'')
            {
                inSingleQuote = true;
            }
            else if (c == '"')
            {
                inDoubleQuote = true;
            }
            else if (c == ':' && i + 1 < length
                    && Character.isJavaIdentifierStart(query.charAt(i + 1)))
            {
                int j = i + 2;
                while (j < length && Character.isJavaIdentifierPart(query.charAt(j)))
                {
                    j++;
                }
                String name = query.substring(i + 1, j);
                c = '?'; // replace the parameter with a question mark
                i += name.length(); // skip past the end if the parameter

                List indexList = (List) paramMap.get(name);
                if (indexList == null)
                {
                    indexList = new LinkedList();
                    paramMap.put(name, indexList);
                }
                indexList.add(new Integer(index));

                index++;
            }
            parsedQuery.append(c);
        }

        // replace the lists of Integer objects with arrays of ints
        for (Iterator itr = paramMap.entrySet().iterator(); itr.hasNext();)
        {
            Map.Entry entry = (Map.Entry) itr.next();
            List list = (List) entry.getValue();
            int[] indexes = new int[list.size()];
            int i = 0;
            for (Iterator itr2 = list.iterator(); itr2.hasNext();)
            {
                Integer x = (Integer) itr2.next();
                indexes[i++] = x.intValue();
            }
            entry.setValue(indexes);
        }

        return parsedQuery.toString();
    }

    /**
     * Returns the indexes for a parameter.
     *
     * @param name parameter name
     * @return parameter indexes
     * @throws IllegalArgumentException if the parameter does not exist
     */
    private int[] getIndexes(String name)
    {
        int[] indexes = (int[]) indexMap.get(name);
        if (indexes == null)
        {
            throw new IllegalArgumentException("Parameter not found: " + name);
        }
        return indexes;
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setObject(int, java.lang.Object)
     */
    public GStatement setObject(String name, Object value)
    {

        try
        {
            compile();

            int[] indexes = getIndexes(name);
            for (int i = 0; i < indexes.length; i++)
            {
                statement.setObject(indexes[i], value);
            }
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }

        return this;
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setString(int, java.lang.String)
     */
    public GStatement setString(String name, String value)
    {

        try
        {
            compile();

            int[] indexes = getIndexes(name);
            for (int i = 0; i < indexes.length; i++)
            {
                statement.setString(indexes[i], value);
            }
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }

        return this;
    }

    public GStatement setString(int i, String val)
    {
        try
        {
            compile();
            statement.setString(i, val);
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }

        return this;
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setInt(int, int)
     */
    public GStatement setInt(String name, int value)
    {

        try
        {
            compile();

            int[] indexes = getIndexes(name);
            for (int i = 0; i < indexes.length; i++)
            {
                statement.setInt(indexes[i], value);
            }
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }

        return this;
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setInt(int, int)
     */
    public GStatement setLong(String name, long value)
    {
        try
        {
            compile();
            int[] indexes = getIndexes(name);
            for (int i = 0; i < indexes.length; i++)
            {
                statement.setLong(indexes[i], value);
            }
            return this;
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }

        return this;
    }

    /**
     * Sets a parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setTimestamp(int, java.sql.Timestamp)
     */
    public GStatement setTimestamp(String name, Timestamp value)
    {
        try
        {
            compile();
            int[] indexes = getIndexes(name);
            for (int i = 0; i < indexes.length; i++)
            {
                statement.setTimestamp(indexes[i], value);
            }
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }

        return this;
    }

    /**
     * Returns the underlying statement.
     *
     * @return the statement
     */
    public PreparedStatement getStatement()
    {
        return statement;
    }

    /**
     * Executes the statement.
     *
     * @return true if the first result is a {@link ResultSet}
     * @throws SQLException if an error occurred
     * @see PreparedStatement#execute()
     */
    public boolean execute()
    {

        try
        {
            compile();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }

        try
        {
            return statement.execute();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }

        return false;
    }

    /**
     * Executes the statement, which must be a query.
     *
     * @return the query results
     * @throws SQLException if an error occurred
     * @see PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery()
    {
        try
        {
            compile();
            return statement.executeQuery();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    /**
     * Executes the statement, which must be an SQL INSERT, UPDATE or DELETE
     * statement; or an SQL statement that returns nothing, such as a DDL
     * statement.
     *
     * @return number of rows affected
     * @throws SQLException if an error occurred
     * @see PreparedStatement#executeUpdate()
     */
    public int executeUpdate()
    {
        try
        {
            compile();
            return statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }

        return -1;
    }

    /**
     * Closes the statement.
     *
     * @throws SQLException if an error occurred
     * @see Statement#close()
     */
    public void close()
    {
        try
        {
            compile();
            statement.close();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Adds the current set of parameters as a batch entry.
     *
     * @throws SQLException if something went wrong
     */
    public void addBatch()
    {
        try
        {
            compile();
            statement.addBatch();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Executes all of the batched statements.
     *
     * See {@link Statement#executeBatch()} for details.
     *
     * @return update counts for each statement
     * @throws SQLException if something went wrong
     */
    public int[] executeBatch()
    {
        try
        {
            compile();
            return statement.executeBatch();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(GStatement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new int[]
        {
        };
    }
}

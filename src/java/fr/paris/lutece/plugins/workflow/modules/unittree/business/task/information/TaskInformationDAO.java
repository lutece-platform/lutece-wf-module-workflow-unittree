/*
 * Copyright (c) 2002-2020, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.unittree.business.task.information;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.Collection;
import java.util.Iterator;

/**
 * This class provides Data Access methods for {@link TaskInformation} objects
 */
public final class TaskInformationDAO implements ITaskInformationDAO
{
    public static final String BEAN_NAME = "workflow-unittree.taskInformationDAO";
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_history, id_task, information_key, information_value FROM workflow_task_unittree_information WHERE id_history = ? AND id_task = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO workflow_task_unittree_information ( id_history, id_task, information_key, information_value ) VALUES ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM workflow_task_unittree_information WHERE id_history = ? AND id_task = ?";
    private static final String SQL_QUERY_INSERT_VALUE = "( ?, ?, ?, ? )";
    private static final String SQL_QUERY_INSERT_VALUE_SEPARATOR = ",";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( TaskInformation taskInformation, Plugin plugin )
    {
        Collection<String> collectionKeys = taskInformation.getKeys( );

        if ( !collectionKeys.isEmpty( ) )
        {
            // First, builds the query
            StringBuilder stringBuilder = new StringBuilder( SQL_QUERY_INSERT );
            Iterator<String> iterator = collectionKeys.iterator( );

            while ( iterator.hasNext( ) )
            {
                stringBuilder.append( SQL_QUERY_INSERT_VALUE );
                iterator.next( );

                if ( iterator.hasNext( ) )
                {
                    stringBuilder.append( SQL_QUERY_INSERT_VALUE_SEPARATOR );
                }
            }

            try ( DAOUtil daoUtil = new DAOUtil( stringBuilder.toString( ), plugin ) )
            {
                // Second, fills the query
                int nIndex = 0;

                for ( String strKey : collectionKeys )
                {
                    daoUtil.setInt( ++nIndex, taskInformation.getIdHistory( ) );
                    daoUtil.setInt( ++nIndex, taskInformation.getIdTask( ) );
                    daoUtil.setString( ++nIndex, strKey );
                    daoUtil.setString( ++nIndex, taskInformation.get( strKey ) );
                }

                daoUtil.executeUpdate( );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public TaskInformation load( int nIdHistory, int nIdTask, Plugin plugin )
    {
        TaskInformation taskInformation = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, nIdHistory );
            daoUtil.setInt( ++nIndex, nIdTask );
            daoUtil.executeQuery( );

            // First, creates the object with one piece of information
            if ( daoUtil.next( ) )
            {
                taskInformation = new TaskInformation( daoUtil.getInt( "id_history" ), daoUtil.getInt( "id_task" ) );
                taskInformation.add( daoUtil.getString( "information_key" ), daoUtil.getString( "information_value" ) );

                // Second, adds other pieces of information
                while ( daoUtil.next( ) )
                {
                    taskInformation.add( daoUtil.getString( "information_key" ), daoUtil.getString( "information_value" ) );
                }
            }
        }
        return taskInformation;
    }

    @Override
    public void delete( int nIdHistory, int nIdTask, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, nIdHistory );
            daoUtil.setInt( ++nIndex, nIdTask );
            daoUtil.executeUpdate( );
        }

    }
}

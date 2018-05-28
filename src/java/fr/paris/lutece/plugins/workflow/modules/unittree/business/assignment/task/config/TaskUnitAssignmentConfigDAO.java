/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.config;

import fr.paris.lutece.plugins.workflow.modules.unittree.service.WorkflowUnittreePlugin;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * This class provides Data Access methods for {@link TaskUnitAssignmentConfig} objects
 */

public final class TaskUnitAssignmentConfigDAO implements ITaskConfigDAO<TaskUnitAssignmentConfig>
{

    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_task, assignment_type, unit_selections FROM workflow_task_unittree_unit_assignment_cf WHERE id_task = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO workflow_task_unittree_unit_assignment_cf ( id_task, assignment_type, unit_selections ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM workflow_task_unittree_unit_assignment_cf WHERE id_task = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE workflow_task_unittree_unit_assignment_cf SET id_task = ?, assignment_type = ?, unit_selections = ? WHERE id_task = ?";

    private static final char LIST_SEPARATOR = ';';

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( TaskUnitAssignmentConfig config )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, WorkflowUnittreePlugin.getPlugin( ) );

        objectToData( config, daoUtil );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public TaskUnitAssignmentConfig load( int nId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, WorkflowUnittreePlugin.getPlugin( ) );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery( );

        TaskUnitAssignmentConfig config = null;

        if ( daoUtil.next( ) )
        {
            config = new TaskUnitAssignmentConfig( );

            int nIndex = 0;
            config.setIdTask( daoUtil.getInt( ++nIndex ) );
            config.setAssignmentType( daoUtil.getString( ++nIndex ) );

            List<String> listUnitSelections = new ArrayList<>( );

            for ( String unitSelection : StringUtils.split( daoUtil.getString( ++nIndex ), LIST_SEPARATOR ) )
            {
                listUnitSelections.add( unitSelection );
            }

            config.setUnitSelections( listUnitSelections );
        }

        daoUtil.free( );

        return config;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nConfigId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, WorkflowUnittreePlugin.getPlugin( ) );
        daoUtil.setInt( 1, nConfigId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( TaskUnitAssignmentConfig config )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, WorkflowUnittreePlugin.getPlugin( ) );

        int index = objectToData( config, daoUtil );

        daoUtil.setInt( ++index, config.getIdTask( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Sets the specified {@code TaskUnitAssignmentConfig} object into the specified {@code DAOUtil}
     * 
     * @param config
     *            the {@code TaskUnitAssignmentConfig} object to set
     * @param daoUtil
     *            the {@code DAOUtil}
     * @return the index of the last data set into the {@code DAOUtil}
     */
    private int objectToData( TaskUnitAssignmentConfig config, DAOUtil daoUtil )
    {
        String strUnitSelections = StringUtils.join( config.getUnitSelections( ), LIST_SEPARATOR );

        int index = 0;
        daoUtil.setInt( ++index, config.getIdTask( ) );
        daoUtil.setString( ++index, config.getAssignmentType( ) );
        daoUtil.setString( ++index, strUnitSelections );

        return index;
    }

}

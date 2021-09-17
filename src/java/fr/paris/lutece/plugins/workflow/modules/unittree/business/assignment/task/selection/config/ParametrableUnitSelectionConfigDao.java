/*
 * Copyright (c) 2002-2021, City of Paris
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
package fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.selection.config;

import fr.paris.lutece.plugins.unittree.service.UnitTreePlugin;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.util.sql.DAOUtil;

public class ParametrableUnitSelectionConfigDao implements ITaskConfigDAO<ParametrableUnitSelectionConfig>
{
    public static final String BEAN_NAME = "workflow-unittree.unitSelection.parametrableUnitSelection.configDAO";
    private static final String SQL_QUERY_SELECT = "SELECT id_task, parametrable_config_handler FROM workflow_task_unittree_unit_selection_parametrable_cf WHERE id_task = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO workflow_task_unittree_unit_selection_parametrable_cf ( id_task,  parametrable_config_handler ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM workflow_task_unittree_unit_selection_parametrable_cf WHERE id_task = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE workflow_task_unittree_unit_selection_parametrable_cf SET id_task = ?, parametrable_config_handler = ? WHERE id_task = ?";
    private static Plugin _plugin = PluginService.getPlugin( UnitTreePlugin.PLUGIN_NAME );

    @Override
    public void insert( ParametrableUnitSelectionConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, _plugin ) )
        {
            int index = 0;
            daoUtil.setInt( ++index, config.getIdTask( ) );
            daoUtil.setString( ++index, config.getParametrableConfigHandler( ) );
            daoUtil.executeUpdate( );
        }

    }

    @Override
    public void store( ParametrableUnitSelectionConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, _plugin ) )
        {
            int index = 0;
            daoUtil.setInt( ++index, config.getIdTask( ) );
            daoUtil.setString( ++index, config.getParametrableConfigHandler( ) );
            daoUtil.setInt( ++index, config.getIdTask( ) );

            daoUtil.executeUpdate( );
        }

    }

    @Override
    public ParametrableUnitSelectionConfig load( int nIdTask )
    {
        ParametrableUnitSelectionConfig config = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, _plugin ) )
        {
            daoUtil.setInt( 1, nIdTask );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                config = new ParametrableUnitSelectionConfig( );

                int nIndex = 0;
                config.setIdTask( daoUtil.getInt( ++nIndex ) );
                config.setParametrableConfigHandler( daoUtil.getString( ++nIndex ) );
            }
        }
        return config;
    }

    @Override
    public void delete( int nIdTask )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, _plugin ) )
        {
            daoUtil.setInt( 1, nIdTask );
            daoUtil.executeUpdate( );
        }
    }

}

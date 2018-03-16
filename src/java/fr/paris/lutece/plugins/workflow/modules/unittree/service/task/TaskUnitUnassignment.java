/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.workflow.modules.unittree.service.task;

import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.UnitAssignmentHome;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.task.information.TaskInformation;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.task.information.TaskInformationHome;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import java.util.Locale;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * This class represents the task for unassignate
 */
public class TaskUnitUnassignment extends SimpleTask
{
    @Inject
    private IResourceHistoryService _resourceHistoryService;
    private static final String MESSAGE_TASK_TITLE = "module.workflow.unittree.task_unit_unassignment.title";
    private static final String TASK_INFORMATION_UNASSIGNOR = "UNASSIGNOR";

    /**
     * {@inheritDoc }
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );

        if ( resourceHistory != null )
        {
            UnitAssignmentHome.deactivateByResource( resourceHistory.getIdResource( ), resourceHistory.getResourceType( ) );
            // save the unassignor in task infos
            saveTaskInformation( nIdResourceHistory, request );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_TASK_TITLE, locale );
    }

    public void saveTaskInformation( int nIdResourceHistory, HttpServletRequest request )
    {
        // Get the unassignor admin user
        AdminUser user = AdminUserService.getAdminUser( request );

        TaskInformation taskInfo = new TaskInformation( nIdResourceHistory, getId( ) );
        taskInfo.add( TASK_INFORMATION_UNASSIGNOR, user.getFirstName( ) + " " + user.getLastName( ) );

        TaskInformationHome.create( taskInfo );
    }

}

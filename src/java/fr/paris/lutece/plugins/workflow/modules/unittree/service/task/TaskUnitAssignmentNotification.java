/*
 * Copyright (c) 2002-2025, City of Paris
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

import java.util.List;
import java.util.Locale;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.notification.business.Notification;
import fr.paris.lutece.plugins.unittree.modules.notification.business.NotificationUnitAttribute;
import fr.paris.lutece.plugins.unittree.modules.notification.service.INotificationService;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.config.TaskUnitAssignmentNotificationConfig;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.UnitAssignmentService;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;

@Dependent
@Named( "workflow-unittree.taskUnitAssignmentNotification" )
public class TaskUnitAssignmentNotification extends SimpleTask
{
    private static final String MESSAGE_TASK_TITLE = "module.workflow.unittree.task_unit_assignment_notification.title";
    
    private static final String BEAN_CONFIG = "workflow-unittree.taskUnitAssignmentNotificationConfigService";

    @Inject
    private IResourceHistoryService _resourceHistoryService;
    
    @Inject
    @Named( BEAN_CONFIG )
    private ITaskConfigService _taskConfigService;
    
    @Inject
    private INotificationService _notifyService;

    /**
     * {@inheritDoc }
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
        TaskUnitAssignmentNotificationConfig config = _taskConfigService.findByPrimaryKey( getId( ) );

        if ( resourceHistory != null && config != null )
        {
            UnitAssignment currentUnitAssignment = UnitAssignmentService.findCurrentAssignment( resourceHistory.getIdResource( ), resourceHistory.getResourceType( ) );
            
            if ( currentUnitAssignment != null && currentUnitAssignment.getAssignedUnit() != null )
            {
            	Unit unit = currentUnitAssignment.getAssignedUnit();
            	_notifyService.populate( unit );
            	Notification notif = (Notification) unit.getAttribute( NotificationUnitAttribute.ATTRIBUTE_NAME ).getAttribute( );
            	
            	notify( notif, config );
            }
        }
    }

    /**
     * Notify a unit or its users of the assignment of the unit
     * 
     * @param notification
     *            the notification
     * @param config
     *            the task configuration
     */
    private void notify( Notification notif, TaskUnitAssignmentNotificationConfig config )
    {
        if ( notif != null && notif.getHasNotif( ) )
        {
        	String strNoReplyEmail = MailService.getNoReplyEmail( );
        	
        	if ( notif.getUseList( ) )
        	{
        		List<String> usersEmailList = _notifyService.getUnitUsersEmail( notif.getIdUnit( ) );
        		if ( CollectionUtils.isNotEmpty( usersEmailList ) )
        		{
        		    for ( String strEmail : usersEmailList )
        		    {
        		    	MailService.sendMailHtml( strEmail, StringUtils.EMPTY, strNoReplyEmail, config.getSubject(), config.getMessage( ) );
        		    }
        		}
        		
        	}
        	
        	if ( notif.getUseEmail() && StringUtils.isNoneBlank(notif.getEmail()))
        	{
        		MailService.sendMailHtml( notif.getEmail(), StringUtils.EMPTY, strNoReplyEmail, config.getSubject(), config.getMessage( ) );    
        	}
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveConfig( )
    {
    	_taskConfigService.remove( getId(  ) );
    }
}

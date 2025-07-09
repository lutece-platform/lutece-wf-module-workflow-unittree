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
package fr.paris.lutece.plugins.workflow.modules.unittree.web.task;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.unittree.service.selection.IUnitSelection;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.config.TaskUnitAssignmentConfig;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.UnitSelectionService;
import fr.paris.lutece.plugins.workflowcore.business.task.ITaskType;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class is a task component for the {@link fr.paris.lutece.plugins.workflow.modules.unittree.service.task.TaskUnitAssignmentAutomatic
 * TaskUnitAssignmentAutomatic} class
 *
 */
@ApplicationScoped
@Named( "workflow-unittree.unitAssignmentAutomaticTaskComponent" )
public class UnitAssignmentAutomaticTaskComponent extends AbstractUnitAssignmentTaskComponent
{
    // Markers
    private static final String MARK_UNIT_SELECTION_LIST = "list_unit_selection";

    // Templates
    private static final String TEMPLATE_CONFIG_UNIT_SELECTION = "admin/plugins/workflow/modules/unittree/task_unit_assignment_automatic_config_unit_selection.html";
    
    @Inject
    public UnitAssignmentAutomaticTaskComponent( @Named( "workflow-unittree.taskTypeUnitAssignmentAutomatic" ) ITaskType taskType, 
    		                                     @Named( "workflow-unittree.taskUnitAssignmentConfigService" ) ITaskConfigService taskConfigService )
    {
        setTaskType( taskType );
        setTaskConfigService( taskConfigService );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected String getUnitSelectionForm( HttpServletRequest request, ITask task )
    {
        TaskUnitAssignmentConfig config = findConfig( task );
        ReferenceList listUnitSelections = buildUnitSelectionlist( request.getLocale( ) );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_UNIT_SELECTION_LIST, listUnitSelections );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONFIG_UNIT_SELECTION, request.getLocale( ), model );

        return template.getHtml( );
    }

    /**
     * Builds the unit selection list as a {@code ReferenceList}
     * 
     * @param locale
     *            the locale
     * @return the {@code ReferenceList}
     */
    private ReferenceList buildUnitSelectionlist( Locale locale )
    {
        ReferenceList referenceList = new ReferenceList( );

        for ( IUnitSelection unitSelection : UnitSelectionService.getInstance( ).getUnitSelections( ) )
        {
            if ( unitSelection.isAutomatic( ) )
            {
                referenceList.addItem( unitSelection.getId( ), unitSelection.getConfigurationHandler( ).getTitle( locale ) );
            }
        }

        return referenceList;
    }
}

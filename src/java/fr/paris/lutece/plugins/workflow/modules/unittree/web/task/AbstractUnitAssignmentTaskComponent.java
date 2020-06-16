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
package fr.paris.lutece.plugins.workflow.modules.unittree.web.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentType;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.config.TaskUnitAssignmentConfig;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.IConfigurationHandler;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.IUnitSelection;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.UnitSelectionService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class is an abstract task component for the task to assign a resource to a unit
 *
 */
public abstract class AbstractUnitAssignmentTaskComponent extends AbstractUnittreeTaskComponent
{
    // Markers
    private static final String MARK_ASSIGNMENT_TYPE_LIST = "list_assignment_type";
    private static final String MARK_CONFIGURED_ASSIGNMENT_TYPE = "configured_assignment_type";
    private static final String MARK_UNIT_SELECTION_HTML = "unit_selection_html";
    private static final String MARK_CONFIGURED_UNIT_SELECTION_LIST = "list_configured_unit_selection";

    // Templates
    private static final String TEMPLATE_CONFIG = "admin/plugins/workflow/modules/unittree/task_unit_assignment_config.html";
    private static final String TEMPLATE_INFORMATION = "admin/plugins/workflow/modules/unittree/task_unit_assignment_information.html";

    // Parameters
    private static final String PARAMETER_APPLY = "apply";
    private static final String PARAMETER_UNIT_SELECTION_ID_TO_ADD = "unit_selection_id_to_add";
    private static final String PARAMETER_ASSIGNMENT_TYPE = "assignment_type";

    // Other contants
    private static final String BUTTON_ADD_UNIT_SELECTION = "addUnitSelection";
    private static final String BUTTON_REMOVE_UNIT_SELECTION = "removeUnitSelection";

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTaskInformationTemplate( )
    {
        return TEMPLATE_INFORMATION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        TaskUnitAssignmentConfig config = findConfig( task );

        String strUnitSelectionHtml = getUnitSelectionForm( request, task );
        List<ProcessedUnitSelection> listProcessedUnitSelectionConfiguration = new ArrayList<>( );

        for ( String strUnitSelectionId : config.getUnitSelections( ) )
        {
            IUnitSelection unitSelection = UnitSelectionService.getInstance( ).find( strUnitSelectionId );

            if ( unitSelection != null )
            {
                IConfigurationHandler configurationHandler = unitSelection.getConfigurationHandler( );

                listProcessedUnitSelectionConfiguration.add( new ProcessedUnitSelection( unitSelection.getId( ), configurationHandler.getTitle( locale ),
                        configurationHandler.getDisplayedForm( locale, task ) ) );
            }
        }

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_ASSIGNMENT_TYPE_LIST, UnitAssignmentType.values( ) );
        model.put( MARK_CONFIGURED_ASSIGNMENT_TYPE, config.getAssignmentType( ) );
        model.put( MARK_UNIT_SELECTION_HTML, strUnitSelectionHtml );
        model.put( MARK_CONFIGURED_UNIT_SELECTION_LIST, listProcessedUnitSelectionConfiguration );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONFIG, locale, model );

        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        TaskUnitAssignmentConfig config = findConfig( task );
        config.setAssignmentType( request.getParameter( PARAMETER_ASSIGNMENT_TYPE ) );

        String strAction = request.getParameter( PARAMETER_APPLY );

        if ( strAction != null )
        {
            doPerformAction( strAction, request, config, task );
        }

        getTaskConfigService( ).update( config );

        for ( String strUnitSelectionId : config.getUnitSelections( ) )
        {
            IUnitSelection unitSelection = UnitSelectionService.getInstance( ).find( strUnitSelectionId );
            String strError = unitSelection.getConfigurationHandler( ).saveConfiguration( request, task );

            if ( strError != null )
            {
                return strError;
            }
        }

        return null;
    }

    /**
     * <p>
     * Finds the configuration associated to the specified task.
     * </p>
     * <p>
     * If no configuration exists for the task, creates an empty one.
     * </p>
     * 
     * @param task
     *            the task
     * @return the configuration
     */
    protected TaskUnitAssignmentConfig findConfig( ITask task )
    {
        TaskUnitAssignmentConfig config = getTaskConfigService( ).findByPrimaryKey( task.getId( ) );

        if ( config == null )
        {
            config = new TaskUnitAssignmentConfig( );
            config.setIdTask( task.getId( ) );
            getTaskConfigService( ).create( config );
        }

        return config;
    }

    /**
     * Performs the specified action
     * 
     * @param strAction
     *            the action
     * @param request
     *            the request containing the information to perform the action
     * @param config
     *            the task configuration
     * @param task
     *            the task
     */
    public void doPerformAction( String strAction, HttpServletRequest request, TaskUnitAssignmentConfig config, ITask task )
    {
        if ( BUTTON_ADD_UNIT_SELECTION.equals( strAction ) )
        {
            addUnitSelection( request.getParameter( PARAMETER_UNIT_SELECTION_ID_TO_ADD ), config );
        }

        if ( strAction.startsWith( BUTTON_REMOVE_UNIT_SELECTION ) )
        {
            // Do not forget to remove the dash character
            String strUnitSelectionId = strAction.substring( BUTTON_REMOVE_UNIT_SELECTION.length( ) + 1 );
            removeUnitSelection( strUnitSelectionId, config, task );
        }
    }

    /**
     * Adds a unit selection in the specified configuration
     * 
     * @param strId
     *            the unit selection id
     * @param config
     *            the configuration
     */
    private void addUnitSelection( String strId, TaskUnitAssignmentConfig config )
    {
        if ( !StringUtils.isEmpty( strId ) )
        {
            config.getUnitSelections( ).add( strId );
        }
    }

    /**
     * <p>
     * Removes a unit selection from the specified configuration.
     * </p>
     * <p>
     * The unit selection configuration is also removed.
     * </p>
     * 
     * @param strId
     *            the unit selection id
     * @param config
     *            the configuration
     * @param task
     *            the task
     */
    private void removeUnitSelection( String strId, TaskUnitAssignmentConfig config, ITask task )
    {
        if ( !StringUtils.isEmpty( strId ) )
        {
            IUnitSelection unitSelection = UnitSelectionService.getInstance( ).find( strId );
            unitSelection.getConfigurationHandler( ).removeConfiguration( task );

            config.getUnitSelections( ).remove( strId );
        }
    }

    /**
     * <p>
     * This class represents a unit selection with a processed form.
     * </p>
     * <p>
     * The form can be the configuration form or the task form. It has been processed by Freemarker.
     * </p>
     *
     */
    public static class ProcessedUnitSelection
    {
        private final String _strId;
        private final String _strTitle;
        private final String _strHtmlForm;

        /**
         * Constructor
         * 
         * @param strId
         *            the unit selection id
         * @param strTitle
         *            the unit selection title
         * @param strHtmlForm
         *            the processed unit selection form
         */
        public ProcessedUnitSelection( String strId, String strTitle, String strHtmlForm )
        {
            _strId = strId;
            _strTitle = strTitle;
            _strHtmlForm = strHtmlForm;
        }

        /**
         * Gives the id
         * 
         * @return the id
         */
        public String getId( )
        {
            return _strId;
        }

        /**
         * Gives the title
         * 
         * @return the title
         */
        public String getTitle( )
        {
            return _strTitle;
        }

        /**
         * Gives the HTML form
         * 
         * @return the HTML Form
         */
        public String getHtmlForm( )
        {
            return _strHtmlForm;
        }
    }

    /**
     * Gives the unit selection form to insert in the task form
     * 
     * @param request
     *            the request containing the information to build the form
     * @param task
     *            the task associated to the form
     * @return the form as a String
     */
    protected abstract String getUnitSelectionForm( HttpServletRequest request, ITask task );

}

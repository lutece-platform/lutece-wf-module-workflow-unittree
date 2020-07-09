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
package fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.exception.AssignmentNotPossibleException;
import fr.paris.lutece.plugins.unittree.service.selection.IConfigurationHandler;
import fr.paris.lutece.plugins.unittree.service.selection.ITaskFormHandler;
import fr.paris.lutece.plugins.unittree.service.selection.IUnitSelection;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.selection.config.UnitSelectionSpecificUnitConfig;
import fr.paris.lutece.plugins.workflow.modules.unittree.util.WorkflowUnittreeConstants;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class is a unit selection to a specific unit
 *
 */
public class UnitSelectionSpecificUnit implements IUnitSelection
{
    private static final String ID = "UnitSelectionSpecificUnit";

    // Services
    private final IUnitService _unitService;
    private final ITaskConfigDAO<UnitSelectionSpecificUnitConfig> _configDAO;

    private final IConfigurationHandler _configurationHandler = new ConfigurationHandler( );
    private final ITaskFormHandler _taskFormHandler = new TaskFormHandler( );

    /**
     * Constructor
     * 
     * @param unitService
     *            the unit service to set
     * @param configDAO
     *            the configuration DAO to set
     */
    @Inject
    public UnitSelectionSpecificUnit( IUnitService unitService, ITaskConfigDAO<UnitSelectionSpecificUnitConfig> configDAO )
    {
        _unitService = unitService;
        _configDAO = configDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId( )
    {
        return ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAutomatic( )
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IConfigurationHandler getConfigurationHandler( )
    {
        return _configurationHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITaskFormHandler getTaskFormHandler( )
    {
        return _taskFormHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int select( int nIdResource, String strResourceType, HttpServletRequest request, ITask task ) throws AssignmentNotPossibleException
    {
        UnitSelectionSpecificUnitConfig config = _configDAO.load( task.getId( ) );

        if ( config == null )
        {
            throw new AssignmentNotPossibleException( "The selection has not been configured" );
        }

        int nIdUnit = config.getUnitId( );

        if ( WorkflowUnittreeConstants.UNSET_ID == nIdUnit )
        {
            throw new AssignmentNotPossibleException( "The unit id is not correct" );
        }

        return nIdUnit;
    }

    /**
     * This class is a configuration handler for the {@link UnitSelectionSpecificUnit} class
     *
     */
    private class ConfigurationHandler implements IConfigurationHandler
    {
        // Messages
        private static final String MESSAGE_TITLE = "module.workflow.unittree.unit_selection.specific_unit.config.title";

        // Markers
        private static final String MARK_UNIT_LIST = "unit_list";
        private static final String MARK_UNIT_SELECTED = "unit_selected";

        // Parameters
        private static final String PARAMETER_UNIT_ID = "task_unit_assignment_config_selection_specific_unit_id";

        // Templates
        private static final String TEMPLATE_CONFIGURATION = "admin/plugins/workflow/modules/unittree/unitselection/config/unit_selection_specific_unit_configuration.html";

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( Locale locale )
        {
            return I18nService.getLocalizedString( MESSAGE_TITLE, locale );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayedForm( Locale locale, ITask task )
        {
            Map<String, Object> model = new HashMap<>( );
            ReferenceList listUnits = buildUnitlist( );
            int nIdUnit = findSelectedUnitId( task );

            model.put( MARK_UNIT_LIST, listUnits );
            model.put( MARK_UNIT_SELECTED, nIdUnit );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONFIGURATION, locale, model );

            return template.getHtml( );
        }

        /**
         * Builds the unit list as a reference list
         * 
         * @return the reference list containing the units
         */
        private ReferenceList buildUnitlist( )
        {
            List<Unit> listUnits = _unitService.getAllUnits( false );

            return ReferenceList.convert( listUnits, "idUnit", "label", true );
        }

        /**
         * Finds the id of the selected unit for the specified task
         * 
         * @param task
         *            the task
         * @return the unit id
         */
        private int findSelectedUnitId( ITask task )
        {
            UnitSelectionSpecificUnitConfig config = _configDAO.load( task.getId( ) );
            int nIdUnit = WorkflowUnittreeConstants.UNSET_ID;

            if ( config != null )
            {
                nIdUnit = config.getUnitId( );
            }

            return nIdUnit;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String saveConfiguration( HttpServletRequest request, ITask task )
        {
            int nIdUnit = NumberUtils.toInt( request.getParameter( PARAMETER_UNIT_ID ), WorkflowUnittreeConstants.UNSET_ID );

            UnitSelectionSpecificUnitConfig config = _configDAO.load( task.getId( ) );

            if ( config == null )
            {
                createConfiguration( task, nIdUnit );
            }
            else
            {
                updateConfiguration( config, nIdUnit );
            }

            return null;
        }

        /**
         * Creates a configuration
         * 
         * @param task
         *            the task for which the configuration is created
         * @param nIdUnit
         *            the id of the selected unit
         */
        private void createConfiguration( ITask task, int nIdUnit )
        {
            UnitSelectionSpecificUnitConfig config = new UnitSelectionSpecificUnitConfig( );
            config.setIdTask( task.getId( ) );
            config.setUnitId( nIdUnit );

            _configDAO.insert( config );
        }

        /**
         * Updates the specified configuration
         * 
         * @param config
         *            the configuration to update
         * @param nIdUnit
         *            the id of the selected unit
         */
        private void updateConfiguration( UnitSelectionSpecificUnitConfig config, int nIdUnit )
        {
            config.setUnitId( nIdUnit );

            _configDAO.store( config );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeConfiguration( ITask task )
        {
            _configDAO.delete( task.getId( ) );
        }
    }

    /**
     * This class is a form handler for the {@link UnitSelectionSpecificUnit} class
     *
     */
    private class TaskFormHandler implements ITaskFormHandler
    {
        // Messages
        private static final String MESSAGE_TITLE = "module.workflow.unittree.unit_selection.specific_unit.form.title";
     
        // Templates
        private static final String TEMPLATE_AUTOMATIC_TASK_FORM = "admin/plugins/workflow/modules/unittree/unitselection/form/unit_selection_automatic_form.html";
        
        // Markers
        private static final String MARK_ASSIGNED_UNIT = "assigned_unit";

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( Locale locale )
        {
            return I18nService.getLocalizedString( MESSAGE_TITLE, locale );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayedForm( int nIdResource, String strResourceType, Locale locale, ITask task ) throws AssignmentNotPossibleException
        {
            Unit unitAssigned = _unitService.getUnit( select( nIdResource, strResourceType, null, task ), false );

            Map<String, Object> model = new HashMap<>( );

            model.put( MARK_ASSIGNED_UNIT, unitAssigned );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_AUTOMATIC_TASK_FORM, locale, model );

            return template.getHtml( );
        }
    }

}

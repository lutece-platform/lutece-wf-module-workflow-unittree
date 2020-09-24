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
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentType;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.exception.AssignmentNotPossibleException;
import fr.paris.lutece.plugins.unittree.service.selection.IConfigurationHandler;
import fr.paris.lutece.plugins.unittree.service.selection.ITaskFormHandler;
import fr.paris.lutece.plugins.unittree.service.selection.IUnitSelection;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.UnitAssignmentService;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.task.AbstractTaskUnitAssignment;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class is a unit selection using the assignor unit
 *
 */
public class UnitSelectionAssignor implements IUnitSelection
{
    private static final String ID = "UnitSelectionAssignor";

    // Services
    @Inject
    private IUnitService _unitService;

    private final IConfigurationHandler _configurationHandler = new ConfigurationHandler( );
    private final ITaskFormHandler _taskFormHandler = new TaskFormHandler( );

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
        UnitAssignment unitAssignment = UnitAssignmentService.findCurrentAssignment( nIdResource, strResourceType );

        if ( unitAssignment == null || UnitAssignmentType.CREATION == unitAssignment.getAssignmentType( )
                || UnitAssignmentType.TRANSFER == unitAssignment.getAssignmentType( )
                || AbstractTaskUnitAssignment.UNSET_ASSIGNED_UNIT_ID == unitAssignment.getAssignorUnit( ).getIdUnit( ) )
        {
            throw new AssignmentNotPossibleException( "Assign to assignor unit : there is no assignor unit." );
        }

        return unitAssignment.getAssignorUnit( ).getIdUnit( );
    }

    /**
     * This class is a configuration handler for the {@link UnitSelectionAssignor} class
     *
     */
    private static class ConfigurationHandler extends AbstractEmptyConfigurationHandler
    {
        // Messages
        private static final String MESSAGE_TITLE = "module.workflow.unittree.unit_selection.assignor.config.title";

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( Locale locale )
        {
            return I18nService.getLocalizedString( MESSAGE_TITLE, locale );
        }
    }

    /**
     * This class is a form handler for the {@link UnitSelectionAssignor} class
     *
     */
    private class TaskFormHandler implements ITaskFormHandler
    {
        // Messages
        private static final String MESSAGE_TITLE = "module.workflow.unittree.unit_selection.assignor.form.title";

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

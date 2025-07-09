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
package fr.paris.lutece.plugins.workflow.modules.unittree.service.task;

import java.util.Locale;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.unittree.service.selection.IUnitSelection;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.UnitSelectionService;
import fr.paris.lutece.portal.service.i18n.I18nService;

/**
 * This class represents a task to manually assign a resource to a unit
 *
 */
@Dependent
@Named( "workflow-unittree.taskUnitAssignmentManual" )
public class TaskUnitAssignmentManual extends AbstractTaskUnitAssignment
{
    // Message
    private static final String MESSAGE_TASK_TITLE = "module.workflow.unittree.task_unit_assignment.manual.title";

    // Parameters
    private static final String PARAMETER_UNIT_SELECTION_ID = "task_unit_assignment_selection_id";

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
    protected IUnitSelection fetchUnitSelection( HttpServletRequest request )
    {
        String strUnitSelectionId = request.getParameter( PARAMETER_UNIT_SELECTION_ID );

        return UnitSelectionService.getInstance( ).find( strUnitSelectionId );
    }

}

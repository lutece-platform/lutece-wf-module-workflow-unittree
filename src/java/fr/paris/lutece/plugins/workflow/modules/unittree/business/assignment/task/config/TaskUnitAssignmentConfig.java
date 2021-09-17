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
package fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.config;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.workflowcore.business.config.TaskConfig;

/**
 * This class represents the configuration of the task {@link fr.paris.lutece.plugins.workflow.modules.unittree.service.task.AbstractTaskUnitAssignment
 * AbstractTaskUnitAssignment}
 */
public class TaskUnitAssignmentConfig extends TaskConfig
{
    private String _strAssignmentType;
    private List<String> _listUnitSelections;

    /**
     * Constructor
     */
    public TaskUnitAssignmentConfig( )
    {
        super( );
        _listUnitSelections = new ArrayList<>( );
    }

    /**
     * Gives the assignment type
     * 
     * @return The assignment type
     */
    public String getAssignmentType( )
    {
        return _strAssignmentType;
    }

    /**
     * Sets the assignment type
     * 
     * @param strAssignmentType
     *            The assignment type to set
     */
    public void setAssignmentType( String strAssignmentType )
    {
        _strAssignmentType = strAssignmentType;
    }

    /**
     * Gives the unit selections
     * 
     * @return The unit selections
     */
    public List<String> getUnitSelections( )
    {
        return _listUnitSelections;
    }

    /**
     * Sets the unit selections
     * 
     * @param listUnitSelections
     *            The unit selections to set
     */
    public void setUnitSelections( List<String> listUnitSelections )
    {
        _listUnitSelections = listUnitSelections;
    }
}

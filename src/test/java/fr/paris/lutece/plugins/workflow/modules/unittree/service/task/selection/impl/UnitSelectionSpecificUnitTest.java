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
package fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.impl;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.plugins.unittree.exception.AssignmentNotPossibleException;
import fr.paris.lutece.plugins.unittree.service.selection.IConfigurationHandler;
import fr.paris.lutece.plugins.unittree.service.selection.ITaskFormHandler;
import fr.paris.lutece.plugins.unittree.service.unit.FakeUnitService;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.selection.config.SpyUnitSelectionSpecificUnitConfigDAO;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.selection.config.UnitSelectionSpecificUnitConfig;
import fr.paris.lutece.plugins.workflow.modules.unittree.util.IdGenerator;
import fr.paris.lutece.plugins.workflow.modules.unittree.util.WorkflowUnittreeConstants;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.service.task.MockTask;
import junit.framework.TestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Locale;

public class UnitSelectionSpecificUnitTest extends TestCase
{
    private static final String PARAMETER_UNIT_ID = "task_unit_assignment_config_selection_specific_unit_id";
    private static final String RESOURCE_TYPE = "UnitSelectionSpecificUnitTest";

    private MockHttpServletRequest _request;
    private SpyUnitSelectionSpecificUnitConfigDAO _configDAO;
    private UnitSelectionSpecificUnit _selection;

    public void setUp( ) throws Exception
    {
        super.setUp( );

        _request = new MockHttpServletRequest( );
        _configDAO = new SpyUnitSelectionSpecificUnitConfigDAO( );
        _selection = new UnitSelectionSpecificUnit( new FakeUnitService( ), _configDAO );
    }

    public void testSaveConfigurationWhenNoUnitIsSelected( )
    {
        ITask task = MockTask.create( );
        IConfigurationHandler configHandler = _selection.getConfigurationHandler( );
        _configDAO.startSpying( );

        configHandler.saveConfiguration( _request, task );

        assertThat( _configDAO.load( task.getId( ) ).getUnitId( ), is( WorkflowUnittreeConstants.UNSET_ID ) );
        assertThat( _configDAO.isInsertCalled( ), is( true ) );
    }

    public void testSaveConfigurationWhenNoConfigurationIsPreviouslySaved( )
    {
        ITask task = MockTask.create( );
        IConfigurationHandler configHandler = _selection.getConfigurationHandler( );
        int nNewUnitId = IdGenerator.generateId( );
        _request.addParameter( PARAMETER_UNIT_ID, String.valueOf( nNewUnitId ) );
        _configDAO.startSpying( );

        configHandler.saveConfiguration( _request, task );

        assertThat( _configDAO.load( task.getId( ) ).getUnitId( ), is( nNewUnitId ) );
        assertThat( _configDAO.isInsertCalled( ), is( true ) );
        assertThat( _configDAO.isStoreCalled( ), is( false ) );
    }

    private UnitSelectionSpecificUnitConfig createConfig( ITask task )
    {
        UnitSelectionSpecificUnitConfig config = new UnitSelectionSpecificUnitConfig( );
        config.setIdTask( task.getId( ) );
        config.setUnitId( IdGenerator.generateId( ) );

        return config;
    }

    public void testSaveConfigurationWhenAConfigurationIsPreviouslySaved( )
    {
        ITask task = MockTask.create( );
        UnitSelectionSpecificUnitConfig config = createConfig( task );
        _configDAO.insert( config );
        IConfigurationHandler configHandler = _selection.getConfigurationHandler( );
        int nNewUnitId = IdGenerator.generateId( );
        _request.addParameter( PARAMETER_UNIT_ID, String.valueOf( nNewUnitId ) );
        _configDAO.startSpying( );

        configHandler.saveConfiguration( _request, task );

        assertThat( _configDAO.load( task.getId( ) ).getUnitId( ), is( nNewUnitId ) );
        assertThat( _configDAO.isInsertCalled( ), is( false ) );
        assertThat( _configDAO.isStoreCalled( ), is( true ) );
    }

    public void testRemoveConfigurationWhenNoConfigurationIsPreviouslySaved( )
    {
        ITask task = MockTask.create( );
        IConfigurationHandler configHandler = _selection.getConfigurationHandler( );
        _configDAO.startSpying( );

        configHandler.removeConfiguration( task );

        assertThat( _configDAO.isDeleteCalled( ), is( true ) );
    }

    public void testRemoveConfigurationWhenAConfigurationIsPreviouslySaved( )
    {
        ITask task = MockTask.create( );
        UnitSelectionSpecificUnitConfig config = createConfig( task );
        _configDAO.insert( config );
        IConfigurationHandler configHandler = _selection.getConfigurationHandler( );
        _configDAO.startSpying( );

        configHandler.removeConfiguration( task );

        assertThat( _configDAO.load( task.getId( ) ), is( nullValue( ) ) );
        assertThat( _configDAO.isDeleteCalled( ), is( true ) );
    }

    public void testDisplayedFormWhenConfiguredUnitIsIncorrect( )
    {
        ITask task = MockTask.create( );
        ITaskFormHandler taskFormHandler = _selection.getTaskFormHandler( );

        try
        {
            taskFormHandler.getDisplayedForm( IdGenerator.generateId( ), RESOURCE_TYPE, Locale.getDefault( ), task );
            fail( "Expected an AssignmentNotPossibleException to be thrown" );
        }
        catch( AssignmentNotPossibleException e )
        {
            // Correct behavior
        }
    }

    public void testUnitSelection( ) throws AssignmentNotPossibleException
    {
        ITask task = MockTask.create( );
        UnitSelectionSpecificUnitConfig config = createConfig( task );
        _configDAO.insert( config );

        int nSelectedUnitId = _selection.select( IdGenerator.generateId( ), RESOURCE_TYPE, _request, task );

        assertThat( nSelectedUnitId, is( config.getUnitId( ) ) );
    }

    public void testUnitSelectionWhenConfiguredUnitIsIncorrect( )
    {
        ITask task = MockTask.create( );

        try
        {
            _selection.select( IdGenerator.generateId( ), RESOURCE_TYPE, _request, task );
            fail( "Expected an AssignmentNotPossibleException to be thrown" );
        }
        catch( AssignmentNotPossibleException e )
        {
            // Correct behavior
        }
    }
}

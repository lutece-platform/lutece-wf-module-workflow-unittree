/*
 * Copyright (c) 2002-2026, City of Paris
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

import java.util.Locale;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.config.TaskUnitAssignmentConfig;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.service.task.MockTask;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * The configuration of a unit assignment task requires an assignment type
 */
public class UnitAssignmentTaskComponentTest extends LuteceTestCase
{
    @Inject
    private UnitAssignmentAutomaticTaskComponent _component;

    @Inject
    @Named( "workflow-unittree.taskUnitAssignmentConfigService" )
    private ITaskConfigService _taskConfigService;

    @Test
    public void testSaveConfigWithoutAssignmentType( )
    {
        ITask task = MockTask.create( );
        MockHttpServletRequest request = new MockHttpServletRequest( );

        String strError = _component.doSaveConfig( request, Locale.FRENCH, task );

        assertNotNull( strError );
        assertEquals( "The assignment type is mandatory.", AdminMessageService.getMessage( request ).getText( Locale.ENGLISH ) );
        assertNull( _taskConfigService.findByPrimaryKey( task.getId( ) ) );
    }

    @Test
    public void testSaveConfigWithAssignmentType( )
    {
        ITask task = MockTask.create( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "assignment_type", "create" );

        try
        {
            assertNull( _component.doSaveConfig( request, Locale.FRENCH, task ) );
            TaskUnitAssignmentConfig config = _taskConfigService.findByPrimaryKey( task.getId( ) );
            assertEquals( "create", config.getAssignmentType( ) );
        }
        finally
        {
            _taskConfigService.remove( task.getId( ) );
        }
    }
}

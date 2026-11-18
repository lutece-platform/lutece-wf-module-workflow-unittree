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
package fr.paris.lutece.plugins.workflow.modules.unittree.service.task;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.information.MockTaskInformation;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.task.information.TaskInformation;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.task.information.TaskInformationHome;
import fr.paris.lutece.plugins.workflowcore.business.resource.MockResourceHistory;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.service.task.MockTask;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;

/**
 * The information of a unit assignment task is removed with the history it belongs to
 */
public class TaskUnitAssignmentRemoveTaskInformationTest extends LuteceTestCase
{
    @Inject
    private TaskUnitAssignmentAutomatic _task;

    @Test
    public void testRemoveTaskInformationOfOneHistory( )
    {
        ITask mockTask = MockTask.create( );
        ResourceHistory history = MockResourceHistory.create( );
        ResourceHistory otherHistory = MockResourceHistory.create( );
        TaskInformationHome.create( MockTaskInformation.createWithPiecesOfInformation( history, mockTask ) );
        TaskInformationHome.create( MockTaskInformation.createWithPiecesOfInformation( otherHistory, mockTask ) );
        _task.setId( mockTask.getId( ) );

        try
        {
            _task.doRemoveTaskInformation( history.getId( ) );

            assertNull( TaskInformationHome.find( history.getId( ), mockTask.getId( ) ) );
            TaskInformation otherTaskInformation = TaskInformationHome.find( otherHistory.getId( ), mockTask.getId( ) );
            assertNotNull( otherTaskInformation );
        }
        finally
        {
            TaskInformationHome.delete( history.getId( ), mockTask.getId( ) );
            TaskInformationHome.delete( otherHistory.getId( ), mockTask.getId( ) );
        }
    }
}

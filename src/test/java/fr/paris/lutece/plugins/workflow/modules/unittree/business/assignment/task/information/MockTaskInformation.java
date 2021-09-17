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
package fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.information;

import java.util.Random;

import fr.paris.lutece.plugins.workflow.modules.unittree.business.task.information.TaskInformation;
import fr.paris.lutece.plugins.workflowcore.business.resource.MockResourceHistory;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.service.task.MockTask;

public class MockTaskInformation
{
    public static final String TASK_INFORMATION_KEY = "task information key";
    public static final String TASK_INFORMATION_VALUE = "task information value";

    public static TaskInformation createWithPiecesOfInformation( )
    {
        ResourceHistory resourceHistory = MockResourceHistory.create( );
        ITask task = MockTask.create( );
        int nNumberOfPiecesOfInformation = generateNumberOfPiecesOfInformation( );

        return createWithPiecesOfInformation( resourceHistory, task, nNumberOfPiecesOfInformation );
    }

    private static int generateNumberOfPiecesOfInformation( )
    {
        // Add 1 at the end to prevent to return the same id (nextInt( ) can return 0)
        return new Random( ).nextInt( 20 ) + 1;
    }

    public static TaskInformation createWithPiecesOfInformation( ResourceHistory resourceHistory, ITask task, int nNumberOfPiecesOfInformation )
    {
        TaskInformation taskInformation = new TaskInformation( resourceHistory.getId( ), task.getId( ) );

        addPiecesOfInformation( taskInformation, nNumberOfPiecesOfInformation );

        return taskInformation;
    }

    private static void addPiecesOfInformation( TaskInformation taskInformation, int nNumberOfPiecesOfInformation )
    {
        for ( int i = 0; i < nNumberOfPiecesOfInformation; i++ )
        {
            taskInformation.add( TASK_INFORMATION_KEY + i, TASK_INFORMATION_VALUE + i );
        }
    }

    public static TaskInformation createWithNoPieceOfInformation( )
    {
        ResourceHistory resourceHistory = MockResourceHistory.create( );
        ITask task = MockTask.create( );

        return createWithPiecesOfInformation( resourceHistory, task, 0 );
    }

    public static TaskInformation createWithPiecesOfInformation( ResourceHistory resourceHistory, ITask task )
    {
        int nNumberOfPiecesOfInformation = generateNumberOfPiecesOfInformation( );
        TaskInformation taskInformation = createWithPiecesOfInformation( resourceHistory, task, nNumberOfPiecesOfInformation );

        return taskInformation;
    }
}

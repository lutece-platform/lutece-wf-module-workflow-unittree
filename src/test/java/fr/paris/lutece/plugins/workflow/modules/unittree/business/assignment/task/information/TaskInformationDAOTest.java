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

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.workflow.modules.unittree.business.task.information.ITaskInformationDAO;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.task.information.TaskInformation;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.WorkflowUnittreePlugin;
import fr.paris.lutece.plugins.workflowcore.business.resource.MockResourceHistory;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.service.task.MockTask;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.sql.DAOUtil;
import jakarta.inject.Inject;

public class TaskInformationDAOTest extends LuteceTestCase
{
    private static final String SQL_QUERY_CLEAR_TABLE = "DELETE FROM workflow_task_unittree_information";

    private static final Plugin _plugin = WorkflowUnittreePlugin.getPlugin( );

    @Inject
    private ITaskInformationDAO _dao;

    @Test
    public void testInsertOneTaskInformation( )
    {
        TaskInformation taskInformation = MockTaskInformation.createWithPiecesOfInformation( );

        insertIntoDabase( taskInformation );

        TaskInformation taskInformationFromDatabase = findFromDatabase( taskInformation );
        assertEqualityBetween( taskInformation, taskInformationFromDatabase );

        clearTable( );
    }

    private void insertIntoDabase( TaskInformation taskInformation )
    {
        _dao.insert( taskInformation, _plugin );
    }

    private TaskInformation findFromDatabase( TaskInformation taskInformation )
    {
        return _dao.load( taskInformation.getIdHistory( ), taskInformation.getIdTask( ), _plugin );
    }

    private void assertEqualityBetween( TaskInformation taskInformation1, TaskInformation taskInformation2 )
    {
        assertEquals( taskInformation1.getIdHistory( ), taskInformation2.getIdHistory( ) );
        assertEquals( taskInformation1.getIdTask( ), taskInformation2.getIdTask( ) );
        assertEquals( taskInformation1.getKeys( ).size( ), taskInformation2.getKeys( ).size( ) );

        assertEqualityBetweenPiecesOfInformation( taskInformation1, taskInformation2 );
    }

    private void assertEqualityBetweenPiecesOfInformation( TaskInformation taskInformation1, TaskInformation taskInformation2 )
    {
        for ( String pieceOfInformationKey1 : taskInformation1.getKeys( ) )
        {
            String pieceOfInformationValue1 = taskInformation1.get( pieceOfInformationKey1 );
            String pieceOfInformationValue2 = taskInformation2.get( pieceOfInformationKey1 );

            assertEquals( pieceOfInformationValue1, pieceOfInformationValue2 );
        }
    }

    public static void clearTable( )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CLEAR_TABLE, _plugin ) )
        {            
            daoUtil.executeUpdate( );
        }
    }

    @Test
    public void testInsertTaskInformationWithNoPieceOfInformation( )
    {
        TaskInformation taskInformation = MockTaskInformation.createWithNoPieceOfInformation( );

        insertIntoDabase( taskInformation );

        TaskInformation taskInformationFromDatabase = findFromDatabase( taskInformation );
        assertNull( taskInformationFromDatabase );
    }

    @Test
    public void testInsertWhenTaskInformationKeyIsNull( )
    {
        TaskInformation taskInformation = MockTaskInformation.createWithNoPieceOfInformation( );
        taskInformation.add( null, MockTaskInformation.TASK_INFORMATION_VALUE );

        try
        {
            insertIntoDabase( taskInformation );
            fail( "Expected an exception to be thrown" );
        }
        catch( Exception e )
        {
            // Correct behavior
        }

        TaskInformation taskInformationFromDatabase = findFromDatabase( taskInformation );
        assertNull( taskInformationFromDatabase );
    }

    @Test
    public void testInsertWhenTaskInformationValueIsNull( )
    {
        TaskInformation taskInformation = MockTaskInformation.createWithNoPieceOfInformation( );
        taskInformation.add( MockTaskInformation.TASK_INFORMATION_KEY, null );

        insertIntoDabase( taskInformation );

        TaskInformation taskInformationFromDatabase = findFromDatabase( taskInformation );
        assertEqualityBetween( taskInformation, taskInformationFromDatabase );

        clearTable( );
    }

    @Test
    public void testInsertTwoTaskInformationToSameHistory( )
    {
        ResourceHistory resourceHistory = MockResourceHistory.create( );
        ITask task1 = MockTask.create( );
        ITask task2 = MockTask.create( );
        TaskInformation taskInformation1 = MockTaskInformation.createWithPiecesOfInformation( resourceHistory, task1 );
        TaskInformation taskInformation2 = MockTaskInformation.createWithPiecesOfInformation( resourceHistory, task2 );

        insertIntoDabase( taskInformation1 );
        insertIntoDabase( taskInformation2 );

        TaskInformation taskInformationFromDatabase1 = findFromDatabase( taskInformation1 );
        TaskInformation taskInformationFromDatabase2 = findFromDatabase( taskInformation2 );

        assertEqualityBetween( taskInformation1, taskInformationFromDatabase1 );
        assertEqualityBetween( taskInformation2, taskInformationFromDatabase2 );

        clearTable( );
    }

    @Test
    public void testInsertTwoTaskInformationToSameTask( )
    {
        ResourceHistory resourceHistory1 = MockResourceHistory.create( );
        ResourceHistory resourceHistory2 = MockResourceHistory.create( );
        ITask task = MockTask.create( );
        TaskInformation taskInformation1 = MockTaskInformation.createWithPiecesOfInformation( resourceHistory1, task );
        TaskInformation taskInformation2 = MockTaskInformation.createWithPiecesOfInformation( resourceHistory2, task );

        insertIntoDabase( taskInformation1 );
        insertIntoDabase( taskInformation2 );

        TaskInformation taskInformationFromDatabase1 = findFromDatabase( taskInformation1 );
        TaskInformation taskInformationFromDatabase2 = findFromDatabase( taskInformation2 );

        assertEqualityBetween( taskInformation1, taskInformationFromDatabase1 );
        assertEqualityBetween( taskInformation2, taskInformationFromDatabase2 );

        clearTable( );
    }
}

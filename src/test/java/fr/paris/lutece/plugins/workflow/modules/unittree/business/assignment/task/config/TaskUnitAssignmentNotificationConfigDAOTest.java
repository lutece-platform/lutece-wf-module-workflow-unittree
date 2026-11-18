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
package fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.config;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.workflow.modules.unittree.util.IdGenerator;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.sql.DAOUtil;
import jakarta.inject.Inject;

/**
 * The subject and the message of the notification are stored in their own columns
 */
public class TaskUnitAssignmentNotificationConfigDAOTest extends LuteceTestCase
{
    private static final String SQL_QUERY_SELECT_COLUMNS = "SELECT subject, message FROM workflow_task_unittree_unit_assignment_notification_cf WHERE id_task = ?";
    private static final String SUBJECT = "The subject";
    private static final String MESSAGE = "<p>The message</p>";

    @Inject
    private TaskUnitAssignmentNotificationConfigDAO _dao;

    @Test
    public void testSubjectAndMessageColumns( )
    {
        TaskUnitAssignmentNotificationConfig config = new TaskUnitAssignmentNotificationConfig( );
        config.setIdTask( IdGenerator.generateId( ) );
        config.setSubject( SUBJECT );
        config.setMessage( MESSAGE );

        try
        {
            _dao.insert( config );
            assertColumns( config.getIdTask( ), SUBJECT, MESSAGE );

            config.setSubject( SUBJECT + " 2" );
            config.setMessage( MESSAGE + "2" );
            _dao.store( config );
            assertColumns( config.getIdTask( ), SUBJECT + " 2", MESSAGE + "2" );

            TaskUnitAssignmentNotificationConfig configLoaded = _dao.load( config.getIdTask( ) );
            assertEquals( SUBJECT + " 2", configLoaded.getSubject( ) );
            assertEquals( MESSAGE + "2", configLoaded.getMessage( ) );
        }
        finally
        {
            _dao.delete( config.getIdTask( ) );
        }
    }

    private void assertColumns( int nIdTask, String strSubject, String strMessage )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_COLUMNS ) )
        {
            daoUtil.setInt( 1, nIdTask );
            daoUtil.executeQuery( );

            assertTrue( daoUtil.next( ) );
            assertEquals( strSubject, daoUtil.getString( 1 ) );
            assertEquals( strMessage, daoUtil.getString( 2 ) );
        }
    }
}

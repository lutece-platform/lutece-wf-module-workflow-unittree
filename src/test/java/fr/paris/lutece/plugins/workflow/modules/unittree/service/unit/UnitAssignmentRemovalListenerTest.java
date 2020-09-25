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
package fr.paris.lutece.plugins.workflow.modules.unittree.service.unit;

import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentHome;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentType;
import fr.paris.lutece.plugins.unittree.service.UnitErrorException;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.MockResource;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.MockUnitAssignment;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.UnitAssignmentDAOTest;
import fr.paris.lutece.plugins.workflow.modules.unittree.util.IdGenerator;
import fr.paris.lutece.test.LuteceTestCase;

public class UnitAssignmentRemovalListenerTest extends LuteceTestCase
{
    private UnitAssignmentDAOTest _dao = new UnitAssignmentDAOTest( );
    private UnitAssignmentRemovalListener _listener = new UnitAssignmentRemovalListener( );

    public void testRemoveUnitWithNoAssignment( ) throws Exception
    {
        _listener.notify( IdGenerator.generateId( ) );
    }

    public void testRemoveUnitWithAssignments( )
    {
        MockResource resource = MockResource.create( );
        int nIdUnit = IdGenerator.generateId( );
        UnitAssignment unitAssignment = MockUnitAssignment.create( resource, MockUnitAssignment.UNIT_ID_UNSET, nIdUnit, UnitAssignmentType.CREATION, true );
        UnitAssignmentHome.create( unitAssignment );

        try
        {
            _listener.notify( nIdUnit );
            fail( "Expected an UnitErrorException to be thrown" );
        }
        catch( UnitErrorException e )
        {
            assertNotNull( e );
        }

        _dao.clearTable( );
    }
}

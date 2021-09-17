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
package fr.paris.lutece.plugins.workflow.modules.unittree.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentHome;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentType;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.MockResource;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.MockUnitAssignment;
import fr.paris.lutece.plugins.workflow.modules.unittree.util.IdGenerator;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.sql.DAOUtil;

public class UnitAssignmentServiceTest extends LuteceTestCase
{
    private static final String SQL_QUERY_CLEAR_TABLE = "DELETE FROM unittree_unit_assignment";

    public void testCurrentAssignmentWhenResourceHasNotBeenAssigned( )
    {
        MockResource resource = MockResource.create( );

        assertThatCurrentAssignmentOfResourceIsNull( resource );

        clearTable( );
    }

    private void assertThatCurrentAssignmentOfResourceIsNull( MockResource resource )
    {
        UnitAssignment unitAssignmentCurrent = UnitAssignmentService.findCurrentAssignment( resource.getId( ), resource.getType( ) );
        assertThat( unitAssignmentCurrent, is( nullValue( ) ) );
    }

    public void testCurrentAssignmentWhenResourceIsInactive( )
    {
        MockResource resource = MockResource.create( );
        int nIdUnit = IdGenerator.generateId( );

        insertAssignmentInDatabase( resource, nIdUnit, UnitAssignmentType.CREATION, false );

        assertThatCurrentAssignmentOfResourceIsNull( resource );

        clearTable( );
    }

    public void testCurrentAssignmentBasicTest( )
    {
        MockResource resource = MockResource.create( );
        int nIdUnit = IdGenerator.generateId( );

        UnitAssignment unitAssignment = insertAssignmentInDatabase( resource, nIdUnit, UnitAssignmentType.CREATION, true );

        assertThatCurrentAssignmentOfResourceIs( resource, unitAssignment );

        clearTable( );
    }

    private UnitAssignment insertAssignmentInDatabase( MockResource resource, int nIdAssignedUnit, UnitAssignmentType assignmentType, boolean bIsActive )
    {
        UnitAssignment unitAssignment = MockUnitAssignment.create( resource, MockUnitAssignment.UNIT_ID_UNSET, nIdAssignedUnit, assignmentType, bIsActive );

        return UnitAssignmentHome.create( unitAssignment );
    }

    private void assertThatCurrentAssignmentOfResourceIs( MockResource resource, UnitAssignment unitAssignment )
    {
        UnitAssignment unitAssignmentCurrent = UnitAssignmentService.findCurrentAssignment( resource.getId( ), resource.getType( ) );
        assertThat( unitAssignmentCurrent.getId( ), is( unitAssignment.getId( ) ) );
    }

    public void testCurrentAssignmentForResourcesWithDifferentTypes( )
    {
        MockResource resource1_1 = MockResource.create( );
        MockResource resource2_1 = MockResource.create( );
        MockResource resource1_2 = MockResource.create( );
        MockResource resource2_2 = MockResource.create( );
        int nIdUnit1 = IdGenerator.generateId( );
        UnitAssignment unitAssignment1_1 = insertAssignmentInDatabase( resource1_1, nIdUnit1, UnitAssignmentType.CREATION, true );
        UnitAssignment unitAssignment2_1 = insertAssignmentInDatabase( resource2_1, nIdUnit1, UnitAssignmentType.CREATION, true );
        UnitAssignment unitAssignment1_2 = insertAssignmentInDatabase( resource1_2, nIdUnit1, UnitAssignmentType.CREATION, true );
        UnitAssignment unitAssignment2_2 = insertAssignmentInDatabase( resource2_2, nIdUnit1, UnitAssignmentType.CREATION, true );

        assertThatCurrentAssignmentOfResourceIs( resource1_1, unitAssignment1_1 );
        assertThatCurrentAssignmentOfResourceIs( resource2_1, unitAssignment2_1 );
        assertThatCurrentAssignmentOfResourceIs( resource1_2, unitAssignment1_2 );
        assertThatCurrentAssignmentOfResourceIs( resource2_2, unitAssignment2_2 );

        clearTable( );
    }

    public void testCurrentAssignmentWhenResourceHasBeenReassigned( )
    {
        MockResource resource = MockResource.create( );
        int nIdUnit1 = IdGenerator.generateId( );
        int nIdUnit2 = IdGenerator.generateId( );
        int nIdUnit3 = IdGenerator.generateId( );
        int nIdUnit4 = IdGenerator.generateId( );
        insertAssignmentInDatabase( resource, nIdUnit2, UnitAssignmentType.ASSIGN_UP, true );
        insertAssignmentInDatabase( resource, nIdUnit1, UnitAssignmentType.ASSIGN_DOWN, true );
        insertAssignmentInDatabase( resource, nIdUnit2, UnitAssignmentType.ASSIGN_UP, true );
        insertAssignmentInDatabase( resource, nIdUnit3, UnitAssignmentType.ASSIGN_UP, true );
        UnitAssignment unitAssignment = insertAssignmentInDatabase( resource, nIdUnit4, UnitAssignmentType.ASSIGN_UP, true );

        assertThatCurrentAssignmentOfResourceIs( resource, unitAssignment );

        insertAssignmentInDatabase( resource, nIdUnit3, UnitAssignmentType.ASSIGN_DOWN, false );
        insertAssignmentInDatabase( resource, nIdUnit2, UnitAssignmentType.ASSIGN_DOWN, false );
        insertAssignmentInDatabase( resource, nIdUnit1, UnitAssignmentType.ASSIGN_DOWN, false );
        unitAssignment = insertAssignmentInDatabase( resource, nIdUnit2, UnitAssignmentType.ASSIGN_UP, true );
        insertAssignmentInDatabase( resource, nIdUnit3, UnitAssignmentType.ASSIGN_UP, false );
        insertAssignmentInDatabase( resource, nIdUnit4, UnitAssignmentType.ASSIGN_UP, false );
        insertAssignmentInDatabase( resource, nIdUnit3, UnitAssignmentType.ASSIGN_DOWN, false );
        insertAssignmentInDatabase( resource, nIdUnit2, UnitAssignmentType.ASSIGN_DOWN, false );

        assertThatCurrentAssignmentOfResourceIs( resource, unitAssignment );

        unitAssignment = insertAssignmentInDatabase( resource, nIdUnit1, UnitAssignmentType.TRANSFER, true );

        assertThatCurrentAssignmentOfResourceIs( resource, unitAssignment );

        clearTable( );
    }

    public void testAssignmentsWhenResourceHasNotBeenAssigned( )
    {
        MockResource resource = MockResource.create( );

        assertThatNumberOfAssignmentsForResourceIs( resource, 0 );

        clearTable( );
    }

    public void testAssignmentsWhenResourceIsInactive( )
    {
        MockResource resource = MockResource.create( );
        int nIdUnit = IdGenerator.generateId( );
        insertAssignmentInDatabase( resource, nIdUnit, UnitAssignmentType.CREATION, false );

        assertThatNumberOfAssignmentsForResourceIs( resource, 0 );

        clearTable( );
    }

    public void testAssignmentsBasicTest( )
    {
        MockResource resource = MockResource.create( );
        int nIdUnit = IdGenerator.generateId( );
        insertAssignmentInDatabase( resource, nIdUnit, UnitAssignmentType.CREATION, true );

        assertThatNumberOfAssignmentsForResourceIs( resource, 1 );

        clearTable( );
    }

    private void assertThatNumberOfAssignmentsForResourceIs( MockResource resource, int nNumberOfAssignments )
    {
        List<UnitAssignment> listUnitAssignment = UnitAssignmentService.findAssignments( resource.getId( ), resource.getType( ) );
        assertThat( listUnitAssignment.size( ), is( nNumberOfAssignments ) );
    }

    public void testAssignmentsForResourcesWithDifferentTypes( )
    {
        MockResource resource1_1 = MockResource.create( );
        MockResource resource2_1 = MockResource.create( );
        MockResource resource1_2 = MockResource.create( );
        MockResource resource2_2 = MockResource.create( );
        int nIdUnit1 = IdGenerator.generateId( );
        int nIdUnit2 = IdGenerator.generateId( );
        insertAssignmentInDatabase( resource1_1, nIdUnit1, UnitAssignmentType.CREATION, true );
        insertAssignmentInDatabase( resource1_1, nIdUnit2, UnitAssignmentType.ASSIGN_UP, true );
        insertAssignmentInDatabase( resource2_1, nIdUnit1, UnitAssignmentType.CREATION, true );
        insertAssignmentInDatabase( resource1_2, nIdUnit1, UnitAssignmentType.CREATION, true );
        insertAssignmentInDatabase( resource2_2, nIdUnit1, UnitAssignmentType.CREATION, true );

        assertThatNumberOfAssignmentsForResourceIs( resource1_1, 2 );
        assertThatNumberOfAssignmentsForResourceIs( resource2_1, 1 );
        assertThatNumberOfAssignmentsForResourceIs( resource1_2, 1 );
        assertThatNumberOfAssignmentsForResourceIs( resource2_2, 1 );

        clearTable( );
    }

    public void testAssignmentsWhenResourceHasBeenReassigned( )
    {
        MockResource resource = MockResource.create( );
        int nIdUnit1 = IdGenerator.generateId( );
        int nIdUnit2 = IdGenerator.generateId( );
        int nIdUnit3 = IdGenerator.generateId( );
        int nIdUnit4 = IdGenerator.generateId( );
        insertAssignmentInDatabase( resource, nIdUnit2, UnitAssignmentType.ASSIGN_UP, true );
        insertAssignmentInDatabase( resource, nIdUnit1, UnitAssignmentType.ASSIGN_DOWN, true );
        insertAssignmentInDatabase( resource, nIdUnit2, UnitAssignmentType.ASSIGN_UP, true );
        insertAssignmentInDatabase( resource, nIdUnit3, UnitAssignmentType.ASSIGN_UP, true );
        insertAssignmentInDatabase( resource, nIdUnit4, UnitAssignmentType.ASSIGN_UP, true );

        assertThatNumberOfAssignmentsForResourceIs( resource, 5 );

        insertAssignmentInDatabase( resource, nIdUnit3, UnitAssignmentType.ASSIGN_DOWN, false );
        insertAssignmentInDatabase( resource, nIdUnit2, UnitAssignmentType.ASSIGN_DOWN, false );
        insertAssignmentInDatabase( resource, nIdUnit1, UnitAssignmentType.ASSIGN_DOWN, false );
        insertAssignmentInDatabase( resource, nIdUnit2, UnitAssignmentType.ASSIGN_UP, true );
        insertAssignmentInDatabase( resource, nIdUnit3, UnitAssignmentType.ASSIGN_UP, false );
        insertAssignmentInDatabase( resource, nIdUnit4, UnitAssignmentType.ASSIGN_UP, false );
        insertAssignmentInDatabase( resource, nIdUnit3, UnitAssignmentType.ASSIGN_DOWN, false );
        insertAssignmentInDatabase( resource, nIdUnit2, UnitAssignmentType.ASSIGN_DOWN, false );

        assertThatNumberOfAssignmentsForResourceIs( resource, 6 );

        insertAssignmentInDatabase( resource, nIdUnit1, UnitAssignmentType.TRANSFER, true );

        assertThatNumberOfAssignmentsForResourceIs( resource, 7 );

        clearTable( );
    }

    private void clearTable( )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CLEAR_TABLE, WorkflowUnittreePlugin.getPlugin( ) ) )
        {
            daoUtil.executeUpdate( );
        }
    }
}

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
package fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentDAO;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentHome;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentType;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.WorkflowUnittreePlugin;
import fr.paris.lutece.plugins.workflow.modules.unittree.util.IdGenerator;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.sql.DAOUtil;

public class UnitAssignmentDAOTest extends LuteceTestCase
{
    private static final String SQL_QUERY_CLEAR_TABLE = "DELETE FROM unittree_unit_assignment";

    private static final Plugin _plugin = WorkflowUnittreePlugin.getPlugin( );

    private UnitAssignmentDAO _dao = new UnitAssignmentDAO( );

    public void testDeactivationOnActiveAssignment( )
    {
        MockResource resource = MockResource.create( );
        UnitAssignment unitAssignment = insertAssignmentInDatabase( resource, true );

        _dao.deactivate( unitAssignment, _plugin );

        assertThatAssignmentIsNotActive( resource );

        clearTable( );

    }

    private UnitAssignment insertAssignmentInDatabase( MockResource resource, boolean bActive )
    {
        int nIdUnit = IdGenerator.generateId( );
        UnitAssignment unitAssignment = MockUnitAssignment.create( resource, MockUnitAssignment.UNIT_ID_UNSET, nIdUnit, UnitAssignmentType.CREATION, bActive );
        UnitAssignmentHome.create( unitAssignment );

        return unitAssignment;
    }

    private void assertThatAssignmentIsNotActive( MockResource resource )
    {
        List<UnitAssignment> listUnitAssignments = _dao.selectByResource( resource.getId( ), resource.getType( ), _plugin );
        assertThat( listUnitAssignments.get( 0 ).isActive( ), is( false ) );
    }

    public void testDeactivationOnInactiveAssignment( )
    {
        MockResource resource = MockResource.create( );
        UnitAssignment unitAssignment = insertAssignmentInDatabase( resource, false );

        _dao.deactivate( unitAssignment, _plugin );

        assertThatAssignmentIsNotActive( resource );

        clearTable( );

    }

    public void clearTable( )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CLEAR_TABLE, WorkflowUnittreePlugin.getPlugin( ) ) )
        {
            daoUtil.executeUpdate( );
        }
    }
}

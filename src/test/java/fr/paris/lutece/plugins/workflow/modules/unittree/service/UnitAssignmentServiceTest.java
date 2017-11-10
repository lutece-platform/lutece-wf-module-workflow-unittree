package fr.paris.lutece.plugins.workflow.modules.unittree.service;

import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.UnitAssignmentHome;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.UnitAssignmentType;
import fr.paris.lutece.test.LuteceTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

/**
 * This class tests the {@link  UnitAssignmentService} class
 *
 */
public class UnitAssignmentServiceTest extends LuteceTestCase
{
    private static final int RESOURCE_ID_1 = 1;
    private static final int RESOURCE_ID_2 = 2;
    private static final int RESOURCE_ID_3 = 3;
    private static final int RESOURCE_ID_4 = 4;
    private static final String RESOURCE_TYPE_1 = "RESOURCE_TYPE_1";
    private static final String RESOURCE_TYPE_2 = "RESOURCE_TYPE_2";
    private static final String RESOURCE_TYPE_3 = "RESOURCE_TYPE_3";
    private static final String RESOURCE_TYPE_4 = "RESOURCE_TYPE_4";
    private static final int UNIT_ID_UNSET = -1;
    private static final int UNIT_ID_1 = 1;
    private static final int UNIT_ID_2 = 2;
    private static final int UNIT_ID_3 = 3;
    private static final int UNIT_ID_4 = 4;
    private static final int UNIT_ID_5 = 5;

    /**
     * Tests the {@link UnitAssignmentService#findCurrentAssignment(int, String)} method
     */
    public void testFindCurrentAssignment( )
    {
        UnitAssignment unitAssignment1_1 = create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_1, UnitAssignmentType.CREATION, true );
        testCurrentAssignment( RESOURCE_ID_1, RESOURCE_TYPE_1, unitAssignment1_1 );

        unitAssignment1_1 = create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_2, UnitAssignmentType.ASSIGN_UP, true );
        testCurrentAssignment( RESOURCE_ID_1, RESOURCE_TYPE_1, unitAssignment1_1 );

        UnitAssignment unitAssignment2_1 = create( RESOURCE_ID_2, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_1, UnitAssignmentType.CREATION, true );
        UnitAssignment unitAssignment1_2 = create( RESOURCE_ID_1, RESOURCE_TYPE_2, UNIT_ID_UNSET, UNIT_ID_1, UnitAssignmentType.CREATION, true );
        UnitAssignment unitAssignment2_2 = create( RESOURCE_ID_2, RESOURCE_TYPE_2, UNIT_ID_UNSET, UNIT_ID_1, UnitAssignmentType.CREATION, true );
        testCurrentAssignment( RESOURCE_ID_1, RESOURCE_TYPE_1, unitAssignment1_1 );
        testCurrentAssignment( RESOURCE_ID_2, RESOURCE_TYPE_1, unitAssignment2_1 );
        testCurrentAssignment( RESOURCE_ID_1, RESOURCE_TYPE_2, unitAssignment1_2 );
        testCurrentAssignment( RESOURCE_ID_2, RESOURCE_TYPE_2, unitAssignment2_2 );

        create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_3, UnitAssignmentType.ASSIGN_UP, true );
        create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_2, UnitAssignmentType.ASSIGN_DOWN, true );
        create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_3, UnitAssignmentType.ASSIGN_UP, true );
        create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_4, UnitAssignmentType.ASSIGN_UP, true );
        unitAssignment1_1 = create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_5, UnitAssignmentType.ASSIGN_UP, true );
        testCurrentAssignment( RESOURCE_ID_1, RESOURCE_TYPE_1, unitAssignment1_1 );

        create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_4, UnitAssignmentType.ASSIGN_DOWN, false );
        create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_3, UnitAssignmentType.ASSIGN_DOWN, false );
        create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_2, UnitAssignmentType.ASSIGN_DOWN, false );
        unitAssignment1_1 = create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_3, UnitAssignmentType.ASSIGN_UP, true );
        create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_4, UnitAssignmentType.ASSIGN_UP, false );
        create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_5, UnitAssignmentType.ASSIGN_UP, false );
        create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_4, UnitAssignmentType.ASSIGN_DOWN, false );
        create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_3, UnitAssignmentType.ASSIGN_DOWN, false );
        testCurrentAssignment( RESOURCE_ID_1, RESOURCE_TYPE_1, unitAssignment1_1 );

        unitAssignment1_1 = create( RESOURCE_ID_1, RESOURCE_TYPE_1, UNIT_ID_UNSET, UNIT_ID_2, UnitAssignmentType.TRANSFER, true );
        testCurrentAssignment( RESOURCE_ID_1, RESOURCE_TYPE_1, unitAssignment1_1 );

    }

    /**
     * Tests the {@link UnitAssignmentService#findAssignments(int, String)} method
     */
    public void testFindAssignments( )
    {
        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_1, UnitAssignmentType.CREATION, true );
        testAssignments( RESOURCE_ID_3, RESOURCE_TYPE_3, 1 );

        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_2, UnitAssignmentType.ASSIGN_UP, true );
        testAssignments( RESOURCE_ID_3, RESOURCE_TYPE_3, 2 );

        create( RESOURCE_ID_4, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_1, UnitAssignmentType.CREATION, true );
        create( RESOURCE_ID_3, RESOURCE_TYPE_4, UNIT_ID_UNSET, UNIT_ID_1, UnitAssignmentType.CREATION, true );
        create( RESOURCE_ID_4, RESOURCE_TYPE_4, UNIT_ID_UNSET, UNIT_ID_1, UnitAssignmentType.CREATION, true );
        testAssignments( RESOURCE_ID_3, RESOURCE_TYPE_3, 2 );
        testAssignments( RESOURCE_ID_4, RESOURCE_TYPE_3, 1 );
        testAssignments( RESOURCE_ID_3, RESOURCE_TYPE_4, 1 );
        testAssignments( RESOURCE_ID_4, RESOURCE_TYPE_4, 1 );

        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_3, UnitAssignmentType.ASSIGN_UP, true );
        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_2, UnitAssignmentType.ASSIGN_DOWN, true );
        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_3, UnitAssignmentType.ASSIGN_UP, true );
        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_4, UnitAssignmentType.ASSIGN_UP, true );
        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_5, UnitAssignmentType.ASSIGN_UP, true );
        testAssignments( RESOURCE_ID_3, RESOURCE_TYPE_3, 7 );

        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_4, UnitAssignmentType.ASSIGN_DOWN, false );
        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_3, UnitAssignmentType.ASSIGN_DOWN, false );
        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_2, UnitAssignmentType.ASSIGN_DOWN, false );
        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_3, UnitAssignmentType.ASSIGN_UP, true );
        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_4, UnitAssignmentType.ASSIGN_UP, false );
        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_5, UnitAssignmentType.ASSIGN_UP, false );
        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_4, UnitAssignmentType.ASSIGN_DOWN, false );
        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_3, UnitAssignmentType.ASSIGN_DOWN, false );
        testAssignments( RESOURCE_ID_3, RESOURCE_TYPE_3, 8 );

        create( RESOURCE_ID_3, RESOURCE_TYPE_3, UNIT_ID_UNSET, UNIT_ID_2, UnitAssignmentType.TRANSFER, true );
        testAssignments( RESOURCE_ID_3, RESOURCE_TYPE_3, 9 );
    }

    /**
     * Creates a unit assignment
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdAssignorUnit the assignor unit id
     * @param nIdAssignedUnit the assigned unit id
     * @param assignmentType the assignment type
     * @param bIsActive {@code true} if the unit assignment is active, {@code false} otherwise
     * @return the created unit assignment
     */
    private UnitAssignment create( int nIdResource, String strResourceType, int nIdAssignorUnit, int nIdAssignedUnit, UnitAssignmentType assignmentType,
            boolean bIsActive )
    {
        UnitAssignment unitAssignment = new UnitAssignment( );
        unitAssignment.setIdResource( nIdResource );
        unitAssignment.setResourceType( strResourceType );
        unitAssignment.setIdAssignorUnit( nIdAssignorUnit );
        unitAssignment.setIdAssignedUnit( nIdAssignedUnit );
        unitAssignment.setAssignmentType( assignmentType );
        unitAssignment.setActive( bIsActive );

        return UnitAssignmentHome.create( unitAssignment );
    }

    /**
     * Tests if the current unit assignment of the specified couple {resource id, resource type} is the specified unit assignment
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param unitAssignment the unit assignment
     */
    private void testCurrentAssignment( int nIdResource, String strResourceType, UnitAssignment unitAssignment )
    {
        UnitAssignment unitAssignmentCurrent = UnitAssignmentService.findCurrentAssignment( nIdResource, strResourceType );
        assertThat( unitAssignmentCurrent.getId( ), is( unitAssignment.getId( ) ) );
    }

    /**
     * Tests if the size of the list of unit assignments of the specified couple {resource id, resource type} is equal to the specified number
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nNumberOfAssignments the number of assignments
     */
    private void testAssignments( int nIdResource, String strResourceType, int nNumberOfAssignments )
    {
        List<UnitAssignment> listUnitAssignment = UnitAssignmentService.findAssignments( nIdResource, strResourceType );
        assertThat( listUnitAssignment.size( ), is( nNumberOfAssignments ) );
    }
}

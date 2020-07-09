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
package fr.paris.lutece.plugins.workflow.modules.unittree.service.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;

import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentHome;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentType;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.exception.AssignmentNotPossibleException;
import fr.paris.lutece.plugins.unittree.service.selection.IUnitSelection;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.config.TaskUnitAssignmentConfig;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.task.information.TaskInformation;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.task.information.TaskInformationHome;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.UnitSelectionService;
import fr.paris.lutece.plugins.workflow.modules.unittree.util.ChangeUnitEventPublisher;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.util.AppException;

/**
 * This class is an abstract task to assign a resource to a unit
 *
 */
public abstract class AbstractTaskUnitAssignment extends SimpleTask
{
    public static final int UNSET_ASSIGNED_UNIT_ID = -1;

    // Informations
    private static final String TASK_INFORMATION_ASSIGNED_UNIT = "ASSIGNED_UNIT";
    private static final String TASK_INFORMATION_ASSIGNOR_UNIT = "ASSIGNOR_UNIT";

    private static final int UNFOUND_INDEX = UNSET_ASSIGNED_UNIT_ID;

    // Services
    @Inject
    private IUnitService _unitService;
    @Inject
    @Named( "workflow-unittree.taskUnitAssignmentConfigService" )
    private ITaskConfigService _taskConfigService;
    @Inject
    private IResourceHistoryService _resourceHistoryService;
    @Inject
    private ChangeUnitEventPublisher _publisher;

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );

        if ( resourceHistory != null )
        {
            try
            {
                IUnitSelection unitSelection = fetchUnitSelection( request );

                if ( unitSelection == null )
                {
                    throw new AppException( "There is no unit selection" );
                }

                int nIdUnit = unitSelection.select( resourceHistory.getIdResource( ), resourceHistory.getResourceType( ), request, this );

                Unit unitAssigned = _unitService.getUnit( nIdUnit, false );

                if ( unitAssigned == null )
                {
                    throw new AppException( "The target unit does not exist" );
                }

                List<UnitAssignment> listUnitAssignment = UnitAssignmentHome.findByResource( resourceHistory.getIdResource( ),
                        resourceHistory.getResourceType( ) );
                Unit unitAssignor = findAssignorUnit( listUnitAssignment );
                TaskUnitAssignmentConfig config = getConfig( );
                UnitAssignmentType unitAssignmentType = UnitAssignmentType.findByCode( config.getAssignmentType( ) );

                UnitAssignment unitAssignment = createUnitAssignment( resourceHistory.getIdResource( ), resourceHistory.getResourceType( ), unitAssignmentType,
                        unitAssignor, unitAssigned );
                manageUnitAssignments( listUnitAssignment, unitAssignment );

                saveTaskInformation( nIdResourceHistory, unitAssigned, unitAssignor );
            }
            catch( AssignmentNotPossibleException e )
            {
                throw new AppException( e.getMessage( ), e );
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveConfig( )
    {
        TaskUnitAssignmentConfig config = getConfig( );

        if ( config != null )
        {
            for ( String strUnitSelectionId : config.getUnitSelections( ) )
            {
                IUnitSelection unitSelection = UnitSelectionService.getInstance( ).find( strUnitSelectionId );

                if ( unitSelection != null )
                {
                    unitSelection.getConfigurationHandler( ).removeConfiguration( this );
                }
            }
        }

        _taskConfigService.remove( this.getId( ) );
    }

    /**
     * Gives the configuration associated to this task
     * 
     * @return the configuration
     */
    protected TaskUnitAssignmentConfig getConfig( )
    {
        return _taskConfigService.findByPrimaryKey( getId( ) );
    }

    /**
     * <p>
     * Finds the assignor unit among the specified unit assignment list.
     * </p>
     * <p>
     * The assignor unit is the assigned unit of the last activated unit assignment.
     * </p>
     * 
     * @param listUnitAssignment
     *            the unit assignment list
     * @return the assignor unit
     */
    private Unit findAssignorUnit( List<UnitAssignment> listUnitAssignment )
    {
        Unit unit = null;

        for ( int i = listUnitAssignment.size( ) - 1; i >= 0; i-- )
        {
            UnitAssignment unitAssignment = listUnitAssignment.get( i );
            if ( unitAssignment.isActive( ) )
            {
                unit = unitAssignment.getAssignedUnit( );
                break;
            }
        }

        return unit;
    }

    /**
     * Creates a unit assignment
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param unitAssignmentType
     *            the assignment type
     * @param unitAssignor
     *            the assignor unit
     * @param unitAssigned
     *            the assigned unit
     * @return the created unit assignment
     */
    private UnitAssignment createUnitAssignment( int nIdResource, String strResourceType, UnitAssignmentType unitAssignmentType, Unit unitAssignor,
            Unit unitAssigned )
    {
        UnitAssignment unitAssignment = new UnitAssignment( );
        unitAssignment.setIdResource( nIdResource );
        unitAssignment.setResourceType( strResourceType );
        unitAssignment.setIdAssignedUnit( unitAssigned.getIdUnit( ) );

        if ( unitAssignor == null )
        {
            unitAssignment.setIdAssignorUnit( UNSET_ASSIGNED_UNIT_ID );
        }
        else
        {
            unitAssignment.setIdAssignorUnit( unitAssignor.getIdUnit( ) );
        }

        unitAssignment.setAssignmentType( unitAssignmentType );
        unitAssignment.setActive( true );

        return unitAssignment;
    }

    /**
     * Manages the unit assignments (creation, activation / deactivation)
     * 
     * @param listUnitAssignment
     *            the unit assignment already present
     * @param unitAssignmentNew
     *            the new unit assignment
     */
    private void manageUnitAssignments( List<UnitAssignment> listUnitAssignment, UnitAssignment unitAssignmentNew )
    {
        List<UnitAssignment> listUnitAssignmentToDeactivate = buildUnitAssignmentListToDeactivate( listUnitAssignment, unitAssignmentNew );

        if ( UnitAssignmentType.ASSIGN_DOWN == unitAssignmentNew.getAssignmentType( ) && listUnitAssignmentToDeactivate.isEmpty( ) )
        {
            throw new AppException( "Cannot assign down to a unit which has not previously been assigned by assign up" );
        }

        deactivateUnitAssignments( listUnitAssignmentToDeactivate );
        if ( CollectionUtils.isNotEmpty( listUnitAssignmentToDeactivate ) )
        {
            _publisher.publish( listUnitAssignmentToDeactivate );
        }

        // ASSIGN DOWN are not recorded
        if ( UnitAssignmentType.ASSIGN_DOWN != unitAssignmentNew.getAssignmentType( ) )
        {
            UnitAssignmentHome.create( unitAssignmentNew );
        }
    }

    /**
     * Builds the list of unit assignments to deactivate
     * 
     * @param listUnitAssignment
     *            the unit assignment already present
     * @param unitAssignmentNew
     *            the new unit assignment
     * @return the list of unit assignments to deactivate
     */
    private List<UnitAssignment> buildUnitAssignmentListToDeactivate( List<UnitAssignment> listUnitAssignment, UnitAssignment unitAssignmentNew )
    {
        List<UnitAssignment> result = new ArrayList<>( );
        UnitAssignmentType assignmentType = unitAssignmentNew.getAssignmentType( );

        if ( UnitAssignmentType.CREATION == assignmentType || UnitAssignmentType.TRANSFER == assignmentType )
        {
            result.addAll( listUnitAssignment
                    .stream( )
                    .filter( UnitAssignment::isActive )
                    .collect( Collectors.toList( ) ) );
            
        }

        if ( UnitAssignmentType.ASSIGN_DOWN == assignmentType || UnitAssignmentType.ASSIGN_UP == assignmentType )
        {
            List<UnitAssignment> listTmp = new ArrayList<>( );

            // First, finds assignments since the last CREATION or TRANSFER
            for ( UnitAssignment unitAssignment : listUnitAssignment )
            {
                if ( UnitAssignmentType.CREATION == unitAssignment.getAssignmentType( ) || UnitAssignmentType.TRANSFER == unitAssignment.getAssignmentType( ) )
                {
                    listTmp.clear( );
                }

                listTmp.add( unitAssignment );
            }

            // Second, finds the index of the original assignment
            int nIndex = UNFOUND_INDEX;

            for ( int i = 0; i < listTmp.size( ); i++ )
            {
                UnitAssignment unitAssignment = listTmp.get( i );

                if ( unitAssignment.isActive( ) && unitAssignment.getAssignedUnit( ).getIdUnit( ) == unitAssignmentNew.getAssignedUnit( ).getIdUnit( ) )
                {
                    nIndex = i;
                    break;
                }
            }

            // Third, keeps only assignments before the current assignment
            if ( UNFOUND_INDEX != nIndex )
            {
                // The assignment is already in the list, the new unit assignment must be deactivated to use the original assignment
                unitAssignmentNew.setActive( false );

                // Do not include the original assignment
                nIndex++;

                for ( int i = nIndex; i < listTmp.size( ); i++ )
                {
                    result.add( listTmp.get( i ) );
                }
            }
        }

        return result;
    }

    /**
     * Deactivates the specified unit assignments
     * 
     * @param listUnitAssignment
     *            the list of unit assignments to deactivate
     */
    private void deactivateUnitAssignments( List<UnitAssignment> listUnitAssignment )
    {
        for ( UnitAssignment unitAssignment : listUnitAssignment )
        {
            UnitAssignmentHome.deactivate( unitAssignment );
        }
    }

    /**
     * Saves the task information
     * 
     * @param nIdResourceHistory
     *            the resource history id
     * @param unitAssigned
     *            the assigned unit
     * @param unitAssignor
     *            the assignor unit
     */
    private void saveTaskInformation( int nIdResourceHistory, Unit unitAssigned, Unit unitAssignor )
    {
        TaskInformation taskInformation = new TaskInformation( nIdResourceHistory, getId( ) );
        taskInformation.add( TASK_INFORMATION_ASSIGNED_UNIT, unitAssigned.getLabel( ) );

        if ( unitAssignor != null )
        {
            taskInformation.add( TASK_INFORMATION_ASSIGNOR_UNIT, unitAssignor.getLabel( ) );
        }

        TaskInformationHome.create( taskInformation );
    }

    /**
     * Gives the unit selection to use
     * 
     * @param request
     *            the request which can contain information to fetch the unit selection
     * @return the unit selection
     */
    protected abstract IUnitSelection fetchUnitSelection( HttpServletRequest request );

}

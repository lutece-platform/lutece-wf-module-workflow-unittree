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
package fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection;

import java.util.List;

import fr.paris.lutece.plugins.unittree.service.selection.IUnitSelection;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * <p>
 * This class is a service for unit selection
 * </p>
 * <p>
 * Designed as a singleton
 * </p>
 *
 */
public final class UnitSelectionService
{
    private final List<IUnitSelection> _listUnitSelections;

    /**
     * Constructor
     */
    private UnitSelectionService( )
    {
        _listUnitSelections = SpringContextService.getBeansOfType( IUnitSelection.class );
    }

    /**
     * Gives the instance of the singleton
     * 
     * @return the instance
     */
    public static UnitSelectionService getInstance( )
    {
        return UnitSelectionServiceHolder._instance;
    }

    /**
     * Gives all unit selections
     * 
     * @return the unit selections
     */
    public List<IUnitSelection> getUnitSelections( )
    {
        return _listUnitSelections;
    }

    /**
     * Finds a unit selection with the specified id
     * 
     * @param strId
     *            the unit selection id
     * @return the unit selection
     */
    public IUnitSelection find( String strId )
    {
        IUnitSelection unitSelection = null;

        for ( IUnitSelection unitSelectionItem : _listUnitSelections )
        {
            if ( unitSelectionItem.getId( ).equals( strId ) )
            {
                unitSelection = unitSelectionItem;
                break;
            }
        }

        return unitSelection;
    }

    /**
     * This class holds the {@link UnitSelectionService} instance
     *
     */
    private static class UnitSelectionServiceHolder
    {
        private static UnitSelectionService _instance = new UnitSelectionService( );
    }
}

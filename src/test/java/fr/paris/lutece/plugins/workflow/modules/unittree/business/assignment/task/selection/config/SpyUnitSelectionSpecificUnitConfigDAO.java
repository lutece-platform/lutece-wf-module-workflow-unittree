/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.selection.config;

import java.util.HashMap;
import java.util.Map;

import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;

public class SpyUnitSelectionSpecificUnitConfigDAO implements ITaskConfigDAO<UnitSelectionSpecificUnitConfig>
{
    private final Map<Integer, UnitSelectionSpecificUnitConfig> _map = new HashMap<>( );
    private boolean _bIsSpyingStarted;
    private boolean _bIsInsertCalled;
    private boolean _bIsStoreCalled;
    private boolean _bIsDeleteCalled;

    @Override
    public void delete( int nIdTask )
    {
        _map.remove( nIdTask );

        if ( _bIsSpyingStarted )
        {
            _bIsDeleteCalled = true;
        }
    }

    @Override
    public void insert( UnitSelectionSpecificUnitConfig config )
    {
        _map.put( config.getIdTask( ), config );

        if ( _bIsSpyingStarted )
        {
            _bIsInsertCalled = true;
        }
    }

    @Override
    public UnitSelectionSpecificUnitConfig load( int nIdTask )
    {
        return _map.get( nIdTask );
    }

    @Override
    public void store( UnitSelectionSpecificUnitConfig config )
    {
        _map.put( config.getIdTask( ), config );

        if ( _bIsSpyingStarted )
        {
            _bIsStoreCalled = true;
        }
    }

    public void startSpying( )
    {
        _bIsSpyingStarted = true;
    }

    public boolean isInsertCalled( )
    {
        return _bIsInsertCalled;
    }

    public boolean isStoreCalled( )
    {
        return _bIsStoreCalled;
    }

    public boolean isDeleteCalled( )
    {
        return _bIsDeleteCalled;
    }

}

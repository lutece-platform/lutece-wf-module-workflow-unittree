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
package fr.paris.lutece.plugins.unittree.service.unit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Source;

import fr.paris.lutece.plugins.unittree.business.action.IAction;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.UnitErrorException;
import fr.paris.lutece.plugins.unittree.service.rbac.UnittreeRBACRecursiveType;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.util.ReferenceList;

public class FakeUnitService implements IUnitService
{

    @Override
    public boolean canCreateSubUnit( int arg0 )
    {
        return false;
    }

    @Override
    public int createUnit( Unit arg0, HttpServletRequest arg1 ) throws UnitErrorException
    {
        return 0;
    }

    @Override
    public List<Unit> findBySectorId( int arg0 )
    {
        return new ArrayList<>( );
    }

    @Override
    public List<Unit> getAllSubUnits( Unit arg0, boolean arg1 )
    {
        return new ArrayList<>( );
    }

    @Override
    public List<Unit> getAllUnits( boolean arg0 )
    {
        return new ArrayList<>( );
    }

    @Override
    public List<IAction> getListActions( String arg0, Locale arg1, Unit arg2, AdminUser arg3, UnittreeRBACRecursiveType arg4 )
    {
        return new ArrayList<>( );
    }

    @Override
    public List<Unit> getListParentUnits( Unit arg0 )
    {
        return new ArrayList<>( );
    }

    @Override
    public Unit getRootUnit( boolean arg0 )
    {
        return null;
    }

    @Override
    public List<Unit> getSubUnits( int arg0, boolean arg1 )
    {
        return new ArrayList<>( );
    }

    @Override
    public ReferenceList getSubUnitsAsReferenceList( int arg0, Locale arg1 )
    {
        return null;
    }

    @Override
    public Source getTreeXsl( )
    {
        return null;
    }

    @Override
    public Unit getUnit( int arg0, boolean arg1 )
    {
        return null;
    }

    @Override
    public List<Unit> getUnitWithNoChildren( )
    {
        return new ArrayList<>( );
    }

    @Override
    public List<Unit> getUnitsByIdUser( int arg0, boolean arg1 )
    {
        return new ArrayList<>( );
    }

    @Override
    public List<Unit> getUnitsFirstLevel( boolean arg0 )
    {
        return new ArrayList<>( );
    }

    @Override
    public String getXMLUnits( AdminUser arg0 )
    {
        return null;
    }

    @Override
    public boolean hasSubUnits( int arg0 )
    {
        return false;
    }

    @Override
    public boolean isAuthorized( Unit arg0, String arg1, AdminUser arg2, UnittreeRBACRecursiveType arg3 )
    {
        return true;
    }

    @Override
    public boolean isAuthorized( String arg0, String arg1, AdminUser arg2, UnittreeRBACRecursiveType arg3 )
    {
        return true;
    }

    @Override
    public boolean isParent( Unit arg0, Unit arg1 )
    {
        return false;
    }

    @Override
    public boolean isUnitInList( Unit arg0, Collection<Unit> arg1 )
    {
        return false;
    }

    @Override
    public boolean moveSubTree( Unit arg0, Unit arg1 )
    {
        return false;
    }

    @Override
    public void removeUnit( int arg0, HttpServletRequest arg1 )
    {
        // Nothing to do
    }

    @Override
    public void updateUnit( Unit arg0, HttpServletRequest arg1 ) throws UnitErrorException
    {
        // Nothing to do
    }

}

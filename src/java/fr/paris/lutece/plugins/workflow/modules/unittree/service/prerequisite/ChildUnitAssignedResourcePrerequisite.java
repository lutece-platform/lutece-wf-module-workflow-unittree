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
package fr.paris.lutece.plugins.workflow.modules.unittree.service.prerequisite;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentHome;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.business.unit.UnitHome;
import fr.paris.lutece.plugins.workflow.service.prerequisite.IManualActionPrerequisiteService;
import fr.paris.lutece.plugins.workflowcore.business.prerequisite.DefaultPrerequisiteConfig;
import fr.paris.lutece.plugins.workflowcore.business.prerequisite.IPrerequisiteConfig;
import fr.paris.lutece.portal.business.user.AdminUser;

public class ChildUnitAssignedResourcePrerequisite implements IManualActionPrerequisiteService
{

    private static final String PREREQUISITE_TITLE_I18N = "module.workflow.unittree.prerequisite.unit.child.resource.title";
    private static final String PREREQUISITE_TYPE = "module.workflow.unittree.prerequisite.unit.child.resource";

    public String getTitleI18nKey( )
    {
        return PREREQUISITE_TITLE_I18N;
    }

    public String getPrerequisiteType( )
    {
        return PREREQUISITE_TYPE;
    }

    @Override
    public boolean hasConfiguration( )
    {
        return false;
    }

    @Override
    public IPrerequisiteConfig getEmptyConfiguration( )
    {
        return new DefaultPrerequisiteConfig( );
    }

    @Override
    public String getConfigurationDaoBeanName( )
    {
        return null;
    }

    @Override
    public String getConfigHtml( IPrerequisiteConfig config, HttpServletRequest request, Locale locale )
    {
        return null;
    }

    @Override
    public boolean canActionBePerformed( int nIdResource, String strResourceType, IPrerequisiteConfig config, int nIdAction )
    {
        return false;
    }

    @Override
    public boolean canManualActionBePerformed( User user, int nIdResource, String strResourceType, IPrerequisiteConfig config, int nIdAction )
    {

        if ( !( user instanceof AdminUser ) )
        {
            return false;
        }

        List<Unit> userUnitList = UnitHome.findByIdUser( ( (AdminUser) user ).getUserId( ) );
        Set<Integer> unitIdList = new HashSet<>( );
        for ( Unit userUnit : userUnitList )
        {
            unitIdList.addAll( UnitHome.getAllSubUnitsId( userUnit.getIdUnit( ) ) );
        }

        List<UnitAssignment> resourceUnitList = UnitAssignmentHome.findByResource( nIdResource, strResourceType );

        for ( UnitAssignment resourceUnit : resourceUnitList )
        {
            if ( resourceUnit.isActive( ) && unitIdList.contains( resourceUnit.getIdAssignedUnit( ) ) )
            {
                return true;
            }
        }
        return false;
    }
}

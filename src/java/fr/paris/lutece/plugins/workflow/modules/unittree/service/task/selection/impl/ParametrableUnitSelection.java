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
package fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.unittree.exception.AssignmentNotPossibleException;
import fr.paris.lutece.plugins.unittree.service.selection.IConfigurationHandler;
import fr.paris.lutece.plugins.unittree.service.selection.IParametrableConfigurationHandler;
import fr.paris.lutece.plugins.unittree.service.selection.IParametrableUnitSelection;
import fr.paris.lutece.plugins.unittree.service.selection.ITaskFormHandler;
import fr.paris.lutece.plugins.unittree.service.selection.IUnitSelection;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.task.selection.config.ParametrableUnitSelectionConfig;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

public class ParametrableUnitSelection implements IUnitSelection
{
    private static final String ID = "ParametrableUnitSelection";

    @Inject
    private ITaskConfigDAO<ParametrableUnitSelectionConfig> _configDAO;

    private final IConfigurationHandler _configurationHandler = new ConfigurationHandler( );

    @Override
    public String getId( )
    {
        return ID;
    }

    @Override
    public boolean isAutomatic( )
    {
        return true;
    }

    @Override
    public IConfigurationHandler getConfigurationHandler( )
    {
        return _configurationHandler;
    }

    @Override
    public ITaskFormHandler getTaskFormHandler( )
    {
        return null;
    }

    @Override
    public int select( int nIdResource, String strResourceType, HttpServletRequest request, ITask task ) throws AssignmentNotPossibleException
    {
        ParametrableUnitSelectionConfig config = _configDAO.load( task.getId( ) );
        if ( config == null )
        {
            throw new AssignmentNotPossibleException( "The selection has not been configured" );
        }

        IParametrableUnitSelection selectionConfig = config.getParametrableSelectionConfig( );
        if ( selectionConfig == null )
        {
            return -1;
        }

        return selectionConfig.select( nIdResource, strResourceType, request, task );
    }

    /**
     * This class is a configuration handler for the {@link ParametrableUnitSelection} class
     *
     */
    private class ConfigurationHandler implements IConfigurationHandler
    {

        // Messages
        private static final String MESSAGE_TITLE = "module.workflow.unittree.unit_selection.parametrable.config.title";

        // Markers
        private static final String MARK_HTML_CUSTOM_CONFIG = "html_custom_config";
        private static final String MARK_CUSTOM_CONFIG = "custom_config";
        private static final String MARK_LIST_CUSTOM_CONFIG = "list_custom_config";
        private static final String MARK_IS_ADD_CONFIG_DISPLAYED = "is_add_config_displayed";

        // Parameters
        private static final String PARAMETER_SELECTION_CONFIG = "unit_selection_config";
        private static final String PARAMETER_APPLY = "apply";
        private static final String PARAMETER_VALUE_CHOOSE_CONFIG = "choose_parametrable_config";
        // Templates
        private static final String TEMPLATE_CONFIGURATION = "admin/plugins/workflow/modules/unittree/unitselection/config/unit_selection_parametrable_configuration.html";

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( Locale locale )
        {
            return I18nService.getLocalizedString( MESSAGE_TITLE, locale );
        }

        @Override
        public String getDisplayedForm( Locale locale, ITask task )
        {
            Map<String, Object> model = new HashMap<>( );
            ParametrableUnitSelectionConfig config = loadConfig( task );
            model.put( MARK_LIST_CUSTOM_CONFIG, getParametrableConfigurationHandlerList( locale ) );

            if ( config.getParametrableSelectionConfigHandler( ) != null )
            {
                model.put( MARK_CUSTOM_CONFIG, config.getParametrableConfigHandler( ) );
                model.put( MARK_HTML_CUSTOM_CONFIG, config.getParametrableSelectionConfigHandler( ).getCustomConfigForm( locale, task ) );
                model.put( MARK_IS_ADD_CONFIG_DISPLAYED, false );
            }
            else
            {
                model.put( MARK_CUSTOM_CONFIG, "" );
                model.put( MARK_HTML_CUSTOM_CONFIG, "" );
                model.put( MARK_IS_ADD_CONFIG_DISPLAYED, true );
            }
            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONFIGURATION, locale, model );
            return template.getHtml( );
        }

        @Override
        public String saveConfiguration( HttpServletRequest request, ITask task )
        {
            String configBean = request.getParameter( PARAMETER_SELECTION_CONFIG );
            ParametrableUnitSelectionConfig config = loadConfig( task );

            String strAction = request.getParameter( PARAMETER_APPLY );
            if ( PARAMETER_VALUE_CHOOSE_CONFIG.equals( strAction ) )
            {
                config.setParametrableConfigHandler( configBean );
                _configDAO.store( config );
            }

            if ( config.getParametrableSelectionConfigHandler( ) != null )
            {
                return config.getParametrableSelectionConfigHandler( ).saveConfiguration( request, task );
            }
            return null;
        }

        @Override
        public void removeConfiguration( ITask task )
        {
            ParametrableUnitSelectionConfig config = loadConfig( task );
            if ( config.getParametrableSelectionConfigHandler( ) != null )
            {
                config.getParametrableSelectionConfigHandler( ).removeConfiguration( task );
            }
            _configDAO.delete( task.getId( ) );
        }

        private ParametrableUnitSelectionConfig loadConfig( ITask task )
        {
            ParametrableUnitSelectionConfig config = _configDAO.load( task.getId( ) );
            if ( config == null )
            {
                config = new ParametrableUnitSelectionConfig( );
                config.setIdTask( task.getId( ) );
                _configDAO.insert( config );
            }
            return config;
        }

        private ReferenceList getParametrableConfigurationHandlerList( Locale locale )
        {
            ReferenceList list = new ReferenceList( );
            for ( IParametrableConfigurationHandler handler : SpringContextService.getBeansOfType( IParametrableConfigurationHandler.class ) )
            {
                list.addItem( handler.getBeanName( ), handler.getTitle( locale ) );
            }
            return list;
        }
    }
}

/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.plugins.workflow.modules.unittree.web.task;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.workflow.modules.unittree.web.task.AbstractUnitAssignmentTaskComponent.ProcessedUnitSelection;
import fr.paris.lutece.portal.service.template.HtmlMarkup;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Renders the workflow-unittree templates that insert the unit selection forms rendered in Java, with HTML auto-escaping
 * (LUT-33153), from the model the components build: the forms marked with HtmlMarkup (unit_selection_html,
 * ProcessedUnitSelection.htmlForm, html_custom_config) keep their tags, the titles beside them are escaped, and a form left
 * as a String would print as text. The core macros the templates call are replaced by stand-ins that write their
 * arguments and their nested content: only the FreeMarker engine is needed.
 */
public class UnitSelectionFormsRenderTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String TEMPLATE_DIR = "admin/plugins/workflow/modules/unittree/";
    private static final String TEMPLATE_CONFIG = TEMPLATE_DIR + "task_unit_assignment_config.html";
    private static final String TEMPLATE_TASK_FORM = TEMPLATE_DIR + "task_unit_assignment_manual_form.html";
    private static final String TEMPLATE_PARAMETRABLE = TEMPLATE_DIR + "unitselection/config/unit_selection_parametrable_configuration.html";

    /** Stand-ins of the core macros the templates call: they write their arguments (escaped) and their nested content. */
    private static final String MACROS = "macros.ftl";
    private static final String MACROS_SOURCE = "<#macro formGroup rest...><div class=\"form-group\"><#nested></div></#macro>"
            + "<#macro fieldSet legend='' rest...><fieldset><legend>${legend}</legend><#nested></fieldset></#macro>"
            + "<#macro radioButton labelKey='' value='' rest...><label><input type=\"radio\" value=\"${value}\">${labelKey}</label></#macro>"
            + "<#macro staticText rest...><p><#nested></p></#macro>" + "<#macro input rest...><input></#macro>"
            + "<#macro select rest...><select></select></#macro>" + "<#macro button rest...><button></button></#macro>"
            + "<#macro inputGroup rest...><#nested></#macro>" + "<#macro inputGroupItem rest...><#nested></#macro>"
            + "<#macro alert rest...><div role=\"alert\"><#nested></div></#macro>" + "<#macro icon rest...></#macro>";

    private static final String SELECTION_FORM = "<div class=\"form-group\"><select name=\"unit_selection_id_to_add\"><option value=\"x\">a &lt;b&gt;</option></select></div>";
    private static final String UNIT_FORM = "<select name=\"task_unit_assignment_selection_from_all_unit_id\"><option value=\"12\">Unit &lt;b&gt;</option></select>";
    private static final String CUSTOM_FORM = "<p class=\"form-control-static\">Custom &lt;b&gt; configuration</p>";
    private static final String TITLE = "From all <b>x</b> \"q\"";
    private static final String TITLE_ESCAPED = "From all &lt;b&gt;x&lt;/b&gt; &quot;q&quot;";

    /**
     * Renders a template of the module with the core configuration of the output format.
     *
     * @param strTemplate
     *            the template path
     * @param model
     *            the model
     * @return the HTML
     * @throws IOException
     *             if the template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( String strTemplate, Map<String, Object> model ) throws IOException, TemplateException
    {
        StringTemplateLoader macros = new StringTemplateLoader( );
        macros.putTemplate( MACROS, MACROS_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                macros, new FileTemplateLoader( new File( TEMPLATES_ROOT ) )
        } ) );
        cfg.addAutoInclude( MACROS );
        StringWriter writer = new StringWriter( );
        cfg.getTemplate( strTemplate ).process( model, writer );

        return writer.toString( );
    }

    /**
     * The model of the configuration form, as AbstractUnitAssignmentTaskComponent.getDisplayConfigForm builds it.
     *
     * @param unitSelectionHtml
     *            the unit selection form
     * @return the model
     */
    private static Map<String, Object> configModel( Object unitSelectionHtml )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "list_assignment_type", List.of( ) );
        model.put( "unit_selection_html", unitSelectionHtml );
        model.put( "list_configured_unit_selection", List.of( new ProcessedUnitSelection( "unittree.unitSelectionFromAll", TITLE, UNIT_FORM ) ) );

        return model;
    }

    /**
     * The configuration form writes the unit selection form and the configuration forms of the selected unit selections
     * as they are, and escapes their titles.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testConfigFormKeepsTheUnitSelectionForms( ) throws Exception
    {
        String strHtml = render( TEMPLATE_CONFIG, configModel( HtmlMarkup.of( SELECTION_FORM ) ) );

        assertTrue( strHtml.contains( SELECTION_FORM ), strHtml );
        assertTrue( strHtml.contains( UNIT_FORM ), strHtml );
        assertTrue( strHtml.contains( "<legend>" + TITLE_ESCAPED + "</legend>" ), strHtml );
    }

    /**
     * A unit selection form left as a String is escaped: the reason the component marks it.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testAStringUnitSelectionFormIsEscaped( ) throws Exception
    {
        String strHtml = render( TEMPLATE_CONFIG, configModel( SELECTION_FORM ) );

        assertFalse( strHtml.contains( SELECTION_FORM ), strHtml );
        assertTrue( strHtml.contains( "&lt;div class=&quot;form-group&quot;&gt;" ), strHtml );
    }

    /**
     * The task form writes the form of each unit selection as it is, with one unit selection (title as text) and with
     * several (title as radio label), and escapes the titles.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testTaskFormKeepsTheUnitSelectionForms( ) throws Exception
    {
        ProcessedUnitSelection selection = new ProcessedUnitSelection( "unittree.unitSelectionFromAll", TITLE, UNIT_FORM );

        for ( List<ProcessedUnitSelection> listSelections : List.of( List.of( selection ), List.of( selection, selection ) ) )
        {
            String strHtml = render( TEMPLATE_TASK_FORM, Map.of( "list_unit_selection_form", listSelections ) );

            assertTrue( strHtml.contains( UNIT_FORM ), strHtml );
            assertTrue( strHtml.contains( TITLE_ESCAPED ), strHtml );
            assertFalse( strHtml.contains( TITLE ), strHtml );
        }
    }

    /**
     * The parametrable unit selection writes the configuration form of its custom configuration as it is.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testParametrableConfigurationKeepsTheCustomForm( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "is_add_config_displayed", false );
        model.put( "list_custom_config", List.of( ) );
        model.put( "html_custom_config", HtmlMarkup.of( CUSTOM_FORM ) );

        String strHtml = render( TEMPLATE_PARAMETRABLE, model );

        assertTrue( strHtml.contains( CUSTOM_FORM ), strHtml );
    }
}

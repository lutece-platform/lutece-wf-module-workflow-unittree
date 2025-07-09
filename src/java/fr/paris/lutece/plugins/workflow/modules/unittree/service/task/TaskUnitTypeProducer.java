/*
 * Copyright (c) 2002-2025, City of Paris
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

import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.paris.lutece.plugins.workflowcore.business.task.ITaskType;
import fr.paris.lutece.plugins.workflowcore.business.task.TaskType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

@ApplicationScoped
public class TaskUnitTypeProducer 
{
	@Produces
    @ApplicationScoped
    @Named( "workflow-unittree.taskTypeUnitAssignmentManual" )
    public ITaskType produceTaskTypeUnitAssignmentManual( @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentManual.key" ) String key,
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentManual.titleI18nKey" ) String titleI18nKey,
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentManual.beanName" ) String beanName, 
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentManual.taskForAutomaticAction", defaultValue = "false" ) boolean taskForAutomaticAction,
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentManual.configRequired", defaultValue = "false" ) boolean configRequired,
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentManual.formTaskRequired", defaultValue = "false" ) boolean formTaskRequired )
    {
        return buildTaskType( key, titleI18nKey, beanName, null, configRequired, formTaskRequired, taskForAutomaticAction );
    }
	
	@Produces
    @ApplicationScoped
    @Named( "workflow-unittree.taskTypeUnitAssignmentAutomatic" )
    public ITaskType produceTaskTypeUnitAssignmentAutomatic( @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentAutomatic.key" ) String key,
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentAutomatic.titleI18nKey" ) String titleI18nKey,
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentAutomatic.beanName" ) String beanName, 
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentAutomatic.taskForAutomaticAction", defaultValue = "false" ) boolean taskForAutomaticAction,
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentAutomatic.configRequired", defaultValue = "false" ) boolean configRequired,
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentAutomatic.formTaskRequired", defaultValue = "false" ) boolean formTaskRequired )
    {
        return buildTaskType( key, titleI18nKey, beanName, null, configRequired, formTaskRequired, taskForAutomaticAction );
    }
	
	@Produces
    @ApplicationScoped
    @Named( "workflow-unittree.taskTypeUnitUnassignment" )
    public ITaskType produceTaskTypeUnitUnassignment( @ConfigProperty( name = "workflow-unittree.taskUnitUnassignment.key" ) String key,
            @ConfigProperty( name = "workflow-unittree.taskUnitUnassignment.titleI18nKey" ) String titleI18nKey,
            @ConfigProperty( name = "workflow-unittree.taskUnitUnassignment.beanName" ) String beanName,
            @ConfigProperty( name = "workflow-unittree.taskUnitUnassignment.taskForAutomaticAction", defaultValue = "false" ) boolean taskForAutomaticAction,
            @ConfigProperty( name = "workflow-unittree.taskUnitUnassignment.configRequired", defaultValue = "false" ) boolean configRequired,
            @ConfigProperty( name = "workflow-unittree.taskUnitUnassignment.formTaskRequired", defaultValue = "false" ) boolean formTaskRequired )
    {
        return buildTaskType( key, titleI18nKey, beanName, null, configRequired, formTaskRequired, taskForAutomaticAction );
    }
	
	@Produces
    @ApplicationScoped
    @Named( "workflow-unittree.taskTypeUnitAssignmentNotification" )
    public ITaskType produceTaskTypeUnitAssignmentNotification( @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentNotification.key" ) String key,
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentNotification.titleI18nKey" ) String titleI18nKey,
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentNotification.beanName" ) String beanName,
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentNotification.configBeanName" ) String configBeanName,
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentNotification.taskForAutomaticAction", defaultValue = "false" ) boolean taskForAutomaticAction,
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentNotification.configRequired", defaultValue = "false" ) boolean configRequired,
            @ConfigProperty( name = "workflow-unittree.taskUnitAssignmentNotification.formTaskRequired", defaultValue = "false" ) boolean formTaskRequired )
    {
        return buildTaskType( key, titleI18nKey, beanName, configBeanName, configRequired, formTaskRequired, taskForAutomaticAction );
    }
	
	private ITaskType buildTaskType( String strKey, String strTitleI18nKey, String strBeanName, String strConfigBeanName, boolean bIsConfigRequired,
            boolean bIsFormTaskRequired, boolean bIsTaskForAutomaticAction )
    {
        TaskType taskType = new TaskType( );
        taskType.setKey( strKey );
        taskType.setTitleI18nKey( strTitleI18nKey );
        taskType.setBeanName( strBeanName );
        taskType.setConfigBeanName( strConfigBeanName );        
        taskType.setTaskForAutomaticAction( bIsTaskForAutomaticAction );
        taskType.setConfigRequired( bIsConfigRequired );
        taskType.setFormTaskRequired( bIsFormTaskRequired );
        return taskType;
    }
}
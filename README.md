![](http://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=wf-module-workflow-unittree-deploy)
# Module workflow unittree

## Introduction

This module contains workflow tasks for the plugin **lutece-system-plugin-unittree** .

The tasks are the following:
 
* manually assign a workflow resource to a unit. The user has to choose the target unit in the task form.
* automatically assign a workflow resource to a unit. This task can be used in an automatic workflow action. In this case, there is no task form.


## Configuration

The admin user has to configure the tasks. A assignment type and at least one unit selection are mandatory.

For a manual unit assignment, several unit selections can be configured. The user will choose the selection she / he wants in the task form.

For an automatic unit assignment, only one unit selection can be configured.

## Assignment types

The assignment types are the following:
 
*  **Create** : indicates that this is the first assignment. A chain of unit assignments is created.
*  **Assign up** : the resource is assigned to a unit with a higher level of knowledge. The target unit is added in the chain of assignment units.
*  **Assign down** : the resource is assigned to a unit with a lower level of knowledge. Often used when the resource has been assigned up but must be then treated by the assignor unit. The corresponding units are removed from the chain of unit assignments.
*  **Transfer** : the resource is transferred to a unit. The chain of unit assignments is reset.


## Unit selections

 **Introduction** 

A unit selection enables to select the target unit. A unit selection can be automatic and so can be used in the task to automatically assign a resource to a unit. All the unit selections (automatic or not) can be used in the task to manually assign a resource to a unit.

This module contains the following unit selections:
 
* Selection of a specific unit. Automatic. The unit is chosen in the task configuration. The user cannot select the unit in the task form.
* Selection of a unit among all the units. Non automatic. The user selects the target unit in a combobox in the task form. It can be used to transfer the resource to a unit for example.
* Selection of the parent unit. Automatic. It can be used to assign the resource up for example.
* Selection of the assignor unit. Automatic. It can be used to assign the resource down for example.


 **Create a unit selection** 

If you want to use your own unit selection, the only thing you have to do is to implement the interface `fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.IUnitSelection` and to declare it as a bean in the Spring context. Your unit selection will be then present in the configuration of the unit assignment tasks.

The method which does the real job is the method `select( ... )` . It returns the id of the target unit. This method is not responsible of storing the assignment. The store is done by the workflow task itself.If this method detects that the assignment is not possible, it must throw a `fr.paris.lutece.plugins.workflow.modules.unittree.exception.AssignmentNotPossibleException` .

The `IUnitSelection` uses a configuration handler to manage the configuration part of the unit selection in the unit assignment tasks. You can create your own configuration handler by implementing the interface `fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.IConfigurationHandler` . If your unit selection has no configuration, you can extends the abstract class `fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.impl.AbstractEmptyConfigurationHandler` . The only thing you have to do is to give the title of your unit selection.

The `IUnitSelection` uses a form handler to manage the task form of the unit selection in the unit assignment tasks. You can create your own form handler by implementing the interface `fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.ITaskFormHandler` . For automatic selections, it is a good idea to test if the assignment is possible in the method `getDisplayedForm( ... )` (by calling the method `select( ... )` ). If the assignment is not possible, then the method must throw a `fr.paris.lutece.plugins.workflow.modules.unittree.exception.AssignmentNotPossibleException` . It enables to hide this selection in the task form.

## Services

The service `fr.paris.lutece.plugins.workflow.modules.unittree.service.UnitAssignmentService` enables to retrieve the unit assignments for a resource.

It contains the following methods:
 
*  `findCurrentAssignment( ... )` : gives the current assignment in the chain of unit assignments.
*  `findAssignments( ... )` : gives the chain of unit assignments.


## Usage

The workflow tasks are used to assign the resource to units. The service `UnitAssignmentService` is used to retrieve the unit assignments for the resource.


[Maven documentation and reports](http://dev.lutece.paris.fr/plugins/module-workflow-unittree/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*
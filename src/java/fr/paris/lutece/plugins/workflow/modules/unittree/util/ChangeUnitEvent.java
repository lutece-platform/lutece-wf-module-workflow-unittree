package fr.paris.lutece.plugins.workflow.modules.unittree.util;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.UnitAssignment;

/**
 * Event created whe a change of unit occures. 
 */
public class ChangeUnitEvent extends ApplicationEvent {

	private static final long serialVersionUID = -6136225245590902847L;
	private final List<UnitAssignment> oldAssignmentList;

	public ChangeUnitEvent( Object source, List<UnitAssignment> oldAssignmentList )
	{
		super( source );
		this.oldAssignmentList = oldAssignmentList;
	}

	/**
	 * @return the oldAssignmentList
	 */
	public List<UnitAssignment> getOldAssignmentList( )
	{
		return oldAssignmentList;
	}

}

package fr.paris.lutece.plugins.workflow.modules.unittree.util;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignment;

/**
 * Event created whe a change of unit occures. 
 */
public class ChangeUnitEvent extends ApplicationEvent {

	private static final long serialVersionUID = -6136225245590902847L;
	private final List<UnitAssignment> _oldAssignmentList;

	public ChangeUnitEvent( Object source, List<UnitAssignment> oldAssignmentList )
	{
		super( source );
		_oldAssignmentList = oldAssignmentList;
	}

	/**
	 * @return the oldAssignmentList
	 */
	public List<UnitAssignment> getOldAssignmentList( )
	{
		return _oldAssignmentList;
	}

}

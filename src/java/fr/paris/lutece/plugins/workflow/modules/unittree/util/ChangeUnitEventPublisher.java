package fr.paris.lutece.plugins.workflow.modules.unittree.util;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.UnitAssignment;

/**
 * Publisher for {@link ChangeUnitEvent} 
 */
public class ChangeUnitEventPublisher implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher publisher;
	 
	public void setApplicationEventPublisher( ApplicationEventPublisher publisher )
    {
    	this.publisher = publisher;
    }
	
	public void publish( List<UnitAssignment> oldAssigmentList )
	{
		publisher.publishEvent( new ChangeUnitEvent( this, oldAssigmentList ) );
	}
}

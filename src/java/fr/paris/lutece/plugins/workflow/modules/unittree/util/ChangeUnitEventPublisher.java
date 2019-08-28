package fr.paris.lutece.plugins.workflow.modules.unittree.util;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignment;

/**
 * Publisher for {@link ChangeUnitEvent}
 */
public class ChangeUnitEventPublisher implements ApplicationEventPublisherAware
{

    private ApplicationEventPublisher _publisher;

    public void setApplicationEventPublisher( ApplicationEventPublisher publisher )
    {
        _publisher = publisher;
    }

    public void publish( List<UnitAssignment> oldAssigmentList )
    {
        _publisher.publishEvent( new ChangeUnitEvent( this, oldAssigmentList ) );
    }
}

/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.plugins.workflow.modules.unittree.business.task.information;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents information stored for a task
 */
public class TaskInformation
{
    private final int _nIdHistory;
    private final int _nIdTask;
    private final Map<String, String> _items;

    /**
     * Constructor
     * 
     * @param nIdHistory
     *            the history id
     * @param nIdTask
     *            the task id
     */
    public TaskInformation( int nIdHistory, int nIdTask )
    {
        _nIdHistory = nIdHistory;
        _nIdTask = nIdTask;
        _items = new HashMap<>( );
    }

    /**
     * Gives the history id
     * 
     * @return The history id
     */
    public int getIdHistory( )
    {
        return _nIdHistory;
    }

    /**
     * Gives the task id
     * 
     * @return The task id
     */
    public int getIdTask( )
    {
        return _nIdTask;
    }

    /**
     * Adds a piece of information
     * 
     * @param strKey
     *            the key of the piece of information
     * @param strValue
     *            the value of the piece of information
     */
    public void add( String strKey, String strValue )
    {
        _items.put( strKey, strValue );
    }

    /**
     * Gives the value of the piece of information with the specified key
     * 
     * @param strKey
     *            the key
     * @return the value
     */
    public String get( String strKey )
    {
        return _items.get( strKey );
    }

    /**
     * Gives all the keys of the pieces of information
     * 
     * @return a collection containing all the keys
     */
    public Collection<String> getKeys( )
    {
        return _items.keySet( );
    }
}

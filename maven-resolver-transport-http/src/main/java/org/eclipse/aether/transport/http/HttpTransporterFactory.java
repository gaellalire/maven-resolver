package org.eclipse.aether.transport.http;



/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.spi.connector.transport.Transporter;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.spi.locator.Service;
import org.eclipse.aether.spi.locator.ServiceLocator;
import org.eclipse.aether.spi.log.Logger;
import org.eclipse.aether.spi.log.LoggerFactory;
import org.eclipse.aether.spi.log.NullLoggerFactory;
import org.eclipse.aether.transfer.NoTransporterException;

/**
 * A transporter factory for repositories using the {@code http:} or {@code https:} protocol. The provided transporters
 * support uploads to WebDAV servers and resumable downloads.
 */
@Named( "http" )
public final class HttpTransporterFactory
    implements TransporterFactory, Service
{

    private Logger logger = NullLoggerFactory.LOGGER;

    private float priority = 5.0f;
    
    private CredentialsProvider defaultCredentialsProvider;
    
    private HttpRoutePlanner defaultHttpRoutePlanner;

    /**
     * Creates an (uninitialized) instance of this transporter factory. <em>Note:</em> In case of manual instantiation
     * by clients, the new factory needs to be configured via its various mutators before first use or runtime errors
     * will occur.
     */
    public HttpTransporterFactory()
    {
        // enables default constructor
    }

    @Inject
    HttpTransporterFactory( LoggerFactory loggerFactory )
    {
        setLoggerFactory( loggerFactory );
    }

    public void initService( ServiceLocator locator )
    {
        setLoggerFactory( locator.getService( LoggerFactory.class ) );
    }

    /**
     * Sets the logger factory to use for this component.
     * 
     * @param loggerFactory The logger factory to use, may be {@code null} to disable logging.
     * @return This component for chaining, never {@code null}.
     */
    public HttpTransporterFactory setLoggerFactory( LoggerFactory loggerFactory )
    {
        this.logger = NullLoggerFactory.getSafeLogger( loggerFactory, HttpTransporter.class );
        return this;
    }

    public float getPriority()
    {
        return priority;
    }

    /**
     * Sets the priority of this component.
     * 
     * @param priority The priority.
     * @return This component for chaining, never {@code null}.
     */
    public HttpTransporterFactory setPriority( float priority )
    {
        this.priority = priority;
        return this;
    }
    
    public CredentialsProvider getDefaultCredentialsProvider()
    {
        return defaultCredentialsProvider;
    }
    
    public void setDefaultCredentialsProvider( CredentialsProvider defaultCredentialsProvider )
    {
        this.defaultCredentialsProvider = defaultCredentialsProvider;
    }

    public void setDefaultRoutePlanner( HttpRoutePlanner defaultHttpRoutePlanner )
    {
        this.defaultHttpRoutePlanner = defaultHttpRoutePlanner;
    }

    public Transporter newInstance( RepositorySystemSession session, RemoteRepository repository )
        throws NoTransporterException
    {
        return new HttpTransporter( repository, session, logger, defaultHttpRoutePlanner, defaultCredentialsProvider );
    }

}

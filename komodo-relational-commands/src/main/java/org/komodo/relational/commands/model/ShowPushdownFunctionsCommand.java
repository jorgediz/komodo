/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */
package org.komodo.relational.commands.model;

import static org.komodo.shell.CompletionConstants.MESSAGE_INDENT;

import java.util.ArrayList;
import java.util.List;

import org.komodo.relational.commands.workspace.WorkspaceCommandsI18n;
import org.komodo.relational.model.Function;
import org.komodo.relational.model.Model;
import org.komodo.relational.model.PushdownFunction;
import org.komodo.shell.CommandResultImpl;
import org.komodo.shell.api.CommandResult;
import org.komodo.shell.api.WorkspaceStatus;
import org.komodo.utils.i18n.I18n;

/**
 * A shell command to show all the {@link PushdownFunction pushdown functions} of a {@link Model model}.
 */
public final class ShowPushdownFunctionsCommand extends ModelShellCommand {

    static final String NAME = "show-pushdown-functions"; //$NON-NLS-1$

    /**
     * @param status
     *        the shell's workspace status (cannot be <code>null</code>)
     */
    public ShowPushdownFunctionsCommand( final WorkspaceStatus status ) {
        super( NAME, status );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#doExecute()
     */
    @Override
    protected CommandResult doExecute() {
        try {
            final String[] namePatterns = processOptionalArguments( 0 );
            final boolean hasPatterns = ( namePatterns.length != 0 );
            final Model model = getModel();
            final Function[] functions = model.getFunctions( getTransaction(), namePatterns );

            if ( functions.length == 0 ) {
                if ( hasPatterns ) {
                    print( MESSAGE_INDENT,
                           I18n.bind( ModelCommandsI18n.noMatchingPushdownFunctions, model.getName( getTransaction() ) ) );
                } else {
                    print( MESSAGE_INDENT,
                           I18n.bind( ModelCommandsI18n.noPushdownFunctions, model.getName( getTransaction() ) ) );
                }
            } else {
                final List< Function > pushdowns = new ArrayList< >( functions.length );

                for ( final Function function : functions ) {
                    if ( PushdownFunction.RESOLVER.resolvable( getTransaction(), function ) ) {
                        pushdowns.add( function );
                    }
                }

                if ( pushdowns.isEmpty() ) {
                    if ( hasPatterns ) {
                        print( MESSAGE_INDENT,
                               I18n.bind( ModelCommandsI18n.noMatchingPushdownFunctions, model.getName( getTransaction() ) ) );
                    } else {
                        print( MESSAGE_INDENT,
                               I18n.bind( ModelCommandsI18n.noPushdownFunctions, model.getName( getTransaction() ) ) );
                    }
                } else {
                    if ( hasPatterns ) {
                        print( MESSAGE_INDENT,
                               I18n.bind( ModelCommandsI18n.matchedPushdownFunctionsHeader, model.getName( getTransaction() ) ) );
                    } else {
                        print( MESSAGE_INDENT,
                               I18n.bind( ModelCommandsI18n.pushdownsHeader, model.getName( getTransaction() ) ) );
                    }

                    final int indent = (MESSAGE_INDENT * 2);

                    for ( final Function function : pushdowns ) {
                        print( indent,
                               I18n.bind( WorkspaceCommandsI18n.printRelationalObject,
                                                    function.getName( getTransaction() ),
                                                    getWorkspaceStatus().getTypeDisplay( function, null ) ) );
                    }
                }
            }

            return CommandResult.SUCCESS;
        } catch ( final Exception e ) {
            return new CommandResultImpl( e );
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#getMaxArgCount()
     */
    @Override
    protected int getMaxArgCount() {
        return -1;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#printHelpDescription(int)
     */
    @Override
    protected void printHelpDescription( final int indent ) {
        print( indent, I18n.bind( ModelCommandsI18n.showPushdownFunctionsHelp, getName() ) );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#printHelpExamples(int)
     */
    @Override
    protected void printHelpExamples( final int indent ) {
        print( indent, I18n.bind( ModelCommandsI18n.showPushdownFunctionsExamples ) );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#printHelpUsage(int)
     */
    @Override
    protected void printHelpUsage( final int indent ) {
        print( indent, I18n.bind( ModelCommandsI18n.showPushdownFunctionsUsage ) );
    }

}

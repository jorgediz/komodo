/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.komodo.relational.commands.model;

import static org.komodo.relational.commands.model.ModelCommandMessages.ShowVirtualProceduresCommand.NO_VIRTUAL_PROCEDURES;
import static org.komodo.relational.commands.model.ModelCommandMessages.ShowVirtualProceduresCommand.VIRTUAL_PROCEDURES_HEADER;
import static org.komodo.relational.commands.workspace.WorkspaceCommandMessages.General.PRINT_RELATIONAL_OBJECT;
import static org.komodo.shell.CompletionConstants.MESSAGE_INDENT;
import java.util.ArrayList;
import java.util.List;
import org.komodo.relational.model.Model;
import org.komodo.relational.model.Procedure;
import org.komodo.relational.model.VirtualProcedure;
import org.komodo.shell.CommandResultImpl;
import org.komodo.shell.api.CommandResult;
import org.komodo.shell.api.WorkspaceStatus;

/**
 * A shell command to show all the {@link VirtualProcedure virtual procedures} of a {@link Model model}.
 */
public final class ShowVirtualProceduresCommand extends ModelShellCommand {

    static final String NAME = "show-virtual-procedures"; //$NON-NLS-1$

    /**
     * @param status
     *        the shell's workspace status (cannot be <code>null</code>)
     */
    public ShowVirtualProceduresCommand( final WorkspaceStatus status ) {
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
            final Model model = getModel();
            final Procedure[] procedures = model.getProcedures( getTransaction() );

            if ( procedures.length == 0 ) {
                print( MESSAGE_INDENT, getMessage( NO_VIRTUAL_PROCEDURES, model.getName( getTransaction() ) ) );
            } else {
                final List< Procedure > virtualProcedures = new ArrayList< >( procedures.length );

                for ( final Procedure procedure : procedures ) {
                    if ( VirtualProcedure.RESOLVER.resolvable( getTransaction(), procedure ) ) {
                        virtualProcedures.add( procedure );
                    }
                }

                if ( virtualProcedures.isEmpty() ) {
                    print( MESSAGE_INDENT, getMessage( NO_VIRTUAL_PROCEDURES, model.getName( getTransaction() ) ) );
                } else {
                    print( MESSAGE_INDENT, getMessage( VIRTUAL_PROCEDURES_HEADER, model.getName( getTransaction() ) ) );

                    final int indent = (MESSAGE_INDENT * 2);

                    for ( final Procedure virtProc : virtualProcedures ) {
                        print( indent,
                               getWorkspaceMessage( PRINT_RELATIONAL_OBJECT,
                                                    virtProc.getName( getTransaction() ),
                                                    virtProc.getTypeDisplayName() ) );
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
        return 0;
    }

}

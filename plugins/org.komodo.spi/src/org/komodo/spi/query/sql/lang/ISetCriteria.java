/*************************************************************************************
 * JBoss, Home of Professional Open Source.
* See the COPYRIGHT.txt file distributed with this work for information
* regarding copyright ownership. Some portions may be licensed
* to Red Hat, Inc. under one or more contributor license agreements.
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
* 02110-1301 USA.
 ************************************************************************************/
package org.komodo.spi.query.sql.lang;

import java.util.Collection;

import org.komodo.spi.query.sql.ILanguageVisitor;

/**
 *
 */
public interface ISetCriteria<E extends IExpression, LV extends ILanguageVisitor>
    extends IPredicateCriteria<LV> {

    /**
     * Get the expression
     * 
     * @return expression
     */
    E getExpression();

    /**
     * Set the expression
     * 
     * @param expression
     */
    void setExpression(E expression);

    /**
     * Returns the set of values.  Returns an empty collection if there are
     * currently no values.
     * 
     * @return The collection of Expression values
     */
    Collection<E> getValues();
      
    /**
     * Sets the values in the set.
     * 
     * @param values The set of value Expressions
     */
    void setValues(Collection<E> values);

    /**
     * Inverse the set criteria
     * 
     * @param value
     */
    void setNegated(boolean value);

}
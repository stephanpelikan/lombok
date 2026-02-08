/*
 * Copyright (C) 2024-2026 The Project Lombok Authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package lombok.eclipse.handlers;

import static lombok.eclipse.handlers.EclipseHandlerUtil.*;

import lombok.ConfigurationKeys;
import lombok.core.HandlerPriority;
import lombok.eclipse.EclipseASTAdapter;
import lombok.eclipse.EclipseASTVisitor;
import lombok.eclipse.EclipseNode;
import lombok.experimental.NonFinal;
import lombok.spi.Provides;

import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;

/**
 * Handles the {@code lombok.localDefaults.defaultFinal} and {@code lombok.parameterDefaults.defaultFinal}
 * configuration keys for eclipse.
 */
@Provides(EclipseASTVisitor.class)
@HandlerPriority(-2048) //-2^11; same as HandleFieldDefaults, to run before other handlers.
public class HandleLocalDefaults extends EclipseASTAdapter {
	@Override public void visitLocal(EclipseNode localNode, LocalDeclaration local) {
		boolean isCatchParam = local instanceof Argument;

		if (isCatchParam) {
			if (!Boolean.TRUE.equals(localNode.getAst().readConfiguration(ConfigurationKeys.PARAMETER_DEFAULTS_FINAL_EVERYWHERE))) return;
		} else {
			if (!Boolean.TRUE.equals(localNode.getAst().readConfiguration(ConfigurationKeys.LOCAL_DEFAULTS_FINAL_EVERYWHERE))) return;
		}

		if ((local.modifiers & ClassFileConstants.AccFinal) != 0) return;
		if (hasAnnotation(NonFinal.class, localNode)) return;

		local.modifiers |= ClassFileConstants.AccFinal;
		localNode.rebuild();
	}

	@Override public void visitMethodArgument(EclipseNode argNode, Argument arg, AbstractMethodDeclaration method) {
		if (!Boolean.TRUE.equals(argNode.getAst().readConfiguration(ConfigurationKeys.PARAMETER_DEFAULTS_FINAL_EVERYWHERE))) return;

		if ((arg.modifiers & ClassFileConstants.AccFinal) != 0) return;
		if (hasAnnotation(NonFinal.class, argNode)) return;

		arg.modifiers |= ClassFileConstants.AccFinal;
		argNode.rebuild();
	}
}

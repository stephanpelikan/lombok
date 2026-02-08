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
package lombok.javac.handlers;

import static lombok.javac.handlers.JavacHandlerUtil.*;

import lombok.ConfigurationKeys;
import lombok.core.HandlerPriority;
import lombok.experimental.NonFinal;
import lombok.javac.JavacASTAdapter;
import lombok.javac.JavacASTVisitor;
import lombok.javac.JavacNode;
import lombok.spi.Provides;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

/**
 * Handles the {@code lombok.localDefaults.defaultFinal} and {@code lombok.parameterDefaults.defaultFinal}
 * configuration keys for javac.
 */
@Provides(JavacASTVisitor.class)
@HandlerPriority(-2048) //-2^11; same as HandleFieldDefaults, to run before other handlers.
public class HandleLocalDefaults extends JavacASTAdapter {
	@Override public void visitLocal(JavacNode localNode, JCVariableDecl local) {
		boolean isCatchParam = localNode.up() != null && localNode.up().get() instanceof JCCatch;

		if (isCatchParam) {
			if (!Boolean.TRUE.equals(localNode.getAst().readConfiguration(ConfigurationKeys.PARAMETER_DEFAULTS_FINAL_EVERYWHERE))) return;
		} else {
			if (!Boolean.TRUE.equals(localNode.getAst().readConfiguration(ConfigurationKeys.LOCAL_DEFAULTS_FINAL_EVERYWHERE))) return;
		}

		if ((local.mods.flags & Flags.FINAL) != 0) return;
		if (hasAnnotationAndDeleteIfNeccessary(NonFinal.class, localNode)) return;

		local.mods.flags |= Flags.FINAL;
		localNode.rebuild();
	}

	@Override public void visitMethodArgument(JavacNode argumentNode, JCVariableDecl argument, JCMethodDecl method) {
		if (!Boolean.TRUE.equals(argumentNode.getAst().readConfiguration(ConfigurationKeys.PARAMETER_DEFAULTS_FINAL_EVERYWHERE))) return;

		if ((argument.mods.flags & Flags.FINAL) != 0) return;
		if (hasAnnotationAndDeleteIfNeccessary(NonFinal.class, argumentNode)) return;

		argument.mods.flags |= Flags.FINAL;
		argumentNode.rebuild();
	}
}

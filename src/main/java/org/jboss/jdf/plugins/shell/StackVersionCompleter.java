/*
 * JBoss, Home of Professional Open Source
 * Copyright <Year>, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jdf.plugins.shell;

import static org.jboss.jdf.plugins.JDFPlugin.OPTION_STACK;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.jboss.forge.shell.completer.CommandCompleterState;
import org.jboss.forge.shell.completer.SimpleTokenCompleter;
import org.jboss.jdf.plugins.stacks.Stack;
import org.jboss.jdf.plugins.stacks.StacksComparator;

public class StackVersionCompleter extends SimpleTokenCompleter
{

   private CommandCompleterState state;

   @Inject
   private List<Stack> availableStacks;

   @Override
   public Set<String> getCompletionTokens()
   {
      Set<String> versions = new TreeSet<String>(new StacksComparator());
      String informedStack = getInformedStack();
      Stack stack = getSelectedStack(informedStack);
      if (stack != null)
      {
         versions.addAll(stack.getVersions());
         
      }
      return versions;
   }

   private Stack getSelectedStack(String informedStack)
   {
      for (Stack stack : availableStacks)
      {
         if (informedStack.equals(stack.getId()))
         {
            return stack;
         }
      }
      return null;
   }

   private String getInformedStack()
   {
      String completeCommand = state.getBuffer();
      String[] splitedCommand = completeCommand.split("[\\s]++"); // split by one or more whitespaces
      int cont = 0;
      for (String token : splitedCommand)
      {
         cont++;
         if (("--" + OPTION_STACK).equals(token))
         {
            break;
         }
      }
      return splitedCommand[cont];
   }

   @Override
   public void complete(CommandCompleterState state)
   {
      this.state = state;
      super.complete(state);
   }

}
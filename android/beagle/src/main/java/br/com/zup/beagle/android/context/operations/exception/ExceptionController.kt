/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.zup.beagle.android.context.operations.exception

import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionOperationTypes
import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionParameterTypes
import br.com.zup.beagle.android.context.operations.exception.strategy.validation.OperationsValidation
import br.com.zup.beagle.android.context.operations.exception.strategy.validation.ValidationFactory
import br.com.zup.beagle.android.context.operations.grammar.GrammarChars
import br.com.zup.beagle.android.context.operations.operation.Operation
import br.com.zup.beagle.android.context.operations.parameter.Parameter
import br.com.zup.beagle.android.context.operations.strategy.invalid.InvalidOperation

internal class ExceptionController {

    internal fun checkOperation(operation: Operation) {
        val type = operation.operationStrategy

        if (type is InvalidOperation) {
            throw ExceptionFactory.createException(
                exceptionTypes = type.operationType as ExceptionOperationTypes,
                details = operation.operationToken
            )
        }
        else if (operation.hasIncorrectSyntax()) {
            throw ExceptionFactory.createException(
                exceptionTypes = ExceptionOperationTypes.MISSING_DELIMITERS,
                details = operation.operationToken
            )
        }
    }

    fun
        checkParameter(parameter: Parameter) {
        if (parameter.arguments.isNotEmpty()) {
            val operationType = parameter.operation.operationStrategy?.operationType

            if (operationType is OperationsValidation) {
                ValidationFactory.validate(operationType, parameter)
            }
        } else {
            throw ExceptionFactory.createException(
                ExceptionParameterTypes.REQUIRED_ARGS,
                parameter.operation,
                parameter.arguments.size.toString()
            )
        }
    }
}

private fun Operation.hasIncorrectSyntax()  : Boolean {
    var openParenthesesCount = 0
    var closeParenthesesCount = 0
    var openBracketCount = 0
    var closeBracketCount = 0

    this.operationToken.forEach {
        when (it) {
            GrammarChars.OPEN_PARENTHESES -> {
                openParenthesesCount++
            }
            GrammarChars.CLOSE_PARENTHESES -> {
                closeParenthesesCount++
            }
            GrammarChars.OPEN_BRACKET -> {
                openBracketCount++
            }
            GrammarChars.CLOSE_BRACKET -> {
                closeBracketCount++
            }
        }
    }

    return missingDelimiter(openParenthesesCount, closeParenthesesCount) ||
        missingDelimiter(openBracketCount, closeBracketCount) ||
        noDelimiter(openParenthesesCount, closeParenthesesCount)
}

private fun missingDelimiter(countA: Int, countB: Int): Boolean =
    countA != countB && (countA > 0 || countA > 0)

private fun noDelimiter(countA: Int, countB: Int): Boolean =
    countA == 0 && countB == 0
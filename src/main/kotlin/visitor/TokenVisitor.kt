package visitor

import tokenizer.Brace
import tokenizer.NumberToken
import tokenizer.Operation

interface TokenVisitor {
    fun visit(token: NumberToken)
    fun visit(token: Brace)
    fun visit(token: Operation)
}
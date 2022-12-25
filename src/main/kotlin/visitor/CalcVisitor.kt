package visitor

import tokenizer.*
import java.lang.IllegalArgumentException

class CalcVisitor: TokenVisitor {
    private val stack = mutableListOf<Int>()

    fun visitAll(tokens: List<Token>): Int {
        tokens.forEach { it.accept(this) }
        if (stack.size != 1) {
            throw IllegalArgumentException("Expect expressions reduce to one number")
        }

        return stack.last()
    }

    override fun visit(token: NumberToken) {
        stack.add(token.number)
    }

    override fun visit(token: Brace) {
        throw IllegalArgumentException("Braces are forbidden in CalcVisitor")
    }

    override fun visit(token: Operation) {
        if (stack.size < 2) {
            throw IllegalArgumentException("Invalid arguments count for $token")
        }
        val arg2 = stack.removeLast()
        val arg1 = stack.removeLast()

        stack.add(
            when(token) {
                is Add -> arg1 + arg2
                is Sub -> arg1 - arg2
                is Mul -> arg1 * arg2
                is Div -> arg1 / arg2
                else -> throw IllegalArgumentException("Unexpected type of token")
            }
        )
    }
}
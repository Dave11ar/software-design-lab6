package visitor

import tokenizer.*

class ParserVisitor: TokenVisitor {
    private val result = mutableListOf<Token>()
    private val stack = mutableListOf<Token>()

    fun visitAll(tokens: List<Token>): List<Token> {
        tokens.forEach { it.accept(this) }
        while (stack.isNotEmpty()) {
            result.add(stack.removeLast())
        }

        return result
    }

    override fun visit(token: NumberToken) {
        result.add(token)
    }

    override fun visit(token: Brace) {
        when(token) {
            is LeftBrace -> stack.add(token)
            is RightBrace -> {
                while (stack.last() != LeftBrace) {
                    result.add(stack.removeLast())
                }
                stack.removeLast()
            }
        }
    }

    override fun visit(token: Operation) {
        while(stack.isNotEmpty() &&
              stack.last() is Operation &&
              (stack.last() as Operation).priority() >= token.priority()) {
            result.add(stack.removeLast())
        }
        stack.add(token)
    }
}
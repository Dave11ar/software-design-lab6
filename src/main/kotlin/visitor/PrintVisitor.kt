package visitor

import tokenizer.*

class PrintVisitor: TokenVisitor {
    private val strings = mutableListOf<String>()

    fun visitAll(tokens: List<Token>): String {
        tokens.forEach { it.accept(this) }
        return strings.joinToString(" ")
    }

    override fun visit(token: NumberToken) {
        strings.add(token.toString())
    }

    override fun visit(token: Brace) {
        strings.add(token.toString())
    }

    override fun visit(token: Operation) {
        strings.add(token.toString())
    }
}
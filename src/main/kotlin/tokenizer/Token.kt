package tokenizer

import visitor.TokenVisitor

interface Token {
    fun accept(visitor: TokenVisitor)
}

interface Brace: Token {
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }
}
interface Operation : Token {
    fun priority(): Int

    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }
}

class NumberToken(val number: Int): Token {
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }

    override fun toString() = "NUMBER($number)"
}

object LeftBrace: Brace {
    override fun toString() = "LEFT"
}
object RightBrace: Brace {
    override fun toString() = "RIGHT"
}

object Add: Operation {
    override fun priority() = 1
    override fun toString() = "ADD"
}

object Sub: Operation {
    override fun priority() = 1
    override fun toString() = "SUB"
}

object Mul: Operation {
    override fun priority() = 2
    override fun toString() = "MUL"
}

object Div: Operation {
    override fun priority() = 2
    override fun toString() = "DIV"
}
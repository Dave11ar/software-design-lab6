package tokenizer


fun tokenize(source: String): List<Token> {
    val stateContext = StateContext()

    source.forEach { stateContext.consumerChar(it) }
    stateContext.consumeEOF()
    return stateContext.getTokens()
}

interface TokenizerState {
    fun handleChar(char: Char, context: StateContext)
    fun handleEOF(context: StateContext)
}

object StartState: TokenizerState {
    override fun handleChar(char: Char, context: StateContext) {
        if (char.isWhitespace()) {
            context.updateState(this, null)
        } else {
            when (char) {
                '+' -> context.updateState(this, Add)
                '-' -> context.updateState(this, Sub)
                '*' -> context.updateState(this, Mul)
                '/' -> context.updateState(this, Div)
                '(' -> context.updateState(this, LeftBrace)
                ')' -> context.updateState(this, RightBrace)
                in '0'..'9' -> context.updateState(NumberState(char - '0'), null)
                else -> throw IllegalStateException("Unexpected char $char")
            }
        }
    }

    override fun handleEOF(context: StateContext) {
        context.updateState(EOFState, null)
    }
}

class NumberState(private var number: Int = 0): TokenizerState {
    override fun handleChar(char: Char, context: StateContext) {
        when(char) {
            in '0'..'9' -> number = number * 10 + (char - '0')
            else -> {
                context.updateState(StartState, NumberToken(number))
                StartState.handleChar(char, context)
            }
        }
    }

    override fun handleEOF(context: StateContext) {
        context.updateState(EOFState, NumberToken(number))
    }
}

object EOFState: TokenizerState {
    override fun handleChar(char: Char, context: StateContext) {
        throw IllegalStateException("Unexpected char after EOF")
    }
    override fun handleEOF(context: StateContext) {
        throw IllegalStateException("Unexpected char after EOF")
    }
}

class StateContext(
    private var state: TokenizerState = StartState,
    private val tokens: MutableList<Token> = mutableListOf()
) {
    fun consumerChar(char: Char) {
        state.handleChar(char, this)
    }

    fun consumeEOF() {
        state.handleEOF(this)
    }

    fun getTokens() = tokens

    fun updateState(nextState: TokenizerState, token: Token?) {
        token?.let { tokens.add(it) }
        state = nextState
    }
}
fun validate(expression: String): ValidationResult {
    return try {
        val allSymbols = expression.toList()
        val listIterator = allSymbols.listIterator()
        transitionE(listIterator)

        return if (listIterator.hasNext()) {
            ValidationResult.Incorrect
        } else {
            ValidationResult.Correct
        }
    } catch (e: UnsupportedOperationException) {
        ValidationResult.Incorrect
    }
}

@Throws(UnsupportedOperationException::class)
private fun transitionE(iterator: ListIterator<Char>) {
    transitionT(iterator)
    transitionEStroke(iterator)
}

@Throws(UnsupportedOperationException::class)
private fun transitionEStroke(iterator: ListIterator<Char>) {
    if (iterator.hasNext()) {
        when (iterator.next()) {
            '+' -> {
                transitionT(iterator)
                transitionEStroke(iterator)
            }
            else -> iterator.previous()
        }
    }
}

@Throws(UnsupportedOperationException::class)
private fun transitionT(iterator: ListIterator<Char>) {
    transitionF(iterator)
    transitionTStroke(iterator)
}

@Throws(UnsupportedOperationException::class)
private fun transitionTStroke(iterator: ListIterator<Char>) {
    if (iterator.hasNext()) {
        when (iterator.next()) {
            '*' -> {
                transitionF(iterator)
                transitionTStroke(iterator)
            }
            else -> iterator.previous()
        }
    }
}

@Throws(UnsupportedOperationException::class)
private fun transitionF(iterator: ListIterator<Char>) {
    if (iterator.hasNext()) {
        when (iterator.next()) {
            '-' -> transitionF(iterator)
            '(' -> {
                transitionE(iterator)
                if (!iterator.hasNext() || iterator.next() != ')') {
                    throw UnsupportedOperationException()
                }
            }
            '7' -> {}
            'a' -> {}
            else -> throw UnsupportedOperationException()
        }
    } else {
        throw UnsupportedOperationException()
    }
}
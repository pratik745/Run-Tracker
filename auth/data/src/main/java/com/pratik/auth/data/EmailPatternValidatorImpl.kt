package com.pratik.auth.data

import android.util.Patterns
import com.pratik.auth.domain.EmailPatternValidator

class EmailPatternValidatorImpl: EmailPatternValidator {
    override fun matches(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
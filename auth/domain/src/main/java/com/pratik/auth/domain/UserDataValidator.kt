package com.pratik.auth.domain

class UserDataValidator(
    private val emailPatternValidator: EmailPatternValidator
) {

    fun isValidEmail(email:String) = emailPatternValidator.matches(email = email.trim())

    fun validatePassword(password: String) : PasswordValidationState {
        val hasMinLength = (password.length >= MIN_PASSWORD_LENGTH)
        val hasDigit = password.any { it.isDigit() }
        val hasLowerCaseChar = password.any { it.isLowerCase() }
        val hasUpperCaseChar = password.any { it.isUpperCase() }

        return PasswordValidationState(
            hasMinLength = hasMinLength,
            hasNumber = hasDigit,
            hasLowerCaseCharacter = hasLowerCaseChar,
            hasUpperCaseCharacter = hasUpperCaseChar
        )
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 9
    }
}